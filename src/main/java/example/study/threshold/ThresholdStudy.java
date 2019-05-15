package example.study.threshold;

import java.util.ArrayList;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.technique.AlvesTechnique;
import org.designroleminer.technique.AnicheTechnique;
import org.designroleminer.technique.DesignRoleTechnique;
import org.designroleminer.technique.TechniqueExecutor;
import org.designroleminer.technique.ValeTechnique;

public class ThresholdStudy {
	
	public static void main(String[] args) {
		final String PASTA_RESULTADO = "thresholds\\";
		TechniqueExecutor gLimiares = new TechniqueExecutor(new DesignRoleTechnique());

		System.out.println("Iniciando a coleta de métricas dos projetos WEB...");
		ArrayList<String> projetosBenchmark = gLimiares.lerProjetos("Benchmark.txt");
		ArrayList<ClassMetricResult> metricasProjetosBenchmark = gLimiares.getMetricsFromProjects(projetosBenchmark);

		System.out.println("Gerando Limiares por Alves usando benchmark...");
		gLimiares.setTechinique(new AlvesTechnique());
		gLimiares.execute(metricasProjetosBenchmark, PASTA_RESULTADO + "A.csv");

		System.out.println("Gerando Limiares por Vale usando benchmark...");
		gLimiares.setTechinique(new ValeTechnique());
		gLimiares.execute(metricasProjetosBenchmark, PASTA_RESULTADO + "V.csv");

		System.out.println("Gerando Limiares por Aniche...");
		gLimiares.setTechinique(new AnicheTechnique());
		gLimiares.execute(metricasProjetosBenchmark, PASTA_RESULTADO + "X.csv");

		System.out.println("Limiares gravados na pasta " + PASTA_RESULTADO + " com sucesso!");	
	}
}
