package org.designroleminer.threshold;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class AlvesTechnique extends AbstractTechnique {

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

		for (ClassMetricResult classe : classes) {
			for (MethodMetricResult method : classe.getMetricsByMethod().values()) {
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
			}
		}

		LimiarMetrica limiarLOC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC, false);
		LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC, false);
		LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC, false);
		LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLoc, 5, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP, false);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOC.getLimiarMaximo() + ";"
				+ limiarCC.getLimiarMaximo() + ";" + limiarEfferent.getLimiarMaximo() + ";"
				+ limiarNOP.getLimiarMaximo() + ";");
	}
}
