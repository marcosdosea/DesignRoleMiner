package org.designroleminer;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetricReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, ClassMetricResult> results;
	private int numberOfClasses;
	private int systemLOC; 
	private int numberOfMethods; 
	
	public MetricReport() {
		this.results = new HashMap<String, ClassMetricResult>();
	}

	public void add(ClassMetricResult ck) {
		results.put(ck.getFile(), ck);
	}

	public ClassMetricResult get(String name) {
		return results.get(name);
	}

	public Collection<ClassMetricResult> all() {
		return results.values();
	}

	public ClassMetricResult getByClassName(String name) {
		for (ClassMetricResult ck : all()) {
			if (ck.getClassName().equals(name))
				return ck;
		}

		return null;
	}

	public int getNumberOfClasses() {
		return numberOfClasses;
	}

	public void setNumberOfClasses(int numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}

	public int getSystemLOC() {
		return systemLOC;
	}

	public void setSystemLOC(int systemLOC) {
		this.systemLOC = systemLOC;
	}

	public int getNumberOfMethods() {
		return numberOfMethods;
	}

	public void setNumberOfMethods(int numberOfMethods) {
		this.numberOfMethods = numberOfMethods;
	}
	
	
}
