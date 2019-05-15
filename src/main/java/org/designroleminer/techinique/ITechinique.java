package org.designroleminer.techinique;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;

public abstract class ITechinique {

	/**
	 * Generate some result from collected metrics
	 * 
	 * @param classes
	 * @param fileResultado
	 */
	abstract void generate(List<ClassMetricResult> classes, String fileResultado);

	/**
	 * 
	 * @param distribuicaoCodigoPorMetrica
	 * @param valorMetrica
	 * @param valorAgrupar
	 * @param designRole
	 */
	protected void agrupaPorValorMetrica(HashMap<String, HashMap<Integer, BigDecimal>> distribuicaoCodigoPorMetrica,
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

	/**
	 * 
	 * @param distribuicaoCodigoPorMetrica
	 * @param totalValorAgrupado
	 * @param percentilMinimo
	 * @param percentilMedio
	 * @param percentilMaximo
	 * @param designRole
	 * @param metrica
	 * @return
	 */
	protected LimiarMetrica obterLimiaresMetrica(
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

	public long obterTotalLinhasCodigoPorDesignRole(List<ClassMetricResult> classes,
			HashMap<String, Long> linhasDeCodigoPorDesignRole) {
		long total = 0;
		if (linhasDeCodigoPorDesignRole == null)
			linhasDeCodigoPorDesignRole = new HashMap<>();

		for (ClassMetricResult classe : classes) {
			for (MethodMetricResult method : classe.getMetricsByMethod().values()) {
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
}
