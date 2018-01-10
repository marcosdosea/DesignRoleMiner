package com.github.limiares;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.github.ck.CKNumber;
import com.github.ck.MethodMetrics;
import com.github.smelldetector.model.LimiarMetrica;

import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.persistence.csv.CSVFile;

public class GerenciadorLimiares {

	public void gerarDesignRoles(List<CKNumber> classes, String fileResultado) {

		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("Classe                              ;DesignRole                        ;Concorda?;");

		for (CKNumber classe : classes) {
			pm.write(classe.getClassName() + ";" + classe.getDesignRole() + ";" + "SIM;");
		}

	}

	public void gerarLimiarQuartil(List<CKNumber> classes, String fileResultado, int quartil) {

		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		ArrayList<Integer> listaLOC = new ArrayList<>();
		ArrayList<Integer> listaCC = new ArrayList<>();
		ArrayList<Integer> listaEfferent = new ArrayList<>();
		ArrayList<Integer> listaNOP = new ArrayList<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				listaLOC.add(method.getLinesOfCode());
				listaCC.add(method.getComplexity());
				listaEfferent.add(method.getEfferentCoupling());
				listaNOP.add(method.getNumberOfParameters());
			}
		}
		if (listaLOC.size() > 0) {
			listaLOC = MedianaQuartis.ordernarOrdemCrescente(listaLOC);
			listaCC = MedianaQuartis.ordernarOrdemCrescente(listaCC);
			listaEfferent = MedianaQuartis.ordernarOrdemCrescente(listaEfferent);
			listaNOP = MedianaQuartis.ordernarOrdemCrescente(listaNOP);
		}
		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + MedianaQuartis.percentil(listaLOC, quartil) + ";"
				+ MedianaQuartis.percentil(listaCC, quartil) + ";" + MedianaQuartis.percentil(listaEfferent, quartil)
				+ ";" + MedianaQuartis.percentil(listaNOP, quartil) + ";");

	}

	public void gerarLimiarAlves(List<CKNumber> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		HashMap<String, Long> linhasDeCodigoPorDesignRole = new HashMap<>();
		long totalLoc = obterTotalLinhasCodigoPorDesignRole(classes, linhasDeCodigoPorDesignRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
			}
		}

		Integer limiarLOC = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaLOC, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		Integer limiarCC = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaCC, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		Integer limiarEfferent = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaEfferent, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		Integer limiarNOP = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaNOP, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOC + ";" + limiarCC + ";" + limiarEfferent + ";"
				+ limiarNOP + ";");
	}

	public void gerarLimiarDoseaPercentil(List<CKNumber> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		HashMap<String, Long> linhasDeCodigoPorDesignRole = new HashMap<>();
		long totalLoc = obterTotalLinhasCodigoPorDesignRole(classes, linhasDeCodigoPorDesignRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
			}
		}

		LimiarMetrica limiarLOC = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaLOC, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		LimiarMetrica limiarCC = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaCC, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		LimiarMetrica limiarEfferent = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaEfferent, totalLoc,
				90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		LimiarMetrica limiarNOP = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaNOP, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOC.getLimiarMaximo() + ";"
				+ limiarCC.getLimiarMaximo() + ";" + limiarEfferent.getLimiarMaximo() + ";"
				+ limiarNOP.getLimiarMaximo() + ";");
	}

	public void gerarLimiarDoseaDesignRolePercentil(List<CKNumber> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		HashMap<String, Long> linhasDeCodigoPorDesignRole = new HashMap<>();
		long totalLoc = obterTotalLinhasCodigoPorDesignRole(classes, linhasDeCodigoPorDesignRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				if (!classe.getDesignRole().toUpperCase().equals(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_CC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_EC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + classe.getDesignRole());
				}
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
			}
		}

		LimiarMetrica limiarLOCUndefined = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaLOC, totalLoc,
				90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		LimiarMetrica limiarCCUndefined = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaCC, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		LimiarMetrica limiarEfferentUndefined = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaEfferent,
				totalLoc, 90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		LimiarMetrica limiarNOPUndefined = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaNOP, totalLoc,
				90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOCUndefined.getLimiarMaximo() + ";" + limiarCCUndefined.getLimiarMaximo() + ";"
				+ limiarEfferentUndefined.getLimiarMaximo() + ";" + limiarNOPUndefined.getLimiarMaximo() + ";");

		for (String designRole : linhasDeCodigoPorDesignRole.keySet()) {
			designRole = designRole.toUpperCase();
			if (!designRole.contains(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
				long totalLOCPorDesignRole = linhasDeCodigoPorDesignRole.get(designRole);
				LimiarMetrica limiarLOC = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaLOC,
						totalLOCPorDesignRole, 90, designRole, LimiarMetrica.METRICA_LOC);
				LimiarMetrica limiarCC = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaCC,
						totalLOCPorDesignRole, 90, designRole, LimiarMetrica.METRICA_CC);
				LimiarMetrica limiarEfferent = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaEfferent,
						totalLOCPorDesignRole, 90, designRole, LimiarMetrica.METRICA_EC);
				LimiarMetrica limiarNOP = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaNOP,
						totalLOCPorDesignRole, 90, designRole, LimiarMetrica.METRICA_NOP);

				// design roles pouco representativos ficam com limiares mais próximo da mediana
				float pesoLOC = (float) linhasDeCodigoPorDesignRole.get(designRole) / totalLoc;
				if (limiarLOC.getLimiarMaximo() < limiarLOCUndefined.getLimiarMedio())
					limiarLOC.setLimiarMaximo(Math.round((limiarLOC.getLimiarMaximo() * pesoLOC)
							+ (limiarLOCUndefined.getLimiarMedio() * (1 - pesoLOC))));
				if (limiarCC.getLimiarMaximo() < limiarCCUndefined.getLimiarMedio())
					limiarCC.setLimiarMaximo(Math.round((limiarCC.getLimiarMaximo() * pesoLOC)
							+ (limiarCCUndefined.getLimiarMedio() * (1 - pesoLOC))));
				if (limiarEfferent.getLimiarMaximo() < limiarEfferentUndefined.getLimiarMedio())
					limiarEfferent.setLimiarMaximo(Math.round((limiarEfferent.getLimiarMaximo() * pesoLOC)
							+ (limiarEfferentUndefined.getLimiarMedio() * (1 - pesoLOC))));
				if (limiarNOP.getLimiarMaximo() < limiarNOPUndefined.getLimiarMedio())
					limiarNOP.setLimiarMaximo(Math.round((limiarNOP.getLimiarMaximo() * pesoLOC)
							+ (limiarNOPUndefined.getLimiarMedio() * (1 - pesoLOC))));
				pm.write(designRole + ";" + limiarLOC.getLimiarMaximo() + ";" + limiarCC.getLimiarMaximo() + ";"
						+ limiarEfferent.getLimiarMaximo() + ";" + limiarNOP.getLimiarMaximo() + ";");
			}
		}
	}

	public void gerarLimiarDoseaOutlier(List<CKNumber> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		HashMap<String, Long> linhasDeCodigoPorDesignRole = new HashMap<>();
		long totalLoc = obterTotalLinhasCodigoPorDesignRole(classes, linhasDeCodigoPorDesignRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
			}
		}

		Limiares limiarLOC = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaLOC, totalLoc,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		Limiares limiarCC = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaCC, totalLoc,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		Limiares limiarEfferent = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaEfferent, totalLoc,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		Limiares limiarNOP = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaNOP, totalLoc,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOC.getOutlier() + ";" + limiarCC.getOutlier() + ";"
				+ limiarEfferent.getOutlier() + ";" + limiarNOP.getOutlier() + ";");
	}

	public void gerarLimiarDoseaDesignRoleOutlier(List<CKNumber> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		HashMap<String, Long> linhasDeCodigoPorDesignRole = new HashMap<>();
		long totalLoc = obterTotalLinhasCodigoPorDesignRole(classes, linhasDeCodigoPorDesignRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				if (!classe.getDesignRole().toUpperCase().equals(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_CC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_EC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
							method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + classe.getDesignRole());
				}
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);
			}
		}

		Integer limiarLOCUndefined = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaLOC, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		Integer limiarCCUndefined = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaCC, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		Integer limiarEfferentUndefined = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaEfferent, totalLoc,
				90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		Integer limiarNOPUndefined = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaNOP, totalLoc, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOCUndefined + ";" + limiarCCUndefined + ";"
				+ limiarEfferentUndefined + ";" + limiarNOPUndefined + ";");

		for (String designRole : linhasDeCodigoPorDesignRole.keySet()) {
			designRole = designRole.toUpperCase();
			if (!designRole.contains(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
				long totalLOCPorDesignRole = linhasDeCodigoPorDesignRole.get(designRole);
				Limiares limiarLOC = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaLOC, totalLOCPorDesignRole,
						designRole, LimiarMetrica.METRICA_LOC);
				Limiares limiarCC = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaCC, totalLOCPorDesignRole,
						designRole, LimiarMetrica.METRICA_CC);
				Limiares limiarEfferent = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaEfferent,
						totalLOCPorDesignRole, designRole, LimiarMetrica.METRICA_EC);
				Limiares limiarNOP = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaNOP, totalLOCPorDesignRole,
						designRole, LimiarMetrica.METRICA_NOP);

				pm.write(designRole + ";" + limiarLOC.getOutlier() + ";" + limiarCC.getOutlier() + ";"
						+ limiarEfferent.getOutlier() + ";" + limiarNOP.getOutlier() + ";");
			}
		}
	}

	public void gerarLimiarAniche(List<CKNumber> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		HashMap<String, Long> linhasDeCodigoPorArchitecturalRole = new HashMap<>();
		obterTotalLinhasCodigoPorArchitecturalRole(classes, linhasDeCodigoPorArchitecturalRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (CKNumber classe : classes) {
			String architecturalRole = LimiarMetrica.DESIGN_ROLE_UNDEFINED;
			if (classe.isArchitecturalRole())
				architecturalRole = classe.getDesignRole();

			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_LOC + architecturalRole);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						LimiarMetrica.METRICA_CC + architecturalRole);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_EC + architecturalRole);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), LimiarMetrica.METRICA_NOP + architecturalRole);
			}
		}

		for (String architecuturalRole : linhasDeCodigoPorArchitecturalRole.keySet()) {
			architecuturalRole = architecuturalRole.toUpperCase();
			long totalLOCPorArchitecturalRole = linhasDeCodigoPorArchitecturalRole.get(architecuturalRole);
			Integer limiarLOC = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaLOC,
					totalLOCPorArchitecturalRole, 90, architecuturalRole, LimiarMetrica.METRICA_LOC);
			Integer limiarCC = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaCC, totalLOCPorArchitecturalRole,
					90, architecuturalRole, LimiarMetrica.METRICA_CC);
			Integer limiarEfferent = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaEfferent,
					totalLOCPorArchitecturalRole, 90, architecuturalRole, LimiarMetrica.METRICA_EC);
			Integer limiarNOP = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaNOP,
					totalLOCPorArchitecturalRole, 90, architecuturalRole, LimiarMetrica.METRICA_NOP);
			pm.write(architecuturalRole + ";" + limiarLOC + ";" + limiarCC + ";" + limiarEfferent + ";" + limiarNOP
					+ ";");
		}
	}

	private void somaLOCPorValorMetrica(HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetrica,
			Integer valorMetrica, Integer linesOfCode, String designRole) {

		HashMap<Integer, BigDecimal> distribuicaoValorMetrica = distribuicaoCodigoPorMetrica.get(designRole);

		if (distribuicaoValorMetrica == null)
			distribuicaoValorMetrica = new HashMap<>();
		BigDecimal totalLOC = distribuicaoValorMetrica.get(valorMetrica);

		if (totalLOC == null) {
			distribuicaoValorMetrica.put(valorMetrica, new BigDecimal(linesOfCode));
			distribuicaoCodigoPorMetrica.put(designRole, distribuicaoValorMetrica);
		} else {
			distribuicaoValorMetrica.put(valorMetrica,
					totalLOC.add(new BigDecimal(linesOfCode), MathContext.DECIMAL128));
			distribuicaoCodigoPorMetrica.put(designRole, distribuicaoValorMetrica);
		}
	}

	private Integer obterLimiarMetricaPercentil(
			HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetrica, long totalLOC,
			Integer percentil, String designRole, String metrica) {
		HashMap<Integer, BigDecimal> valoresMetricas = distribuicaoCodigoPorMetrica.get(metrica + designRole);

		if (valoresMetricas != null) {
			ArrayList<Integer> listaOrdenadaMetrica = new ArrayList<Integer>(valoresMetricas.keySet());
			Collections.sort(listaOrdenadaMetrica);

			BigDecimal somaPeso = null;
			for (Integer valorMetrica : listaOrdenadaMetrica) {
				BigDecimal locPorValor = valoresMetricas.get(valorMetrica);
				BigDecimal peso = totalLOC > 0 ? locPorValor.divide(new BigDecimal(totalLOC), MathContext.DECIMAL128)
						: new BigDecimal(0);
				somaPeso = (somaPeso == null) ? peso : somaPeso.add(peso, MathContext.DECIMAL128);
				BigDecimal percentilFracao = new BigDecimal(percentil).divide(new BigDecimal(100),
						MathContext.DECIMAL128);
				if (somaPeso.compareTo(percentilFracao) >= 0) {
					return valorMetrica;
				}
			}
		}
		return 0;
	}

	private LimiarMetrica obterLimiarMetricaPercentilAcima(
			HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetrica, long totalLOC,
			Integer percentil, String designRole, String metrica) {
		HashMap<Integer, BigDecimal> valoresMetricas = distribuicaoCodigoPorMetrica.get(metrica + designRole);
	
		LimiarMetrica limiarMetrica = new LimiarMetrica();
		limiarMetrica.setDesignRole(designRole);
		limiarMetrica.setMetrica(metrica);
		if (valoresMetricas != null) {
			ArrayList<Integer> listaOrdenadaMetrica = new ArrayList<Integer>(valoresMetricas.keySet());
			Collections.sort(listaOrdenadaMetrica);

			BigDecimal percentilFracao = new BigDecimal(percentil).divide(new BigDecimal(100), MathContext.DECIMAL128);
			BigDecimal percentilMediana = new BigDecimal(50).divide(new BigDecimal(100), MathContext.DECIMAL128);
			BigDecimal somaPeso = null;
			if (listaOrdenadaMetrica.size() > 0)
				limiarMetrica.setLimiarMedio(listaOrdenadaMetrica.get(0));
			int indexLista = 0;
			for (Integer valorMetrica : listaOrdenadaMetrica) {
				BigDecimal locPorValor = valoresMetricas.get(valorMetrica);
				BigDecimal peso = totalLOC > 0 ? locPorValor.divide(new BigDecimal(totalLOC), MathContext.DECIMAL128)
						: new BigDecimal(0);
				somaPeso = (somaPeso == null) ? peso : somaPeso.add(peso, MathContext.DECIMAL128);
				if (somaPeso.compareTo(percentilMediana) <= 0)
					limiarMetrica.setLimiarMedio(listaOrdenadaMetrica.get(indexLista));

				indexLista++;
				if (somaPeso.compareTo(percentilFracao) >= 0)
					break;
			}
			// dessa forma pega sempre o valor seguinte quando o percentil é alcançado
			if (indexLista < listaOrdenadaMetrica.size())
				limiarMetrica.setLimiarMaximo(listaOrdenadaMetrica.get(indexLista));
			else
				limiarMetrica.setLimiarMaximo(listaOrdenadaMetrica.get(--indexLista));
		}
		return limiarMetrica;
	}

	private Limiares obterLimiarMetricaOutlier(
			HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetrica, long totalLOC,
			String designRole, String metrica) {

		Limiares limiares = new Limiares(0, 0);

		HashMap<Integer, BigDecimal> valoresMetricas = distribuicaoCodigoPorMetrica.get(metrica + designRole);

		ArrayList<Integer> listaOrdenadaMetrica = new ArrayList<Integer>(valoresMetricas.keySet());
		Collections.sort(listaOrdenadaMetrica);

		BigDecimal percentilPrimeiroQuartil = new BigDecimal(25).divide(new BigDecimal(100), MathContext.DECIMAL128);
		BigDecimal percentilTerceiroQuartil = new BigDecimal(75).divide(new BigDecimal(100), MathContext.DECIMAL128);
		BigDecimal somaPeso = null;

		for (Integer valorMetrica : listaOrdenadaMetrica) {
			BigDecimal locPorValor = valoresMetricas.get(valorMetrica);
			BigDecimal peso = totalLOC > 0 ? locPorValor.divide(new BigDecimal(totalLOC), MathContext.DECIMAL128)
					: new BigDecimal(0);
			somaPeso = (somaPeso == null) ? peso : somaPeso.add(peso, MathContext.DECIMAL128);
			if ((somaPeso.compareTo(percentilPrimeiroQuartil) >= 0) && (limiares.getLimiarPrimeiroQuartil() == 0)) {
				limiares.setLimiarPrimeiroQuartil(valorMetrica);
				limiares.setLimiarTerceiroQuartil(valorMetrica);
			} else if (somaPeso.compareTo(percentilTerceiroQuartil) >= 0) {
				limiares.setLimiarTerceiroQuartil(valorMetrica);
				break;
			}
		}

		return limiares;
	}

	private long obterTotalLinhasCodigoPorDesignRole(List<CKNumber> classes,
			HashMap<String, Long> linhasDeCodigoPorDesignRole) {
		long total = 0;
		if (linhasDeCodigoPorDesignRole == null)
			linhasDeCodigoPorDesignRole = new HashMap<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				total += method.getLinesOfCode();
				Long somaLocPorDesignRole = linhasDeCodigoPorDesignRole.get(classe.getDesignRole());
				if (somaLocPorDesignRole == null) {
					linhasDeCodigoPorDesignRole.put(classe.getDesignRole(), new Long(method.getLinesOfCode()));
				} else {
					somaLocPorDesignRole += method.getLinesOfCode();
					linhasDeCodigoPorDesignRole.put(classe.getDesignRole(), somaLocPorDesignRole);
				}
			}
		}
		return total;
	}

	private long obterTotalLinhasCodigoPorArchitecturalRole(List<CKNumber> classes,
			HashMap<String, Long> linhasDeCodigoPorDesignRole) {
		long total = 0;
		if (linhasDeCodigoPorDesignRole == null)
			linhasDeCodigoPorDesignRole = new HashMap<>();

		for (CKNumber classe : classes) {
			String architecturalRole = LimiarMetrica.DESIGN_ROLE_UNDEFINED;
			if (classe.isArchitecturalRole())
				architecturalRole = classe.getDesignRole();

			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
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

	private class Limiares {
		private int limiarPrimeiroQuartil;
		private int limiarTerceiroQuartil;

		public Limiares(int limiarPrimeiroQuartil, int limiarTerceiroQuartil) {
			super();
			this.limiarPrimeiroQuartil = limiarPrimeiroQuartil;
			this.limiarTerceiroQuartil = limiarTerceiroQuartil;
		}

		public int getLimiarPrimeiroQuartil() {
			return limiarPrimeiroQuartil;
		}

		public void setLimiarPrimeiroQuartil(int limiarPrimeiroQuartil) {
			this.limiarPrimeiroQuartil = limiarPrimeiroQuartil;
		}

		public int getLimiarTerceiroQuartil() {
			return limiarTerceiroQuartil;
		}

		public void setLimiarTerceiroQuartil(int limiarTerceiroQuartil) {
			this.limiarTerceiroQuartil = limiarTerceiroQuartil;
		}

		public long getOutlier() {
			long outlier = Math.round(
					getLimiarTerceiroQuartil() + ((getLimiarTerceiroQuartil() - getLimiarPrimeiroQuartil()) * 1.5));
			if (outlier <= 0)
				return 0;

			return outlier;
		}
	}

}
