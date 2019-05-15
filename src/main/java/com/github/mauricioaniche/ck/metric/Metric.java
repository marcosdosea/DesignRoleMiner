package com.github.mauricioaniche.ck.metric;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MetricReport;
import org.eclipse.jdt.core.dom.CompilationUnit;

public interface Metric {

	void execute(CompilationUnit cu, ClassMetricResult result, MetricReport report);
	void setResult(ClassMetricResult result);
}
