package org.designroleminer.smelldetector.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DadosMetodoSmell implements Comparable<DadosMetodoSmell> {

	private String diretorioDaClasse;
	private String nomeClasse;
	private int totalMetodosClasse;
	private String nomeMetodo;
	private int linhaInicial;
	private int linesOfCode;
	private int complexity;
	private int numberOfParameters;
	private int efferent;
	private int charInicial;
	private int charFinal;
	private HashSet<String> listaMensagens;
	private String codigoMetodo;
	private HashSet<String> listaTecnicas;
	private String smell;
	private String classDesignRole;
	
		
	public String getClassDesignRole() {
		return classDesignRole;
	}

	public void setClassDesignRole(String classDesignRole) {
		this.classDesignRole = classDesignRole;
	}

	public String getKey() {
		return diretorioDaClasse+nomeClasse+nomeMetodo+smell;
	}
	
	public HashSet<String> getListaTecnicas() {
		return listaTecnicas;
	}

	public void addTecnica(String tecnica) {
		if (listaTecnicas == null)
			listaTecnicas = new HashSet<>();
		listaTecnicas.add(tecnica);
	}
	
	public String getSmell() {
		return smell;
	}

	public String setSmell(String smell) {
		return this.smell = smell;
	}

	
	
	public HashSet<String> getListaMensagens() {
		return listaMensagens;
	}

	public void addMensagem(String mensagem) {
		if (listaMensagens == null) 
			listaMensagens = new HashSet<>();
		listaMensagens.add(mensagem);
	}

	public int getTotalMetodosClasse() {
		return totalMetodosClasse;
	}

	public void setTotalMetodosClasse(int totalMetodosClasse) {
		this.totalMetodosClasse = totalMetodosClasse;
	}

	public String getCodigoMetodo() {
		return codigoMetodo;
	}

	public void setCodigoMetodo(String codigoMetodo) {
		this.codigoMetodo = codigoMetodo;
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

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;
		DadosMetodoSmell metodo = (DadosMetodoSmell) arg0;
		if (metodo.getDiretorioDaClasse().equals(getDiretorioDaClasse())
				&& (metodo.getNomeClasse().equals(getNomeClasse())) && (metodo.getSmell().equals(getSmell())))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return getDiretorioDaClasse().hashCode() + getNomeClasse().hashCode() + getNomeMetodo().hashCode();
	}
	
	@Override
	public int compareTo(DadosMetodoSmell o) {
		if (o == null)
			return -1;
		return o.getDiretorioDaClasse().compareTo(getDiretorioDaClasse()) + 
			o.getNomeClasse().compareTo(getNomeClasse()) + 
			o.getSmell().compareTo(getSmell());
	}

}
