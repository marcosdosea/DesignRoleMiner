package org.designroleminer.smelldetector.model;

import java.util.HashSet;

public class MethodDataSmelly {

	public static String LONG_METHOD = "Metodo Longo";
	public static String COMPLEX_METHOD = "Muitos Desvios";
	public static String HIGH_EFFERENT_COUPLING = "Alto Acoplamento Efferent";
	public static String MANY_PARAMETERS = "Muitos Parametros";
		
	
	private String commit;
	private String diretorioDaClasse;
	private String nomeClasse;
	private String nomeMetodo;
	private int linhaInicial;
	private int linesOfCode;
	private int complexity;
	private int numberOfParameters;
	private int efferent;
	private int charInicial;
	private int charFinal;
	private String classDesignRole;
	private HashSet<String> listaTecnicas;
	private HashSet<String> listaMensagens;
	private String smell;
	
	
	public String getCommit() {
		return commit;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}

	public String getClassDesignRole() {
		return classDesignRole;
	}

	public void setClassDesignRole(String classDesignRole) {
		this.classDesignRole = classDesignRole;
	}

	public String getDiretorioDaClasse() {
		return diretorioDaClasse;
	}

	public void setDiretorioDaClasse(String diretorioDaClasse) {
		this.diretorioDaClasse = diretorioDaClasse;
	}

	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}

	public String getNomeMetodo() {
		return nomeMetodo;
	}

	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}

	public int getLinhaInicial() {
		return linhaInicial;
	}

	public void setLinhaInicial(int linhaInicial) {
		this.linhaInicial = linhaInicial;
	}

	public int getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(int numeroLinhas) {
		this.linesOfCode = numeroLinhas;
	}

	public int getCharInicial() {
		return charInicial;
	}

	public void setCharInicial(int charInicial) {
		this.charInicial = charInicial;
	}

	public int getCharFinal() {
		return charFinal;
	}

	public void setCharFinal(int charFinal) {
		this.charFinal = charFinal;
	}

	public int getComplexity() {
		return complexity;
	}

	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}

	public int getNumberOfParameters() {
		return numberOfParameters;
	}

	public void setNumberOfParameters(int numberOfParameters) {
		this.numberOfParameters = numberOfParameters;
	}

	public int getEfferent() {
		return efferent;
	}

	public void setEfferent(int efferent) {
		this.efferent = efferent;
	}
	
	public HashSet<String> getListaTecnicas() {
		return listaTecnicas;
	}

	public void addTecnica(String tecnica) {
		if (listaTecnicas == null)
			listaTecnicas = new HashSet<String>();
		listaTecnicas.add(tecnica);
	}

	public void addMensagem(String mensagem) {
		if (listaMensagens == null)
			listaMensagens = new HashSet<String>();
		listaMensagens.add(mensagem);
	}

	public HashSet<String> getListaMensagens() {
		return listaMensagens;
	}

	public String getSmell() {
		return smell;
	}

	public String setSmell(String smell) {
		return this.smell = smell;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;
		MethodDataSmelly metodo = (MethodDataSmelly) arg0;
		if (smell != null) {
			return metodo.getDiretorioDaClasse().equals(getDiretorioDaClasse()) 
					&& metodo.getNomeClasse().equals(getNomeClasse()) 
					&& metodo.getNomeMetodo().equals(getNomeMetodo())
					&& metodo.getCommit().equals(getCommit())
					&& metodo.getSmell().equals(getSmell());
		}
		return metodo.getDiretorioDaClasse().equals(getDiretorioDaClasse())
				&& metodo.getNomeClasse().equals(getNomeClasse()) 
				&& metodo.getNomeMetodo().equals(getNomeMetodo())
				&& metodo.getCommit().equals(getCommit());
	}

	@Override
	public int hashCode() {
		if (smell != null )
			return getDiretorioDaClasse().hashCode() + getNomeClasse().hashCode() 
					+ getNomeMetodo().hashCode() + getCommit().hashCode() + getSmell().hashCode();
		return getDiretorioDaClasse().hashCode() + getNomeClasse().hashCode() 
				+ getNomeMetodo().hashCode() + getCommit().hashCode();
	}
}
