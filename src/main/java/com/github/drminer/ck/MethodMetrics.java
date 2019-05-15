package com.github.drminer.ck;

import java.io.Serializable;

public class MethodMetrics implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int linesOfCode;
	private int complexity;
	private int cohesion;
	private int numberOfParameters;
	private int efferentCoupling;
	
	public MethodMetrics() {
		linesOfCode = 0;
		complexity = 0;
		cohesion = 0;
		numberOfParameters = 0;
		efferentCoupling = 0;
	}
	
	public int getLinesOfCode() {
		return linesOfCode;
	}
	public void setLinesOfCode(int linesOfCode) {
		this.linesOfCode = linesOfCode;
	}
	public int getComplexity() {
		return complexity;
	}
	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}
	public int getCohesion() {
		return cohesion;
	}
	public void setCohesion(int cohesion) {
		this.cohesion = cohesion;
	}
	public int getNumberOfParameters() {
		return numberOfParameters;
	}
	public void setNumberOfParameters(int numberOfParameters) {
		this.numberOfParameters = numberOfParameters;
	}
	
	public int getEfferentCoupling() {
		return efferentCoupling;
	}

	public void setEfferentCoupling(int efferentCoupling) {
		this.efferentCoupling = efferentCoupling;
	}
}
