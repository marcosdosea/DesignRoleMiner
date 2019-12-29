package org.designroleminer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mauricioaniche.ck.MethodData;

public class ClassMetricResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String file;
	private String className;
	private String type;
	private String packageName;
	private String superClassNameLevel1;
	private String superClassNameLevel2;
	private String superClassNameLevel3;
	private List<String> interfaces;
	private String designRole;
	private boolean ehArchitecturalRole;
	
	private int dit;
	private int noc;
	private int wmc;
	private int cbo;
	private int lcom;
	private int rfc;
	private int nom;
	private int cloc;

	private Map<String, Integer> specific;
	private Map<MethodData, MethodMetricResult> metricsByMethod;

	public ClassMetricResult(String file, String className, String type) {
		this.file = file;
		this.className = className;
		this.type = type;

		this.specific = new HashMap<String, Integer>();
	}

	public String getFile() {
		return file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassMetricResult other = (ClassMetricResult) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	public int getDit() {
		return dit;
	}

	public void setDit(int dit) {
		this.dit = dit;
	}
	
	public int getCLoc() {
		return cloc;
	}

	public void setLoc(int cloc) {
		this.cloc = cloc;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<String> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
	}

	public String getSuperClassNameLevel1() {
		return superClassNameLevel1;
	}

	public void setSuperClassNameLevel1(String superClassNameLevel1) {
		this.superClassNameLevel1 = superClassNameLevel1;
	}

	public String getSuperClassNameLevel2() {
		return superClassNameLevel2;
	}

	public void setSuperClassNameLevel2(String superClassNameLevel2) {
		this.superClassNameLevel2 = superClassNameLevel2;
	}

	public String getSuperClassNameLevel3() {
		return superClassNameLevel3;
	}

	public void setSuperClassNameLevel3(String superClassNameLevel3) {
		this.superClassNameLevel3 = superClassNameLevel3;
	}
	public void incNoc() {
		this.noc++;
	}

	public int getNoc() {
		return noc;
	}

	public void setNoc(int noc) {
		this.noc = noc;
	}
	
	public void setWmc(int cc) {
		this.wmc = cc;
	}

	public int getWmc() {
		return wmc;
	}

	public int getCbo() {
		return cbo;
	}

	public void setCbo(int cbo) {
		this.cbo = cbo;
	}

	public void setLcom(int lcom) {
		this.lcom = lcom;
	}

	public int getLcom() {
		return lcom;
	}

	public void setRfc(int rfc) {
		this.rfc = rfc;
	}

	public int getRfc() {
		return rfc;
	}

	public void setNom(int nom) {
		this.nom = nom;
	}

	public int getNom() {
		return nom;
	}

	public int getSpecific(String key) {
		if (!specific.containsKey(key))
			return -1;
		return specific.get(key);
	}

	public void addSpecific(String key, int value) {
		specific.put(key, value);
	}

	public String getType() {
		return type;
	}
	
	public String getDesignRole() {
		if (designRole != null)
			return designRole.toUpperCase();
		return designRole;
	}

	public void setDesignRole(String designRole) {
		this.designRole = designRole;
	}


	public Map<MethodData, MethodMetricResult> getMetricsByMethod() {
		return metricsByMethod;
	}

	public void setMetricsByMethod(Map<MethodData, MethodMetricResult> metricsByMethod) {
		this.metricsByMethod = metricsByMethod;
	}

	
	public boolean isArchitecturalRole() {
		return ehArchitecturalRole;
	}

	public void setArchitecturalRole(boolean ehArchitecturalRole) {
		this.ehArchitecturalRole = ehArchitecturalRole;
	}
	@Override
	public String toString() {
		return "ClassMetricResult [file=" + file + ", className=" + className + ", designRole=" + designRole + ", type=" + type + ", dit=" + dit + ", noc="
				+ noc + ", wmc=" + wmc + ", cbo=" + cbo + ", lcom=" + lcom + ", rfc=" + rfc + ", nom=" + nom
				+ ", specific=" + specific + "]";
	}
}
