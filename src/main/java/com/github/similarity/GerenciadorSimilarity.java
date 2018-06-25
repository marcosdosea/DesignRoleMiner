package com.github.similarity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.ck.CKNumber;
import com.github.limiares.GerenciadorLimiares;

public class GerenciadorSimilarity {

	public float calculateSimilarityPerDesignRole(ArrayList<CKNumber> projeto1, ArrayList<CKNumber> projeto2) {
		Integer totalClasses = 0;

		Map<CharSequence, Integer> drProjeto1 = countDesignRoles(projeto1);
		Map<CharSequence, Integer> drProjeto2 = countDesignRoles(projeto2);

		// Conta classes associadas a design roles
		for (CharSequence dr : drProjeto1.keySet())
			totalClasses += drProjeto1.get(dr);
		for (CharSequence dr : drProjeto2.keySet())
			totalClasses += drProjeto2.get(dr);

		GerenciadorLimiares gLimiares = new GerenciadorLimiares();
		HashMap<String, Long> linhasDeCodigoPorDesignRoleProjeto1 = new HashMap<>();
		HashMap<String, Long> linhasDeCodigoPorDesignRoleProjeto2 = new HashMap<>();
		long totalLocProjeto1 = gLimiares.obterTotalLinhasCodigoPorDesignRole(projeto1,
				linhasDeCodigoPorDesignRoleProjeto1);
		long totalLocProjeto2 = gLimiares.obterTotalLinhasCodigoPorDesignRole(projeto2,
				linhasDeCodigoPorDesignRoleProjeto2);
		if (linhasDeCodigoPorDesignRoleProjeto1.get("TEST") != null)
			totalLocProjeto1 -= linhasDeCodigoPorDesignRoleProjeto1.get("TEST");
		if (linhasDeCodigoPorDesignRoleProjeto1.get("UNDEFINED") != null)
			totalLocProjeto1 -= linhasDeCodigoPorDesignRoleProjeto1.get("UNDEFINED");

		if (linhasDeCodigoPorDesignRoleProjeto2.get("TEST") != null)
			totalLocProjeto2 -= linhasDeCodigoPorDesignRoleProjeto2.get("TEST");
		if (linhasDeCodigoPorDesignRoleProjeto2.get("UNDEFINED") != null)
			totalLocProjeto2 -= linhasDeCodigoPorDesignRoleProjeto2.get("UNDEFINED");

		float similaridade = (float) 0;

		// for (CharSequence key : drProjeto1.keySet()) {
		// if (!key.equals("TEST") && !key.equals("UNDEFINED")) {
		// if (drProjeto2.containsKey(key))
		// similaridade += (float) (drProjeto1.get(key) / totalClasses);
		// else
		// similaridade -= (float) drProjeto1.get(key) / totalClasses;
		// }
		// }
		//
		// for (CharSequence key : drProjeto2.keySet()) {
		// if (!key.equals("TEST") && !key.equals("UNDEFINED")) {
		// if (drProjeto1.containsKey(key))
		// similaridade += (float) drProjeto2.get(key) / totalClasses;
		// else
		// similaridade -= (float) drProjeto2.get(key) / totalClasses;
		// }
		// }

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

		similaridade = similaridade / 2;
		BigDecimal bd = new BigDecimal(Float.toString(similaridade));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	private Map<CharSequence, Integer> countDesignRoles(ArrayList<CKNumber> projeto) {
		Map<CharSequence, Integer> drProjeto = new HashMap<>();

		for (CKNumber ckNumber : projeto) {
			Integer numero = drProjeto.get(ckNumber.getDesignRole());

			if (numero != null)
				drProjeto.put(ckNumber.getDesignRole(), ++numero);
			else
				drProjeto.put(ckNumber.getDesignRole(), 1);
		}
		return drProjeto;
	}
}
