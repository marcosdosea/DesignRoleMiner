package com.github.mauricioaniche.ck.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

public class DesignRole extends ASTVisitor implements Metric {

	String designRole;
	int dit = 1;
	static final HashMap<String, String> defaultDesignRoles = new LinkedHashMap<String, String>();
	Set<String> listAnnotations = new HashSet<String>();

	static {
		// general
		defaultDesignRoles.put("test", "Test");
		defaultDesignRoles.put("exception", "Exception");
		defaultDesignRoles.put("thread", "AsyncTask");
		defaultDesignRoles.put("throwable", "AsyncTask");
		defaultDesignRoles.put("runnable", "AsyncTask");

		defaultDesignRoles.put("repository", "Persistence");
		defaultDesignRoles.put("dao", "Persistence");
		defaultDesignRoles.put("storage", "Persistence");
		defaultDesignRoles.put("store", "Persistence");
		defaultDesignRoles.put("datasource", "Persistence");
		defaultDesignRoles.put("database", "Persistence");
		defaultDesignRoles.put("SQL", "Persistence");
		defaultDesignRoles.put("stream", "Persistence");
		defaultDesignRoles.put("serializable", "Entity");
		defaultDesignRoles.put("comparable", "Entity");
		defaultDesignRoles.put("parcelable", "Entity");
		defaultDesignRoles.put("cloneable", "Entity");
		
		// web
		defaultDesignRoles.put("entity", "Entity");
		defaultDesignRoles.put("pojo", "Entity");
		defaultDesignRoles.put("bean", "Entity");
		defaultDesignRoles.put("controller", "Controller");
		defaultDesignRoles.put("model", "Model");
		defaultDesignRoles.put("service", "Service");

		// android
		defaultDesignRoles.put("adapter", "Adapter");
		defaultDesignRoles.put("actionbar", "Actionbar");
		defaultDesignRoles.put("DialogFragment", "Fragment");
		defaultDesignRoles.put("dialog", "Dialog");
		defaultDesignRoles.put("presentation", "Dialog");
		defaultDesignRoles.put("content", "Persistence");
		defaultDesignRoles.put("AsyncTaskLoader", "Persistence");

		defaultDesignRoles.put("asynctask", "AsyncTask");
		defaultDesignRoles.put("activity", "Activity");
		defaultDesignRoles.put("fragment", "Fragment");
		defaultDesignRoles.put("view", "View");
		defaultDesignRoles.put("listener", "View");
		defaultDesignRoles.put("layout", "View");
		defaultDesignRoles.put("widget", "Widget");
		defaultDesignRoles.put("notification", "Notification");
		// eclipse
		defaultDesignRoles.put("wizard", "View");
		defaultDesignRoles.put("page", "View");
		defaultDesignRoles.put("action", "Action");
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
		if (binding.isMember())
			return false;

		String interfacesConcern = node.superInterfaceTypes().size() > 0 ? node.superInterfaceTypes().toString() : "";

		if (binding != null)
			calculate(binding, interfacesConcern);

		return super.visit(node);
	}

	private void calculate(ITypeBinding binding, String interfaces) {
		ITypeBinding father = binding.getSuperclass();
		
		if (binding.getName().equals("BPMNEndNodeFigure")){
			String name = binding.getName();
			String bname = binding.getBinaryName();
			String qname = binding.getQualifiedName();
		}
		if (father != null) {
			String fatherName = father.getBinaryName();
			if (father.isFromSource()) { // classe pai é interna
				designRole = father.getName();
				dit++;
				calculate(father, interfaces);
			} else {
				String defaultDesignRole = "";
				if ((dit == 1) && fatherName.endsWith("Object") && (interfaces.length() == 0)) {
					if (ehEntityDesignRole(binding))
						designRole = "Entity";
					designRole = "Undefined";
				} else if ((dit == 1) && fatherName.endsWith("Object") && (interfaces.length() > 0)) {
					defaultDesignRole = findDefaultDesignRole(interfaces);
					designRole = defaultDesignRole.isEmpty() ? interfaces : defaultDesignRole;
				} else if (fatherName.endsWith("Object")) {
					defaultDesignRole = findDefaultDesignRole(extractParameters(binding.getName()));
					if (defaultDesignRole.isEmpty())
						defaultDesignRole = findDefaultDesignRole(binding.getBinaryName());
					designRole = defaultDesignRole.isEmpty() ? binding.getName() : defaultDesignRole;
				} else {
					defaultDesignRole = findDefaultDesignRole(father.getBinaryName());
					if (defaultDesignRole.isEmpty())
						defaultDesignRole = findDefaultDesignRole(father.getName());
					designRole = defaultDesignRole.isEmpty() ? father.getName() : defaultDesignRole;
				}
			}
		}
	}

	private boolean ehEntityDesignRole(ITypeBinding binding) {
		IVariableBinding[] listDeclaredFields =  binding.getDeclaredFields();
		boolean hasNonStaticFinalFields = false;
		int countGetSet = 0;
		if (listDeclaredFields.length == 0) { 
			return false;
		} else {
			for (IVariableBinding field: listDeclaredFields) {
				String declaracaoField = field.toString().toLowerCase();
				if (!declaracaoField.contains(" final ") && !declaracaoField.contains(" static ")) {
					hasNonStaticFinalFields = true;
					break;
				}
			}
		}
		IMethodBinding[] listDeclaredMethods = binding.getDeclaredMethods();
		for (IMethodBinding method : listDeclaredMethods) {
			if (!method.isConstructor() && !method.getName().toLowerCase().startsWith("get") && 
					!method.getName().toLowerCase().startsWith("set") && !method.getName().toLowerCase().startsWith("to"))
				return false;
			else if (method.getName().toLowerCase().startsWith("get") || method.getName().toLowerCase().startsWith("set"))
				countGetSet++;
		}
		return countGetSet > 0 && hasNonStaticFinalFields;
	}

	@Override
	public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
		cu.accept(this);
	}

	@Override
	public void setResult(CKNumber result) {
		// Avalia atribuir um concern default pelos tokens das anotações da
		for (String annotation : listAnnotations) {
			for (String tokenConcern : defaultDesignRoles.keySet()) {
				if (annotation.toLowerCase().equals(tokenConcern.toLowerCase())) {
					designRole = defaultDesignRoles.get(tokenConcern);
				}
			}
		}
		designRole = extractParameters(designRole);
		result.setConcern(designRole);
	}

	private String extractParameters(String className) {
		if (className.indexOf("<") >= 0) {
			className = className.substring(0, className.indexOf("<")); // retirar
			// informação
			// dos tipos
			// parametrizados.
			if (className.indexOf("[") >= 0)
				className += "]";
		}
		return className;
	}

	/**
	 * Avalia atribuir um design role default pelos tokens da superclasse ou
	 * interface
	 */
	private String findDefaultDesignRole(String designRole) {
		Set<String> tokenSet = defaultDesignRoles.keySet();

		for (String tokenDesignRole : defaultDesignRoles.keySet()) {
			if (designRole.toLowerCase().contains(tokenDesignRole.toLowerCase())) {
				return defaultDesignRoles.get(tokenDesignRole);
			}
		}
		return "";
	}

}
