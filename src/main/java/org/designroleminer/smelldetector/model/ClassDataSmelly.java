package org.designroleminer.smelldetector.model;

import java.util.HashSet;

public class ClassDataSmelly {

	public static String LONG_CLASS = "Class Longa";
	
	private String commit;
	private String diretorioDaClasse;
	private String nomeClasse;
	private int linesOfCode;
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

	public int getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(int numeroLinhas) {
		this.linesOfCode = numeroLinhas;
	}

	public HashSet<String> getListaTecnicas() {
		return listaTecnicas;
	}

	public void addTecnica(String tecnica) {
		if (listaTecnicas == null)
			listaTecnicas = new HashSet<>();
		listaTecnicas.add(tecnica);
	}

	public void addMensagem(String mensagem) {
		if (listaMensagens == null)
			listaMensagens = new HashSet<>();
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
		ClassDataSmelly metodo = (ClassDataSmelly) arg0;
		if (smell != null) {
			return metodo.getDiretorioDaClasse().equals(getDiretorioDaClasse()) 
					&& metodo.getNomeClasse().equals(getNomeClasse()) 
					&& metodo.getCommit().equals(getCommit())
					&& metodo.getSmell().equals(getSmell());
		}
		return metodo.getDiretorioDaClasse().equals(getDiretorioDaClasse())
				&& metodo.getNomeClasse().equals(getNomeClasse()) 
				&& metodo.getCommit().equals(getCommit());
	}

	@Override
	public int hashCode() {
		if (smell != null )
			return getDiretorioDaClasse().hashCode() + getNomeClasse().hashCode() 
					+ getCommit().hashCode() + getSmell().hashCode();
		return getDiretorioDaClasse().hashCode() + getNomeClasse().hashCode() 
				+ getCommit().hashCode();
	}
}
