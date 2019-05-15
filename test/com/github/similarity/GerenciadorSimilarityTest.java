package com.github.similarity;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.drminer.similarity.GerenciadorSimilarity;
import com.github.drminer.threshold.GerenciadorLimiares;

public class GerenciadorSimilarityTest {

	GerenciadorSimilarity gSimilarity;
	GerenciadorLimiares gLimiares;
	
	
	@Before
	public void initialize() {
		gSimilarity = new GerenciadorSimilarity();
		gLimiares = new GerenciadorLimiares();
	}
	
	@Test
	public void testCalculateSimilarityMenor() {
		try {
			ArrayList<String> listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\bitcoin-wallet");
			ArrayList<CKNumber> bitcoin = gLimiares.getMetricasProjetos(listAndroid);
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\Talon-for-Twitter");
			ArrayList<CKNumber> k9 = gLimiares.getMetricasProjetos(listAndroid);
			System.out.println("======>>>> Bitcoin x Talon : " + gSimilarity.calculateDoseaSimilarity(bitcoin, k9));
			
		} catch (Exception e) {
			assertTrue(false);
		}
		assertTrue(true);
	}
	
	///@Test
	public void testCalculateSimilarityWeb() {
		try {
			System.out.println("Iniciando Cálculo Similaridade 1...");
			ArrayList<String> listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\bitcoin-wallet");
			ArrayList<CKNumber> bitcoin = gLimiares.getMetricasProjetos(listAndroid);
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\k-9");
			ArrayList<CKNumber> k9 = gLimiares.getMetricasProjetos(listAndroid);
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\ExoPlayer");
			ArrayList<CKNumber> exoplayer = gLimiares.getMetricasProjetos(listAndroid);
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\sms-backup-plus");
			ArrayList<CKNumber> sms = gLimiares.getMetricasProjetos(listAndroid);
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\Talon-for-Twitter");
			ArrayList<CKNumber> talon = gLimiares.getMetricasProjetos(listAndroid);
			
			ArrayList<String> listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\Activiti-Designer");
			ArrayList<CKNumber> activiti = gLimiares.getMetricasProjetos(listEclipse);
			
			listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\angularjs-eclipse");
			ArrayList<CKNumber> angularjs = gLimiares.getMetricasProjetos(listEclipse);
			
			listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\arduino-eclipse-plugin");
			ArrayList<CKNumber> arduino = gLimiares.getMetricasProjetos(listEclipse);
			
			listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\droolsjbpm-tools");
			ArrayList<CKNumber> droolsjbpm = gLimiares.getMetricasProjetos(listEclipse);
			
			listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\sonarlint-eclipse");
			ArrayList<CKNumber> sonarlint = gLimiares.getMetricasProjetos(listEclipse);
			
			ArrayList<String> web = new ArrayList<>();
			web.add("D:\\Projetos\\_Web\\bigbluebutton");
			ArrayList<CKNumber> bigbluebutton  = gLimiares.getMetricasProjetos(web);
			compareSystems("Bigbluebutton", bigbluebutton , bitcoin, k9, exoplayer, sms, talon, activiti, angularjs, arduino, droolsjbpm,
					sonarlint);
			
			web = new ArrayList<>();
			web.add("D:\\Projetos\\_Web\\openmrs-core");
			ArrayList<CKNumber> openmrs = gLimiares.getMetricasProjetos(web);
			compareSystems("OpenMRS", openmrs, bitcoin, k9, exoplayer, sms, talon, activiti, angularjs, arduino, droolsjbpm,
					sonarlint);
			
			web = new ArrayList<>();
			web.add("D:\\Projetos\\_Web\\heritrix3");
			ArrayList<CKNumber> heritrix3 = gLimiares.getMetricasProjetos(web);
			compareSystems("heritrix3", heritrix3, bitcoin, k9, exoplayer, sms, talon, activiti, angularjs, arduino, droolsjbpm,
					sonarlint);
			
			web = new ArrayList<>();
			web.add("D:\\Projetos\\_Web\\qalingo-engine");
			ArrayList<CKNumber> qalingo = gLimiares.getMetricasProjetos(web);
			compareSystems("qalingo", qalingo, bitcoin, k9, exoplayer, sms, talon, activiti, angularjs, arduino, droolsjbpm,
					sonarlint);
			
			web = new ArrayList<>();
			web.add("D:\\Projetos\\_Web\\libreplan");
			ArrayList<CKNumber> libreplan = gLimiares.getMetricasProjetos(web);
			compareSystems("libreplan", libreplan, bitcoin, k9, exoplayer, sms, talon, activiti, angularjs, arduino, droolsjbpm,
					sonarlint);
			System.out.println("*********** WEB ************");
			System.out.println("======>>>> BigBlueButton x BigBlueButton: " + gSimilarity.calculateDoseaSimilarity(bigbluebutton, bigbluebutton));
			System.out.println("======>>>> BigBlueButton x OpenMRS: " + gSimilarity.calculateDoseaSimilarity(bigbluebutton, openmrs));
			System.out.println("======>>>> BigBlueButton x Qalingo: " + gSimilarity.calculateDoseaSimilarity(bigbluebutton, qalingo));
			System.out.println("======>>>> BigBlueButton x Heritrix3: " + gSimilarity.calculateDoseaSimilarity(bigbluebutton, heritrix3));
			System.out.println("======>>>> BigBlueButton x Libreplan: " + gSimilarity.calculateDoseaSimilarity(bigbluebutton, libreplan));
			
			System.out.println("======>>>> OpenMRS x OpenMRS: " + gSimilarity.calculateDoseaSimilarity(openmrs, openmrs));
			System.out.println("======>>>> OpenMRS x Qalingo: " + gSimilarity.calculateDoseaSimilarity(openmrs, qalingo));
			System.out.println("======>>>> OpenMRS x Heritrix3: " + gSimilarity.calculateDoseaSimilarity(openmrs, heritrix3));
			System.out.println("======>>>> OpenMRS x Libreplan: " + gSimilarity.calculateDoseaSimilarity(openmrs, libreplan));
			
			System.out.println("======>>>> Qalingo x Qalingo: " + gSimilarity.calculateDoseaSimilarity(qalingo, qalingo));
			System.out.println("======>>>> Qalingo x Heritrix3: " + gSimilarity.calculateDoseaSimilarity(qalingo, heritrix3));
			System.out.println("======>>>> Qalingo x Libreplan: " + gSimilarity.calculateDoseaSimilarity(qalingo, libreplan));
			
			System.out.println("======>>>> Heritrix3 x Heritrix3: " + gSimilarity.calculateDoseaSimilarity(heritrix3, heritrix3));
			System.out.println("======>>>> Heritrix3 x Libreplan: " + gSimilarity.calculateDoseaSimilarity(heritrix3, libreplan));
			
			System.out.println("======>>>> Libreplan x Libreplan: " + gSimilarity.calculateDoseaSimilarity(libreplan, libreplan));
			
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

	private void compareSystems(String nameWebSystem, ArrayList<CKNumber> webSystem, ArrayList<CKNumber> bitcoin, ArrayList<CKNumber> k9,
			ArrayList<CKNumber> exoplayer, ArrayList<CKNumber> sms, ArrayList<CKNumber> talon,
			ArrayList<CKNumber> activiti, ArrayList<CKNumber> angularjs, ArrayList<CKNumber> arduino,
			ArrayList<CKNumber> droolsjbpm, ArrayList<CKNumber> sonarlint) {
		
		System.out.println("*********** Android ************");
		System.out.println("======>>>> " + nameWebSystem + " x Bitcoin : " + gSimilarity.calculateDoseaSimilarity(webSystem, bitcoin));
		System.out.println("======>>>> " + nameWebSystem + "  x K9 : " + gSimilarity.calculateDoseaSimilarity(webSystem, k9));
		System.out.println("======>>>> " + nameWebSystem + "  x Exoplayer : " + gSimilarity.calculateDoseaSimilarity(webSystem, exoplayer));
		System.out.println("======>>>> " + nameWebSystem + "  x SMS : " + gSimilarity.calculateDoseaSimilarity(webSystem, sms));
		System.out.println("======>>>> " + nameWebSystem + "  x Talon : " + gSimilarity.calculateDoseaSimilarity(webSystem, talon));
		System.out.println("======>>>> " + nameWebSystem + "  x " + nameWebSystem + " : " + gSimilarity.calculateDoseaSimilarity(webSystem, webSystem));
		
		System.out.println("******* Eclipse *****");
		System.out.println("======>>>> " + nameWebSystem + "  x Activiti : " + gSimilarity.calculateDoseaSimilarity(webSystem, activiti));
		System.out.println("======>>>> " + nameWebSystem + "  x AngularJS : " + gSimilarity.calculateDoseaSimilarity(webSystem, angularjs));
		System.out.println("======>>>> " + nameWebSystem + "  x Arduino : " + gSimilarity.calculateDoseaSimilarity(webSystem, arduino));
		System.out.println("======>>>> " + nameWebSystem + "  x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(webSystem, droolsjbpm));
		System.out.println("======>>>> " + nameWebSystem + "  x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(webSystem, sonarlint));
		
		
		System.out.println("**** Android x Android ***********");
		System.out.println("======>>>> Bitcoin x Bitcoin: " + gSimilarity.calculateDoseaSimilarity(bitcoin, bitcoin));
		System.out.println("======>>>> Bitcoin x K9: " + gSimilarity.calculateDoseaSimilarity(bitcoin, k9));
		System.out.println("======>>>> Bitcoin x Exoplayer: " + gSimilarity.calculateDoseaSimilarity(bitcoin, exoplayer));
		System.out.println("======>>>> Bitcoin x SMS: " + gSimilarity.calculateDoseaSimilarity(bitcoin, sms));
		System.out.println("======>>>> Bitcoin x Talon: " + gSimilarity.calculateDoseaSimilarity(bitcoin, talon));
		System.out.println("======>>>> K9 X K9: " + gSimilarity.calculateDoseaSimilarity(k9, k9));
		System.out.println("======>>>> K9 X Exoplayer: " + gSimilarity.calculateDoseaSimilarity(k9, exoplayer));
		System.out.println("======>>>> K9 X SMS: " + gSimilarity.calculateDoseaSimilarity(k9, sms));
		System.out.println("======>>>> K9 X Talon: " + gSimilarity.calculateDoseaSimilarity(k9, talon));
		System.out.println("======>>>> Exoplayer X Exoplayer: " + gSimilarity.calculateDoseaSimilarity(exoplayer, exoplayer));
		System.out.println("======>>>> Exoplayer X SMS: " + gSimilarity.calculateDoseaSimilarity(exoplayer, sms));
		System.out.println("======>>>> Exoplayer X Talon: " + gSimilarity.calculateDoseaSimilarity(exoplayer, talon));
		System.out.println("======>>>> SMS X SMS: " + gSimilarity.calculateDoseaSimilarity(sms, sms));
		System.out.println("======>>>> SMS X talon: " + gSimilarity.calculateDoseaSimilarity(sms, talon));
		System.out.println("======>>>> Talon X talon: " + gSimilarity.calculateDoseaSimilarity(talon, talon));
		
		System.out.println("**** Eclipse x Eclipse ***********");
		System.out.println("======>>>> Activiti x Activiti : " + gSimilarity.calculateDoseaSimilarity(activiti, activiti));
		System.out.println("======>>>> Activiti x AngularJS : " + gSimilarity.calculateDoseaSimilarity(activiti, angularjs));
		System.out.println("======>>>> Activiti x Arduino : " + gSimilarity.calculateDoseaSimilarity(activiti, arduino));
		System.out.println("======>>>> Activiti x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(activiti, droolsjbpm));
		System.out.println("======>>>> Activiti x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(activiti, sonarlint));
		System.out.println("======>>>> AngularJS x AngularJS : " + gSimilarity.calculateDoseaSimilarity(angularjs, angularjs));
		System.out.println("======>>>> AngularJS x Arduino : " + gSimilarity.calculateDoseaSimilarity(angularjs, arduino));
		System.out.println("======>>>> AngularJS x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(angularjs, droolsjbpm));
		System.out.println("======>>>> AngularJS x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(angularjs, sonarlint));
		System.out.println("======>>>> Arduino x Arduino : " + gSimilarity.calculateDoseaSimilarity(arduino, arduino));
		System.out.println("======>>>> Arduino x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(arduino, droolsjbpm));
		System.out.println("======>>>> Arduino x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(arduino, sonarlint));
		System.out.println("======>>>> Droolsjbpm x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(droolsjbpm, droolsjbpm));
		System.out.println("======>>>> Droolsjbpm x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(droolsjbpm, sonarlint));
		System.out.println("======>>>> Sonarlint x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(sonarlint, sonarlint));
		
		System.out.println("**** Android x Eclipse ***********");
		System.out.println("======>>>> k9 x Activiti : " + gSimilarity.calculateDoseaSimilarity(k9, activiti));
		System.out.println("======>>>> k9 x AngularJS : " + gSimilarity.calculateDoseaSimilarity(k9, angularjs));
		System.out.println("======>>>> k9 x Arduino : " + gSimilarity.calculateDoseaSimilarity(k9, arduino));
		System.out.println("======>>>> k9 x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(k9, droolsjbpm));
		System.out.println("======>>>> k9 x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(k9, sonarlint));
		
		System.out.println("======>>>> bitcoin x Activiti : " + gSimilarity.calculateDoseaSimilarity(bitcoin, activiti));
		System.out.println("======>>>> bitcoin x AngularJS : " + gSimilarity.calculateDoseaSimilarity(bitcoin, angularjs));
		System.out.println("======>>>> bitcoin x Arduino : " + gSimilarity.calculateDoseaSimilarity(bitcoin, arduino));
		System.out.println("======>>>> bitcoin x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(bitcoin, droolsjbpm));
		System.out.println("======>>>> bitcoin x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(bitcoin, sonarlint));

		System.out.println("======>>>> exoplayer x Activiti : " + gSimilarity.calculateDoseaSimilarity(exoplayer, activiti));
		System.out.println("======>>>> exoplayer x AngularJS : " + gSimilarity.calculateDoseaSimilarity(exoplayer, angularjs));
		System.out.println("======>>>> exoplayer x Arduino : " + gSimilarity.calculateDoseaSimilarity(exoplayer, arduino));
		System.out.println("======>>>> exoplayer x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(exoplayer, droolsjbpm));
		System.out.println("======>>>> exoplayer x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(exoplayer, sonarlint));

		System.out.println("======>>>> sms x Activiti : " + gSimilarity.calculateDoseaSimilarity(sms, activiti));
		System.out.println("======>>>> sms x AngularJS : " + gSimilarity.calculateDoseaSimilarity(sms, angularjs));
		System.out.println("======>>>> sms x Arduino : " + gSimilarity.calculateDoseaSimilarity(sms, arduino));
		System.out.println("======>>>> sms x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(sms, droolsjbpm));
		System.out.println("======>>>> sms x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(sms, sonarlint));
		
		System.out.println("======>>>> talon x Activiti : " + gSimilarity.calculateDoseaSimilarity(talon, activiti));
		System.out.println("======>>>> talon x AngularJS : " + gSimilarity.calculateDoseaSimilarity(talon, angularjs));
		System.out.println("======>>>> talon x Arduino : " + gSimilarity.calculateDoseaSimilarity(talon, arduino));
		System.out.println("======>>>> talon x Droolsjbpm : " + gSimilarity.calculateDoseaSimilarity(talon, droolsjbpm));
		System.out.println("======>>>> talon x Sonarlint : " + gSimilarity.calculateDoseaSimilarity(talon, sonarlint));
	}

}
