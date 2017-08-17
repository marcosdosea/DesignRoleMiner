package br.ufba.limiares;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.MethodMetrics;

import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.persistence.csv.CSVFile;

public class GerenciadorLimiares {
	static final String DESIGN_ROLE_UNDEFINED = "UNDEFINED";
	static final String METRICA_LOC = "LOC";
	static final String METRICA_CC = "CC";
	static final String METRICA_EC = "EC";
	static final String METRICA_NOP = "NOP";

	public void gerarDesignRoles(List<CKNumber> classes, String fileResultado) {

		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("Classe;DesignRole;NOM;LOCC;Concorda?;");

		for (CKNumber classe : classes) {
			long totalLoc = 0;
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				totalLoc += method.getLinesOfCode();
			}
			pm.write(classe.getClassName() + ";" + classe.getDesignRole() + ";" + classe.getNom() + ";" + totalLoc
					+ ";SIM;");
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
		pm.write(DESIGN_ROLE_UNDEFINED + ";" + MedianaQuartis.percentil(listaLOC, quartil) + ";"
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
						method.getLinesOfCode(), METRICA_LOC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						METRICA_CC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), METRICA_EC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), METRICA_NOP + DESIGN_ROLE_UNDEFINED);
			}
		}

		Integer limiarLOC = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaLOC, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_LOC);
		Integer limiarCC = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaCC, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_CC);
		Integer limiarEfferent = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaEfferent, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_EC);
		Integer limiarNOP = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaNOP, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_NOP);

		pm.write(DESIGN_ROLE_UNDEFINED + ";" + limiarLOC + ";" + limiarCC + ";" + limiarEfferent + ";" + limiarNOP
				+ ";");
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
						method.getLinesOfCode(), METRICA_LOC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						METRICA_CC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), METRICA_EC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), METRICA_NOP + DESIGN_ROLE_UNDEFINED);
			}
		}

		Integer limiarLOC = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaLOC, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_LOC);
		Integer limiarCC = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaCC, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_CC);
		Integer limiarEfferent = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaEfferent, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_EC);
		Integer limiarNOP = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaNOP, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_NOP);

		pm.write(DESIGN_ROLE_UNDEFINED + ";" + limiarLOC + ";" + limiarCC + ";" + limiarEfferent + ";" + limiarNOP
				+ ";");
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
				if (!classe.getDesignRole().toUpperCase().equals(DESIGN_ROLE_UNDEFINED)) {
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
							method.getLinesOfCode(), METRICA_LOC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(),
							method.getLinesOfCode(), METRICA_CC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
							method.getLinesOfCode(), METRICA_EC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
							method.getLinesOfCode(), METRICA_NOP + classe.getDesignRole());
				}
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), METRICA_LOC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						METRICA_CC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), METRICA_EC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), METRICA_NOP + DESIGN_ROLE_UNDEFINED);
			}
		}

		Integer limiarLOCUndefined = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaLOC, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_LOC);
		Integer limiarCCUndefined = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaCC, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_CC);
		Integer limiarEfferentUndefined = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaEfferent, totalLoc,
				90, DESIGN_ROLE_UNDEFINED, METRICA_EC);
		Integer limiarNOPUndefined = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaNOP, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_NOP);

		pm.write(DESIGN_ROLE_UNDEFINED + ";" + limiarLOCUndefined + ";" + limiarCCUndefined + ";"
				+ limiarEfferentUndefined + ";" + limiarNOPUndefined + ";");

		for (String designRole : linhasDeCodigoPorDesignRole.keySet()) {
			designRole = designRole.toUpperCase();
			if (!designRole.contains(DESIGN_ROLE_UNDEFINED)) {
				long totalLOCPorDesignRole = linhasDeCodigoPorDesignRole.get(designRole);
				Integer limiarLOC = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaLOC, totalLOCPorDesignRole,
						90, designRole, METRICA_LOC);
				Integer limiarCC = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaCC, totalLOCPorDesignRole,
						90, designRole, METRICA_CC);
				Integer limiarEfferent = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaEfferent,
						totalLOCPorDesignRole, 90, designRole, METRICA_EC);
				Integer limiarNOP = obterLimiarMetricaPercentilAcima(distribuicaoCodigoPorMetricaNOP, totalLOCPorDesignRole,
						90, designRole, METRICA_NOP);

//				if (limiarLOC < limiarLOCUndefined) {
//					float pesoLOC = (float) linhasDeCodigoPorDesignRole.get(designRole) / totalLoc;
//					limiarLOC = Math.round((limiarLOC * pesoLOC) + (limiarLOCUndefined * (1-pesoLOC))); 
//				}
					
							// limiarLOC = limiarLOC < limiarLOCUndefined ? limiarLOCUndefined : limiarLOC;
							// limiarEfferent = limiarEfferent < limiarEfferentUndefined ?
							// limiarEfferentUndefined : limiarEfferent;
							// limiarCC = limiarCC < limiarCCUndefined ? limiarCCUndefined : limiarCC;
							// limiarNOP = limiarNOP < limiarNOPUndefined ? limiarNOPUndefined : limiarNOP;

							pm.write(designRole + ";" + limiarLOC + ";" + limiarCC + ";" + limiarEfferent + ";"
									+ limiarNOP + ";");
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
						method.getLinesOfCode(), METRICA_LOC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						METRICA_CC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), METRICA_EC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), METRICA_NOP + DESIGN_ROLE_UNDEFINED);
			}
		}

		Limiares limiarLOC = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaLOC, totalLoc, DESIGN_ROLE_UNDEFINED,
				METRICA_LOC);
		Limiares limiarCC = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaCC, totalLoc, DESIGN_ROLE_UNDEFINED,
				METRICA_CC);
		Limiares limiarEfferent = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaEfferent, totalLoc,
				DESIGN_ROLE_UNDEFINED, METRICA_EC);
		Limiares limiarNOP = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaNOP, totalLoc, DESIGN_ROLE_UNDEFINED,
				METRICA_NOP);

		pm.write(DESIGN_ROLE_UNDEFINED + ";" + limiarLOC.getOutlier() + ";" + limiarCC.getOutlier() + ";"
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
				if (!classe.getDesignRole().toUpperCase().equals(DESIGN_ROLE_UNDEFINED)) {
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
							method.getLinesOfCode(), METRICA_LOC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(),
							method.getLinesOfCode(), METRICA_CC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
							method.getLinesOfCode(), METRICA_EC + classe.getDesignRole());
					somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
							method.getLinesOfCode(), METRICA_NOP + classe.getDesignRole());
				}
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), METRICA_LOC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						METRICA_CC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), METRICA_EC + DESIGN_ROLE_UNDEFINED);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), METRICA_NOP + DESIGN_ROLE_UNDEFINED);
			}
		}

		Integer limiarLOCUndefined = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaLOC, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_LOC);
		Integer limiarCCUndefined = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaCC, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_CC);
		Integer limiarEfferentUndefined = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaEfferent, totalLoc,
				90, DESIGN_ROLE_UNDEFINED, METRICA_EC);
		Integer limiarNOPUndefined = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaNOP, totalLoc, 90,
				DESIGN_ROLE_UNDEFINED, METRICA_NOP);

		pm.write(DESIGN_ROLE_UNDEFINED + ";" + limiarLOCUndefined + ";" + limiarCCUndefined + ";"
				+ limiarEfferentUndefined + ";" + limiarNOPUndefined + ";");

		for (String designRole : linhasDeCodigoPorDesignRole.keySet()) {
			designRole = designRole.toUpperCase();
			if (!designRole.contains(DESIGN_ROLE_UNDEFINED)) {
				long totalLOCPorDesignRole = linhasDeCodigoPorDesignRole.get(designRole);
				Limiares limiarLOC = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaLOC, totalLOCPorDesignRole,
						designRole, METRICA_LOC);
				Limiares limiarCC = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaCC, totalLOCPorDesignRole,
						designRole, METRICA_CC);
				Limiares limiarEfferent = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaEfferent,
						totalLOCPorDesignRole, designRole, METRICA_EC);
				Limiares limiarNOP = obterLimiarMetricaOutlier(distribuicaoCodigoPorMetricaNOP, totalLOCPorDesignRole,
						designRole, METRICA_NOP);

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
			String architecturalRole = DESIGN_ROLE_UNDEFINED;
			if (classe.isArchitecturalRole())
				architecturalRole = classe.getDesignRole();

			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaLOC, method.getLinesOfCode(),
						method.getLinesOfCode(), METRICA_LOC + architecturalRole);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaCC, method.getComplexity(), method.getLinesOfCode(),
						METRICA_CC + architecturalRole);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaEfferent, method.getEfferentCoupling(),
						method.getLinesOfCode(), METRICA_EC + architecturalRole);
				somaLOCPorValorMetrica(distribuicaoCodigoPorMetricaNOP, method.getNumberOfParameters(),
						method.getLinesOfCode(), METRICA_NOP + architecturalRole);
			}
		}

		for (String architecuturalRole : linhasDeCodigoPorArchitecturalRole.keySet()) {
			architecuturalRole = architecuturalRole.toUpperCase();
			long totalLOCPorArchitecturalRole = linhasDeCodigoPorArchitecturalRole.get(architecuturalRole);
			Integer limiarLOC = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaLOC,
					totalLOCPorArchitecturalRole, 90, architecuturalRole, METRICA_LOC);
			Integer limiarCC = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaCC, totalLOCPorArchitecturalRole,
					90, architecuturalRole, METRICA_CC);
			Integer limiarEfferent = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaEfferent,
					totalLOCPorArchitecturalRole, 90, architecuturalRole, METRICA_EC);
			Integer limiarNOP = obterLimiarMetricaPercentil(distribuicaoCodigoPorMetricaNOP,
					totalLOCPorArchitecturalRole, 90, architecuturalRole, METRICA_NOP);
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

	private Integer obterLimiarMetricaPercentilAcima(
			HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetrica, long totalLOC,
			Integer percentil, String designRole, String metrica) {
		HashMap<Integer, BigDecimal> valoresMetricas = distribuicaoCodigoPorMetrica.get(metrica + designRole);

		if (valoresMetricas != null) {
			ArrayList<Integer> listaOrdenadaMetrica = new ArrayList<Integer>(valoresMetricas.keySet());
			Collections.sort(listaOrdenadaMetrica);

			BigDecimal somaPeso = null;
			//Integer valorMetricaPercentil90 = 0;
			int indexLista = 0;
			for (Integer valorMetrica : listaOrdenadaMetrica) {
				BigDecimal locPorValor = valoresMetricas.get(valorMetrica);
				BigDecimal peso = totalLOC > 0 ? locPorValor.divide(new BigDecimal(totalLOC), MathContext.DECIMAL128)
						: new BigDecimal(0);
				somaPeso = (somaPeso == null) ? peso : somaPeso.add(peso, MathContext.DECIMAL128);
				BigDecimal percentilFracao = new BigDecimal(percentil).divide(new BigDecimal(100),
						MathContext.DECIMAL128);
				indexLista++;
				if (somaPeso.compareTo(percentilFracao) >= 0) {
					break;
				}
			}
			// dessa forma pega sempre o valor seguinte quando o percentil é alcançado
			if (indexLista < listaOrdenadaMetrica.size())
				return listaOrdenadaMetrica.get(indexLista);
			return listaOrdenadaMetrica.get(--indexLista);
		}
		return 0;
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
			String architecturalRole = DESIGN_ROLE_UNDEFINED;
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
