package org.designroleminer.technique;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class DoseaOutlierTechnique extends AbstractTechnique {

	/**
	 * Generate sheet with design role assigned to each class
	 * 
	 * @param classes
	 * @param fileResultado
	 */
	@Override
	public void generate(List<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRoleTechnique;LOC;CC;Efferent;NOP;");

		HashMap<String, Long> linhasDeCodigoPorDesignRole = new HashMap<>();
		long totalLoc = obterTotalLinhasCodigoPorDesignRole(classes, linhasDeCodigoPorDesignRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		HashMap<String, List<Integer>> metricasLOCPorDesignRole = new HashMap<>();
		HashMap<String, List<Integer>> metricasCCPorDesignRole = new HashMap<>();
		HashMap<String, List<Integer>> metricasEfferentPorDesignRole = new HashMap<>();
		HashMap<String, List<Integer>> metricasNOPPorDesignRole = new HashMap<>();

		for (ClassMetricResult classe : classes) {
			for (MethodMetricResult method : classe.getMetricsByMethod().values()) {
				if (!classe.getDesignRole().toUpperCase().equals(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
					agrupaPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + classe.getDesignRole());
					agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_CC + classe.getDesignRole());
					agrupaPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_EC + classe.getDesignRole());
					agrupaPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + classe.getDesignRole());
				}
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
			
				adicionarMetricaDesignRole(metricasLOCPorDesignRole, classe.getDesignRole().toUpperCase(), method.getLinesOfCode());
				adicionarMetricaDesignRole(metricasCCPorDesignRole, classe.getDesignRole().toUpperCase(), method.getComplexity());
				adicionarMetricaDesignRole(metricasEfferentPorDesignRole, classe.getDesignRole().toUpperCase(), method.getEfferentCoupling());
				adicionarMetricaDesignRole(metricasNOPPorDesignRole, classe.getDesignRole().toUpperCase(), method.getNumberOfParameters());
			}
		}

		LimiarMetrica limiarLOCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC, false);
		
		LimiarMetrica limiarCCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC, false);
		LimiarMetrica limiarEfferentUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent, totalLoc, 5,
				70, 90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC, false);
		LimiarMetrica limiarNOPUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLoc, 3, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP, false);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOCUndefined.getLimiarMaximo() + ";"
				+ limiarCCUndefined.getLimiarMaximo() + ";" + limiarEfferentUndefined.getLimiarMaximo() + ";"
				+ limiarNOPUndefined.getLimiarMaximo() + ";");

		for (String designRole : linhasDeCodigoPorDesignRole.keySet()) {
			designRole = designRole.toUpperCase();
			if (!designRole.contains(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
				//long totalLOCPorDesignRole = linhasDeCodigoPorDesignRole.get(designRole);
				LimiarMetrica limiarLOC = obterLimiaresBoxPlot(metricasLOCPorDesignRole, designRole);
				LimiarMetrica limiarCC = obterLimiaresBoxPlot(metricasCCPorDesignRole, designRole);
				LimiarMetrica limiarEfferent = obterLimiaresBoxPlot(metricasEfferentPorDesignRole, designRole);
				LimiarMetrica limiarNOP = obterLimiaresBoxPlot(metricasNOPPorDesignRole, designRole);
				
				// para limiares muitos baixos assume o limiar médio da aplicacao
				if (limiarLOC.getLimiarMaximo() < limiarLOCUndefined.getLimiarMedio())
					limiarLOC.setLimiarMaximo(limiarLOCUndefined.getLimiarMedio());
				if (limiarCC.getLimiarMaximo() < limiarCCUndefined.getLimiarMedio())
					limiarCC.setLimiarMaximo(limiarCCUndefined.getLimiarMedio());
				if (limiarEfferent.getLimiarMaximo() < limiarEfferentUndefined.getLimiarMedio())
					limiarEfferent.setLimiarMaximo(limiarEfferentUndefined.getLimiarMedio());
				if (limiarNOP.getLimiarMaximo() < limiarNOPUndefined.getLimiarMedio())
					limiarNOP.setLimiarMaximo(limiarNOPUndefined.getLimiarMedio());
				pm.write(designRole + ";" + limiarLOC.getLimiarMaximo() + ";" + limiarCC.getLimiarMaximo() + ";"
						+ limiarEfferent.getLimiarMaximo() + ";" + limiarNOP.getLimiarMaximo() + ";");
			}
		}
	}

	private LimiarMetrica obterLimiaresBoxPlot(HashMap<String, List<Integer>> metricasPorDesignRole,
			String designRole) {
		
		if (designRole.equals("ABSTRACTATTRIBUTEDTYPE"))
			System.out.println("ABSTRACTATTRIBUTEDTYPE");
		
		LimiarMetrica limiarMetrica = new LimiarMetrica();
		List<Integer> valores = metricasPorDesignRole.get(designRole.toUpperCase());
		if (valores != null) {
			double[] listaValoresDouble = new double[valores.size()];
 			for (int i=0; i < valores.size(); i++) {
				listaValoresDouble[i] = valores.get(i);
			}
 			BoxPlotOutlier boxplot = new BoxPlotOutlier(listaValoresDouble);
 			limiarMetrica.setLimiarMinimo((int) boxplot.getLowerThresholdAdjusted());
 			limiarMetrica.setLimiarMaximo((int) boxplot.getUpperThresholdAdjusted());
 			limiarMetrica.setLimiarMedio((int) boxplot.getQ3());
		}
		return limiarMetrica;
	}

	private void adicionarMetricaDesignRole(HashMap<String, List<Integer>> metricasPorDesignRole, String designRole,
			int valorMetrica) {
		List<Integer> listaValores = metricasPorDesignRole.get(designRole);
		if (listaValores == null)
			listaValores = new ArrayList<Integer>();
		listaValores.add(valorMetrica);
		metricasPorDesignRole.put(designRole, listaValores);
	}
		

}
