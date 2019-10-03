package org.designroleminer.smelldetector.model;

import java.util.HashSet;

public class FilterSmellResult {

	private HashSet<DadosMetodo> metodosSmell;
	private HashSet<DadosMetodo> metodosNotSmelly;


	public HashSet<DadosMetodo> getMetodosNotSmelly() {
		return metodosNotSmelly;
	}
	public void setMetodosNotSmelly(HashSet<DadosMetodo> metodosNotSmelly) {
		this.metodosNotSmelly = metodosNotSmelly;
	}
	public HashSet<DadosMetodo> getMetodosSmell() {
		return metodosSmell;
	}
	public void setMetodosSmell(HashSet<DadosMetodo> metodosSmell) {
		this.metodosSmell = metodosSmell;
	}
	
}
