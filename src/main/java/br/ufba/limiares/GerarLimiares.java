package br.ufba.limiares;

import java.util.ArrayList;

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
		ArrayList<String> projetos = new ArrayList<>();
		projetos.add("D:/Projetos/_Web/heritrix3/");

		ArrayList<CKNumber> projetosMetricas = getMetricasProjetos(projetos);

		GerenciadorLimiares gLimiares = new GerenciadorLimiares();
		gLimiares.gravarLimiarBenchMark(projetosMetricas, "C:\\eclipse\\configuration\\br.ufs.smelldetector\\A.csv");
	}

}
