package org.designroleminer.limiares;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.techinique.AlvesTechinique;
import org.designroleminer.techinique.AnicheTechinique;
import org.designroleminer.techinique.DesignRoleTechinique;
import org.designroleminer.techinique.DoseaTechinique;
import org.designroleminer.techinique.TechiniqueExecutor;
import org.designroleminer.techinique.ValeTechinique;
import org.junit.Test;

public class GerenciadorLimiaresTest {

	@Test
	public void testGerarLimiaresReference() {
		final String PASTA_RESULTADO = "thresholds\\";
		TechiniqueExecutor gLimiares = new TechiniqueExecutor(new DesignRoleTechinique());

		System.out.println("Iniciando a coleta de métricas do projeto referencia...");
		ArrayList<String> projetosReferencia = gLimiares.lerProjetos("Reference.txt");
		ArrayList<ClassMetricResult> metricasProjetosReferencia = gLimiares.getMetricsFromProjects(projetosReferencia);

		System.out.println("Gerando Limiares por Alves apontando para projetos Referencia...");
		gLimiares.setTechinique(new AlvesTechinique());
		gLimiares.execute(metricasProjetosReferencia, PASTA_RESULTADO + "R.csv");

		System.out.println("Gerando Limiares por Dosea Referencia e Design Role...");
		gLimiares.setTechinique(new DoseaTechinique());
		gLimiares.execute(metricasProjetosReferencia, PASTA_RESULTADO + "D.csv");

		System.out.println("Limiares gravados na pasta " + PASTA_RESULTADO + " com sucesso!");
		assertTrue(true);
	}

	// @Test
	public void testGerarLimiaresBenchmarkExterno() {
		final String PASTA_RESULTADO = "thresholds\\";
		TechiniqueExecutor gLimiares = new TechiniqueExecutor(new DesignRoleTechinique());

		System.out.println("Iniciando a coleta de métricas dos projetos WEB...");
		ArrayList<String> projetosBenchmark = gLimiares.lerProjetos("Benchmark.txt");
		ArrayList<ClassMetricResult> metricasProjetosBenchmark = gLimiares.getMetricsFromProjects(projetosBenchmark);

		System.out.println("Gerando Limiares por Alves usando benchmark...");
		gLimiares.setTechinique(new AlvesTechinique());
		gLimiares.execute(metricasProjetosBenchmark, PASTA_RESULTADO + "A.csv");

		System.out.println("Gerando Limiares por Vale usando benchmark...");
		gLimiares.setTechinique(new ValeTechinique());
		gLimiares.execute(metricasProjetosBenchmark, PASTA_RESULTADO + "V.csv");

		System.out.println("Gerando Limiares por Aniche...");
		gLimiares.setTechinique(new AnicheTechinique());
		gLimiares.execute(metricasProjetosBenchmark, PASTA_RESULTADO + "X.csv");

		System.out.println("Limiares gravados na pasta " + PASTA_RESULTADO + " com sucesso!");
		assertTrue(true);
	}

}
