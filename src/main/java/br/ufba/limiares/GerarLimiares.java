package br.ufba.limiares;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

public class GerarLimiares {

	public static ArrayList<CKNumber> getMetricasProjetos(ArrayList<String> projetos) {
		ArrayList<CKNumber> listaClasses = new ArrayList<>();
		for (String path : projetos) {
			CKReport report = new CK().calculate(path);
			listaClasses.addAll(report.all());
		}
		return listaClasses;
	}

	public static void main(String[] args) {
		final String PASTA_RESULTADO = "C:\\eclipse\\configuration\\br.ufs.smelldetector\\";
		GerenciadorLimiares gLimiares = new GerenciadorLimiares();
		
		// D:\Projetos\_Benchmark\phenotips
		
		
		System.out.println("Gerando Planila de Design Roles...");
		ArrayList<String> projetosAnalisar = lerProjetos("analisar.txt"); 
		ArrayList<CKNumber> metricasProjetosAnalisar = getMetricasProjetos(projetosAnalisar);
		gLimiares.gerarDesignRoles(metricasProjetosAnalisar, "C:\\DR\\DR-DEH.CSV");		
		
		System.out.println("Iniciando a coleta de métricas do projeto referencia...");
		ArrayList<String> projetosReferencia = lerProjetos("referencia.txt"); 
		ArrayList<CKNumber> metricasProjetosReferencia = getMetricasProjetos(projetosReferencia);
		
		System.out.println("Iniciando a coleta de métricas dos projetos WEB...");
		ArrayList<String> projetosBenchmark = lerProjetos("benchmark.txt");
		ArrayList<CKNumber> metricasProjetosBenchmark = getMetricasProjetos(projetosBenchmark);
		
		System.out.println("Gerando Limiares por Alves...");
		gLimiares.gerarLimiarAlves(metricasProjetosBenchmark, PASTA_RESULTADO + "A.csv");
		
		System.out.println("Gerando Limiares por Aniche...");
		gLimiares.gerarLimiarAniche(metricasProjetosBenchmark, PASTA_RESULTADO + "X.csv");

		System.out.println("Gerando Limiares por Dosea Referencia...");
		gLimiares.gerarLimiarDoseaPercentil(metricasProjetosReferencia, PASTA_RESULTADO + "R.csv");

		System.out.println("Gerando Limiares por Dosea Referencia por Design Role...");
		gLimiares.gerarLimiarDoseaDesignRolePercentil(metricasProjetosReferencia, PASTA_RESULTADO + "D.csv");

		System.out.println("Limiares gravados na pasta " + PASTA_RESULTADO + " com sucesso!");
	}

	private static ArrayList<String> lerProjetos(String nomeArquivo) {
		ArrayList<String> projetos = new ArrayList<>();
		String arquivo = System.getProperty("user.dir") + "\\" + nomeArquivo;
  	    File file = new File(arquivo);
		 try {
			   if (file.exists()) {
				   Scanner scanner = new Scanner(file);
				   while (scanner.hasNext()) {
					   projetos.add(scanner.next());
				   }
				   scanner.close();
			   }
		 } catch (FileNotFoundException e) {
			   System.out.println("Arquivo não encontrados: " + arquivo);
	     }
		 return projetos;
	}

}
