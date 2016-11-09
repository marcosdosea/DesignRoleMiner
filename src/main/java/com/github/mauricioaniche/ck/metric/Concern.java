package com.github.mauricioaniche.ck.metric;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

public class Concern extends ASTVisitor implements Metric {

	String concern = "util";
	int dit = 1;
	static final Hashtable<String, String> defaultConcerns = new Hashtable<String, String>();
	Set<String> listAnnotations = new HashSet<String>();

	static {
		// general
		defaultConcerns.put("test", "test");
		defaultConcerns.put("exception", "exception");
		defaultConcerns.put("repository", "persistence");
		defaultConcerns.put("dao", "persistence");
		defaultConcerns.put("storage", "persistence");
		defaultConcerns.put("thread", "asynctask");
		defaultConcerns.put("throwable", "asynctask");

		// web
		defaultConcerns.put("entity", "entity");
		defaultConcerns.put("pojo", "entity");
		defaultConcerns.put("controller", "controller");
		defaultConcerns.put("model", "model");
		defaultConcerns.put("service", "service");
		
		// android
		defaultConcerns.put("actionbar", "actionbar");
		defaultConcerns.put("dialog", "dialog");
		defaultConcerns.put("presentation", "dialog");
		defaultConcerns.put("Instrumentation", "instrumentation");
		defaultConcerns.put("notification", "notification");
		defaultConcerns.put("content", "persistence");
		defaultConcerns.put("AsyncTaskLoader", "persistence");
		defaultConcerns.put("asynctask", "asynctask");
		defaultConcerns.put("activity", "activity");
		defaultConcerns.put("fragment", "fragment");
		defaultConcerns.put("view", "view");
		defaultConcerns.put("listener", "view");

		// eclipse
		defaultConcerns.put("wizard", "view");
		defaultConcerns.put("preferencepage", "view");
		
		
	}

	public boolean visit(MarkerAnnotation node) {
		ITypeBinding binding = node.resolveTypeBinding();

		if (binding != null)
			calculateAnnotation(binding, 1);
		return super.visit(node);
	}

	public boolean visit(NormalAnnotation node) {
		ITypeBinding binding = node.resolveTypeBinding();

		if (binding != null)
			calculateAnnotation(binding, 1);
		return super.visit(node);
	}

	public boolean visit(SingleMemberAnnotation node) {
		ITypeBinding binding = node.resolveTypeBinding();

		if (binding != null)
			calculateAnnotation(binding, 1);
		return super.visit(node);
	}

	private void calculateAnnotation(ITypeBinding binding, int ditAnnotation) {
		ITypeBinding father = binding.getSuperclass();

		if (father != null) {
			String fatherName = father.getName();
			if ((ditAnnotation == 1) && fatherName.endsWith("Object")) {
				listAnnotations.add(binding.getName());
			} else if ((dit >= 2) && fatherName.endsWith("Object")) {
				listAnnotations.add(binding.getBinaryName());
			}

			if (!fatherName.endsWith("Object")) {
				calculateAnnotation(father, ++ditAnnotation);
			}
		}
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding binding = node.resolveBinding();

		if (binding.getQualifiedName().contains("KotlinDiagnosticsTestCase"))
			System.out.println("aqui");

		if (binding.isMember())
			return false;

		String interfacesConcern = node.superInterfaceTypes().size() > 0 ? node.superInterfaceTypes().toString() : "";

		if (binding != null)
			calculate(binding, interfacesConcern);

		return super.visit(node);
	}

	private void calculate(ITypeBinding binding, String interfacesConcern) {
		ITypeBinding father = binding.getSuperclass();

		if (father != null) {
			String fatherName = father.getBinaryName();
			if (father.isFromSource()) { // classe pai é interna
				concern = father.getName();
				dit++;
				calculate(father, interfacesConcern);
			} else {
				String defaultConcern = "";
				if ((dit == 1) && fatherName.endsWith("Object") && (interfacesConcern.length() == 0))
					concern = "util";
				else if ((dit == 1) && fatherName.endsWith("Object") && (interfacesConcern.length() > 0)) {
					defaultConcern = findDefaultConcern(interfacesConcern);
					concern = defaultConcern.isEmpty() ? concern : defaultConcern;
				} else if (fatherName.endsWith("Object")) {
					defaultConcern = findDefaultConcern(binding.getQualifiedName());
					concern = defaultConcern.isEmpty() ? binding.getName() : defaultConcern;
				} else {
					defaultConcern = findDefaultConcern(father.getQualifiedName());
					concern = defaultConcern.isEmpty() ? father.getName() : defaultConcern;
				}

			}
		}

	}

	@Override
		public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
			cu.accept(this);
		}

	@Override
	public void setResult(CKNumber result) {
		// Avalia atribuir um concern default pelos tokens das anotações da
		for (String annotation : listAnnotations) {
			for (String tokenConcern : defaultConcerns.keySet()) {
				if (annotation.toLowerCase().contains(tokenConcern.toLowerCase())) {
					concern = defaultConcerns.get(tokenConcern);
				}
			}
		}
		if (concern.indexOf("<") >= 0) {
			concern = concern.substring(0, concern.indexOf("<")); // retirar
																	// informação
																	// dos tipos
																	// parametrizados.
			if (concern.indexOf("[") >= 0)
				concern += "]";
		}
		result.setConcern(concern);
	}

	private String findDefaultConcern(String concern) {
		// Avalia atribuir um concern default pelos token da superclasse ou interface
		for (String tokenConcern : defaultConcerns.keySet()) {
			if (concern.toLowerCase().contains(tokenConcern.toLowerCase())) {
				return defaultConcerns.get(tokenConcern);
			}
		}
		return "";
	}

}
