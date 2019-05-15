package org.designroleminer.technique;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class ValeTechnique extends AbstractTechnique {

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

		HashMap<String, Long> metodosPorDesignRole = new HashMap<>();
		long totalMetodos = obterTotalMetodosPorDesignRole(classes, metodosPorDesignRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (ClassMetricResult classe : classes) {
			for (MethodMetricResult method : classe.getMetricsByMethod().values()) {
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(), 1,
						LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), 1,
						LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(), 1,
						LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				agrupaPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(), 1,
						LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
			}
		}

		LimiarMetrica limiarLOC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalMetodos, 3, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalMetodos, 3, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent, totalMetodos, 3, 90,
				95, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalMetodos, 3, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOC.getLimiarMaximo() + ";"
				+ limiarCC.getLimiarMaximo() + ";" + limiarEfferent.getLimiarMaximo() + ";"
				+ limiarNOP.getLimiarMaximo() + ";");
	}
	
	private long obterTotalMetodosPorDesignRole(List<ClassMetricResult> classes, HashMap<String, Long> metodosPorDesignRole) {
		long total = 0;
		if (metodosPorDesignRole == null)
			metodosPorDesignRole = new HashMap<>();

		for (ClassMetricResult classe : classes) {
			total += classe.getNom();
			Long somaMetodosPorDesignRole = metodosPorDesignRole.get(classe.getDesignRole());
			if (somaMetodosPorDesignRole == null) {
				metodosPorDesignRole.put(classe.getDesignRole(), new Long(classe.getNom()));
			} else {
				somaMetodosPorDesignRole += classe.getNom();
				metodosPorDesignRole.put(classe.getDesignRole(), somaMetodosPorDesignRole);
			}
		}
		return total;
	}


}
