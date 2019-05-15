package org.designroleminer.similarity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.technique.DesignRoleTechnique;
import org.designroleminer.technique.TechniqueExecutor;

public class SimilaritySystems {

	public float calculate(ArrayList<ClassMetricResult> projeto1, ArrayList<ClassMetricResult> projeto2) {

		DesignRoleTechnique designRoleTechinique = new DesignRoleTechnique();

		float similaridade = (float) 0;

		HashMap<String, Long> linhasDeCodigoPorDesignRoleProjeto1 = new HashMap<String, Long>();
		Map<CharSequence, Integer> drProjeto1 = countDesignRoles(projeto1); 
		Long totalLocProjeto1 = designRoleTechinique.obterTotalLinhasCodigoPorDesignRole(projeto1, linhasDeCodigoPorDesignRoleProjeto1);
		
		if (linhasDeCodigoPorDesignRoleProjeto1.get("TEST") != null)
			totalLocProjeto1 -= linhasDeCodigoPorDesignRoleProjeto1.remove("TEST");
		if (linhasDeCodigoPorDesignRoleProjeto1.get("UNDEFINED") != null)
			totalLocProjeto1 -= linhasDeCodigoPorDesignRoleProjeto1.remove("UNDEFINED");
		
		
		HashMap<String, Long> linhasDeCodigoPorDesignRoleProjeto2 = new HashMap<>();
		Map<CharSequence, Integer> drProjeto2 = countDesignRoles(projeto2); 
		Long totalLocProjeto2 = designRoleTechinique.obterTotalLinhasCodigoPorDesignRole(projeto2, linhasDeCodigoPorDesignRoleProjeto2);
		if (linhasDeCodigoPorDesignRoleProjeto2.get("TEST") != null)
			totalLocProjeto2 -= linhasDeCodigoPorDesignRoleProjeto2.remove("TEST");
		if (linhasDeCodigoPorDesignRoleProjeto2.get("UNDEFINED") != null)
			totalLocProjeto2 -= linhasDeCodigoPorDesignRoleProjeto2.remove("UNDEFINED");


		for (CharSequence key : drProjeto1.keySet()) {
			if (!key.equals("TEST") && !key.equals("UNDEFINED")) {
				if (drProjeto2.containsKey(key)) {
					if (linhasDeCodigoPorDesignRoleProjeto1.get(key) != null)
						similaridade += (float) linhasDeCodigoPorDesignRoleProjeto1.get(key) / totalLocProjeto1;
				} else {
					if (linhasDeCodigoPorDesignRoleProjeto1.get(key) != null)
						similaridade -= (float) linhasDeCodigoPorDesignRoleProjeto1.get(key) / totalLocProjeto1;
				}
			}
		}

		for (CharSequence key : drProjeto2.keySet()) {
			if (!key.equals("TEST") && !key.equals("UNDEFINED")) {
				if (drProjeto1.containsKey(key)) {
					if (linhasDeCodigoPorDesignRoleProjeto2.get(key) != null)
						similaridade += (float) linhasDeCodigoPorDesignRoleProjeto2.get(key) / totalLocProjeto2;
				} else {
					if (linhasDeCodigoPorDesignRoleProjeto2.get(key) != null)
						similaridade -= (float) linhasDeCodigoPorDesignRoleProjeto2.get(key) / totalLocProjeto2;
				}
			}
		}
		
		Map<CharSequence, Double> drProjetoPercentual1 = new HashMap<>();
		Map<CharSequence, Double> drProjetoPercentual2 = new HashMap<>();
		
		for (CharSequence key : linhasDeCodigoPorDesignRoleProjeto1.keySet()) {
			Double valor = ((double) linhasDeCodigoPorDesignRoleProjeto1.get(key) / totalLocProjeto1) * 100;
			drProjetoPercentual1.put(key, valor);
		}

		for (CharSequence key : linhasDeCodigoPorDesignRoleProjeto2.keySet()) {
			Double valor = ((double) linhasDeCodigoPorDesignRoleProjeto2.get(key) / totalLocProjeto2) * 100;
			drProjetoPercentual2.put(key, valor);
		}
		
		System.out.println("======>>>> Cosine Similarity = " + cosineSimilarity(drProjetoPercentual1, drProjetoPercentual2));
		
		similaridade = similaridade / 2;
		BigDecimal bd = new BigDecimal(Float.toString(similaridade));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
//	public float calculateCossineSimilarity(ArrayList<ClassMetricResult> projeto1, ArrayList<ClassMetricResult> projeto2) {
//		
//		Map<CharSequence, Integer> drProjeto1 = countDesignRoles(projeto1); 
//		Map<CharSequence, Integer> drProjeto2 = countDesignRoles(projeto2); 
//		
//		
//		Double similaridade  = cosineSimilarity(drProjeto1, drProjeto2);
//		return similaridade.floatValue();
//	}
	
	

	private Map<CharSequence, Integer> countDesignRoles(ArrayList<ClassMetricResult> projeto) {
		Map<CharSequence, Integer> drProjeto = new HashMap<>();

		for (ClassMetricResult ckNumber : projeto) {
			Integer numero = drProjeto.get(ckNumber.getDesignRole());

			if (numero != null)
				drProjeto.put(ckNumber.getDesignRole(), ++numero);
			else
				drProjeto.put(ckNumber.getDesignRole(), 1);
		}
		return drProjeto;
	}

	/**
	 * Calculates the cosine similarity for two given vectors.
	 *
	 * @param leftVector left vector
	 * @param rightVector right vector
	 * @return cosine similarity between the two vectors
	 */
	private Double cosineSimilarity(final Map<CharSequence, Double> leftVector,
			final Map<CharSequence, Double> rightVector) {
		if (leftVector == null || rightVector == null) {
			throw new IllegalArgumentException("Vectors must not be null");
		}

		final Set<CharSequence> intersection = getIntersection(leftVector, rightVector);

		final double dotProduct = dot(leftVector, rightVector, intersection);
		double d1 = 0.0d;
		for (final Double value : leftVector.values()) {
			d1 += Math.pow(value, 2);
		}
		double d2 = 0.0d;
		for (final Double value : rightVector.values()) {
			d2 += Math.pow(value, 2);
		}
		double cosineSimilarity;
		if (d1 <= 0.0 || d2 <= 0.0) {
			cosineSimilarity = 0.0;
		} else {
			cosineSimilarity = (double) (dotProduct / (double) (Math.sqrt(d1) * Math.sqrt(d2)));
		}
		return cosineSimilarity;
	}

	/**
	 * Returns a set with strings common to the two given maps.
	 * @param leftVector left vector map
	 * @param rightVector right vector map
	 * @return common strings
	 */
	private Set<CharSequence> getIntersection(final Map<CharSequence, Double> leftVector,
			final Map<CharSequence, Double> rightVector) {
		final Set<CharSequence> intersection = new HashSet<>(leftVector.keySet());
		intersection.retainAll(rightVector.keySet());
		return intersection;
	}

	/**
	 * Computes the dot product of two vectors. It ignores remaining elements. It
	 * means that if a vector is longer than other, then a smaller part of it will
	 * be used to compute the dot product.
	 * @param leftVector left vector
	 * @param rightVector right vector
	 * @param intersection  common elements
	 * @return the dot product
	 */
	private double dot(final Map<CharSequence, Double> leftVector, final Map<CharSequence, Double> rightVector,
			final Set<CharSequence> intersection) {
		long dotProduct = 0;
		for (final CharSequence key : intersection) {
			dotProduct += leftVector.get(key) * rightVector.get(key);
		}
		return dotProduct;
	}

}
