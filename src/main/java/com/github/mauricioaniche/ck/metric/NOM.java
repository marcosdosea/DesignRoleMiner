package com.github.mauricioaniche.ck.metric;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MetricReport;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class NOM extends ASTVisitor implements Metric {

	private int methods;

	@Override
	public boolean visit(MethodDeclaration node) {
		methods++;

		return false;
	}

	public void execute(CompilationUnit cu, ClassMetricResult number, MetricReport report) {
		cu.accept(this);
	}

	public void setResult(ClassMetricResult result) {
		result.setNom(methods);
	}
}
