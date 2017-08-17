package br.ufba.smelldetector.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.MethodData;
import com.github.mauricioaniche.ck.MethodMetrics;

import br.ufba.smelldetector.model.DadosMetodoSmell;
import br.ufba.smelldetector.model.LimiarMetrica;
import br.ufba.smelldetector.model.LimiarTecnica;

public class FiltrarMetodosSmell {

	static final String TECNICA_ANICHE = "X";
	
	public static HashMap<String, DadosMetodoSmell> filtrar(ArrayList<CKNumber> classesAnalisar,
			List<LimiarTecnica> listaTecnicas, HashMap<String, DadosMetodoSmell> metodosSmell) {
		if (metodosSmell == null)
			metodosSmell = new HashMap<>();

		for (CKNumber classe : classesAnalisar) {
			for (LimiarTecnica limiarTecnica : listaTecnicas) {
				
				boolean consideraArchitecturalRoles = limiarTecnica.getTecnica().equals(TECNICA_ANICHE) ?true:false;
				
				HashMap<String, LimiarMetrica> mapLimiarMetrica = limiarTecnica.getMetricas();
				LimiarMetrica limiarLOC = mapLimiarMetrica.get(LimiarMetrica.LOC + classe.getDesignRole().toUpperCase());
				if ((consideraArchitecturalRoles && !classe.isArchitecturalRole()) || (limiarLOC == null))
					limiarLOC = mapLimiarMetrica.get(LimiarMetrica.LOC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

				LimiarMetrica limiarCC = mapLimiarMetrica.get(LimiarMetrica.CC + classe.getDesignRole().toUpperCase());
				if ((consideraArchitecturalRoles && !classe.isArchitecturalRole()) || (limiarCC == null))
					limiarCC = mapLimiarMetrica.get(LimiarMetrica.CC + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

				LimiarMetrica limiarEfferent = mapLimiarMetrica.get(LimiarMetrica.Efferent + classe.getDesignRole().toUpperCase());
				if ((consideraArchitecturalRoles && !classe.isArchitecturalRole()) || (limiarEfferent == null))
					limiarEfferent = mapLimiarMetrica.get(LimiarMetrica.Efferent + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

				LimiarMetrica limiarNOP = mapLimiarMetrica.get(LimiarMetrica.NOP + classe.getDesignRole().toUpperCase());
				if ((consideraArchitecturalRoles && !classe.isArchitecturalRole()) || (limiarNOP == null))
					limiarNOP = mapLimiarMetrica.get(LimiarMetrica.NOP + LimiarMetrica.DESIGN_ROLE_UNDEFINED);

				for (MethodData metodo : classe.getMetricsByMethod().keySet()) {

					MethodMetrics metodoMetrics = classe.getMetricsByMethod().get(metodo);

					if (metodoMetrics.getLinesOfCode() > limiarLOC.getLimiarMaximo()) {
						String mensagem = "Methods in this system have on maximum " + limiarLOC.getLimiarMaximo()
								+ " lines of code. " + "\nMake sure refactoring could be applied.";
						String type = "Método Longo";
						addMetodoSmell(classe, metodo, metodoMetrics, type, mensagem, metodosSmell, limiarTecnica.getTecnica());
					}
					if (metodoMetrics.getComplexity() > limiarCC.getLimiarMaximo()) {
						String mensagem = "Methods in this type class have on maximum " + limiarCC.getLimiarMaximo()
								+ " cyclomatic complexity. " + "\nMake sure refactoring could be applied.";
						String type = "Muitos Desvios";
						addMetodoSmell(classe, metodo, metodoMetrics, type, mensagem, metodosSmell, limiarTecnica.getTecnica());
					}
					if (metodoMetrics.getEfferentCoupling() > limiarEfferent.getLimiarMaximo()) {
						String mensagem = "Methods in this type class have on maximum "
								+ limiarEfferent.getLimiarMaximo() + " efferent coupling. "
								+ "\nMake sure refactoring could be applied.";
						String type = "Alto Acoplamento Efferent";
						addMetodoSmell(classe, metodo, metodoMetrics, type, mensagem, metodosSmell, limiarTecnica.getTecnica());
					}
					if (metodoMetrics.getNumberOfParameters() > limiarNOP.getLimiarMaximo()) {
						String mensagem = "Methods in this type class have on maximum " + limiarNOP.getLimiarMaximo()
								+ " number of parameters. " + "\nMake sure refactoring could be applied.";
						String type = "Muitos Parâmetros";
						addMetodoSmell(classe, metodo, metodoMetrics, type, mensagem, metodosSmell, limiarTecnica.getTecnica());
					}
				}
			}
		}
		return metodosSmell;
	}

	private static void addMetodoSmell(CKNumber classe, MethodData metodo, MethodMetrics metricas, String type,
			String mensagem, HashMap<String, DadosMetodoSmell> metodosSmell, String tecnica) {

		DadosMetodoSmell dadosMetodoSmell = new DadosMetodoSmell();
		dadosMetodoSmell.setCharFinal(metodo.getFinalChar());
		dadosMetodoSmell.setCharInicial(metodo.getInitialChar());
		dadosMetodoSmell.setDiretorioDaClasse(classe.getFile());
		dadosMetodoSmell.setLinhaInicial(metodo.getInitialLine());
		dadosMetodoSmell.setNomeClasse(classe.getClassName());
		dadosMetodoSmell.setNomeMetodo(metodo.getNomeMethod());
		dadosMetodoSmell.setTotalMetodosClasse(classe.getNom());
		dadosMetodoSmell.setSmell(type);

		DadosMetodoSmell metodoSmellExistente = metodosSmell.get(dadosMetodoSmell.getKey());
		if (metodoSmellExistente != null)
			dadosMetodoSmell = metodoSmellExistente;
		else {
			String codigoMetodo = null;
			for (DadosMetodoSmell metodoSmell: metodosSmell.values()) {
				if (metodoSmell.getDiretorioDaClasse().equals(classe.getFile()) &&
						(metodoSmell.getLinhaInicial() == metodo.getInitialLine()) ) {
					dadosMetodoSmell.setCodigoMetodo(metodoSmell.getCodigoMetodo()); 
					codigoMetodo = metodoSmell.getCodigoMetodo();
					break;
				}
			}
			if (codigoMetodo == null) {
				codigoMetodo = String.format("%02d", metodosSmell.size() + 1);
				dadosMetodoSmell.setCodigoMetodo("M" + codigoMetodo);
			}
		}

		dadosMetodoSmell.setLinesOfCode(metricas.getLinesOfCode());
		dadosMetodoSmell.setEfferent(metricas.getEfferentCoupling());
		dadosMetodoSmell.setComplexity(metricas.getComplexity());
		dadosMetodoSmell.setNumberOfParameters(metricas.getNumberOfParameters());
		dadosMetodoSmell.addMensagem(mensagem);
		dadosMetodoSmell.addTecnica(tecnica);
		metodosSmell.put(dadosMetodoSmell.getKey(), dadosMetodoSmell);
	}

}
