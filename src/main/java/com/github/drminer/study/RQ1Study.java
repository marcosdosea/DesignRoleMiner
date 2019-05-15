package com.github.drminer.study;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

import com.github.drminer.visitor.MethodVisitorMetric;

public class RQ1Study implements Study {

	public static void main(String[] args) {
		new RepoDriller().start(new RQ1Study());
	}

	public void execute() {
		mineAndroidApplications(); // RQ1
		mineEclipseApplications(); // RQ1
		mineWebApplications(); // RQ1
	}

	private void mineWebApplications() {

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/bigbluebutton"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/method-bigbluebutton.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/method-openmrs.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/heritrix3"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/method-heritrix3.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/qalingo-engine"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/method-qalingo.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.onlyInHead())
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/method-libreplan.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/JDeSurvey"))
				.through(Commits.onlyInHead())
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/method-jdesurvey.csv")).mine();

	}

	private void mineEclipseApplications() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/Activiti-Designer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Eclipse/method-Activiti.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/angularjs-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Eclipse/method-angularjs.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/arduino-eclipse-plugin"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Eclipse/method-arduino.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Eclipse/method-droolsjbpm.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/sonarlint-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Eclipse/method-sonarlint.csv")).mine();
	}

	private void mineAndroidApplications() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/bitcoin-wallet"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Android/method-bitcoin.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/ExoPlayer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Android/method-exoPlayer.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/Talon-for-Twitter"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Android/method-talon.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/sms-backup-plus"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Android/method-smsbackup.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/k-9")).through(Commits.onlyInHead())
				.withThreads(5).process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Android/method-k9.csv"))
				.mine();
	}
}
