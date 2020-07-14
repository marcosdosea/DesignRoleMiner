package org.designroleminer.smelldetector;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.MetricReport;
import org.designroleminer.smelldetector.model.ClassDataSmelly;
import org.designroleminer.smelldetector.model.FilterSmellResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.designroleminer.smelldetector.model.LimiarTecnica;
import org.designroleminer.smelldetector.model.MethodDataSmelly;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

import com.github.mauricioaniche.ck.MethodData;

public class FilterSmells {

	static final String TECNICA_ANICHE = "X";
	// static final String TECNICA_DOSEA_DESIGNROLE = "D";
	// static final String TECNICA_DOSEA_REFERENCE = "R";

	public static FilterSmellResult filtrar(MetricReport report, List<LimiarTecnica> listaTecnicas,
			String commitAnalisado) {

		Collection<ClassMetricResult> classesAnalisar = report.all();

		HashSet<MethodDataSmelly> listaMethodsSmelly = new HashSet<>();
		HashSet<MethodDataSmelly> listaMethodsNotSmelly = new HashSet<>();

		HashSet<ClassDataSmelly> listaClassesSmelly = new HashSet<>();
		HashSet<ClassDataSmelly> listaClassesNotSmelly = new HashSet<>();
		try {
			for (ClassMetricResult classe : classesAnalisar) {
				for (LimiarTecnica limiarTecnica : listaTecnicas) {

					HashMap<String, LimiarMetrica> mapLimiarMetrica = limiarTecnica.getMetricas();
					// classes
					LimiarMetrica limiarCLOC = mapLimiarMetrica
							.get(LimiarMetrica.METRICA_CLOC + classe.getDesignRole().toUpperCase());
					if (limiarCLOC == null)
						limiarCLOC = mapLimiarMetrica
								.get(LimiarMetrica.METRICA_CLOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

					// method
					LimiarMetrica limiarLOC = mapLimiarMetrica
							.get(LimiarMetrica.METRICA_LOC + classe.getDesignRole().toUpperCase());
					if (limiarLOC == null)
						limiarLOC = mapLimiarMetrica
								.get(LimiarMetrica.METRICA_LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

					LimiarMetrica limiarCC = mapLimiarMetrica
							.get(LimiarMetrica.METRICA_CC + classe.getDesignRole().toUpperCase());
					if (limiarCC == null)
						limiarCC = mapLimiarMetrica.get(LimiarMetrica.METRICA_CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

					LimiarMetrica limiarEfferent = mapLimiarMetrica
							.get(LimiarMetrica.METRICA_EC + classe.getDesignRole().toUpperCase());
					if (limiarEfferent == null)
						limiarEfferent = mapLimiarMetrica
								.get(LimiarMetrica.METRICA_EC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

					LimiarMetrica limiarNOP = mapLimiarMetrica
							.get(LimiarMetrica.METRICA_NOP + classe.getDesignRole().toUpperCase());
					if (limiarNOP == null)
						limiarNOP = mapLimiarMetrica
								.get(LimiarMetrica.METRICA_NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

					ClassDataSmelly classeSmelly = new ClassDataSmelly();
					classeSmelly.setClassDesignRole(classe.getDesignRole());
					classeSmelly.setCommit(commitAnalisado);
					classeSmelly.setDiretorioDaClasse(classe.getFile());
					classeSmelly.setLinesOfCode(classe.getCLoc());
					classeSmelly.setNomeClasse(classe.getClassName());
					boolean isClassSmelly = false;
					if (classe.getCLoc() > limiarCLOC.getLimiarMaximo()) {
						String mensagem = "Class in this system have on maximum " + limiarLOC.getLimiarMaximo()
								+ " lines of code. " + "\nMake sure refactoring could be applied.";
						String type = ClassDataSmelly.LONG_CLASS;
						addClasseSmell(classeSmelly, type, mensagem, listaClassesSmelly, limiarTecnica.getTecnica());
						isClassSmelly = true;
					}
					if (!isClassSmelly)
						listaClassesNotSmelly.add(classeSmelly);

					for (MethodData methodData : classe.getMetricsByMethod().keySet()) {

						MethodMetricResult methodMetrics = classe.getMetricsByMethod().get(methodData);

						MethodDataSmelly metodo = new MethodDataSmelly();
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
								String mensagem = "Methods in this system have on maximum "
										+ limiarLOC.getLimiarMaximo() + " lines of code. "
										+ "\nMake sure refactoring could be applied.";
								String type = MethodDataSmelly.LONG_METHOD;
								addMetodoSmell(metodo, type, mensagem, listaMethodsSmelly, limiarTecnica.getTecnica());
								isSmelly = true;
							}
							if (methodMetrics.getComplexity() > limiarCC.getLimiarMaximo()) {
								String mensagem = "Methods in this type class have on maximum "
										+ limiarCC.getLimiarMaximo() + " cyclomatic complexity. "
										+ "\nMake sure refactoring could be applied.";
								String type = MethodDataSmelly.COMPLEX_METHOD;
								addMetodoSmell(metodo, type, mensagem, listaMethodsSmelly, limiarTecnica.getTecnica());
								isSmelly = true;
							}
							if (methodMetrics.getEfferentCoupling() > limiarEfferent.getLimiarMaximo()) {
								String mensagem = "Methods in this type class have on maximum "
										+ limiarEfferent.getLimiarMaximo() + " efferent coupling. "
										+ "\nMake sure refactoring could be applied.";
								String type = MethodDataSmelly.HIGH_EFFERENT_COUPLING;
								addMetodoSmell(metodo, type, mensagem, listaMethodsSmelly, limiarTecnica.getTecnica());
								isSmelly = true;
							}
							if (methodMetrics.getNumberOfParameters() > limiarNOP.getLimiarMaximo()) {
								String mensagem = "Methods in this type class have on maximum "
										+ limiarNOP.getLimiarMaximo() + " number of parameters. "
										+ "\nMake sure refactoring could be applied.";
								String type = MethodDataSmelly.MANY_PARAMETERS;
								addMetodoSmell(metodo, type, mensagem, listaMethodsSmelly, limiarTecnica.getTecnica());
								isSmelly = true;
							}
						}
						if (!isSmelly)
							listaMethodsNotSmelly.add(metodo);
					}
				}
			}

			for (MethodDataSmelly methodSmelly : listaMethodsSmelly) {
				for (MethodDataSmelly methodNotSmelly : listaMethodsNotSmelly) {
					if (methodSmelly.getNomeClasse().equals(methodNotSmelly.getNomeClasse())
							&& methodSmelly.getNomeMetodo().equals(methodNotSmelly.getNomeMetodo())
							&& methodSmelly.getCommit().equals(methodNotSmelly.getCommit())
							&& methodSmelly.getCharInicial() == methodNotSmelly.getCharInicial()) {
						listaMethodsNotSmelly.remove(methodNotSmelly);
						break;
					}
				}
			}

			for (ClassDataSmelly classSmelly : listaClassesSmelly) {
				for (ClassDataSmelly classNotSmelly : listaClassesNotSmelly) {
					if (classSmelly.getNomeClasse().equals(classNotSmelly.getNomeClasse())
							&& classSmelly.getDiretorioDaClasse().equals(classNotSmelly.getDiretorioDaClasse())
							&& classSmelly.getCommit().equals(classNotSmelly.getCommit())) {
						listaClassesNotSmelly.remove(classNotSmelly);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		FilterSmellResult result = new FilterSmellResult();

		result.setMetodosNotSmelly(listaMethodsNotSmelly);
		result.setMetodosSmell(listaMethodsSmelly);

		result.setClassesNotSmelly(listaClassesNotSmelly);
		result.setClassesSmell(listaClassesSmelly);

		return result;
	}

	private static void addMetodoSmell(MethodDataSmelly metodo, String typeSmell, String mensagemSmell,
			HashSet<MethodDataSmelly> metodosSmell, String tecnica) {

		metodo.setSmell(typeSmell);
		if (metodosSmell.contains(metodo)) {
			for (MethodDataSmelly metodoSmell : metodosSmell) {
				if (metodoSmell.equals(metodo)) {
					metodoSmell.addMensagem(mensagemSmell);
					metodoSmell.addTecnica(tecnica);
					break;
				}
			}
		} else {
			MethodDataSmelly novoMetodo = new MethodDataSmelly();
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

	private static void addClasseSmell(ClassDataSmelly classe, String typeSmell, String mensagemSmell,
			HashSet<ClassDataSmelly> classesSmell, String tecnica) {
		classe.setSmell(typeSmell);
		if (classesSmell.contains(classe)) {
			for (ClassDataSmelly classeSmell : classesSmell) {
				if (classeSmell.equals(classe)) {
					classeSmell.addMensagem(mensagemSmell);
					classeSmell.addTecnica(tecnica);
					break;
				}
			}
		} else {
			ClassDataSmelly novaClasse = new ClassDataSmelly();
			novaClasse.setCommit(classe.getCommit());
			novaClasse.setDiretorioDaClasse(classe.getDiretorioDaClasse());
			novaClasse.setNomeClasse(classe.getNomeClasse());
			novaClasse.setLinesOfCode(classe.getLinesOfCode());
			novaClasse.setClassDesignRole(classe.getClassDesignRole());
			novaClasse.setSmell(classe.getSmell());
			novaClasse.addMensagem(mensagemSmell);
			novaClasse.addTecnica(tecnica);
			classesSmell.add(novaClasse);
		}
	}

	public static void gravarMetodosSmellProgramador(HashSet<MethodDataSmelly> metodosSmell, String arquivoDestino) {

		PersistenceMechanism pm = new CSVFile(arquivoDestino);
		pm.write("Tecnicas", "Design Role", "Classe", "Método", "LOC", "CC", "Efferent", "NOP", "Problema de Design",
				"Deveria ser REFATORADO por conta desse problema?", "Se DISCORDAR, quais os motivos? ");
		for (MethodDataSmelly metodoSmell : metodosSmell) {
			pm.write(metodoSmell.getListaTecnicas().toString().replace('[', ' ').replace(']', ' '),
					metodoSmell.getClassDesignRole(), metodoSmell.getNomeClasse(), metodoSmell.getNomeMetodo(),
					metodoSmell.getLinesOfCode(), metodoSmell.getComplexity(), metodoSmell.getEfferent(),
					metodoSmell.getNumberOfParameters(), metodoSmell.getSmell(), "(1) Discordo Fortemente;");
		}
		System.out.println("Total de métodos longos: " + metodosSmell.size());
	}

	public static void gravarClassesSmellProgramador(HashSet<ClassDataSmelly> classesSmell, String arquivoDestino) {

		PersistenceMechanism pm = new CSVFile(arquivoDestino);
		pm.write("Tecnicas", "Design Role", "Classe", "LOC", "Problema de Design",
				"Deveria ser REFATORADO por conta desse problema?", "Se DISCORDAR, quais os motivos? ");
		for (ClassDataSmelly classeSmell : classesSmell) {
			pm.write(classeSmell.getListaTecnicas().toString().replace('[', ' ').replace(']', ' '),
					classeSmell.getClassDesignRole(), classeSmell.getNomeClasse(), classeSmell.getLinesOfCode(),
					classeSmell.getSmell(), "(1) Discordo Fortemente;");
		}
		System.out.println("Total de classes longas: " + classesSmell.size());
	}

	public static void gravarMetodosSmell(HashSet<MethodDataSmelly> metodosSmell, String arquivoDestino) {

		PersistenceMechanism pm = new CSVFile(arquivoDestino);
		pm.write("Tecnicas", "Design Role", "Classe", "Método", "LOC", "CC", "Efferent", "NOP", "Problema de Design");
		for (MethodDataSmelly metodoSmell : metodosSmell) {
			pm.write(metodoSmell.getListaTecnicas().toString().replace('[', ' ').replace(']', ' '),
					metodoSmell.getClassDesignRole(), metodoSmell.getNomeClasse(), metodoSmell.getNomeMetodo(),
					metodoSmell.getLinesOfCode(), metodoSmell.getComplexity(), metodoSmell.getEfferent(),
					metodoSmell.getNumberOfParameters(), metodoSmell.getSmell());
		}
	}

}
