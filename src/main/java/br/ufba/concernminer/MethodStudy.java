package br.ufba.concernminer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;

public class MethodStudy implements Study {

	public static void main(String[] args) {
		new MetricMiner2().start(new MethodStudy());
	}
	
	public void execute() {
		
		List<String> kotlinEclipse = new ArrayList<String>(); 
		//kotlinAndroid.add("c4fb07897b4bd351337d46bc48f38dde20a2678a"); //head      04.2016
		kotlinEclipse.add("ce8238fce71ab3959845dc79f9e632bd81faae6e"); //0.5.1.12  01.2016
		//kotlinAndroid.add("c0b3cad93f82dd483c32ef0043590b3a0694fd40"); //0.3.0.25  10.2015
		kotlinEclipse.add("dcdcf370b7780de1619972e28578997922437072"); //0.2.1.10  06.2015 
		//kotlinAndroid.add("6ac22608ad937b030c34e23f474d51dd9513ab26"); //0.1.0.525 01.2015
		kotlinEclipse.add("fba2edcee468293097e08033499b22f3de2e7da8"); //0.1.0.123 06.2014
		
		//k9Android.add("99193195f98e7e55ca8805e79f4cab0da54a00bf"); //4.800  11.2013
		//k9Android.add("823c9ff1c521b107871041387ca966d90a36f793"); //4.700  10.2013
		
		List<String> openmrsWeb = new ArrayList<String>(); 
		openmrsWeb.add("a0c979f7da855444cbec33e5d751d5029d1db5d2"); //1.11.5  11.2015
		openmrsWeb.add("03b68a5e1484e3940ce49fe60e11fb11fd5303c6"); //1.11.3  05.2015
		openmrsWeb.add("5d5d298a2e5794c0de26f76ba930c59b18b3349c"); //1.11.1  02.2015
		//openmrsWeb.add("4ba6996fdaefe15bde8ecbc9661fcd03f84f4262"); //1.10.1  11.2014
		//openmrsWeb.add("fd241c10bb4d6f9af7d827edbc9e8a621647c4ca"); //1.9.8   07.2014
		
		
		
		
//		new RepositoryMining()
//		.in(GitRepository.singleProject("D:/Projetos/_Eclipse/Activiti-Designer"))
//		.through(Commits.list(activitiEclipse))
//		.process(new MethodVisitor(), new CSVFile("D:/Projetos/_Eclipse/Activiti-Designer-metric-versions.csv"))
//		.mine();
		
//		new RepositoryMining()
//		.in(GitRepository.singleProject("D:/Projetos/_Eclipse/kotlin-eclipse"))
//		.through(Commits.onlyInHead())
//		.process(new MethodVisitor(), new CSVFile("D:/Projetos/_Eclipse/kotlin-eclipse-metric.csv"))
//		.mine();
		
//		new RepositoryMining()
//		.in(GitRepository.singleProject("D:/Projetos/_Eclipse/kotlin-eclipse"))
//		.through(Commits.list(kotlinEclipse))
//		.process(new MethodVisitor(), new CSVFile("D:/Projetos/_Eclipse/kotlin-eclipse-versions.csv"))
//		.mine();
		
		new RepositoryMining()
		.in(GitRepository.singleProject("D:/Projetos/_Eclipse/kotlin-eclipse"))
		.through(Commits.list(kotlinEclipse))
		.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Eclipse/kotlin-eclipse-versions-ck.csv"))
		.mine();
	}
}
