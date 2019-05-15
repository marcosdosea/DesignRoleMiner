package org.designroleminer.smelldetector.negocio;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.smelldetector.CarregaSalvaArquivo;
import org.designroleminer.smelldetector.FilterSmells;
import org.designroleminer.smelldetector.model.DadosMetodoSmell;
import org.designroleminer.smelldetector.model.LimiarTecnica;
import org.designroleminer.techinique.ArchitecturalRoleTechnique;
import org.designroleminer.techinique.DesignRoleTechnique;
import org.designroleminer.techinique.TechniqueExecutor;
import org.junit.Test;

public class FiltrarMetodosSmellTest {

	@Test
	public void testFiltrarSmells() {
		TechniqueExecutor executor = new TechniqueExecutor(new DesignRoleTechnique());

		try {
			System.out.println("Iniciando a coleta de métricas do projeto a ser analisado...");
			ArrayList<String> projetosAnalisar = executor.lerProjetos("Analysis.txt");
			ArrayList<ClassMetricResult> metricasProjetosAnalisar = executor.getMetricsFromProjects(projetosAnalisar);

			System.out.println("Gerando DR.csv com a lista classes e design roles atribuídos...");
			executor.execute(metricasProjetosAnalisar, System.getProperty("user.dir") + "\\DR.CSV");

			System.out.println("Gerando AR.csv com a lista classes e design roles atribuídos...");
			executor.setTechinique(new ArchitecturalRoleTechnique());
			executor.execute(metricasProjetosAnalisar, System.getProperty("user.dir") + "\\AR.CSV");

			System.out.println("Carregando valores limiares...");
			List<LimiarTecnica> listaTecnicas = CarregaSalvaArquivo
					.carregarLimiares(System.getProperty("user.dir") + "\\thresholds\\");

			System.out.println("Gerando SMELLS.csv com a lista de problemas de design encontrados...");
			HashMap<String, DadosMetodoSmell> metodosSmell = null;
			metodosSmell = FilterSmells.filtrar(metricasProjetosAnalisar, listaTecnicas, metodosSmell);
			FilterSmells.gravarMetodosSmell(metodosSmell, "SMELLS.csv");

			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

}
