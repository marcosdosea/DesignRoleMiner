package br.ufba.concernminer;

import java.util.ArrayList;
import java.util.List;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;

public class ProjectStudy implements Study {

	public static void main(String[] args) {
		new MetricMiner2().start(new ProjectStudy());
	}
	
	public void execute() {
		
		List<String> kotlinEclipse = new ArrayList<String>(); 
		kotlinEclipse.add("c4fb07897b4bd351337d46bc48f38dde20a2678a"); //head      04.2016
		kotlinEclipse.add("ce8238fce71ab3959845dc79f9e632bd81faae6e"); //0.5.1.12  01.2016
		kotlinEclipse.add("dcdcf370b7780de1619972e28578997922437072"); //0.2.1.10  06.2015 
		kotlinEclipse.add("fba2edcee468293097e08033499b22f3de2e7da8"); //0.1.0.123 06.2014
		
		List<String> k9Android = new ArrayList<String>(); 
		k9Android.add("f794cc1f896a9755c535370c8527c49be88ed2f1"); //head   04.2016
		k9Android.add("b586522ca11b190717c24e4abea2583be7ff9043"); //5.108  03.2016
		k9Android.add("99193195f98e7e55ca8805e79f4cab0da54a00bf"); //4.800  11.2013
		k9Android.add("3349c51bcd0f13619250ed802f8ffce6b9b5f037"); //4.900  01.2014
		k9Android.add("0f844fd4d2be05daac656858096d481c1d88c266"); //5.100  10.2014
		
		List<String> openmrsWeb = new ArrayList<String>(); 
		//openmrsWeb.add("6f2a1c3b5e4eaffc31101a303eedb0dd20fe7920"); //head    03.2016
		//openmrsWeb.add("a0c979f7da855444cbec33e5d751d5029d1db5d2"); //1.11.5  11.2015
		//openmrsWeb.add("03b68a5e1484e3940ce49fe60e11fb11fd5303c6"); //1.11.3  05.2015
		openmrsWeb.add("5d5d298a2e5794c0de26f76ba930c59b18b3349c"); //1.11.1  02.2015

//		new RepositoryMining()
//		.in(GitRepository.singleProject("D:/Projetos/_Web/qalingo-engine"))
//		.through(Commits.single("d1a81cad1cfeb22c820975014a4df8356dfc8ced"))
//		.process(new ProjectVisitor(), new CSVFile("D:/Projetos/_Web/qalingo-engine-nom.csv"))
//		.mine();
		
//		new RepositoryMining()
//		.in(GitRepository.singleProject("D:/Projetos/_Eclipse/kotlin-eclipse"))
//		.through(Commits.list(kotlinEclipse))
//		.process(new ProjectVisitor(), new CSVFile("D:/Projetos/_Eclipse/kotlin-eclipse-nom.csv"))
//		.mine();

		
		new RepositoryMining()
		.in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
		.through(Commits.list(openmrsWeb))
		.process(new ProjectVisitor(), new CSVFile("D:/Projetos/_Web/openmrs-core-nom2.csv"))
		.mine();
		
	
//		new RepositoryMining()
//		.in(GitRepository.singleProject("D:/Projetos/_Android/k-9"))
//		.through(Commits.list(k9Android))
//		.process(new ProjectVisitor(), new CSVFile("D:/Projetos/_Android/k-9-nom.csv"))
//		.mine();
	}
}
