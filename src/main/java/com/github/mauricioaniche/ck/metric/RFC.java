package com.github.mauricioaniche.ck.metric;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.github.drminer.ClassMetricResult;
import com.github.drminer.MetricReport;

public class RFC extends ASTVisitor implements Metric {

	private HashSet<String> methodInvocations = new HashSet<String>();

	public boolean visit(MethodInvocation node) {
		IMethodBinding binding = node.resolveMethodBinding();
		if(binding!=null) {
			String method = getMethodName(binding);
			methodInvocations.add(method);
		} else {
			methodInvocations.add(node.getName()  + "/" + node.arguments().size());
			
		}
		
		return super.visit(node);
	}

	private String getMethodName(IMethodBinding binding) {
		
		String argumentList = "";
		ITypeBinding[] args = binding.getParameterTypes();
		for(ITypeBinding arg : args) {
			argumentList += arg.getName();
		}
		String method = binding.getDeclaringClass().getQualifiedName() + "." + binding.getName() + "/" + binding.getTypeArguments().length + "[" + argumentList + "]";
		return method;
	}
	
	@Override
	public void execute(CompilationUnit cu, ClassMetricResult number, MetricReport report) {
		cu.accept(this);
	}

	@Override
	public void setResult(ClassMetricResult result) {
		result.setRfc(methodInvocations.size());
		
	}
	
}
