package com.github.ck;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CKReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, CKNumber> results;

	public CKReport() {
		this.results = new HashMap<String, CKNumber>();
	}

	public void add(CKNumber ck) {
		results.put(ck.getFile(), ck);
	}

	public CKNumber get(String name) {
		return results.get(name);
	}

	public Collection<CKNumber> all() {
		return results.values();
	}

	public CKNumber getByClassName(String name) {
		for (CKNumber ck : all()) {
			if (ck.getClassName().equals(name))
				return ck;
		}

		return null;
	}
}
