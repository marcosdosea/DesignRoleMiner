package com.github.mauricioaniche.ck.metric;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.drminer.ClassMetricResult;
import com.github.drminer.MetricReport;

public interface Metric {

	void execute(CompilationUnit cu, ClassMetricResult result, MetricReport report);
	void setResult(ClassMetricResult result);
}
