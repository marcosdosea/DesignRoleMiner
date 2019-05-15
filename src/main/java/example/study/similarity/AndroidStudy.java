package example.study.similarity;

import java.util.ArrayList;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.similarity.SimilaritySystems;
import org.designroleminer.technique.DesignRoleTechnique;
import org.designroleminer.technique.TechniqueExecutor;

public class AndroidStudy {

	public static void main(String[] args) {
		SimilaritySystems gSimilarity = new SimilaritySystems();
		TechniqueExecutor executor = new TechniqueExecutor(new DesignRoleTechnique());

		ArrayList<String> listAndroid = new ArrayList<>();
		listAndroid.add("D:\\Projetos\\_Android\\bitcoin-wallet");
		ArrayList<ClassMetricResult> bitcoin = executor.getMetricsFromProjects(listAndroid);

		listAndroid = new ArrayList<>();
		listAndroid.add("D:\\Projetos\\_Android\\Talon-for-Twitter");
		ArrayList<ClassMetricResult> k9 = executor.getMetricsFromProjects(listAndroid);
		System.out.println("======>>>> Bitcoin x Talon : " + gSimilarity.calculate(bitcoin, k9));
	}

}
