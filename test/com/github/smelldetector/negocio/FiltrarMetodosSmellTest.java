package com.github.smelldetector.negocio;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.github.ck.CK;
import com.github.ck.CKNumber;
import com.github.ck.CKReport;
import com.github.smelldetector.model.DadosMetodoSmell;
import com.github.smelldetector.model.LimiarTecnica;
import com.github.smelldetector.negocio.CarregaSalvaArquivo;
import com.github.smelldetector.negocio.FiltrarMetodosSmell;

public class FiltrarMetodosSmellTest {
	
	@Test
	public void testFiltrarPorValoresPredefinidos() {

		List<LimiarTecnica> listaTecnicas = new ArrayList<>();
		listaTecnicas = CarregaSalvaArquivo.carregarLimiares("configuration\\designroletool\\");

		ArrayList<String> listaPathProjetos = new ArrayList<>();
		listaPathProjetos.add("D:/Projetos/mobilemedia/MobileMedia01_OO");

		ArrayList<CKNumber> listaClasses = new ArrayList<>();
		for (String path : listaPathProjetos) {
			CKReport report = new CK().calculate(path);
			listaClasses.addAll(report.all());
		}
		
		HashMap<String, DadosMetodoSmell> metodosSmell = null;
		metodosSmell = FiltrarMetodosSmell.filtrar(listaClasses, listaTecnicas, metodosSmell);

		exibeMetodosLongos(metodosSmell.values(), "Filtrar por valor limiar");

		assertTrue(metodosSmell.keySet().size() > 0);
	}

	
	private void exibeMetodosLongos(Collection<DadosMetodoSmell> metodosSmell, String descricaoMetodo) {
		System.out.println("\n\n\n" + descricaoMetodo);
		System.out.println("Classe   |     Método       |     Linhas Código     |  CC  | Efferent |  NOP");
		for (DadosMetodoSmell metodoSmell : metodosSmell) {
			System.out.println(metodoSmell.getNomeClasse() + "  | " + metodoSmell.getNomeMetodo() + " | "
					+ metodoSmell.getLinesOfCode() + "|" + metodoSmell.getComplexity() + " | "
					+ metodoSmell.getEfferent() + " | " + metodoSmell.getNumberOfParameters());
		}
		System.out.println("Total de métodos longos: " + metodosSmell.size());
	}

}
