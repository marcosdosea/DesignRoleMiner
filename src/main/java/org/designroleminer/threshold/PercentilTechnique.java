package org.designroleminer.threshold;

import java.util.ArrayList;
import java.util.Collection;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class PercentilTechnique extends AbstractTechnique {

	private int percentile;

	public PercentilTechnique(int percentile) {
		this.percentile = percentile;
	}

	/**
	 * Generate thresholds do LOC, CC, Efferent and NOP metrics using quartil of
	 * distribution of values
	 * 
	 * @param classes
	 * @param fileResultado
	 * @param quartil
	 */
	@Override
	public void generate(Collection<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("DesignRole;LOC;CC;Efferent;NOP;");

		ArrayList<Integer> listaLOC = new ArrayList<>();
		ArrayList<Integer> listaCC = new ArrayList<>();
		ArrayList<Integer> listaEfferent = new ArrayList<>();
		ArrayList<Integer> listaNOP = new ArrayList<>();

		for (ClassMetricResult classe : classes) {
			for (MethodMetricResult method : classe.getMetricsByMethod().values()) {
				listaLOC.add(method.getLinesOfCode());
				listaCC.add(method.getComplexity());
				listaEfferent.add(method.getEfferentCoupling());
				listaNOP.add(method.getNumberOfParameters());
			}
		}

		double limiarLOC = new BoxPlotOutlier(convert(listaLOC)).getValuePercentil(percentile);
		double limiarCC = new BoxPlotOutlier(convert(listaCC)).getValuePercentil(percentile);
		double limiarEfferent = new BoxPlotOutlier(convert(listaEfferent)).getValuePercentil(percentile);
		double limiarNOP = new BoxPlotOutlier(convert(listaNOP)).getValuePercentil(percentile);

		pm.write(LimiarMetrica.DESIGN_ROLE_UNDEFINED + ";" + limiarLOC + ";" + limiarCC + ";" + limiarEfferent + ";"
				+ limiarNOP + ";");
	}

	private double[] convert(ArrayList<Integer> myIntegerValues) {
		double[] myList = new double[myIntegerValues.size()];
		for (int i = 0; i < myIntegerValues.size(); i++) {
			myList[i] = (double) myIntegerValues.get(i);
		}
		return myList;
	}

}
