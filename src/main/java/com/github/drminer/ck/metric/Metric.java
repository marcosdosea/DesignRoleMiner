package com.github.drminer.ck.metric;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.drminer.ck.CKNumber;
import com.github.drminer.ck.CKReport;

public interface Metric {

	void execute(CompilationUnit cu, CKNumber result, CKReport report);
	void setResult(CKNumber result);
}
