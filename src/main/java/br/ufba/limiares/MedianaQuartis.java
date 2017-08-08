package br.ufba.limiares;

import java.util.ArrayList;
import java.util.Collections;

public class MedianaQuartis {
	
	public static ArrayList<Integer> ordernarOrdemCrescente(
			ArrayList<Integer> metodosDesordenados) {
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
			return (metodos.get(numeroElementosLista/2) + 
					metodos.get((numeroElementosLista/2) - 1)) / 2; 
		} else {
			return metodos.get((numeroElementosLista-1)/2); 
		}
		
	}

	public static int percentil(ArrayList<Integer> metodosOrdenados, int porcentagemLimiar) {
		if (metodosOrdenados.size() == 0) {
			return 0;
		}
		double posicaoReal = ((double)porcentagemLimiar/100)*(metodosOrdenados.size());
		int posicaoInteira = (int)posicaoReal;
		if (posicaoReal != posicaoInteira) {
			return metodosOrdenados.get(posicaoInteira);
		} else {
			return ((metodosOrdenados.get(posicaoInteira) + metodosOrdenados.get(posicaoInteira-1))/2);
		}
	}
	
	public static boolean eCompreendidoPrimeiroTerceiroQuartil(int numero, 
			int primeiroQuartil, int terceiroQuartil) {
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
