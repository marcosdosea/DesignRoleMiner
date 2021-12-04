package com.github.mauricioaniche.ck.metric;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MetricReport;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class NOC extends ASTVisitor implements Metric {

	private MetricReport report;

	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding binding = node.resolveBinding();
		ITypeBinding father = binding.getSuperclass();
		if(father!=null) {
			ClassMetricResult fatherCk = report.getByClassName(father.getBinaryName());
			if(fatherCk!=null) fatherCk.incNoc();
		}

		return false;
	}

	public void execute(CompilationUnit cu, ClassMetricResult number, MetricReport report) {
		this.report = report;
		cu.accept(this);
	}

	public void setResult(ClassMetricResult result) {
	}
}
