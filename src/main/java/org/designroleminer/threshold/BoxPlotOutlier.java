package org.designroleminer.threshold;

import java.util.ArrayList;
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


	
	public double getLowerThresholdAdjusted() {
		double medCouple = getMedCouple();
		double lower = 0;
		if (medCouple >= 0)
			lower = getQ1() - 1.5 * Math.exp(-3.5 * medCouple) * getIQR();
		else
			lower = getQ1() - 1.5 * Math.exp(-4 * medCouple) * getIQR();
		return lower;
	}

	public double getUpperThresholdAdjusted() {
		double medCouple = getMedCouple();
		double upper = 0;
		if (medCouple >= 0)
			upper = getQ3() + 1.5 * Math.exp(4 * medCouple) * getIQR();
		else
			upper = getQ3() + 1.5 * Math.exp(3.5 * medCouple) * getIQR();
		return upper;
	}

	
	/**
	 * a robust measure of skewness for a skewed distribution .
	 * 
	 * @return
	 */
	public double getMedCouple() {
		int endList1 = 0;
		int startList2 = 0;
		double median = getMedian();

		if ((listValues.length % 2) == 0) {
			endList1 = listValues.length / 2;
			startList2 = listValues.length / 2;
		} else {
			endList1 = (listValues.length - 1) / 2;
			startList2 = (listValues.length + 1) / 2;
		}
		ArrayList<Double> listCalculated = new ArrayList<>();
		for (int i = 0; i < endList1; i++) {
			for (int j = startList2; j < listValues.length; j++) {
				double calculated = 0;
				if (listValues[j] != listValues[i])
					calculated = ((listValues[j] - median) - (median - listValues[i]))
						/ (listValues[j] - listValues[i]);
				listCalculated.add(calculated);
			}
		}

		double[] target = new double[listCalculated.size()];
		for (int i = 0; i < target.length; i++) {
			target[i] = listCalculated.get(i); // java 1.5+ style (outboxing)
		}
		Arrays.sort(target);

		int lenghtList = target.length;

		if (lenghtList == 0) {
			return 0;
		}
		if (lenghtList % 2 == 0) {
			return (target[lenghtList / 2] + target[(lenghtList / 2) - 1]) / 2;
		} else {
			return target[(lenghtList - 1) / 2];
		}
	}

}