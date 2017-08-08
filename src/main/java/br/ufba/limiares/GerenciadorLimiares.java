package br.ufba.limiares;

import java.util.ArrayList;
import java.util.List;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.MethodMetrics;

import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.persistence.csv.CSVFile;

public class GerenciadorLimiares {

	public void gravarLimiarBenchMark(List<CKNumber> classes, String fileResultado) {

		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole", "LOC", "CC", "Efferent", "NOP");

		ArrayList<Integer> listaLOC = new ArrayList<>();
		ArrayList<Integer> listaCC = new ArrayList<>();
		ArrayList<Integer> listaEfferent = new ArrayList<>();
		ArrayList<Integer> listaNOP = new ArrayList<>();

		for (CKNumber classe : classes) {
			for (MethodMetrics method : classe.getMetricsByMethod().values()) {
				listaLOC.add(method.getLinesOfCode());
				listaCC.add(method.getComplexity());
				listaEfferent.add(method.getEfferentCoupling());
				listaNOP.add(method.getNumberOfParameters());
			}
		}
		if (listaLOC.size() > 0) {
			listaLOC = MedianaQuartis.ordernarOrdemCrescente(listaLOC);
			listaCC = MedianaQuartis.ordernarOrdemCrescente(listaCC);
			listaEfferent = MedianaQuartis.ordernarOrdemCrescente(listaEfferent);
			listaNOP = MedianaQuartis.ordernarOrdemCrescente(listaNOP);
		}
		pm.write("UNDEFINED", MedianaQuartis.percentil(listaLOC, 95), MedianaQuartis.percentil(listaCC, 95),
				MedianaQuartis.percentil(listaEfferent, 95), MedianaQuartis.percentil(listaNOP, 95), "");

	}

	// public int obterValorLimiarGlobal(String projetoExemplo, int
	// porcentagemLimiar) {
	// List<DadosClasse> listaClasses = obterListaClassesProjetoExemplo(false,
	// projetoExemplo);
	// ArrayList<Integer> listaNumLinhasOrdenada = MedianaQuartis
	// .ordernarOrdemCrescente(criarListaNumeroLinhasMetodosDoGrupo(listaClasses));
	// return MedianaQuartis.percentil(listaNumLinhasOrdenada, porcentagemLimiar);
	// }
	//
	// public int obterMedianaGlobal(String projetoExemplo, int porcentagemLimiar) {
	// List<DadosClasse> listaClasses = obterListaClassesProjetoExemplo(false,
	// projetoExemplo);
	// ArrayList<Integer> listaNumLinhasOrdenada = MedianaQuartis
	// .ordernarOrdemCrescente(criarListaNumeroLinhasMetodosDoGrupo(listaClasses));
	// return MedianaQuartis.calcularMediana(listaNumLinhasOrdenada);
	// }

	// public LinkedList<DadosComponentesArquiteturais>
	// criarTabelaCompArquiteturais(String projetoExemplo,
	// int porcentagemLimiar) {
	// LinkedList<List<DadosClasse>> grupos =
	// criarGruposComponentesArquiteturais(projetoExemplo);
	// LinkedList<DadosComponentesArquiteturais> tabelaComponentes = new
	// LinkedList<>();
	// for (List<DadosClasse> grupo : grupos) {
	// DadosComponentesArquiteturais dadosCA = new DadosComponentesArquiteturais();
	// ArrayList<Integer> listaNumLinhasOrdenada = MedianaQuartis
	// .ordernarOrdemCrescente(criarListaNumeroLinhasMetodosDoGrupo(grupo));
	// dadosCA.setMediana(MedianaQuartis.calcularMediana(listaNumLinhasOrdenada));
	// //
	// dadosCA.setPrimeiroQuartil(MedianaQuartis.primeiroQuartil(listaNumLinhasOrdenada));
	// dadosCA.setTerceiroQuartil(MedianaQuartis.percentil(listaNumLinhasOrdenada,
	// porcentagemLimiar));
	// if (grupoEhRegra1(grupo)) {
	// dadosCA.setExtendsClass(grupo.get(0).getClassesExtendsImplements().get(0));
	// dadosCA.setImplementsAPIJava(null);
	// dadosCA.setImplementsArquitecture(null);
	// } else {
	// if (grupoEhRegra2(grupo)) {
	// dadosCA.setExtendsClass(null);
	// dadosCA.setImplementsAPIJava(null);
	// dadosCA.setImplementsArquitecture(obterClasseImplementadaEmComum(grupo.get(0),
	// grupo.get(1)));
	// } else {
	// if (grupoEhRegra3(grupo)) {
	// dadosCA.setExtendsClass(null);
	// dadosCA.setImplementsAPIJava(obterClasseImplementadaEmComum(grupo.get(0),
	// grupo.get(1)));
	// dadosCA.setImplementsArquitecture(null);
	// } else {
	// // É o grupo das classes não classificadas
	// dadosCA.setExtendsClass(null);
	// dadosCA.setImplementsAPIJava(null);
	// dadosCA.setImplementsArquitecture(null);
	// }
	// }
	// }
	// tabelaComponentes.add(dadosCA);
	// }
	// /*
	// * for (DadosComponentesArquiteturais dca : tabelaComponentes) {
	// * System.out.print("Classe extendinda: "+dca.getExtendsClass());
	// * System.out.print(" -- Implements API: "+dca.getImplementsAPIJava());
	// * System.out.print(" -- Implements Arquitetura: "+dca.
	// * getImplementsArquitecture()); System.out.print(" -- Mediana: " +
	// * dca.getMediana() + " -- 1º Quartil: " + dca.getPrimeiroQuartil() +
	// * " -- 3º Quartil: " +dca.getTerceiroQuartil()); System.out.println();
	// * System.out.println(); }
	// */
	// return tabelaComponentes;
	// }
	//
	// private ArrayList<Integer>
	// criarListaNumeroLinhasMetodosDoGrupo(List<DadosClasse> grupo) {
	// ArrayList<Integer> retorno = new ArrayList<>();
	// for (DadosClasse dadosClasse : grupo) {
	// for (DadosMetodo metodo : dadosClasse.getMetodos()) {
	// retorno.add(metodo.getLinesOfCode());
	// }
	// }
	// return retorno;
	// }
	//
	// public boolean grupoEhRegra3(List<DadosClasse> grupo) {
	// if (grupo.size() > 1) {
	// if (temClasseImplementada(grupo.get(0)) &&
	// !implementaInterfaceDaArquitetura(grupo.get(0))
	// && implementaMesmaClasse(grupo.get(0), grupo.get(1))) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public boolean grupoEhRegra2(List<DadosClasse> grupo) {
	// if (grupo.size() > 1) {
	// if (temClasseImplementada(grupo.get(0)) &&
	// implementaInterfaceDaArquitetura(grupo.get(0))
	// && implementaMesmaClasse(grupo.get(0), grupo.get(1))) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public boolean grupoEhRegra1(List<DadosClasse> grupo) {
	// if (grupo.size() > 1) {
	// if (grupo.get(0).getClassesExtendsImplements().get(0) != null
	// && extendMesmaClasse(grupo.get(0), grupo.get(1))) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public LinkedList<List<DadosClasse>>
	// criarGruposComponentesArquiteturais(String projetoExemplo) {
	// List<DadosClasse> listaClasses = obterListaClassesProjetoExemplo(true,
	// projetoExemplo);
	// LinkedList<List<DadosClasse>> grupos = new LinkedList<List<DadosClasse>>();
	// for (DadosClasse classe : listaClasses) {
	// classificarClassesGrupos(classe, grupos);
	// }
	// formarGrupoClassesNaoClassificadas(grupos);
	// return grupos;
	// }
	//
	// /**
	// * TODO: Falta descrever
	// *
	// * @param adicionarArquitetura
	// * @param projetoExemplo
	// * @return
	// */
	// public static List<DadosClasse> obterListaClassesProjetoExemplo(boolean
	// adicionarArquitetura, String projetoExemplo) {
	// ArrayList<String> listaProjetoExemplo = new ArrayList<String>();
	// listaProjetoExemplo.add(projetoExemplo);
	// AnalisadorProjeto analisador = new AnalisadorProjeto();
	// return analisador.getInfoMetodosPorProjetos(listaProjetoExemplo,
	// adicionarArquitetura);
	// }
	//
	// public void formarGrupoClassesNaoClassificadas(LinkedList<List<DadosClasse>>
	// grupos) {
	// List<DadosClasse> novoGrupo = new ArrayList<>();
	// for (Iterator<List<DadosClasse>> iter = grupos.iterator(); iter.hasNext();) {
	// List<DadosClasse> data = iter.next();
	// if (data.size() == 1) {
	// novoGrupo.add(data.get(0));
	// iter.remove();
	// }
	// }
	// if (novoGrupo.size() > 0) {
	// grupos.add(novoGrupo);
	// }
	// }
	//
	// public void classificarClassesGrupos(DadosClasse classe,
	// LinkedList<List<DadosClasse>> grupos) {
	// boolean adicionou = false;
	// for (List<DadosClasse> grupo : grupos) {
	// if (adicionarClasseGrupo(classe, grupo)) {
	// adicionou = true;
	// }
	// }
	// if (!adicionou) {
	// ArrayList<DadosClasse> novoGrupo = new ArrayList<>();
	// novoGrupo.add(classe);
	// grupos.add(novoGrupo);
	// }
	// }
	//
	// public boolean adicionarClasseGrupo(DadosClasse classe, List<DadosClasse>
	// grupo) {
	// // Regra 1
	// if ((classe.getClassesExtendsImplements().get(0) != null) &&
	// extendMesmaClasse(classe, grupo.get(0))) {
	// grupo.add(classe);
	// return true;
	// } else {
	// // Regra 2
	// if (temClasseImplementada(classe) && implementaInterfaceDaArquitetura(classe)
	// && implementaMesmaClasse(classe, grupo.get(0))) {
	// grupo.add(classe);
	// return true;
	// } else {
	// // Regra 3
	// if (temClasseImplementada(classe) &&
	// !implementaInterfaceDaArquitetura(classe)
	// && implementaMesmaClasse(classe, grupo.get(0))) {
	// grupo.add(classe);
	// return true;
	// }
	// }
	// }
	// return false;
	// }
	//
	// public boolean extendMesmaClasse(DadosClasse classe, DadosClasse grupo) {
	// return
	// classe.getClassesExtendsImplements().get(0).equals(grupo.getClassesExtendsImplements().get(0));
	// }
	//
	// public boolean implementaMesmaClasse(DadosClasse classe, DadosClasse grupo) {
	// if (!temClasseImplementada(grupo)) {
	// return false;
	// }
	// for (int i = 1; i < classe.getClassesExtendsImplements().size(); i++) {
	// for (int j = 1; j < grupo.getClassesExtendsImplements().size(); j++) {
	// if
	// (classe.getClassesExtendsImplements().get(i).equals(grupo.getClassesExtendsImplements().get(j)))
	// {
	// return true;
	// }
	// }
	// }
	// return false;
	// }
	//
	// public boolean temClasseImplementada(DadosClasse classe) {
	// if (classe.getClassesExtendsImplements().size() > 1) {
	// return true;
	// }
	// return false;
	// }
	//
	// public boolean implementaInterfaceDaArquitetura(DadosClasse classe) {
	// ArrayList<String> interfacesAPI = ProviderModel.INSTANCE.getInterfacesAPI();
	// for (int i = 1; i < classe.getClassesExtendsImplements().size(); i++) {
	// for (int j = 0; j < interfacesAPI.size(); j++) {
	// if (classe.getClassesExtendsImplements().get(i).equals(interfacesAPI.get(j)))
	// {
	// return false;
	// }
	// }
	// }
	// return true;
	// }
	//
	// public String obterClasseImplementadaEmComum(DadosClasse classe, DadosClasse
	// grupo) {
	// for (int i = 1; i < classe.getClassesExtendsImplements().size(); i++) {
	// for (int j = 1; j < grupo.getClassesExtendsImplements().size(); j++) {
	// if
	// (classe.getClassesExtendsImplements().get(i).equals(grupo.getClassesExtendsImplements().get(j)))
	// {
	// return classe.getClassesExtendsImplements().get(i);
	// }
	// }
	// }
	// return null;
	// }
	//
	// public static String getClasseComponente(DadosComponentesArquiteturais
	// componente) {
	// if (componente.getExtendsClass() != null) {
	// return componente.getExtendsClass();
	// }
	// if (componente.getImplementsAPIJava() != null) {
	// return componente.getImplementsAPIJava();
	// }
	// if (componente.getImplementsArquitecture() != null) {
	// return componente.getImplementsArquitecture();
	// }
	// return componenteNaoClassificado;
	// }

}
