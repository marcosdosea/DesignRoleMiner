package com.github.similarity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.ck.CKNumber;

public class GerenciadorSimilarity {

	public float calculateSimilarityPerDesignRole(ArrayList<CKNumber> projeto1, ArrayList<CKNumber> projeto2) {
		Integer totalClasses = 0;
		
		Map<CharSequence, Integer> drProjeto1 = countDesignRoles(projeto1);
		Map<CharSequence, Integer> drProjeto2 = countDesignRoles(projeto2);
		
		// Conta classes associadas a design roles
		for (CharSequence dr: drProjeto1.keySet()) 
			totalClasses += drProjeto1.get(dr);
		for (CharSequence dr: drProjeto2.keySet()) 
			totalClasses += drProjeto2.get(dr);
		

		float similaridade = (float) 0;

		for (CharSequence key : drProjeto1.keySet()) {
			if (drProjeto2.containsKey(key))
				similaridade += (float) drProjeto1.get(key) / totalClasses;
			else
				similaridade -= (float) drProjeto1.get(key) / totalClasses;
		}

		for (CharSequence key : drProjeto2.keySet()) {
			if (drProjeto1.containsKey(key))
				similaridade += (float) drProjeto2.get(key) / totalClasses;
			else
				similaridade -= (float) drProjeto2.get(key) / totalClasses;
		}
		BigDecimal bd = new BigDecimal(Float.toString(similaridade));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	private Map<CharSequence, Integer> countDesignRoles(ArrayList<CKNumber> projeto) {
		Map<CharSequence, Integer> drProjeto = new HashMap<>();

		for (CKNumber ckNumber : projeto) {
			if (!ckNumber.getDesignRole().equals("TEST") && !ckNumber.getDesignRole().equals("UNDEFINED")) {
				Integer numero = drProjeto.get(ckNumber.getDesignRole());
				if (numero != null)
					drProjeto.put(ckNumber.getDesignRole(), ++numero);
				else
					drProjeto.put(ckNumber.getDesignRole(), 1);
			}
		}
		return drProjeto;
	}
}
