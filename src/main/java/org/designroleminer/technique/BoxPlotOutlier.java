package org.designroleminer.technique;

import java.util.Arrays;

public class BoxPlotOutlier {

	private double[] listValues;

	public BoxPlotOutlier(double[] listValues) {
		this.listValues = listValues;
		Arrays.sort(listValues);
	}

	public double[] getListValues() {
		return listValues;
	}

	public double getMedian() {
		int lenghtList = listValues.length;

		if (lenghtList == 0) {
			return 0;
		}
		if (lenghtList % 2 == 0) {
			return (listValues[lenghtList / 2] + listValues[(lenghtList / 2) - 1]) / 2;
		} else {
			return listValues[(lenghtList - 1) / 2];
		}

	}

	public double getValuePercentil(int percentil) {
		if (listValues.length == 0) {
			return 0;
		}
		double posicaoReal = ((double) percentil / 100) * (listValues.length);
		int posicaoInteira = (int) posicaoReal;
		if (posicaoReal != posicaoInteira) {
			return listValues[posicaoInteira];
		} else {
			return (listValues[posicaoInteira] + listValues[posicaoInteira - 1]) / 2;
		}
	}
	
	public double getQ1() {
		return getValuePercentil(25);
	}
	
	public double getQ3() {
		return getValuePercentil(75);
	}
	
	public double getIQR() {
		return getQ3() - getQ1();
	}
	
	public double getLowerThresholdTurkey() {
		return getQ1() - 1.5 * getIQR();
	}
	
	public double getUpperThresholdTurkey() {
		return getQ3() + 1.5 * getIQR();
	}
}