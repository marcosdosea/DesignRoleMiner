package com.github.limiares;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.github.ck.CK;
import com.github.ck.CKNumber;
import com.github.ck.CKReport;
import com.github.ck.MethodMetrics;
import com.github.ck.Utils;
import com.github.smelldetector.model.LimiarMetrica;

import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.persistence.csv.CSVFile;

public class GerenciadorLimiares {

	public ArrayList<CKNumber> getMetricasProjetos(ArrayList<String> projetos) {
		ArrayList<CKNumber> listaClasses = new ArrayList<>();
		for (String path : projetos) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
			String dataHora = sf.format(Calendar.getInstance().getTime());

			System.out.println("[" + dataHora + "] Extracting metrics from project " + path + "...");
			CKReport report = new CK().calculate(path);
			
			
			System.out.println("Number of classes: " + report.all().size());
			Collection<CKNumber> metricasClasses = report.all();
			long totalMetodos = 0;
			long totalLoc = 0;
			for(CKNumber ckNumber: metricasClasses) {
				totalMetodos += ckNumber.getNom();
				totalLoc += Utils.countLineNumbers(Utils.readFile(new File(ckNumber.getFile())));
			}
			
			System.out.println("Number of methods: " + totalMetodos);
			System.out.println("Total Lines of Code: " + totalLoc);
			
			listaClasses.addAll(report.all());
		}

		return listaClasses;
	}

	public ArrayList<String> lerProjetos(String nomeArquivo) {
		ArrayList<String> projetos = new ArrayList<>();
		String arquivo = System.getProperty("user.dir") + "\\" + nomeArquivo;
		File file = new File(arquivo);
		try {
			if (file.exists()) {
				Scanner scanner = new Scanner(file);
				while (scanner.hasNext()) {
					projetos.add(scanner.next());
				}
				scanner.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrados: " + arquivo);
		}
		return projetos;
	}

	/**
	 * Generate sheet with design role assigned to each class
	 * 
	 * @param classes
	 * @param fileResultado
	 */
	public void gerarDesignRoles(List<CKNumber> classes, String fileResultado) {

		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("Classe                              ;DesignRole                        ;Concorda?;");

		for (CKNumber classe : classes) {
			pm.write(classe.getClassName() + ";" + classe.getDesignRole() + ";" + "SIM;");
		}

	}

	/**
	 * Generate thresholds do LOC, CC, Efferent and NOP metrics using quartil of
	 * distribution of values
	 * 
	 * @param classes
	 * @param fileResultado
	 * @param quartil
	 */
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

	/**
	 * Para cada valor encontrado da métrica soma o LOC dos métodos encontrados com
	 * o mesmo valor. Em seguida, ordena-se essa distribuição pelo valor dessas
	 * métricas. O limiar da métrica será correspondente ao valor da métrica que
	 * corresponde a X% do LOC do sistema.
	 * 
	 * @param classes
	 * @param fileResultado
	 */
	public void gerarLimiarDoseaReference(List<CKNumber> classes, String fileResultado) {
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
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		// NOP Metric
		LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLoc, 3, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOC.getLimiarMaximo() + ";"
				+ limiarCC.getLimiarMaximo() + ";" + limiarEfferent.getLimiarMaximo() + ";"
				+ limiarNOP.getLimiarMaximo() + ";");
	}

	public void gerarLimiarDoseaReferenceDesignRole(List<CKNumber> classes, String fileResultado) {
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
		}

		LimiarMetrica limiarLOCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		LimiarMetrica limiarCCUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		LimiarMetrica limiarEfferentUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent, totalLoc, 5,
				70, 90, LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		LimiarMetrica limiarNOPUndefined = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLoc, 3, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOCUndefined.getLimiarMaximo() + ";"
				+ limiarCCUndefined.getLimiarMaximo() + ";" + limiarEfferentUndefined.getLimiarMaximo() + ";"
				+ limiarNOPUndefined.getLimiarMaximo() + ";");

		for (String designRole : linhasDeCodigoPorDesignRole.keySet()) {
			designRole = designRole.toUpperCase();
			if (!designRole.contains(LimiarMetrica.DESIGN_ROLE_UNDEFINED)) {
				long totalLOCPorDesignRole = linhasDeCodigoPorDesignRole.get(designRole);
				LimiarMetrica limiarLOC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaLOC, totalLOCPorDesignRole,
						5, 70, 90, designRole, LimiarMetrica.METRICA_LOC);
				LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLOCPorDesignRole, 5,
						70, 90, designRole, LimiarMetrica.METRICA_CC);
				LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent,
						totalLOCPorDesignRole, 5, 70, 90, designRole, LimiarMetrica.METRICA_EC);
				LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLOCPorDesignRole,
						3, 90, 95, designRole, LimiarMetrica.METRICA_NOP);

				// para limiares muitos baixos assume o limiar médio da aplicacao
				if (limiarLOC.getLimiarMaximo() < limiarLOCUndefined.getLimiarMinimo())
					limiarLOC.setLimiarMaximo(limiarLOCUndefined.getLimiarMinimo());
				if (limiarCC.getLimiarMaximo() < limiarCCUndefined.getLimiarMinimo())
					limiarCC.setLimiarMaximo(limiarCCUndefined.getLimiarMinimo());
				if (limiarEfferent.getLimiarMaximo() < limiarEfferentUndefined.getLimiarMinimo())
					limiarEfferent.setLimiarMaximo(limiarEfferentUndefined.getLimiarMinimo());
				if (limiarNOP.getLimiarMaximo() < limiarNOPUndefined.getLimiarMinimo())
					limiarNOP.setLimiarMaximo(limiarNOPUndefined.getLimiarMinimo());
				pm.write(designRole + ";" + limiarLOC.getLimiarMaximo() + ";" + limiarCC.getLimiarMaximo() + ";"
						+ limiarEfferent.getLimiarMaximo() + ";" + limiarNOP.getLimiarMaximo() + ";");
			}
		}
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
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_LOC);
		LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_CC);
		LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent, totalLoc, 5, 70, 90,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_EC);
		LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP, totalLoc, 5, 90, 95,
				LimiarMetrica.DESIGN_ROLE_UNDEFINED, LimiarMetrica.METRICA_NOP);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOC.getLimiarMaximo() + ";"
				+ limiarCC.getLimiarMaximo() + ";" + limiarEfferent.getLimiarMaximo() + ";"
				+ limiarNOP.getLimiarMaximo() + ";");
	}

	public void gerarLimiarVale(List<CKNumber> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		HashMap<String, Long> metodosPorDesignRole = new HashMap<>();
		long totalMetodos = obterTotalMetodosPorDesignRole(classes, metodosPorDesignRole);

		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaLOC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaCC = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaEfferent = new HashMap<>();
		HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetricaNOP = new HashMap<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
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
					totalLOCPorArchitecturalRole, 5, 70, 90, architecuturalRole, LimiarMetrica.METRICA_LOC);
			LimiarMetrica limiarCC = obterLimiaresMetrica(distribuicaoCodigoPorMetricaCC, totalLOCPorArchitecturalRole,
					5, 70, 90, architecuturalRole, LimiarMetrica.METRICA_CC);
			LimiarMetrica limiarEfferent = obterLimiaresMetrica(distribuicaoCodigoPorMetricaEfferent,
					totalLOCPorArchitecturalRole, 5, 70, 90, architecuturalRole, LimiarMetrica.METRICA_EC);
			LimiarMetrica limiarNOP = obterLimiaresMetrica(distribuicaoCodigoPorMetricaNOP,
					totalLOCPorArchitecturalRole, 5, 90, 95, architecuturalRole, LimiarMetrica.METRICA_NOP);
			pm.write(architecuturalRole + ";" + limiarLOC.getLimiarMaximo() + ";" + limiarCC.getLimiarMaximo() + ";"
					+ limiarEfferent.getLimiarMaximo() + ";" + limiarNOP.getLimiarMaximo() + ";");
		}
	}

	private void agrupaPorValorMetrica(HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetrica,
			Integer valorMetrica, Integer valorAgrupar, String designRole) {

		HashMap<Integer, BigDecimal> distribuicaoValorMetrica = distribuicaoCodigoPorMetrica.get(designRole);

		if (distribuicaoValorMetrica == null)
			distribuicaoValorMetrica = new HashMap<>();
		BigDecimal totalValorAgrupado = distribuicaoValorMetrica.get(valorMetrica);

		if (totalValorAgrupado == null) {
			distribuicaoValorMetrica.put(valorMetrica, new BigDecimal(valorAgrupar));
			distribuicaoCodigoPorMetrica.put(designRole, distribuicaoValorMetrica);
		} else {
			distribuicaoValorMetrica.put(valorMetrica,
					totalValorAgrupado.add(new BigDecimal(valorAgrupar), MathContext.DECIMAL128));
			distribuicaoCodigoPorMetrica.put(designRole, distribuicaoValorMetrica);
		}
	}

	private LimiarMetrica obterLimiaresMetrica(
			HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetrica, long totalValorAgrupado,
			Integer percentilMinimo, Integer percentilMedio, Integer percentilMaximo, String designRole,
			String metrica) {
		HashMap<Integer, BigDecimal> valoresMetricas = distribuicaoCodigoPorMetrica.get(metrica + designRole);

		LimiarMetrica limiarMetrica = new LimiarMetrica();
		limiarMetrica.setDesignRole(designRole);
		limiarMetrica.setMetrica(metrica);
		if (valoresMetricas != null) {
			ArrayList<Integer> listaOrdenadaMetrica = new ArrayList<Integer>(valoresMetricas.keySet());
			Collections.sort(listaOrdenadaMetrica);

			BigDecimal pMinimo = new BigDecimal(percentilMinimo).divide(new BigDecimal(100), MathContext.DECIMAL128);
			BigDecimal pMedio = new BigDecimal(percentilMedio).divide(new BigDecimal(100), MathContext.DECIMAL128);
			BigDecimal pMaximo = new BigDecimal(percentilMaximo).divide(new BigDecimal(100), MathContext.DECIMAL128);
			BigDecimal somaPeso = null;
			if (listaOrdenadaMetrica.size() > 0) {
				limiarMetrica.setLimiarMinimo(listaOrdenadaMetrica.get(0));
				limiarMetrica.setLimiarMedio(listaOrdenadaMetrica.get(0));
				limiarMetrica.setLimiarMaximo(listaOrdenadaMetrica.get(0));
			}
			int indexLista = 0;
			for (Integer valorMetrica : listaOrdenadaMetrica) {
				BigDecimal valorAgrupado = valoresMetricas.get(valorMetrica);
				BigDecimal peso = totalValorAgrupado > 0
						? valorAgrupado.divide(new BigDecimal(totalValorAgrupado), MathContext.DECIMAL128)
						: new BigDecimal(0);
				somaPeso = (somaPeso == null) ? peso : somaPeso.add(peso, MathContext.DECIMAL128);
				if (somaPeso.compareTo(pMinimo) <= 0)
					limiarMetrica.setLimiarMinimo(listaOrdenadaMetrica.get(indexLista));
				if (somaPeso.compareTo(pMedio) <= 0)
					limiarMetrica.setLimiarMedio(listaOrdenadaMetrica.get(indexLista));
				if (somaPeso.compareTo(pMaximo) <= 0)
					limiarMetrica.setLimiarMaximo(listaOrdenadaMetrica.get(indexLista));
				if (somaPeso.compareTo(pMaximo) >= 0)
					break;
				indexLista++;
			}
		}
		return limiarMetrica;
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

	private long obterTotalMetodosPorDesignRole(List<CKNumber> classes, HashMap<String, Long> metodosPorDesignRole) {
		long total = 0;
		if (metodosPorDesignRole == null)
			metodosPorDesignRole = new HashMap<>();

		for (CKNumber classe : classes) {
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

}
