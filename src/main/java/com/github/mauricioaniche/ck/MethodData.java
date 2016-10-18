package com.github.mauricioaniche.ck;

import java.util.List;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class MethodData {

	private String nomeMethod;
    private List<SingleVariableDeclaration> parameters;
	
    public String getNomeMethod() {
		return nomeMethod;
	}
	public void setNomeMethod(String nomeMethod) {
		this.nomeMethod = nomeMethod;
	}
	
	@Override
	public int hashCode() {
		if ((nomeMethod != null) && (parameters != null))
			return nomeMethod.hashCode() + parameters.hashCode();
		return nomeMethod.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		MethodData receive = (MethodData) obj;
		boolean parametersEqual = (parameters == receive.getParameters()) || parameters.equals(receive.getParameters()); 
		return nomeMethod.equals(receive.getNomeMethod()) &&
				parametersEqual;
	}
	public List<SingleVariableDeclaration> getParameters() {
		return parameters;
	}
	public void setParameters(List<SingleVariableDeclaration> parameters) {
		this.parameters = parameters;
	}
	@Override
	public String toString() {
		return nomeMethod.toString();
	}
}
