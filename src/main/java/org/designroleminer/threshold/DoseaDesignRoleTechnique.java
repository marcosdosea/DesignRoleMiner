package org.designroleminer.threshold;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class DoseaDesignRoleTechnique extends AbstractTechnique {

	private Collection<ClassMetricResult> classesProjetoAnalisado;

	public DoseaDesignRoleTechnique(Collection<ClassMetricResult> classesProjetoAnalisado) {
		super();
		this.classesProjetoAnalisado = classesProjetoAnalisado;
	}

	/**
	 * Generate sheet with design role assigned to each class
	 * 
	 * @param classes
	 * @param fileResultado
	 */
	@Override
	public void generate(Collection<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;CLOC;%LOCDR");

		HashMap<String, Long> linhasDeCodigoPorDesignRole = new HashMap<>();
		long totalLoc = obterTotalLinhasCodigoPorDesignRole(classes, linhasDeCodigoPorDesignRole);

		HashMap<String, Long> linhasDeCodigoPorDesignRoleProjetoAnalisado = new HashMap<>();
		long totalLocProjetoAnalisado = obterTotalLinhasCodigoPorDesignRole(classesProjetoAnalisado,
				linhasDeCodigoPorDesignRoleProjetoAnalisado);

		// METHOD THRESHOLDS
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
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCLOC, classe.getCLoc(), classe.getCLoc(),
						LimiarMetrica.METRICA_CLOC + classe.getDesignRole());
			}

			agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCLOC, classe.getCLoc(), classe.getCLoc(),
					LimiarMetrica.METRICA_CLOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		}

		LimiarMetrica limiarLOCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC, false);
		LimiarMetrica limiarCCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC, false);
		LimiarMetrica limiarEfferentUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent, totalLoc, 5,
				70, 90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC, false);
		LimiarMetrica limiarNOPUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLoc, 7, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP, false);
		// CLASS THRESHOLD
		LimiarMetrica limiarCLOCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCLOC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CLOC, false);

		Long linhasDR = linhasDeCodigoPorDesignRoleProjetoAnalisado.get(LimiarMetrica.DESIGN_ROLE_UNDEFINED);

		float percLocDesignRole = ((float) linhasDR / totalLocProjetoAnalisado * 100);

		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		formatter.setMaximumFractionDigits(2);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOCUndefined.getLimiarMaximo() + ";"
				+ limiarCCUndefined.getLimiarMaximo() + ";" + limiarEfferentUndefined.getLimiarMaximo() + ";"
				+ limiarNOPUndefined.getLimiarMaximo() + ";" + limiarCLOCUndefined.getLimiarMaximo() + ";"
				+ formatter.format(percLocDesignRole) + ";");

		for (String designRole : linhasDeCodigoPorDesignRole.keySet()) {
			linhasDR = linhasDeCodigoPorDesignRoleProjetoAnalisado.get(designRole);
			if (linhasDR != null)
				percLocDesignRole = ((float) linhasDR / totalLocProjetoAnalisado * 100);
			boolean avaliarDesignRole = (linhasDR != null) && (percLocDesignRole > 1);
			if (!designRole.contains(LimiarMetrica.DESIGN_ROLE_UNDEFINED) && avaliarDesignRole) {
				long totalLOCPorDesignRole = linhasDeCodigoPorDesignRole.get(designRole);
				// METHOD THRESHOLDS
				LimiarMetrica limiarLOC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalLOCPorDesignRole,
						5, 70, 90, designRole, LimiarMetrica.METRICA_LOC, false);
				LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLOCPorDesignRole, 5,
						70, 90, designRole, LimiarMetrica.METRICA_CC, false);
				LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent,
						totalLOCPorDesignRole, 5, 70, 90, designRole, LimiarMetrica.METRICA_EC, false);
				LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLOCPorDesignRole,
						7, 90, 95, designRole, LimiarMetrica.METRICA_NOP, false);
				// CLASS THRESHOLDS
				LimiarMetrica limiarCLOC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCLOC, totalLOCPorDesignRole,
						5, 70, 90, designRole, LimiarMetrica.METRICA_CLOC, false);

				// para limiares muitos baixos assume o limiar médio da aplicacao
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

//				if (designRole.contains(LimiarMetrica.DESIGN_ROLE_TEST)
//						|| designRole.contains(LimiarMetrica.DESIGN_ROLE_ENTITY)) {
//					if (limiarLOC.getLimiarMaximo() < limiarLOCUndefined.getLimiarMaximo())
//						limiarLOC.setLimiarMaximo(limiarLOCUndefined.getLimiarMaximo());
//					if (limiarCC.getLimiarMaximo() < limiarCCUndefined.getLimiarMaximo())
//						limiarCC.setLimiarMaximo(limiarCCUndefined.getLimiarMaximo());
//					if (limiarEfferent.getLimiarMaximo() < limiarEfferentUndefined.getLimiarMaximo())
//						limiarEfferent.setLimiarMaximo(limiarEfferentUndefined.getLimiarMaximo());
//					if (limiarNOP.getLimiarMaximo() < limiarNOPUndefined.getLimiarMaximo())
//						limiarNOP.setLimiarMaximo(limiarNOPUndefined.getLimiarMaximo());
//					// CLASS
//					if (limiarCLOC.getLimiarMaximo() < limiarCLOCUndefined.getLimiarMaximo())
//						limiarCLOC.setLimiarMaximo(limiarCLOCUndefined.getLimiarMaximo());
//				}

				pm.write(designRole + ";" + limiarLOC.getLimiarMaximo() + ";" + limiarCC.getLimiarMaximo() + ";"
						+ limiarEfferent.getLimiarMaximo() + ";" + limiarNOP.getLimiarMaximo() + ";"
						+ limiarCLOC.getLimiarMaximo() + ";" + formatter.format(percLocDesignRole) + ";");
			}
		}
	}

}
