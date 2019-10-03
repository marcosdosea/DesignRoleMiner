package org.designroleminer.smelldetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.DadosMetodo;
import org.designroleminer.smelldetector.model.FilterSmellResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.designroleminer.smelldetector.model.LimiarTecnica;
import org.hamcrest.core.Is;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

import com.github.mauricioaniche.ck.MethodData;

public class FilterSmells {

	static final String TECNICA_ANICHE = "X";

	public static FilterSmellResult filtrar(ArrayList<ClassMetricResult> classesAnalisar,
			List<LimiarTecnica> listaTecnicas, String commitAnalisado) {

		HashSet<DadosMetodo> listaMethodsSmelly = new HashSet<>();
		HashSet<DadosMetodo> listaMethodsNotSmelly = new HashSet<>();

		for (ClassMetricResult classe : classesAnalisar) {
			for (LimiarTecnica limiarTecnica : listaTecnicas) {

				boolean consideraArchitecturalRoles = limiarTecnica.getTecnica().equals(TECNICA_ANICHE) ? true : false;

				HashMap<String, LimiarMetrica> mapLimiarMetrica = limiarTecnica.getMetricas();
				LimiarMetrica limiarLOC = mapLimiarMetrica
						.get(LimiarMetrica.METRICA_LOC + classe.getDesignRole().toUpperCase());
				if ((consideraArchitecturalRoles && !classe.isArchitecturalRole()) || (limiarLOC == null))
					limiarLOC = mapLimiarMetrica.get(LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

				LimiarMetrica limiarCC = mapLimiarMetrica
						.get(LimiarMetrica.METRICA_CC + classe.getDesignRole().toUpperCase());
				if ((consideraArchitecturalRoles && !classe.isArchitecturalRole()) || (limiarCC == null))
					limiarCC = mapLimiarMetrica.get(LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

				LimiarMetrica limiarEfferent = mapLimiarMetrica
						.get(LimiarMetrica.METRICA_EC + classe.getDesignRole().toUpperCase());
				if ((consideraArchitecturalRoles && !classe.isArchitecturalRole()) || (limiarEfferent == null))
					limiarEfferent = mapLimiarMetrica
							.get(LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

				LimiarMetrica limiarNOP = mapLimiarMetrica
						.get(LimiarMetrica.METRICA_NOP + classe.getDesignRole().toUpperCase());
				if ((consideraArchitecturalRoles && !classe.isArchitecturalRole()) || (limiarNOP == null))
					limiarNOP = mapLimiarMetrica.get(LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

				for (MethodData methodData : classe.getMetricsByMethod().keySet()) {

					MethodMetricResult methodMetrics = classe.getMetricsByMethod().get(methodData);

					DadosMetodo metodo = new DadosMetodo();
					metodo.setCommit(commitAnalisado);
					metodo.setCharFinal(methodData.getFinalChar());
					metodo.setCharInicial(methodData.getInitialChar());
					metodo.setDiretorioDaClasse(classe.getFile());
					metodo.setLinhaInicial(methodData.getInitialLine());
					metodo.setNomeClasse(classe.getClassName());
					metodo.setNomeMetodo(methodData.getNomeMethod());
					metodo.setLinesOfCode(methodMetrics.getLinesOfCode());
					metodo.setEfferent(methodMetrics.getEfferentCoupling());
					metodo.setComplexity(methodMetrics.getComplexity());
					metodo.setNumberOfParameters(methodMetrics.getNumberOfParameters());
					metodo.setClassDesignRole(classe.getDesignRole());

					boolean isSmelly = false;
					if (methodMetrics != null) {
						if (methodMetrics.getLinesOfCode() > limiarLOC.getLimiarMaximo()) {
							String mensagem = "Methods in this system have on maximum " + limiarLOC.getLimiarMaximo()
									+ " lines of code. " + "\nMake sure refactoring could be applied.";
							String type = "Metodo Longo";
							addMetodoSmell(metodo, type, mensagem, listaMethodsSmelly, limiarTecnica.getTecnica());
							isSmelly = true;
						}
						if (methodMetrics.getComplexity() > limiarCC.getLimiarMaximo()) {
							String mensagem = "Methods in this type class have on maximum " + limiarCC.getLimiarMaximo()
									+ " cyclomatic complexity. " + "\nMake sure refactoring could be applied.";
							String type = "Muitos Desvios";
							addMetodoSmell(metodo, type, mensagem, listaMethodsSmelly, limiarTecnica.getTecnica());
							isSmelly = true;
						}
						if (methodMetrics.getEfferentCoupling() > limiarEfferent.getLimiarMaximo()) {
							String mensagem = "Methods in this type class have on maximum "
									+ limiarEfferent.getLimiarMaximo() + " efferent coupling. "
									+ "\nMake sure refactoring could be applied.";
							String type = "Alto Acoplamento Efferent";
							addMetodoSmell(metodo, type, mensagem, listaMethodsSmelly, limiarTecnica.getTecnica());
							isSmelly = true;
						}
						if (methodMetrics.getNumberOfParameters() > limiarNOP.getLimiarMaximo()) {
							String mensagem = "Methods in this type class have on maximum "
									+ limiarNOP.getLimiarMaximo() + " number of parameters. "
									+ "\nMake sure refactoring could be applied.";
							String type = "Muitos Parametros";
							addMetodoSmell(metodo, type, mensagem, listaMethodsSmelly, limiarTecnica.getTecnica());
							isSmelly = true;
						}
					}
					if (!isSmelly)
						listaMethodsNotSmelly.add(metodo);
				}
			}
		}

		FilterSmellResult result = new FilterSmellResult();

		result.setMetodosNotSmelly(listaMethodsNotSmelly);
		result.setMetodosSmell(listaMethodsSmelly);
		return result;
	}

	private static void addMetodoSmell(DadosMetodo metodo, String typeSmell, String mensagemSmell,
			HashSet<DadosMetodo> metodosSmell, String tecnica) {

		metodo.setSmell(typeSmell);
		if (metodosSmell.contains(metodo)) {
			for (DadosMetodo metodoSmell : metodosSmell) {
				if (metodoSmell.equals(metodo)) {
					metodoSmell.addMensagem(mensagemSmell);
					metodoSmell.addTecnica(tecnica);
					break;
				}
			}
		} else {
			DadosMetodo novoMetodo = new DadosMetodo();
			novoMetodo.setCommit(metodo.getCommit());
			novoMetodo.setCharFinal(metodo.getCharFinal());
			novoMetodo.setCharInicial(metodo.getCharInicial());
			novoMetodo.setDiretorioDaClasse(metodo.getDiretorioDaClasse());
			novoMetodo.setLinhaInicial(metodo.getLinhaInicial());
			novoMetodo.setNomeClasse(metodo.getNomeClasse());
			novoMetodo.setNomeMetodo(metodo.getNomeMetodo());
			novoMetodo.setLinesOfCode(metodo.getLinesOfCode());
			novoMetodo.setEfferent(metodo.getEfferent());
			novoMetodo.setComplexity(metodo.getComplexity());
			novoMetodo.setNumberOfParameters(metodo.getNumberOfParameters());
			novoMetodo.setClassDesignRole(metodo.getClassDesignRole());
			novoMetodo.setSmell(metodo.getSmell());
			novoMetodo.addMensagem(mensagemSmell);
			novoMetodo.addTecnica(tecnica);
			metodosSmell.add(novoMetodo);
		}
	}

	public static void gravarMetodosSmell(HashSet<DadosMetodo> metodosSmell, String arquivoDestino) {

		PersistenceMechanism pm = new CSVFile(System.getProperty("user.dir") + "\\" + arquivoDestino);
		pm.write("Tecnicas", "Design Role", "Classe", "Método", "LOC", "CC", "Efferent", "NOP", "Problema de Design",
				"Deveria ser REFATORADO por conta desse problema?", "Se DISCORDAR, quais os motivos? ");
		for (DadosMetodo metodoSmell : metodosSmell) {
			pm.write(metodoSmell.getListaTecnicas().toString().replace('[', ' ').replace(']', ' '),
					metodoSmell.getClassDesignRole(), metodoSmell.getNomeClasse(), metodoSmell.getNomeMetodo(),
					metodoSmell.getLinesOfCode(), metodoSmell.getComplexity(), metodoSmell.getEfferent(),
					metodoSmell.getNumberOfParameters(), metodoSmell.getSmell(), "(1) Discordo Fortemente;");
		}
		System.out.println("Total de métodos longos: " + metodosSmell.size());
	}

}
