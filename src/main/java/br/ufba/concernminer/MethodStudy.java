package br.ufba.concernminer;

import java.util.ArrayList;
import java.util.List;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.filter.range.Commits;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;

public class MethodStudy implements Study {

	public static void main(String[] args) {
		new MetricMiner2().start(new MethodStudy());
	}

	public void execute() {

		// mineEclipseVersions();
		// mineWebVersions();
		// mineAndroidVersions();

		// mineMVCExample();
		// mineAndroidApplications();
		// mineEclipseApplications();
		mineWebApplications();

	}

	private void mineMVCExample() {
		new RepositoryMining().in(GitRepository.singleProject("C:/Users/Marcos Dósea/Desktop/spring-mvc-showcase"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorCK(),
						new CSVFile("C:/Users/Marcos Dósea/Desktop/spring-mvc-showcase-metrics.csv"))
				.mine();
	}

	private void mineWebApplications() {

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/bigbluebutton"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/method-bigbluebutton-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/method-openmrs-core-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/heritrix3"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/method-heritrix3-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/qalingo-engine"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/method-qalingo-engine-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/method-libreplan-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/JDeSurvey"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/method-JDeSurvey-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/web-budget"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/method-web-budget-metrics.csv")).mine();
	}

	private void mineEclipseApplications() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/Activiti-Designer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Eclipse/Activiti-Designer-metrics.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/angularjs-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Eclipse/angularjs-eclipse-metrics.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/arduino-eclipse-plugin"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Eclipse/arduino-eclipse-plugin-metrics.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Eclipse/droolsjbpm-tools-metrics.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/sonarlint-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Eclipse/sonarlint-eclipse-metrics.csv"))
				.mine();
	}

	private void mineAndroidApplications() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/bitcoin-wallet"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Android/bitcoin-wallet-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/ExoPlayer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Android/ExoPlayer-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/Talon-for-Twitter"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Android/Talon-for-Twitter-metrics.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/sms-backup-plus"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Android/sms-backup-plus-metrics.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/k-9")).through(Commits.onlyInHead())
				.withThreads(5).process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Android/k-9-metrics.csv"))
				.mine();
	}

	private void mineAndroidVersions() {
		List<String> k9Android = new ArrayList<String>();
		k9Android.add("f794cc1f896a9755c535370c8527c49be88ed2f1"); // head
																	// 04.2016
		// k9Android.add("b586522ca11b190717c24e4abea2583be7ff9043"); //5.108
		// 03.2016
		// k9Android.add("99193195f98e7e55ca8805e79f4cab0da54a00bf"); //4.800
		// 11.2013
		// k9Android.add("823c9ff1c521b107871041387ca966d90a36f793"); //4.700
		// 10.2013
	}

	private void mineWebVersions() {
		List<String> openmrsWeb = new ArrayList<String>();
		openmrsWeb.add("a0c979f7da855444cbec33e5d751d5029d1db5d2"); // 1.11.5
																	// 11.2015
		openmrsWeb.add("03b68a5e1484e3940ce49fe60e11fb11fd5303c6"); // 1.11.3
																	// 05.2015
		openmrsWeb.add("5d5d298a2e5794c0de26f76ba930c59b18b3349c"); // 1.11.1
																	// 02.2015
		// openmrsWeb.add("4ba6996fdaefe15bde8ecbc9661fcd03f84f4262"); //1.10.1
		// 11.2014
		// openmrsWeb.add("fd241c10bb4d6f9af7d827edbc9e8a621647c4ca"); //1.9.8
		// 07.2014
	}

	private void mineEclipseVersions() {
		List<String> kotlinEclipse = new ArrayList<String>();
		kotlinEclipse.add("c4fb07897b4bd351337d46bc48f38dde20a2678a"); // head
																		// 04.2016
		kotlinEclipse.add("ce8238fce71ab3959845dc79f9e632bd81faae6e"); // 0.5.1.12
																		// 01.2016
		kotlinEclipse.add("c0b3cad93f82dd483c32ef0043590b3a0694fd40"); // 0.3.0.25
																		// 10.2015
		kotlinEclipse.add("dcdcf370b7780de1619972e28578997922437072"); // 0.2.1.10
																		// 06.2015
		kotlinEclipse.add("6ac22608ad937b030c34e23f474d51dd9513ab26"); // 0.1.0.525
																		// 01.2015
		kotlinEclipse.add("fba2edcee468293097e08033499b22f3de2e7da8"); // 0.1.0.123
																		// 06.2014
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/kotlin-eclipse"))
				.through(Commits.list(kotlinEclipse))
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Eclipse/kotlin-eclipse-versions.csv")).mine();

	}
}
