package org.designroleminer.threshold;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class AnicheTechnique extends AbstractTechnique {

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

		HashMap<String, Long> linhasDeCodigoPorArchitecturalRole = new HashMap<>();
		obterTotalLinhasCodigoPorArchitecturalRole(classes, linhasDeCodigoPorArchitecturalRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (ClassMetricResult classe : classes) {
			String architecturalRole = LimiarMetrica.DESIGN_ROLE_UNDEFINED;
			if (classe.isArchitecturalRole())
				architecturalRole = classe.getDesignRole();

			for (MethodMetricResult method : classe.getMetricsByMethod().values()) {
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_LOC + architecturalRole);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + architecturalRole);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + architecturalRole);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + architecturalRole);
			}
		}

		for (String architecuturalRole : linhasDeCodigoPorArchitecturalRole.keySet()) {
			architecuturalRole = architecuturalRole.toUpperCase();
			long totalLOCPorArchitecturalRole = linhasDeCodigoPorArchitecturalRole.get(architecuturalRole);
			LimiarMetrica limiarLOC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC,
					totalLOCPorArchitecturalRole, 5, 70, 90, architecuturalRole, LimiarMetrica.METRICA_LOC, false);
			LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLOCPorArchitecturalRole,
					5, 70, 90, architecuturalRole, LimiarMetrica.METRICA_CC, false);
			LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent,
					totalLOCPorArchitecturalRole, 5, 70, 90, architecuturalRole, LimiarMetrica.METRICA_EC, false);
			LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP,
					totalLOCPorArchitecturalRole, 5, 90, 95, architecuturalRole, LimiarMetrica.METRICA_NOP, false);
			pm.write(architecuturalRole + ";" + limiarLOC.getLimiarMaximo() + ";" + limiarCC.getLimiarMaximo() + ";"
					+ limiarEfferent.getLimiarMaximo() + ";" + limiarNOP.getLimiarMaximo() + ";");
		}
	}
	
	private long obterTotalLinhasCodigoPorArchitecturalRole(List<ClassMetricResult> classes,
			HashMap<String, Long> linhasDeCodigoPorDesignRole) {
		long total = 0;
		if (linhasDeCodigoPorDesignRole == null)
			linhasDeCodigoPorDesignRole = new HashMap<>();

		for (ClassMetricResult classe : classes) {
			String architecturalRole = LimiarMetrica.DESIGN_ROLE_UNDEFINED;
			if (classe.isArchitecturalRole())
				architecturalRole = classe.getDesignRole();

			for (MethodMetricResult method : classe.getMetricsByMethod().values()) {
				total += method.getLinesOfCode();
				Long somaLocPorDesignRole = linhasDeCodigoPorDesignRole.get(architecturalRole);
				if (somaLocPorDesignRole == null) {
					linhasDeCodigoPorDesignRole.put(architecturalRole, new Long(method.getLinesOfCode()));
				} else {
					somaLocPorDesignRole += method.getLinesOfCode();
					linhasDeCodigoPorDesignRole.put(architecturalRole, somaLocPorDesignRole);
				}
			}
		}
		return total;
	}


}
