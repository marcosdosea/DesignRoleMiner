package com.github.ck.metric;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.ck.CKNumber;
import com.github.ck.CKReport;

public interface Metric {

	void execute(CompilationUnit cu, CKNumber result, CKReport report);
	void setResult(CKNumber result);
}
