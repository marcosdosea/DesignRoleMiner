package com.github.drminer.study;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

import com.github.drminer.visitor.MethodVisitorCK;

public class RQ3Study implements Study {

	public static void main(String[] args) {
		new RepoDriller().start(new RQ3Study());
	}

	public void execute() {
		List<String> androidSelectedDR = new ArrayList<String>();
		List<String> eclipseSelectedDR = new ArrayList<String>();
		List<String> webSelectedDR = new ArrayList<String>();

		mineAndroidVersions(androidSelectedDR); // RQ3
		mineEclipseVersions(eclipseSelectedDR); // RQ3
		mineWebVersions(webSelectedDR); // RQ3
	}

	private void mineWebVersions(List<String> webSelectedDR) {
		List<String> bigbluebuttonWeb = new ArrayList<String>();
		bigbluebuttonWeb.add("eeb0d95aab8f4a09c6813b37c9cfadb185b03d34"); // HEAD 2016-10-18
		bigbluebuttonWeb.add("ad746beb5df032a18ebf9efa257066e25a10c8be"); // 1.0.0 2016-05-17
		bigbluebuttonWeb.add("25cd1701c69a23817746df891601dd5b0a74674f"); // 0.9.2 2015-10-16
		bigbluebuttonWeb.add("18aee902eebd0b21172f49d21a75fa89793988ab"); // 0.9.1 2015-06-24

		Map<String, String> bigbluebuttonTags = new HashMap<String, String>();
		bigbluebuttonTags.put("eeb0d95aab8f4a09c6813b37c9cfadb185b03d34", "HEAD");
		bigbluebuttonTags.put("ad746beb5df032a18ebf9efa257066e25a10c8be", "1.0.0");
		bigbluebuttonTags.put("25cd1701c69a23817746df891601dd5b0a74674f", "0.9.2");
		bigbluebuttonTags.put("18aee902eebd0b21172f49d21a75fa89793988ab", "0.9.1");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/bigbluebutton"))
				.through(Commits.list(bigbluebuttonWeb)).withThreads(1)
				.process(new MethodVisitorCK(webSelectedDR, "Bigbluebutton", bigbluebuttonTags),
						new CSVFile("D:/Projetos/_Web/versions-web-bigbluebutton.csv"))
				.mine();

		List<String> openmrsWeb = new ArrayList<String>();
		openmrsWeb.add("4fba6363dc5e3865dcf166331a129c3b9066193b"); // HEAD 2016-10-12
		openmrsWeb.add("889ccc1cf4d13b4e09ae9ff3ea6713cb685b3cef"); // 2.0.0 2016-07-27
		openmrsWeb.add("cdfdaa09548b9ae33482725f3701d402ab6a4656"); // 1.9.11 2016-06-23
		openmrsWeb.add("8f283ea385d71421d810027c4a6ea70c22c7675b"); // 1.12.0 2016-05-24

		Map<String, String> openmrsTags = new HashMap<String, String>();
		openmrsTags.put("4fba6363dc5e3865dcf166331a129c3b9066193b", "HEAD");
		openmrsTags.put("889ccc1cf4d13b4e09ae9ff3ea6713cb685b3cef", "2.0.0");
		openmrsTags.put("cdfdaa09548b9ae33482725f3701d402ab6a4656", "1.9.11");
		openmrsTags.put("8f283ea385d71421d810027c4a6ea70c22c7675b", "1.12.0");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/openmrs-core"))
				.through(Commits.list(openmrsWeb)).withThreads(1)
				.process(new MethodVisitorCK(webSelectedDR, "openmrs", openmrsTags),
						new CSVFile("D:/Projetos/_Web/versions-web-openmrs.csv"))
				.mine();

		List<String> heritrix3Web = new ArrayList<String>();
		heritrix3Web.add("fbb0173459b698b1bb9fcedf4ea3c4ac7b5f0cf6"); // HEAD 2016-07-21
		heritrix3Web.add("4485b8c8c7e3bcb6cf136b22778e335b52469691"); // r 2016-07-01
		heritrix3Web.add("c04162a0f784a818c99ef3befc12d163f8212a15"); // r 2016-06-08
		heritrix3Web.add("c80f0cee2b4353e26d18f9d0abde6ee59af54c40"); // r 2016-05-09

		Map<String, String> heritrix3Tags = new HashMap<String, String>();
		heritrix3Tags.put("fbb0173459b698b1bb9fcedf4ea3c4ac7b5f0cf6", "HEAD");
		heritrix3Tags.put("4485b8c8c7e3bcb6cf136b22778e335b52469691", "r2017-07-01");
		heritrix3Tags.put("c04162a0f784a818c99ef3befc12d163f8212a15", "r2016-08-06");
		heritrix3Tags.put("c80f0cee2b4353e26d18f9d0abde6ee59af54c40", "r2016-05-09");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/heritrix3"))
				.through(Commits.list(heritrix3Web)).withThreads(1)
				.process(new MethodVisitorCK(webSelectedDR, "Heritrix3", heritrix3Tags),
						new CSVFile("D:/Projetos/_Web/versions-web-heritrix3.csv"))
				.mine();

		List<String> qalingoWeb = new ArrayList<String>();
		qalingoWeb.add("7380084369034012d9ad6cb1ab4973ce7960aa2d"); // HEAD 2016-09-25
		qalingoWeb.add("3593d40f7680a839cf039447fcda0e6afe6533e7"); // 0.9.0 2016-07-24
		qalingoWeb.add("b42ff0c05b3717cabef596adbc9ce613de357986"); // 0.8.2 2016-07-23
		qalingoWeb.add("69325058f748e253493793be21586cfda8f9b453"); // 0.8.0 2014-05-07

		Map<String, String> qalingoTags = new HashMap<String, String>();
		qalingoTags.put("7380084369034012d9ad6cb1ab4973ce7960aa2d", "HEAD");
		qalingoTags.put("3593d40f7680a839cf039447fcda0e6afe6533e7", "0.9.0");
		qalingoTags.put("b42ff0c05b3717cabef596adbc9ce613de357986", "0.8.1");
		qalingoTags.put("69325058f748e253493793be21586cfda8f9b453", "0.8.0");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/qalingo-engine"))
				.through(Commits.list(qalingoWeb)).withThreads(1)
				.process(new MethodVisitorCK(webSelectedDR, "Qalingo", qalingoTags),
						new CSVFile("D:/Projetos/_Web/versions-web-qalingo.csv"))
				.mine();

		List<String> libreplanWeb = new ArrayList<String>();
		libreplanWeb.add("f2e700f3739ce38d008100c3d515fce3f0755369"); // HEAD 2016-11-09
		libreplanWeb.add("edf8f775e7dcb7f6c10a5441a87c268ba1f36bae"); // 1.4.1 2015-04-15
		libreplanWeb.add("4e06cbe71aeb04f91eaee1b564d2060f66bf480f"); // 1.4.0 2013-04-29
		libreplanWeb.add("9c4c8e50fefa8431258abba717806f8d3164a71d"); // 1.3.3 2012-12-21

		Map<String, String> libreplanTags = new HashMap<String, String>();
		libreplanTags.put("f2e700f3739ce38d008100c3d515fce3f0755369", "HEAD");
		libreplanTags.put("edf8f775e7dcb7f6c10a5441a87c268ba1f36bae", "1.4.1");
		libreplanTags.put("4e06cbe71aeb04f91eaee1b564d2060f66bf480f", "1.4.0");
		libreplanTags.put("9c4c8e50fefa8431258abba717806f8d3164a71d", "1.3.3");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.list(libreplanWeb)).withThreads(1)
				.process(new MethodVisitorCK(webSelectedDR, "libreplan", libreplanTags),
						new CSVFile("D:/Projetos/_Web/versions-web-libreplan.csv"))
				.mine();
	}

	private void mineEclipseVersions(List<String> eclipseSelectedDR) {
		List<String> activitiEclipse = new ArrayList<String>();
		activitiEclipse.add("3421dcfa090fcb47df3a207ea8707eb12d0e8033"); // HEAD 2016-08-18
		activitiEclipse.add("ea49c4a5651d98041a817faadcdf5a115dbf781a"); // 5.18.0 2015-08-10
		activitiEclipse.add("79d5a8c22f23a4491261593abb705707284d2d99"); // 5.15.0 2014-07-08
		activitiEclipse.add("874bebf5f8189006bc9e33c6e9a417dbfd829f92"); // 5.14.1 2014-02-28

		Map<String, String> activitiTags = new HashMap<String, String>();
		activitiTags.put("3421dcfa090fcb47df3a207ea8707eb12d0e8033", "HEAD");
		activitiTags.put("ea49c4a5651d98041a817faadcdf5a115dbf781a", "5.18.0");
		activitiTags.put("79d5a8c22f23a4491261593abb705707284d2d99", "5.15.0");
		activitiTags.put("874bebf5f8189006bc9e33c6e9a417dbfd829f92", "5.14.1");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/Activiti-Designer"))
				.through(Commits.list(activitiEclipse)).withThreads(5)
				.process(new MethodVisitorCK(eclipseSelectedDR, "Activiti", activitiTags),
						new CSVFile("D:/Projetos/_Eclipse/versions-eclipse-Activiti.csv"))
				.mine();

		List<String> angularJSEclipse = new ArrayList<String>();
		angularJSEclipse.add("59e5e042adbfb2084aaf9886bd48bab00b479d35"); // HEAD 2016-07-03
		angularJSEclipse.add("51d20140287a81f20e4343d98479896d8712bf56"); // 1.2.0 2016-06-16
		angularJSEclipse.add("a79436314845c52eb60586f17c63d04a15580bc6"); // 1.1.0 2015-11-09
		angularJSEclipse.add("d7589b4da984ff889765d825f4637940115e795e"); // 1.0.0 2015-08-31

		Map<String, String> angularJSTags = new HashMap<String, String>();
		angularJSTags.put("59e5e042adbfb2084aaf9886bd48bab00b479d35", "HEAD");
		angularJSTags.put("51d20140287a81f20e4343d98479896d8712bf56", "1.2.0");
		angularJSTags.put("a79436314845c52eb60586f17c63d04a15580bc6", "1.1.0");
		angularJSTags.put("d7589b4da984ff889765d825f4637940115e795e", "1.0.0");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/angularjs-eclipse"))
				.through(Commits.list(angularJSEclipse)).withThreads(5)
				.process(new MethodVisitorCK(eclipseSelectedDR, "AngularJS", angularJSTags),
						new CSVFile("D:/Projetos/_Eclipse/versions-eclipse-angularjs.csv"))
				.mine();

		List<String> arduinoEclipse = new ArrayList<String>();
		arduinoEclipse.add("30587400b74d75d028ee27b4dacb0196f2015532"); // HEAD 2016-04-04
		arduinoEclipse.add("839ed91565a66979e6f9cbe10cfaf635ca1a8c5e"); // 3.0 2016-03-31
		arduinoEclipse.add("28f2e18ce3207d1cf76731ec9800704f4aeaf1ce"); // 2.5 2016-01-24
		arduinoEclipse.add("d14c4e2ba20edcddb10850a3e0ba2cc7d4ef2d34"); // 2.4 2015-12-07

		Map<String, String> arduinoTags = new HashMap<String, String>();
		arduinoTags.put("30587400b74d75d028ee27b4dacb0196f2015532", "HEAD");
		arduinoTags.put("839ed91565a66979e6f9cbe10cfaf635ca1a8c5e", "3.0");
		arduinoTags.put("28f2e18ce3207d1cf76731ec9800704f4aeaf1ce", "2.5");
		arduinoTags.put("d14c4e2ba20edcddb10850a3e0ba2cc7d4ef2d34", "2.4");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/arduino-eclipse-plugin"))
				.through(Commits.list(arduinoEclipse)).withThreads(5)
				.process(new MethodVisitorCK(eclipseSelectedDR, "Arduino", arduinoTags),
						new CSVFile("D:/Projetos/_Eclipse/versions-eclipse-arduino.csv"))
				.mine();

		List<String> droolsEclipse = new ArrayList<String>();
		droolsEclipse.add("025afd23170b7277b3901322617895e55d3b9b4a"); // head 09.12.2016
		droolsEclipse.add("7fdd2e6d6091a7ae1062ed32615dfabda3694749"); // 6.5.0.Final 17.10.2016
		droolsEclipse.add("8058332d95ce2f782fd49d5593b8bd83559da42a"); // 6.4.1.Final 03.05.2016
		droolsEclipse.add("d02257a1eb4fe243bd36279540b4465d848d3ad5"); // 6.3.0.Final 15.09.2015

		Map<String, String> droolsTags = new HashMap<String, String>();
		droolsTags.put("025afd23170b7277b3901322617895e55d3b9b4a", "HEAD");
		droolsTags.put("7fdd2e6d6091a7ae1062ed32615dfabda3694749", "6.5.0.Final");
		droolsTags.put("8058332d95ce2f782fd49d5593b8bd83559da42a", "6.4.1.Final");
		droolsTags.put("d02257a1eb4fe243bd36279540b4465d848d3ad5", "6.3.0.Final");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/droolsjbpm-tools"))
				.through(Commits.list(droolsEclipse)).withThreads(5)
				.process(new MethodVisitorCK(eclipseSelectedDR, "droolsJBPM", droolsTags),
						new CSVFile("D:/Projetos/_Eclipse/versions-eclipse-droolsjbpm.csv"))
				.mine();

		List<String> sonarlintEclipse = new ArrayList<String>();
		sonarlintEclipse.add("69bb4a9265a8f382066ec6fdc2e526f715d2e49c"); // HEAD 2016-12-12
		sonarlintEclipse.add("3f162aad99be7a5c13feb883aabcc010642a7b3d"); // 2.2.1 2016-08-26
		sonarlintEclipse.add("3423e72b6aad04c836853f2862022dd2c5288227"); // 2.2.0 2016-07-29
		sonarlintEclipse.add("6df2aa1dd63134fe2b26f34da77eaac9505f0786"); // 2.1.0 2016-06-03

		Map<String, String> sonarlintTags = new HashMap<String, String>();
		sonarlintTags.put("69bb4a9265a8f382066ec6fdc2e526f715d2e49c", "HEAD"); // HEAD
		sonarlintTags.put("3f162aad99be7a5c13feb883aabcc010642a7b3d", "2.2.1");
		sonarlintTags.put("3423e72b6aad04c836853f2862022dd2c5288227", "2.2.0");
		sonarlintTags.put("6df2aa1dd63134fe2b26f34da77eaac9505f0786", "2.1.0");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Eclipse/sonarlint-eclipse"))
				.through(Commits.list(sonarlintEclipse)).withThreads(5)
				.process(new MethodVisitorCK(eclipseSelectedDR, "Sonarlint", sonarlintTags),
						new CSVFile("D:/Projetos/_Eclipse/versions-eclipse-sonarlint.csv"))
				.mine();
	}

	/**
	 * Android Versions
	 * 
	 * @param androidSelectedDR
	 */
	private void mineAndroidVersions(List<String> androidSelectedDR) {
		List<String> bitcoinAndroid = new ArrayList<String>();
		bitcoinAndroid.add("4d2573cc9552cc435b20a012e0dd3345d56f3040"); // head 2016-10-07
		bitcoinAndroid.add("08a58502d14ff0db9a4db1621dcde48126b1eea6"); // 4.72 release 2016-10-05
		bitcoinAndroid.add("28d5ad5db2536dc9f3ab03a3517bebce27fb2d85"); // 4.71 release 2016-09-30
		bitcoinAndroid.add("b6f23aa1e9cfa8c08c0c3d08278107ccb6a0d912"); // 4.70 release 2016-09-16

		Map<String, String> bitcoinTags = new HashMap<String, String>();
		bitcoinTags.put("4d2573cc9552cc435b20a012e0dd3345d56f3040", "HEAD");
		bitcoinTags.put("08a58502d14ff0db9a4db1621dcde48126b1eea6", "4.72");
		bitcoinTags.put("28d5ad5db2536dc9f3ab03a3517bebce27fb2d85", "4.71");
		bitcoinTags.put("b6f23aa1e9cfa8c08c0c3d08278107ccb6a0d912", "4.70");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/bitcoin-wallet"))
				.through(Commits.list(bitcoinAndroid)).withThreads(5)
				.process(new MethodVisitorCK(androidSelectedDR, "Bitcoin", bitcoinTags),
						new CSVFile("D:/Projetos/_Android/versions-android-bitcoin.csv"))
				.mine();

		List<String> exoplayerAndroid = new ArrayList<String>();
		exoplayerAndroid.add("f94218a758bf6810520b5392521818b8c1b5e604"); // head 2016-10-06
		exoplayerAndroid.add("f8a8302f7bbcb47997095bd97b086eb633e49972"); // r.2.0.1 2016-09-30
		exoplayerAndroid.add("7d991cef305e95cae5cd2a9feadf4af8858b284b"); // r.2.0.0 2016-09-14
		exoplayerAndroid.add("0571557ed4da8e446fe4c676b0acee99fc8e528c"); // r.1.5.11 2016-09-13

		Map<String, String> exoplayerTags = new HashMap<String, String>();
		exoplayerTags.put("f94218a758bf6810520b5392521818b8c1b5e604", "HEAD");
		exoplayerTags.put("f8a8302f7bbcb47997095bd97b086eb633e49972", "r.2.0.1");
		exoplayerTags.put("7d991cef305e95cae5cd2a9feadf4af8858b284b", "r.2.0.0");
		exoplayerTags.put("0571557ed4da8e446fe4c676b0acee99fc8e528c", "r.1.5.11");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/ExoPlayer"))
				.through(Commits.list(exoplayerAndroid)).withThreads(5)
				.process(new MethodVisitorCK(androidSelectedDR, "Exoplayer", exoplayerTags),
						new CSVFile("D:/Projetos/_Android/versions-android-exoPlayer.csv"))
				.mine();

		List<String> talonAndroid = new ArrayList<String>();
		talonAndroid.add("2054d4f9928c15246ee8dad8d1a9f5dc524be3db"); // head 2016-04-03
		talonAndroid.add("2a7837a2a283724ce9a6f4c474c33db6d7fb51ea"); // c-15 2016-03-12
		talonAndroid.add("4b0cd0cae09dcb0a8ca9f1dc7d6d2afb664e2954"); // c-30 2016-02-25
		talonAndroid.add("211b3b8e4bfffff9dffac4ee5af81a9acbfe71d4"); // c-45 2016-02-10

		Map<String, String> talonTags = new HashMap<String, String>();
		talonTags.put("2054d4f9928c15246ee8dad8d1a9f5dc524be3db", "HEAD");
		talonTags.put("2a7837a2a283724ce9a6f4c474c33db6d7fb51ea", "c-15");
		talonTags.put("4b0cd0cae09dcb0a8ca9f1dc7d6d2afb664e2954", "c-30");
		talonTags.put("211b3b8e4bfffff9dffac4ee5af81a9acbfe71d4", "c-45");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/Talon-for-Twitter"))
				.through(Commits.list(talonAndroid)).withThreads(5)
				.process(new MethodVisitorCK(androidSelectedDR, "Talon", talonTags),
						new CSVFile("D:/Projetos/_Android/versions-android-talon.csv"))
				.mine();

		List<String> smsAndroid = new ArrayList<String>();
		smsAndroid.add("20949b1fdd38b7dc183f8f2de5d8b30484b3ed31"); // head 2016-07-08
		smsAndroid.add("b1536510031c209eb89251e591ab50a216afa394"); // 1.5.10 2015-08-20
		smsAndroid.add("0c95fc247155a907a632fddf3fe3b698440c365a"); // 1.5.9 2015-06-23
		smsAndroid.add("5befe9043b9290877fca4fdb5741c357e555f472"); // 1.5.8 2015-06-08

		Map<String, String> smsTags = new HashMap<String, String>();
		smsTags.put("20949b1fdd38b7dc183f8f2de5d8b30484b3ed31", "HEAD");
		smsTags.put("b1536510031c209eb89251e591ab50a216afa394", "1.5.10");
		smsTags.put("0c95fc247155a907a632fddf3fe3b698440c365a", "1.5.9");
		smsTags.put("5befe9043b9290877fca4fdb5741c357e555f472", "1.5.8");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/sms-backup-plus"))
				.through(Commits.list(smsAndroid)).withThreads(5)
				.process(new MethodVisitorCK(androidSelectedDR, "SMSBackup", smsTags),
						new CSVFile("D:/Projetos/_Android/versions-android-smsbackup.csv"))
				.mine();

		List<String> k9Android = new ArrayList<String>();
		k9Android.add("855f1c3d5d5068e525e367bf18a5236a44ef360a"); // head 2016-04-13
		k9Android.add("af9b4d3f0167fcb2de644722078e7685a17134cd"); // 5.010 2016-04-04
		k9Android.add("0a79f40c6056e5262996fbb9fe1ae7217b9d276f"); // 5.009 2016-03-03
		k9Android.add("8925ebdc060883f407e4d721be083f637cce9b09"); // 5.008 2016-02-09

		Map<String, String> k9Tags = new HashMap<String, String>();
		k9Tags.put("855f1c3d5d5068e525e367bf18a5236a44ef360a", "HEAD");
		k9Tags.put("af9b4d3f0167fcb2de644722078e7685a17134cd", "5.010");
		k9Tags.put("0a79f40c6056e5262996fbb9fe1ae7217b9d276f", "5.009");
		k9Tags.put("8925ebdc060883f407e4d721be083f637cce9b09", "5.008");

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Android/k-9"))
				.through(Commits.list(k9Android)).withThreads(5)
				.process(new MethodVisitorCK(androidSelectedDR, "K9", k9Tags),
						new CSVFile("D:/Projetos/_Android/versions-android-k9.csv"))
				.mine();
	}
}
