package org.designroleminer.smelldetector.model;

import java.util.HashSet;

public class FilterSmellResult {

	private HashSet<MethodDataSmelly> metodosSmell;
	private HashSet<MethodDataSmelly> metodosNotSmelly;


	public HashSet<MethodDataSmelly> getMetodosNotSmelly() {
		return metodosNotSmelly;
	}
	public void setMetodosNotSmelly(HashSet<MethodDataSmelly> metodosNotSmelly) {
		this.metodosNotSmelly = metodosNotSmelly;
	}
	public HashSet<MethodDataSmelly> getMetodosSmell() {
		return metodosSmell;
	}
	public void setMetodosSmell(HashSet<MethodDataSmelly> metodosSmell) {
		this.metodosSmell = metodosSmell;
	}
	
}
