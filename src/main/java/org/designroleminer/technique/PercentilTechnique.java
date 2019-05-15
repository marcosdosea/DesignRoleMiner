package org.designroleminer.technique;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class PercentilTechnique extends AbstractTechnique {

	private int percentile;

	public PercentilTechnique(int percentile) {
		this.percentile = percentile;
	}

	/**
	 * Generate thresholds do LOC, CC, Efferent and NOP metrics using quartil of
	 * distribution of values
	 * 
	 * @param classes
	 * @param fileResultado
	 * @param quartil
	 */
	@Override
	public void generate(List<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRoleTechnique;LOC;CC;Efferent;NOP;");

		ArrayList<Integer> listaLOC = new ArrayList<>();
		ArrayList<Integer> listaCC = new ArrayList<>();
		ArrayList<Integer> listaEfferent = new ArrayList<>();
		ArrayList<Integer> listaNOP = new ArrayList<>();

		for (ClassMetricResult classe : classes) {
			for (MethodMetricResult method : classe.getMetricsByMethod().values()) {
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
		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + MedianaQuartis.percentil(listaLOC, percentile) + ";"
				+ MedianaQuartis.percentil(listaCC, percentile) + ";"
				+ MedianaQuartis.percentil(listaEfferent, percentile) + ";"
				+ MedianaQuartis.percentil(listaNOP, percentile) + ";");
	}

}

class MedianaQuartis {

	public static ArrayList<Integer> ordernarOrdemCrescente(ArrayList<Integer> metodosDesordenados) {
		ArrayList<Integer> metodosOrdenados = metodosDesordenados;
		Collections.sort(metodosOrdenados);
		return metodosOrdenados;
	}

	public static int calcularMediana(ArrayList<Integer> metodos) {
		if (metodos.size() == 0) {
			return 0;
		}
		int numeroElementosLista = metodos.size();
		if (numeroElementosLista % 2 == 0) {
			return (metodos.get(numeroElementosLista / 2) + metodos.get((numeroElementosLista / 2) - 1)) / 2;
		} else {
			return metodos.get((numeroElementosLista - 1) / 2);
		}

	}

	public static int percentil(ArrayList<Integer> metodosOrdenados, int porcentagemLimiar) {
		if (metodosOrdenados.size() == 0) {
			return 0;
		}
		double posicaoReal = ((double) porcentagemLimiar / 100) * (metodosOrdenados.size());
		int posicaoInteira = (int) posicaoReal;
		if (posicaoReal != posicaoInteira) {
			return metodosOrdenados.get(posicaoInteira);
		} else {
			return ((metodosOrdenados.get(posicaoInteira) + metodosOrdenados.get(posicaoInteira - 1)) / 2);
		}
	}

	public static boolean eCompreendidoPrimeiroTerceiroQuartil(int numero, int primeiroQuartil, int terceiroQuartil) {
		if (numero >= primeiroQuartil && numero <= terceiroQuartil) {
			return true;
		}
		return false;
	}

	public static boolean eMaiorQueTerceiroQuartil(int numero, int terceiroQuartil) {
		if (numero > terceiroQuartil) {
			return true;
		}
		return false;
	}
}

class Quartis {

	private ArrayList<Integer> listaMetrica;

	public Quartis(ArrayList<Integer> listaMetrica) {
		super();
		this.listaMetrica = listaMetrica;
		Collections.sort(listaMetrica);
	}

	public static int calcularMediana(ArrayList<Integer> metodos) {
		if (metodos.size() == 0) {
			return 0;
		}
		int numeroElementosLista = metodos.size();
		if (numeroElementosLista % 2 == 0) {
			return (metodos.get(numeroElementosLista / 2) + metodos.get((numeroElementosLista / 2) - 1)) / 2;
		} else {
			return metodos.get((numeroElementosLista - 1) / 2);
		}

	}

	public static int percentil(ArrayList<Integer> metodosOrdenados, int porcentagemLimiar) {
		if (metodosOrdenados.size() == 0) {
			return 0;
		}
		double posicaoReal = ((double) porcentagemLimiar / 100) * (metodosOrdenados.size());
		int posicaoInteira = (int) posicaoReal;
		if (posicaoReal != posicaoInteira) {
			return metodosOrdenados.get(posicaoInteira);
		} else {
			return ((metodosOrdenados.get(posicaoInteira) + metodosOrdenados.get(posicaoInteira - 1)) / 2);
		}
	}

	public static boolean eCompreendidoPrimeiroTerceiroQuartil(int numero, int primeiroQuartil, int terceiroQuartil) {
		if (numero >= primeiroQuartil && numero <= terceiroQuartil) {
			return true;
		}
		return false;
	}

	public static boolean eMaiorQueTerceiroQuartil(int numero, int terceiroQuartil) {
		if (numero > terceiroQuartil) {
			return true;
		}
		return false;
	}
}
