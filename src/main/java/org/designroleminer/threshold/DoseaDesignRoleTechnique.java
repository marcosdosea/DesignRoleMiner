package org.designroleminer.threshold;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class DoseaDesignRoleTechnique extends AbstractTechnique {

	/**
	 * Generate sheet with design role assigned to each class
	 * 
	 * @param classes
	 * @param fileResultado
	 */
	@Override
	public void generate(Collection<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRoleTechnique;LOC;CC;Efferent;NOP;CLOC");

		HashMap<String, Long> linhasDeCodigoPorDesignRole = new HashMap<>();
		long totalLoc = obterTotalLinhasCodigoPorDesignRole(classes, linhasDeCodigoPorDesignRole);

		//METHOD THRESHOLDS
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();
		// CLASS THRESHOLDS
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCLOC = new HashMap<>();

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
			}
			if (!classe.getDesignRole().toUpperCase().equals(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCLOC, classe.getCLoc(),
						classe.getCLoc(), LimiarMetrica.METRICA_CLOC + classe.getDesignRole());
			}
			agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCLOC, classe.getCLoc(), classe.getCLoc(),
					LimiarMetrica.METRICA_CLOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		}

		//METHOD THRESHOLD
		LimiarMetrica limiarLOCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalLoc, 5, 80, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC, true);
		LimiarMetrica limiarCCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLoc, 5, 80, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC, true);
		LimiarMetrica limiarEfferentUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent, totalLoc, 5,
				80, 90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC, true);
		LimiarMetrica limiarNOPUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLoc, 3, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP, true);
		// CLASS THRESHOLD
		LimiarMetrica limiarCLOCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCLOC, totalLoc, 5, 80, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CLOC, true);
		
		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOCUndefined.getLimiarMaximo() + ";"
				+ limiarCCUndefined.getLimiarMaximo() + ";" + limiarEfferentUndefined.getLimiarMaximo() + ";"
				+ limiarNOPUndefined.getLimiarMaximo() + ";"+ limiarCLOCUndefined.getLimiarMaximo() + ";");

		for (String designRole : linhasDeCodigoPorDesignRole.keySet()) {
			designRole = designRole.toUpperCase();
			if (!designRole.contains(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
				long totalLOCPorDesignRole = linhasDeCodigoPorDesignRole.get(designRole);
				// METHOD THRESHOLDS
				LimiarMetrica limiarLOC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalLOCPorDesignRole,
						5, 75, 90, designRole, LimiarMetrica.METRICA_LOC, true);
				LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLOCPorDesignRole, 5,
						75, 90, designRole, LimiarMetrica.METRICA_CC, true);
				LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent,
						totalLOCPorDesignRole, 5, 75, 90, designRole, LimiarMetrica.METRICA_EC, true);
				LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLOCPorDesignRole,
						3, 90, 95, designRole, LimiarMetrica.METRICA_NOP, true);
				// CLASS THRESHOLDS
				LimiarMetrica limiarCLOC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCLOC, totalLOCPorDesignRole,
						5, 75, 90, designRole, LimiarMetrica.METRICA_CLOC, true);

				// para limiares muitos baixos assume o limiar médio da aplicacao
				// METHOD
				if (limiarLOC.getLimiarMaximo() < limiarLOCUndefined.getLimiarMedio())
					limiarLOC.setLimiarMaximo(limiarLOCUndefined.getLimiarMedio());
				if (limiarCC.getLimiarMaximo() < limiarCCUndefined.getLimiarMedio())
					limiarCC.setLimiarMaximo(limiarCCUndefined.getLimiarMedio());
				if (limiarEfferent.getLimiarMaximo() < limiarEfferentUndefined.getLimiarMedio())
					limiarEfferent.setLimiarMaximo(limiarEfferentUndefined.getLimiarMedio());
				if (limiarNOP.getLimiarMaximo() < limiarNOPUndefined.getLimiarMedio())
					limiarNOP.setLimiarMaximo(limiarNOPUndefined.getLimiarMedio());
				// CLASS
				if (limiarCLOC.getLimiarMaximo() < limiarCLOCUndefined.getLimiarMedio())
					limiarCLOC.setLimiarMaximo(limiarCLOCUndefined.getLimiarMedio());
				
				pm.write(designRole + ";" + limiarLOC.getLimiarMaximo() + ";" + limiarCC.getLimiarMaximo() + ";"
						+ limiarEfferent.getLimiarMaximo() + ";" + limiarNOP.getLimiarMaximo() + ";"
						+ limiarCLOC.getLimiarMaximo() + ";");
			}
		}
	}

}
