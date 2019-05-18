package org.designroleminer.technique;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoxPlotOutlierTest {

	double[] values = { 2, 5, 7, 11, 15 };
	BoxPlotOutlier boxplot = new BoxPlotOutlier(values);

	@Test
	public void testGetMedian() {
		assertEquals(7, boxplot.getMedian(), 1);
	}

	@Test
	public void testGetValuePercentil() {
		assertEquals(2, boxplot.getValuePercentil(10), 1);
	}

	@Test
	public void testGetQ1() {
		assertEquals(5, boxplot.getQ1(), 1);
	}

	@Test
	public void testGetQ3() {
		assertEquals(11, boxplot.getQ3(), 1);
	}

	@Test
	public void testGetIQR() {
		assertEquals(6, boxplot.getIQR(), 1);
	}

	@Test
	public void testGetLowerThresholdTurkey() {
		assertEquals(-4, boxplot.getLowerThresholdTurkey(), 1);
	}

	@Test
	public void testGetUpperThresholdTurkey() {
		assertEquals(20, boxplot.getUpperThresholdTurkey(), 1);
	}

}
