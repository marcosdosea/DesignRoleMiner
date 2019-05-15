package org.designroleminer.study;

import java.util.Arrays;
import java.util.List;

import org.designroleminer.visitor.MethodVisitorMetric;
import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class RQ2Study implements Study {

	public static void main(String[] args) {
		new RepoDriller().start(new RQ2Study());
	}

	public void execute() {
		// List<String> androidDR = Arrays.asList("ACTIVITY", "PERSISTENCE", "SERVICE",
		// "ASYNCTASK", "VIEW"); SANER 2018
		List<String> androidDR = Arrays.asList("ACTIVITY", "PERSISTENCE", "SERVICE", "ASYNCTASK", "VIEW", "ADAPTER",
				"BROADCASTRECEIVER", "ENTITY", "EXCEPTION", "FRAGMENT", "TEST", "UNDEFINED");
		// List<String> eclipseDR = Arrays.asList("DIALOG", "VIEW", "PERSISTENCE");
		// SANER 2018
		List<String> eclipseDR = Arrays.asList("DIALOG", "VIEW", "PERSISTENCE", "ABSTRACTHANDLER",
				"ABSTRACTPREFERENCEINITIALIZER", "ABSTRACTUIPLUGIN", "ACTION", "ADAPTER", "ENTITY", "EXCEPTION",
				"FIELDEDITOR", "JOB", "LABELPROVIDER", "MODEL", "NLS", "PLUGIN", "SERVICE", "TEST", "UNDEFINED");
		// List<String> webDR = Arrays.asList("ENTITY", "VIEW", "PERSISTENCE", "TEST");
		// SANER 2018
		List<String> webDR = Arrays.asList("ENTITY", "VIEW", "PERSISTENCE", "TEST", "[COMPARATOR]", "[VALIDATOR]",
				"ADAPTER", "ASYNCTASK", "COMPONENT", "CONTROLLER", "DIALOG", "DISPATCHERSERVLET", "EXCEPTION",
				"HASHMAP", "HTTPSERVLET", "LINKEDHASHMAP", "SERVICE", "UNDEFINED");
		mineAndroidDesignRoles(androidDR);
		mineEclipseDesignRoles(eclipseDR);
		mineWebDesignRoles(webDR);
	}

	private void mineAndroidDesignRoles(List<String> drs) {

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/bitcoin-wallet"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(drs, "bitcoin"),
						new CSVFile("D:/Projetos/_Android/selected-drs-android.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/ExoPlayer"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(drs, "exoplayer"),
						new CSVFile("D:/Projetos/_Android/selected-drs-android.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/Talon-for-Twitter"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(drs, "talon"),
						new CSVFile("D:/Projetos/_Android/selected-drs-android.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/sms-backup-plus"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(drs, "sms-backup"),
						new CSVFile("D:/Projetos/_Android/selected-drs-android.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/k-9")).through(Commits.onlyInHead())
				.withThreads(5).process(new MethodVisitorMetric(drs, "k9"),
						new CSVFile("D:/Projetos/_Android/selected-drs-android.csv", true))
				.mine();
	}

	private void mineEclipseDesignRoles(List<String> eclipseDR) {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/Activiti-Designer"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(eclipseDR, "Activiti"),
						new CSVFile("D:/Projetos/_Eclipse/selected-drs-eclipse.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/angularjs-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(eclipseDR, "AngularJS"),
						new CSVFile("D:/Projetos/_Eclipse/selected-drs-eclipse.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/arduino-eclipse-plugin"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(eclipseDR, "Arduino"),
						new CSVFile("D:/Projetos/_Eclipse/selected-drs-eclipse.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(eclipseDR, "DroolsJBPM"),
						new CSVFile("D:/Projetos/_Eclipse/selected-drs-eclipse.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/sonarlint-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(eclipseDR, "Sonarlint"),
						new CSVFile("D:/Projetos/_Eclipse/selected-drs-eclipse.csv", true))
				.mine();
	}

	private void mineWebDesignRoles(List<String> webDR) {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/bigbluebutton"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(webDR, "bigbluebutton"),
						new CSVFile("D:/Projetos/_Web/selected-drs-web.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(webDR, "openmrs"),
						new CSVFile("D:/Projetos/_Web/selected-drs-web.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/heritrix3"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(webDR, "heritrix3"),
						new CSVFile("D:/Projetos/_Web/selected-drs-web.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/qalingo-engine"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorMetric(webDR, "qalingo"),
						new CSVFile("D:/Projetos/_Web/selected-drs-web.csv", true))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.single("f2e700f3739ce38d008100c3d515fce3f0755369"))
				.process(new MethodVisitorMetric(webDR, "libreplan"),
						new CSVFile("D:/Projetos/_Web/selected-drs-web.csv", true))
				.mine();
	}
}
