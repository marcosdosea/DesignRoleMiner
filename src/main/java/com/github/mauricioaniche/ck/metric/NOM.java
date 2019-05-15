package com.github.mauricioaniche.ck.metric;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.github.drminer.ClassMetricResult;
import com.github.drminer.MetricReport;

public class NOM extends ASTVisitor implements Metric {

	private int methods;

	@Override
	public boolean visit(MethodDeclaration node) {
		methods++;

		return false;
	}

	@Override
	public void execute(CompilationUnit cu, ClassMetricResult number, MetricReport report) {
		cu.accept(this);
	}

	@Override
	public void setResult(ClassMetricResult result) {
		result.setNom(methods);
	}
}
