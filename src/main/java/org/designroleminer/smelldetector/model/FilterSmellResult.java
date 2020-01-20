package org.designroleminer.smelldetector.model;

import java.util.HashSet;

public class FilterSmellResult {

	private String commitId;
	private HashSet<MethodDataSmelly> metodosSmell;
	private HashSet<MethodDataSmelly> metodosNotSmelly;

	private HashSet<ClassDataSmelly> classesSmell;
	private HashSet<ClassDataSmelly> classesNotSmelly;

	
	public FilterSmellResult(String commitId) {
		this.commitId = commitId;
	}
	public String getCommitId() {
		return commitId;
	}
	
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

	public HashSet<ClassDataSmelly> getClassesSmell() {
		return classesSmell;
	}
	public void setClassesSmell(HashSet<ClassDataSmelly> classesSmell) {
		this.classesSmell = classesSmell;
	}
	public HashSet<ClassDataSmelly> getClassesNotSmelly() {
		return classesNotSmelly;
	}
	public void setClassesNotSmelly(HashSet<ClassDataSmelly> classesNotSmelly) {
		this.classesNotSmelly = classesNotSmelly;
	}
}
