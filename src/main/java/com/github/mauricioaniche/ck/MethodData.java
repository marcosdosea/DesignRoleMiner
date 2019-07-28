package com.github.mauricioaniche.ck;

import java.io.Serializable;
import java.util.List;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class MethodData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8680058564070002960L;
	private String nomeMethod;
    private List<SingleVariableDeclaration> parameters;
	private int initialLine;
	private int initialChar;
	private int finalChar;
	private boolean isConstructor;
	
    public boolean isConstructor() {
		return isConstructor;
	}
	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}
	public String getNomeMethod() {
		return nomeMethod;
	}
	public void setNomeMethod(String nomeMethod) {
		this.nomeMethod = nomeMethod;
	}
	
	@Override
	public int hashCode() {
		if (nomeMethod != null)
			return nomeMethod.hashCode();
		return nomeMethod.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		MethodData receive = (MethodData) obj;
		return getNomeMethod().equals(receive.getNomeMethod()) &&
				//parametersEqual && 
				(receive.getInitialChar()==getInitialChar());
	}
	public List<SingleVariableDeclaration> getParameters() {
		return parameters;
	}
	public void setParameters(List<SingleVariableDeclaration> parameters) {
		this.parameters = parameters;
	}
	
    public int getInitialLine() {
		return initialLine;
	}
	public void setInitialLine(int initialLine) {
		this.initialLine = initialLine;
	}
	public int getInitialChar() {
		return initialChar;
	}
	public void setInitialChar(int initialChar) {
		this.initialChar = initialChar;
	}
	public int getFinalChar() {
		return finalChar;
	}
	public void setFinalChar(int finalChar) {
		this.finalChar = finalChar;
	}
	@Override
	public String toString() {
		return nomeMethod.toString();
	}
}
