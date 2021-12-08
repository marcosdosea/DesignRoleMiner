package org.designroleminer;

public class LimiarMetrica {

	// public static final int NUMERO_METRICAS_DISPONIVEIS = 4;
	public static final String DESIGN_ROLE_UNDEFINED = "UNDEFINED";
	public static final String DESIGN_ROLE_TEST = "TEST";
	public static final String DESIGN_ROLE_ENTITY = "ENTITY";

	public static final String METRICA_LOC = "LOC";
	public static final String METRICA_CC = "CC";
	public static final String METRICA_EC = "EC";
	public static final String METRICA_NOP = "NOP";
	public static final String METRICA_CLOC = "CLOC";

	private String metrica;
	private String designRole;
	private int limiarMinimo;
	private int limiarMedio;
	private int limiarMaximo;

	public String getMetrica() {
		return metrica;
	}

	public void setMetrica(String metrica) {
		this.metrica = metrica;
	}

	public String getDesignRole() {
		return designRole;
	}

	public void setDesignRole(String designRole) {
		this.designRole = designRole;
	}

	public int getLimiarMedio() {
		return limiarMedio;
	}

	public void setLimiarMedio(int limiarMedio) {
		this.limiarMedio = limiarMedio;
	}

	public int getLimiarMaximo() {
		return limiarMaximo;
	}

	public void setLimiarMaximo(int limiarMaximo) {
		this.limiarMaximo = limiarMaximo;
	}

	public int getLimiarMinimo() {
		return limiarMinimo;
	}

	public void setLimiarMinimo(int limiarMinimo) {
		this.limiarMinimo = limiarMinimo;
	}

	public String getKey() {
		return getMetrica() + getDesignRole();
	}

	@Override
	public boolean equals(Object obj) {
		LimiarMetrica limiarMetrica = (LimiarMetrica) obj;
		return getDesignRole().equals(limiarMetrica.getDesignRole()) && getMetrica().equals(limiarMetrica.getMetrica());
	}

}
