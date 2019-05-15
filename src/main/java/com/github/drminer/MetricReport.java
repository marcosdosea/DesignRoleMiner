package com.github.drminer;

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
}
