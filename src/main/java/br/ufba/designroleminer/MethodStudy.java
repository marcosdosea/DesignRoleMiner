package br.ufba.designroleminer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//		mineAndroidApplications();
//		mineEclipseApplications();
		mineWebApplications();
//		
		List<String> androidDR =  Arrays.asList("Activity", "Persistence");
//		mineAndroidDesignRoles(androidDR);
		List<String> eclipseDR = Arrays.asList("Dialog", "View");
//		mineEclipseDesignRoles(eclipseDR);
		List<String> webDR = Arrays.asList("Entity", "View", "Persistence");
//		mineWebDesignRoles(webDR);
		//mineVersions(androidDR, eclipseDR, webDR);
		
	
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
				.through(Commits.onlyInHead())
				.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/method-libreplan-metrics.csv")).mine();

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

private void mineAndroidDesignRoles(List<String> drs) {
		
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/bitcoin-wallet"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(drs, "bitcoin"), new CSVFile("D:/Projetos/_Android/drs-android-bitcoin.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/ExoPlayer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(drs, "exoplayer"), new CSVFile("D:/Projetos/_Android/drs-android-exoplayer.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/Talon-for-Twitter"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(drs, "talon"), new CSVFile("D:/Projetos/_Android/drs-android-talon.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/sms-backup-plus"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(drs, "sms-backup"), new CSVFile("D:/Projetos/_Android/drs-android-sms.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/k-9")).through(Commits.onlyInHead())
				.withThreads(5).process(new MethodVisitorCK(drs, "k9"), new CSVFile("D:/Projetos/_Android/drs-android-k9.csv"))
				.mine();
	}
	
	private void mineEclipseDesignRoles(List<String> eclipseDR) {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/Activiti-Designer"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(eclipseDR, "Activiti"), new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-Activiti.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/angularjs-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(eclipseDR, "AngularJS"), new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-angularjs.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/arduino-eclipse-plugin"))
				.through(Commits.onlyInHead()).withThreads(5).process(new MethodVisitorCK(eclipseDR, "Arduino"),
						new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-arduino.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(eclipseDR, "DroolsJBPM"), new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-droolsjbpm.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/sonarlint-eclipse"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(eclipseDR, "Sonarlint"), new CSVFile("D:/Projetos/_Eclipse/drs-eclipse-sonarlint.csv"))
				.mine();
	}

	private void mineWebDesignRoles(List<String> webDR) {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/bigbluebutton"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(webDR,"bigbluebutton"), new CSVFile("D:/Projetos/_Web/drs-web-bigbluebutton.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(webDR, "openmrs"), new CSVFile("D:/Projetos/_Web/drs-web-openmrs-core.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/heritrix3"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(webDR, "heritrix3"), new CSVFile("D:/Projetos/_Web/drs-heritrix3.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/qalingo-engine"))
				.through(Commits.onlyInHead()).withThreads(5)
				.process(new MethodVisitorCK(webDR, "qalingo"), new CSVFile("D:/Projetos/_Web/drs-web-qalingo-engine.csv"))
				.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.single("f2e700f3739ce38d008100c3d515fce3f0755369"))
				.process(new MethodVisitorCK(webDR, "libreplan"), new CSVFile("D:/Projetos/_Web/drs-web-libreplan.csv")).mine();
	}

	private void mineVersions(List<String> androidDR, List<String> eclipseDR, List<String> webDR) {
		List<String> k9Android = new ArrayList<String>();
		k9Android.add("f794cc1f896a9755c535370c8527c49be88ed2f1"); // head   13.04.2016
		k9Android.add("b586522ca11b190717c24e4abea2583be7ff9043"); // 5.108  22.03.2016
		k9Android.add("9d225dc84c1c8f64ec07048492b0c8ba7e8e5cf0"); // 5.106  02.05.2015 
		k9Android.add("83de921a1afd5ec393aa05865a7504a48cfc43eb"); // 5.105  14.03.2015
		k9Android.add("272a4bc1cf75b3401f042b849c46b9c46c01a072"); // 5.103  05.12.2014
		
		Map<String, String> k9Tags = new HashMap<String, String>();
		k9Tags.put("f794cc1f896a9755c535370c8527c49be88ed2f1", "HEAD");
		k9Tags.put("b586522ca11b190717c24e4abea2583be7ff9043", "5.108");
		k9Tags.put("9d225dc84c1c8f64ec07048492b0c8ba7e8e5cf0", "5.106");
		k9Tags.put("83de921a1afd5ec393aa05865a7504a48cfc43eb", "5.105");
		k9Tags.put("272a4bc1cf75b3401f042b849c46b9c46c01a072", "5.103");

		//
		List<String> droolsEclipse = new ArrayList<String>();
		droolsEclipse.add("db359739bf6e9d20aafffaf59be5ba33aead375a"); // head 09.12.2016
		droolsEclipse.add("7fdd2e6d6091a7ae1062ed32615dfabda3694749"); // 6.5.0.Final 17.10.2016
		droolsEclipse.add("8058332d95ce2f782fd49d5593b8bd83559da42a"); // 6.4.1.Final 03.05.2016
		droolsEclipse.add("d02257a1eb4fe243bd36279540b4465d848d3ad5"); // 6.3.0.Final 15.09.2015
		droolsEclipse.add("50288c91e6e9070593dc60ea2153bbecb8d5db6c"); // 6.2.0.Final 02.03.2015
		
		Map<String, String> droolsTags = new HashMap<String, String>();
		droolsTags.put("db359739bf6e9d20aafffaf59be5ba33aead375a", "HEAD");
		droolsTags.put("7fdd2e6d6091a7ae1062ed32615dfabda3694749", "6.5.0.Final");
		droolsTags.put("8058332d95ce2f782fd49d5593b8bd83559da42a", "6.4.1.Final");
		droolsTags.put("d02257a1eb4fe243bd36279540b4465d848d3ad5", "6.3.0.Final");
		droolsTags.put("50288c91e6e9070593dc60ea2153bbecb8d5db6c", "6.2.0.Final");
		
//		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
//			.through(Commits.list(droolsEclipse)).withThreads(5)
//			.process(new MethodVisitorCK(eclipseDR, "droolsJBPM", droolsTags), new CSVFile("D:/Projetos/_Eclipse/versions-eclipse-droolsjbpm.csv"))
//			.mine();
//

		List<String> openmrsWeb = new ArrayList<String>();
		openmrsWeb.add("4fba6363dc5e3865dcf166331a129c3b9066193b"); //head    12.10.2016
		openmrsWeb.add("889ccc1cf4d13b4e09ae9ff3ea6713cb685b3cef"); //2.0.0   27.07.2016
		openmrsWeb.add("f2ddde827e2f4a2b0782b93af6b38279292e5cf5"); //2.0.1   07.11.2016
		openmrsWeb.add("8f283ea385d71421d810027c4a6ea70c22c7675b"); //1.12.0  24.05.2016
		openmrsWeb.add("5d5d298a2e5794c0de26f76ba930c59b18b3349c"); // 1.11.0 05.02.2015
		openmrsWeb.add("d8b5d007c91e64eb1e82d9d5db10076863570fcd"); // 1.10.0 19.09.2014

		Map<String, String> openmrsTags = new HashMap<String, String>();
		openmrsTags.put("4fba6363dc5e3865dcf166331a129c3b9066193b", "HEAD");
		openmrsTags.put("889ccc1cf4d13b4e09ae9ff3ea6713cb685b3cef", "2.0.0");
		openmrsTags.put("f2ddde827e2f4a2b0782b93af6b38279292e5cf5", "2.0.1");
		openmrsTags.put("8f283ea385d71421d810027c4a6ea70c22c7675b", "1.12.0");
		openmrsTags.put("5d5d298a2e5794c0de26f76ba930c59b18b3349c", "1.11.0");
		openmrsTags.put("d8b5d007c91e64eb1e82d9d5db10076863570fcd", "1.10.0");
		
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
			.through(Commits.list(openmrsWeb)).withThreads(1)
			.process(new MethodVisitorCK(webDR, "openmrs", openmrsTags), new CSVFile("D:/Projetos/_Web/versions-web-openmrs.csv"))
			.mine();
	}

}
