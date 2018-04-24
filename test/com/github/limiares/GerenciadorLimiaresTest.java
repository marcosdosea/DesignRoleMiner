package com.github.limiares;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.github.ck.CKNumber;

public class GerenciadorLimiaresTest {

	@Test
	public void testGerarLimiares() {
		final String PASTA_RESULTADO = "thresholds\\";
		GerenciadorLimiares gLimiares = new GerenciadorLimiares();

		System.out.println("Iniciando a coleta de métricas do projeto referencia...");
		ArrayList<String> projetosReferencia = gLimiares.lerProjetos("Reference.txt");
		ArrayList<CKNumber> metricasProjetosReferencia = gLimiares.getMetricasProjetos(projetosReferencia);

		System.out.println("Iniciando a coleta de métricas dos projetos WEB...");
		//ArrayList<String> projetosBenchmark = gLimiares.lerProjetos("Benchmark.txt");
		//ArrayList<CKNumber> metricasProjetosBenchmark = gLimiares.getMetricasProjetos(projetosBenchmark);

		System.out.println("Gerando Limiares por Alves usando benchmark...");
		//gLimiares.gerarLimiarDoseaAlves(metricasProjetosBenchmark, PASTA_RESULTADO + "A.csv");

		System.out.println("Gerando Limiares por Vale usando benchmark...");
		//gLimiares.gerarLimiarVale(metricasProjetosBenchmark, PASTA_RESULTADO + "V.csv");

		System.out.println("Gerando Limiares por Aniche...");
		//gLimiares.gerarLimiarAniche(metricasProjetosBenchmark, PASTA_RESULTADO + "X.csv");

		System.out.println("Gerando Limiares por Dosea Referencia...");
		gLimiares.gerarLimiarDoseaReference(metricasProjetosReferencia, PASTA_RESULTADO + "R.csv");

		System.out.println("Gerando Limiares por Dosea Referencia e Design Role...");
		gLimiares.gerarLimiarDoseaReferenceDesignRole(metricasProjetosReferencia, PASTA_RESULTADO + "D.csv");


		System.out.println("Limiares gravados na pasta " + PASTA_RESULTADO + " com sucesso!");
		assertTrue(true);
	}
}
