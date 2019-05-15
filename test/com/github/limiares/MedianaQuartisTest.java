package com.github.limiares;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.github.drminer.threshold.MedianaQuartis;

public class MedianaQuartisTest {

	public ArrayList<Integer> getListaDesordenada(boolean ehImpar) {
		ArrayList<Integer> lista = new ArrayList<>();
		lista.add(5);
		lista.add(27);
		lista.add(15);
		if (ehImpar) {
			lista.add(4);
		}
		lista.add(3);
		return lista;
	}
	
	@Test
	public void ordenarOrdemCrescente() {
		ArrayList<Integer> listaOrdenada = MedianaQuartis.ordernarOrdemCrescente(getListaDesordenada(true));
		assertTrue(listaOrdenada.get(0) < listaOrdenada.get(1) &
				listaOrdenada.get(1) < listaOrdenada.get(2) &
				listaOrdenada.get(2) < listaOrdenada.get(3) &
				listaOrdenada.get(3) < listaOrdenada.get(4));
		assertEquals((int)listaOrdenada.get(0), 3);
		assertEquals((int)listaOrdenada.get(1), 4);
		assertEquals((int)listaOrdenada.get(2), 5);
		assertEquals((int)listaOrdenada.get(3), 15);
		assertEquals((int)listaOrdenada.get(4), 27);
	}
	
	@Test
	public void calcularMediana() {
		// Impar
		ArrayList<Integer> listaOrdenada = MedianaQuartis.ordernarOrdemCrescente(getListaDesordenada(true));
		int mediana = MedianaQuartis.calcularMediana(listaOrdenada);
		assertEquals(mediana, 5);
		// Par
		listaOrdenada = MedianaQuartis.ordernarOrdemCrescente(getListaDesordenada(false));
		mediana = MedianaQuartis.calcularMediana(listaOrdenada);
		assertEquals(mediana, 10);
	}
	
	@Test
	public void percentil() {
		ArrayList<Integer> listaOrdenada = MedianaQuartis.ordernarOrdemCrescente(getListaDesordenada(true));
		// (quantidade lista) 5*0.75 (porcentagem) = 3.75 (posicao 3) = quarto item da lista = 15 
		int percentil = MedianaQuartis.percentil(listaOrdenada, 75);
		assertEquals(percentil, 15);
		listaOrdenada = MedianaQuartis.ordernarOrdemCrescente(getListaDesordenada(false));
		// (quantidade lista) 4*0.75 (porcentagem) = 3 (posicao 3) = 
		// = (quarto item + terceiro tem)/2 = (27 + 15)/2 = 21 
		percentil = MedianaQuartis.percentil(listaOrdenada, 75);
		assertEquals(percentil, 21);
	}
}
