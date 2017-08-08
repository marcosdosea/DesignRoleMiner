package br.ufba.designroleminer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.filter.range.Commits;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;

public class ClassStudy2 implements Study {

	public static void main(String[] args) {
		new MetricMiner2().start(new ClassStudy2());
		
	}

	public void execute() {
		//mineAndroidApplications();
		//mineEclipseApplications();
		mineWebApplications();
		//mineDesignRoles();
		//mineVersions();
		
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/heritrix3"))
		.through(Commits.onlyInHead())
		.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/dr-heritrix3.csv")).mine();
	}

	private void mineDesignRoles() {
		List<String> androidDR = new ArrayList<String>();
		androidDR.add("Activity");
		androidDR.add("Persistence");
		mineAndroidDesignRoles(androidDR);
		List<String> eclipseDR = new ArrayList<String>();
		eclipseDR.add("Dialog");
		eclipseDR.add("View");
		mineEclipseDesignRoles(eclipseDR);
		List<String> webDR = new ArrayList<String>();
		webDR.add("View");
		webDR.add("Entity");
		webDR.add("Persistence");
		mineWebDesignRoles(webDR);
	}


	private void mineWebApplications() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/bigbluebutton"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/dr-bigbluebutton.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/dr-openmrs-core.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/heritrix3"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/dr-heritrix3.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/qalingo-engine"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/dr-qalingo-engine.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.single("f2e700f3739ce38d008100c3d515fce3f0755369"))
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/dr-libreplan.csv")).mine();
	}

	private void mineEclipseApplications() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/Activiti-Designer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Eclipse/dr-Activiti-Designer.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/angularjs-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Eclipse/dr-angularjs-eclipse.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/arduino-eclipse-plugin"))
				.through(Commits.onlyInHead()).withThreads(5).process(new ClassVisitorDR(),
						new CSVFile("D:/Projetos/_Eclipse/dr-arduino-eclipse-plugin.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Eclipse/dr-droolsjbpm-tools.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/sonarlint-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Eclipse/dr-sonarlint-eclipse.csv"))
				.mine();
	}

	private void mineAndroidApplications() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/bitcoin-wallet"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Android/dr-bitcoin-wallet.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/ExoPlayer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Android/dr-ExoPlayer.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/Talon-for-Twitter"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Android/dr-Talon-for-Twitter.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/sms-backup-plus"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Android/dr-sms-backup-plus.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/k-9")).through(Commits.onlyInHead())
				.withThreads(5).process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Android/dr-k9.csv"))
				.mine();
	}

	private void mineAndroidDesignRoles(List<String> drs) {
		
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/bitcoin-wallet"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(drs, "bitcoin"), new CSVFile("D:/Projetos/_Android/drs-android-bitcoin.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/ExoPlayer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(drs, "exoplayer"), new CSVFile("D:/Projetos/_Android/drs-android-exoplayer.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/Talon-for-Twitter"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(drs, "talon"), new CSVFile("D:/Projetos/_Android/drs-android-talon.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/sms-backup-plus"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(drs, "sms-backup"), new CSVFile("D:/Projetos/_Android/drs-android-sms.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/k-9")).through(Commits.onlyInHead())
				.withThreads(5).process(new ClassVisitorDR(drs, "k9"), new CSVFile("D:/Projetos/_Android/drs-android-k9.csv"))
				.mine();
	}
	
	private void mineEclipseDesignRoles(List<String> eclipseDR) {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/Activiti-Designer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(eclipseDR, "Activiti"), new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-Activiti.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/angularjs-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(eclipseDR, "AngularJS"), new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-angularjs.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/arduino-eclipse-plugin"))
				.through(Commits.onlyInHead()).withThreads(5).process(new ClassVisitorDR(eclipseDR, "Arduino"),
						new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-arduino.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(eclipseDR, "DroolsJBPM"), new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-droolsjbpm.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/sonarlint-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(eclipseDR, "Sonarlint"), new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-sonarlint.csv"))
				.mine();
	}

	private void mineWebDesignRoles(List<String> webDR) {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/bigbluebutton"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(webDR,"bigbluebutton"), new CSVFile("D:/Projetos/_Web/drs-web-bigbluebutton.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(webDR, "openmrs"), new CSVFile("D:/Projetos/_Web/drs-web-openmrs-core.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/heritrix3"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/dr-heritrix3.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/qalingo-engine"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new ClassVisitorDR(webDR, "qalingo"), new CSVFile("D:/Projetos/_Web/drs-web-qalingo-engine.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.single("f2e700f3739ce38d008100c3d515fce3f0755369"))
				.process(new ClassVisitorDR(webDR, "libreplan"), new CSVFile("D:/Projetos/_Web/drs-web-libreplan.csv")).mine();
	}


	private void mineVersions() {
		List<String> k9Android = new ArrayList<String>();
		k9Android.add("f794cc1f896a9755c535370c8527c49be88ed2f1"); // head   13.04.2016
		k9Android.add("b586522ca11b190717c24e4abea2583be7ff9043"); // 5.108  22.03.2016
		k9Android.add("9d225dc84c1c8f64ec07048492b0c8ba7e8e5cf0"); // 5.106  02.05.2015 
		k9Android.add("83de921a1afd5ec393aa05865a7504a48cfc43eb"); // 5.105  14.03.2015
		k9Android.add("272a4bc1cf75b3401f042b849c46b9c46c01a072"); // 5.103  05.12.2014
		
		List<String> filterAndroidDesignRoles = Arrays.asList("Activity", "Service");
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/k-9")).through(Commits.list(k9Android))
			.withThreads(5)
			.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Android/versions-class-k-9-metrics.csv")).mine();

		List<String> filterEclipseDesignRoles = Arrays.asList("Dialog", "View");
		List<String> droolsEclipse = new ArrayList<String>();
		droolsEclipse.add("db359739bf6e9d20aafffaf59be5ba33aead375a"); // head 09.12.2016
		droolsEclipse.add("7fdd2e6d6091a7ae1062ed32615dfabda3694749"); // 6.5.0.Final 17.10.2016
		droolsEclipse.add("8058332d95ce2f782fd49d5593b8bd83559da42a"); // 6.4.1.Final 03.05.2016
		droolsEclipse.add("d02257a1eb4fe243bd36279540b4465d848d3ad5"); // 6.3.0.Final 15.09.2015
		droolsEclipse.add("50288c91e6e9070593dc60ea2153bbecb8d5db6c"); // 6.2.0.Final 02.03.2015
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
			.through(Commits.list(droolsEclipse)).withThreads(5)
			.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Eclipse/versions-class-droolsjbpm-tools-metrics.csv"))
			.mine();

		List<String> filterWebDesignRoles = Arrays.asList("Persistence", "Service");
		List<String> openmrsWeb = new ArrayList<String>();
		openmrsWeb.add("4fba6363dc5e3865dcf166331a129c3b9066193b"); //head    12.10.2016
		openmrsWeb.add("889ccc1cf4d13b4e09ae9ff3ea6713cb685b3cef"); //2.0.0   27.07.2016
		openmrsWeb.add("f2ddde827e2f4a2b0782b93af6b38279292e5cf5"); //2.0.1   07.11.2016
		openmrsWeb.add("8f283ea385d71421d810027c4a6ea70c22c7675b"); //1.12.0  24.05.2016
		openmrsWeb.add("5d5d298a2e5794c0de26f76ba930c59b18b3349c"); // 1.11.0 05.02.2015
		openmrsWeb.add("d8b5d007c91e64eb1e82d9d5db10076863570fcd"); // 1.10.0 19.09.2014
		
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
			.through(Commits.list(openmrsWeb)).withThreads(5)
			.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/versions-class-openmrs-core-metrics.csv"))
			.mine();
	}
	
}
