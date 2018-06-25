package com.github.similarity;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.ck.CKNumber;
import com.github.limiares.GerenciadorLimiares;

public class GerenciadorSimilarityTest {

	GerenciadorSimilarity gSimilarity;
	GerenciadorLimiares gLimiares;
	
	
	@Before
	public void initialize() {
		gSimilarity = new GerenciadorSimilarity();
		gLimiares = new GerenciadorLimiares();
	}
	
	//@Test
	public void testCalculateSimilarityMenor() {
		try {
			ArrayList<String> listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\bitcoin-wallet");
			ArrayList<CKNumber> bitcoin = gLimiares.getMetricasProjetos(listAndroid);
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\k-9");
			ArrayList<CKNumber> k9 = gLimiares.getMetricasProjetos(listAndroid);
			System.out.println("======>>>> Bitcoin x K9 : " + gSimilarity.calculateSimilarityPerDesignRole(bitcoin, k9));
		} catch (Exception e) {
			assertTrue(false);
		}
		assertTrue(true);
	}
	
	@Test
	public void testCalculateSimilarityBigBlueButton() {
		try {
			System.out.println("Iniciando Cálculo Similaridade 1...");
			ArrayList<String> web = new ArrayList<>();
			web.add("D:\\Projetos\\_Web\\bigbluebutton");
			ArrayList<CKNumber> metricasBigBlueButton = gLimiares.getMetricasProjetos(web);

			ArrayList<String> listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\bitcoin-wallet");
			ArrayList<CKNumber> bitcoin = gLimiares.getMetricasProjetos(listAndroid);
			System.out.println("======>>>> BigblueButton x Bitcoin : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, bitcoin));
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\k-9");
			ArrayList<CKNumber> k9 = gLimiares.getMetricasProjetos(listAndroid);
			System.out.println("======>>>> BigblueButton x K9 : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, k9));
			
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\ExoPlayer");
			ArrayList<CKNumber> exoplayer = gLimiares.getMetricasProjetos(listAndroid);
			System.out.println("======>>>> BigblueButton x Exoplayer : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, exoplayer));
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\sms-backup-plus");
			ArrayList<CKNumber> sms = gLimiares.getMetricasProjetos(listAndroid);
			System.out.println("======>>>> BigblueButton x SMS : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, sms));
			
			listAndroid = new ArrayList<>();
			listAndroid.add("D:\\Projetos\\_Android\\Talon-for-Twitter");
			ArrayList<CKNumber> talon = gLimiares.getMetricasProjetos(listAndroid);
			System.out.println("======>>>> BigblueButton x Talon : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, talon));
			
			
			System.out.println("======>>>> BigblueButton x BigblueButton: " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, metricasBigBlueButton));
			System.out.println("======>>>> Bitcoin x Bitcoin: " + gSimilarity.calculateSimilarityPerDesignRole(bitcoin, bitcoin));
			System.out.println("======>>>> Bitcoin x K9: " + gSimilarity.calculateSimilarityPerDesignRole(bitcoin, k9));
			System.out.println("======>>>> Bitcoin x Exoplayer: " + gSimilarity.calculateSimilarityPerDesignRole(bitcoin, exoplayer));
			System.out.println("======>>>> Bitcoin x SMS: " + gSimilarity.calculateSimilarityPerDesignRole(bitcoin, sms));
			System.out.println("======>>>> Bitcoin x Talon: " + gSimilarity.calculateSimilarityPerDesignRole(bitcoin, talon));
			System.out.println("======>>>> K9 X K9: " + gSimilarity.calculateSimilarityPerDesignRole(k9, k9));
			System.out.println("======>>>> K9 X Exoplayer: " + gSimilarity.calculateSimilarityPerDesignRole(k9, exoplayer));
			System.out.println("======>>>> K9 X SMS: " + gSimilarity.calculateSimilarityPerDesignRole(k9, sms));
			System.out.println("======>>>> K9 X Talon: " + gSimilarity.calculateSimilarityPerDesignRole(k9, talon));
			System.out.println("======>>>> Exoplayer X Exoplayer: " + gSimilarity.calculateSimilarityPerDesignRole(exoplayer, exoplayer));
			System.out.println("======>>>> Exoplayer X SMS: " + gSimilarity.calculateSimilarityPerDesignRole(exoplayer, sms));
			System.out.println("======>>>> Exoplayer X Talon: " + gSimilarity.calculateSimilarityPerDesignRole(exoplayer, talon));
			System.out.println("======>>>> SMS X SMS: " + gSimilarity.calculateSimilarityPerDesignRole(sms, sms));
			System.out.println("======>>>> SMS X talon: " + gSimilarity.calculateSimilarityPerDesignRole(sms, talon));
			System.out.println("======>>>> Talon X talon: " + gSimilarity.calculateSimilarityPerDesignRole(talon, talon));
			
			System.out.println("******* Eclipse *****");
			ArrayList<String> listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\Activiti-Designer");
			ArrayList<CKNumber> activiti = gLimiares.getMetricasProjetos(listEclipse);
			System.out.println("======>>>> BigblueButton x Activiti : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, activiti));
			
			listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\angularjs-eclipse");
			ArrayList<CKNumber> angularjs = gLimiares.getMetricasProjetos(listEclipse);
			System.out.println("======>>>> BigblueButton x AngularJS : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, angularjs));
			
			listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\arduino-eclipse-plugin");
			ArrayList<CKNumber> arduino = gLimiares.getMetricasProjetos(listEclipse);
			System.out.println("======>>>> BigblueButton x Arduino : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, arduino));
			
			listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\droolsjbpm-tools");
			ArrayList<CKNumber> droolsjbpm = gLimiares.getMetricasProjetos(listEclipse);
			System.out.println("======>>>> BigblueButton x Droolsjbpm : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, droolsjbpm));
			
			listEclipse = new ArrayList<>();
			listEclipse.add("D:\\Projetos\\_Eclipse\\sonarlint-eclipse");
			ArrayList<CKNumber> sonarlint = gLimiares.getMetricasProjetos(listEclipse);
			System.out.println("======>>>> BigblueButton x Sonarlint : " + gSimilarity.calculateSimilarityPerDesignRole(metricasBigBlueButton, sonarlint));
			
			System.out.println("======>>>> Activiti x Activiti : " + gSimilarity.calculateSimilarityPerDesignRole(activiti, activiti));
			System.out.println("======>>>> Activiti x AngularJS : " + gSimilarity.calculateSimilarityPerDesignRole(activiti, angularjs));
			System.out.println("======>>>> Activiti x Arduino : " + gSimilarity.calculateSimilarityPerDesignRole(activiti, arduino));
			System.out.println("======>>>> Activiti x Droolsjbpm : " + gSimilarity.calculateSimilarityPerDesignRole(activiti, droolsjbpm));
			System.out.println("======>>>> Activiti x Sonarlint : " + gSimilarity.calculateSimilarityPerDesignRole(activiti, sonarlint));
			
			System.out.println("======>>>> AngularJS x AngularJS : " + gSimilarity.calculateSimilarityPerDesignRole(angularjs, angularjs));
			System.out.println("======>>>> AngularJS x Arduino : " + gSimilarity.calculateSimilarityPerDesignRole(angularjs, arduino));
			System.out.println("======>>>> AngularJS x Droolsjbpm : " + gSimilarity.calculateSimilarityPerDesignRole(angularjs, droolsjbpm));
			System.out.println("======>>>> AngularJS x Sonarlint : " + gSimilarity.calculateSimilarityPerDesignRole(angularjs, sonarlint));
			
			System.out.println("======>>>> Arduino x Arduino : " + gSimilarity.calculateSimilarityPerDesignRole(arduino, arduino));
			System.out.println("======>>>> Arduino x Droolsjbpm : " + gSimilarity.calculateSimilarityPerDesignRole(arduino, droolsjbpm));
			System.out.println("======>>>> Arduino x Sonarlint : " + gSimilarity.calculateSimilarityPerDesignRole(arduino, sonarlint));
			
			System.out.println("======>>>> Droolsjbpm x Droolsjbpm : " + gSimilarity.calculateSimilarityPerDesignRole(droolsjbpm, droolsjbpm));
			System.out.println("======>>>> Droolsjbpm x Sonarlint : " + gSimilarity.calculateSimilarityPerDesignRole(droolsjbpm, sonarlint));
			
			System.out.println("======>>>> Sonarlint x Sonarlint : " + gSimilarity.calculateSimilarityPerDesignRole(sonarlint, sonarlint));
			
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

}
