package org.designroleminer.smelldetector.model;

import java.util.HashMap;
import java.util.HashSet;

import com.github.mauricioaniche.ck.MethodData;

public class FilterSmellResult {

	private HashMap<String, DadosMetodoSmell> metodosSmell;
	private HashSet<MethodData> listaMethodsSmelly;
	private HashSet<MethodData> listaMethodsNotSmelly;
	public HashMap<String, DadosMetodoSmell> getMetodosSmell() {
		return metodosSmell;
	}
	public void setMetodosSmell(HashMap<String, DadosMetodoSmell> metodosSmell) {
		this.metodosSmell = metodosSmell;
	}
	public HashSet<MethodData> getListaMethodsSmelly() {
		return listaMethodsSmelly;
	}
	public void setListaMethodsSmelly(HashSet<MethodData> listaMethodsSmelly) {
		this.listaMethodsSmelly = listaMethodsSmelly;
	}
	public HashSet<MethodData> getListaMethodsNotSmelly() {
		return listaMethodsNotSmelly;
	}
	public void setListaMethodsNotSmelly(HashSet<MethodData> listaMethodsNotSmelly) {
		this.listaMethodsNotSmelly = listaMethodsNotSmelly;
	}
	
	
}
