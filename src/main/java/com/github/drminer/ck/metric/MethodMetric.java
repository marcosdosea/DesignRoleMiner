package com.github.drminer.ck.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.github.drminer.ck.CKNumber;
import com.github.drminer.ck.CKReport;
import com.github.drminer.ck.MethodData;
import com.github.drminer.ck.MethodMetrics;
import com.github.drminer.ck.Utils;



public class MethodMetric extends ASTVisitor implements Metric {

	private Map<MethodData, MethodMetrics> metricsByMethod;
    private Stack<MethodData> methodStack;
    private String currentPackage;
    private List<String> currentInterfaces;
    private String currentMethod;
    private List<SingleVariableDeclaration> currentParameters;
    private Map<String, String> declaredTypes;
    private Set<String> usedTypes;
    private int currentInitialChar;
    private CompilationUnit cu;
    
    public MethodMetric() {
        metricsByMethod = new HashMap<MethodData, MethodMetrics>();
        methodStack = new Stack<MethodData>();
        usedTypes = new HashSet<String>();
        declaredTypes = new HashMap<>();
    }
    
    public boolean visit(PackageDeclaration node) {
    	currentPackage = node.getName().getFullyQualifiedName();
    	return super.visit(node);
    }
    
    public boolean visit(TypeDeclaration node) {
    	if (node.isInterface() || (node.superInterfaceTypes() == null)) {
    		return false;
    	}
    	currentInterfaces = node.superInterfaceTypes();
    	List<BodyDeclaration> declarations =  node.bodyDeclarations();
    	for(BodyDeclaration declaration : declarations) {
    		if (declaration instanceof FieldDeclaration) {
    			FieldDeclaration fields = (FieldDeclaration) declaration;
    			List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) fields.fragments();
    			for(VariableDeclarationFragment frag: fragments) {
    				declaredTypes.put(frag.getName().getIdentifier(), fields.getType().toString());
    			}
    		}
    	}
    	return super.visit(node);
    }

    public boolean visit(MethodDeclaration node) {  	
    	currentMethod = node.getName().getIdentifier();
    	currentInitialChar = node.getStartPosition();
    	if (currentMethod.startsWith("addJobDirectory")) {
    		System.out.println("addjobdirectory");
    	}
    	
    	node.resolveBinding();

    	currentParameters = node.parameters();
    	for (SingleVariableDeclaration parameter: currentParameters) {
    		declaredTypes.put(parameter.getName().getIdentifier(), parameter.getType().toString());
    	}
    	if (!node.isConstructor() &&  (node.getReturnType2() != null) &&!node.getReturnType2().toString().equals("void")) {
    		usedTypes.add(node.getReturnType2().toString());
    	}
    	
    	MethodData methodData = getMethodData();

    	methodData.setInitialLine(cu.getLineNumber(node.getName().getStartPosition()));
    	methodData.setInitialChar(node.getStartPosition());
    	methodData.setFinalChar(node.getLength()+node.getStartPosition());
    	if (!metricsByMethod.containsKey(methodData))
    		metricsByMethod.put(methodData, new MethodMetrics());
    	
    	MethodMetrics methodMetrics =  metricsByMethod.get(methodData);
    	methodMetrics.setLinesOfCode(Utils.countLineNumbers(node.getBody().toString()));
    	methodMetrics.setNumberOfParameters(node.parameters().size());
    	methodStack.push(getMethodData());
    	increaseCc();
    	return super.visit(node);
    }
    
    public boolean visit(ClassInstanceCreation node) {
   		String typeDeclared =  node.getType().toString();
		usedTypes.add(typeDeclared);
   		return super.visit(node);
    }
    
    public boolean visit(SingleVariableDeclaration node) {
   		String typeDeclared =  node.getType().toString();
   		usedTypes.add(typeDeclared);
   		return super.visit(node);
    }
    
    public boolean visit(VariableDeclarationStatement node) {
   		String typeDeclared =  node.getType().toString();
   		usedTypes.add(typeDeclared);
   		return super.visit(node);
    }

    
    public boolean visit(FieldDeclaration node) {
   		String typeDeclared =  node.getType().toString();
   		
   		List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) node.fragments();
   		
   		for (VariableDeclarationFragment fragment : fragments ) {
   			declaredTypes.put(fragment.getName().getIdentifier(), typeDeclared);
   		}
   		return super.visit(node);
    }

    public boolean visit(FieldAccess node) {
   		//String identifier = node.getName().getIdentifier();
    	//String type = declaredTypes.get(identifier);
   		String type = node.getExpression().resolveTypeBinding().getName();	 
   	   	if (type != null) {
   			usedTypes.add(type);
   		} else {
   			System.out.println("problema em encontrar o tip");
   		}
   		return super.visit(node);
    }

    public boolean visit(MethodInvocation node) {
   		List<Expression> arguments = (List<Expression>) node.arguments();
    	
   		Expression exp = node.getExpression();
   		if (exp != null) {
   			String type = declaredTypes.get(exp.toString());
   			
		   	if (type != null) {
		   		usedTypes.add(type);
		   	} else if (!exp.toString().contains("(") &&  Character.isUpperCase(exp.toString().charAt(0)))	{
	   			// chamada estática de método
		   		usedTypes.add(exp.toString());
		   	}
   		}
   		
   		for(Expression argument: arguments) {
   			if (argument instanceof SimpleName) {
   				String identifier = node.getName().getIdentifier();
   	   	   		
   		    	String type = declaredTypes.get(identifier);
   		   		if (type != null) {
   		   			usedTypes.add(type);
   		   		}
   			}
   		}
   		return super.visit(node);
    }

    
	public void endVisit(MethodDeclaration node) {
    	MethodMetrics methodMetrics =  metricsByMethod.get(getMethodData());
    	methodMetrics.setEfferentCoupling(usedTypes.size());
    	usedTypes.clear();
    	methodStack.pop();
    }

    @Override
    public boolean visit(ForStatement node) {
    	increaseCc();
    	
    	return super.visit(node);
    }

    @Override
    public boolean visit(EnhancedForStatement node) {
    	increaseCc();
    	return super.visit(node);
    }
    
    @Override
    public boolean visit(ConditionalExpression node) {
    	increaseCc();
    	return super.visit(node);
    }
    
    @Override
    public boolean visit(DoStatement node) {
    	increaseCc();
    	return super.visit(node);
    }
    
    @Override
    public boolean visit(WhileStatement node) {
    	increaseCc();
    	return super.visit(node);
    }
    
    @Override
    public boolean visit(SwitchCase node) {
    	if(!node.isDefault())
    		increaseCc();
    	return super.visit(node);
    }
    
    @Override
    public boolean visit(Initializer node) {
    	currentMethod = "(static_block)";
    
    	methodStack.push(getMethodData());
    	increaseCc();
    	return super.visit(node);
    }

    @Override
    public void endVisit(Initializer node) {
    	methodStack.pop();
    }

    @Override
    public boolean visit(CatchClause node) {
    	increaseCc();
    	String typeDeclared =  node.getException().getType().toString();
    	usedTypes.add(typeDeclared);
    	return super.visit(node);
    }
    
    public boolean visit(IfStatement node) {
    	
		String expr = node.getExpression().toString().replace("&&", "&").replace("||", "|");
		int ands = StringUtils.countMatches(expr, "&");
		int ors = StringUtils.countMatches(expr, "|");
		
		increaseCc(ands + ors);
    	increaseCc();
    	
    	return super.visit(node);
    }
    
    private void increaseCc() {
    	increaseCc(1);
    }

    private void increaseCc(int qtd) {
    	// i dont know the method... ignore!
    	if(methodStack.isEmpty()) return;
    	MethodData methodData = getMethodData();
    	
    	if (!metricsByMethod.containsKey(methodData))
    		metricsByMethod.put(methodData, new MethodMetrics());
    	
    	MethodMetrics methodMetrics =  metricsByMethod.get(methodData);
    	methodMetrics.setComplexity(methodMetrics.getComplexity() + qtd);
    }
    
    public Map<MethodData, MethodMetrics> getMetricsByMethod() {
        return metricsByMethod;
    }
    
    private MethodData getMethodData() {
		MethodData methodData = new MethodData();
    	methodData.setNomeMethod(currentMethod);
    	methodData.setParameters(currentParameters);
    	methodData.setInitialChar(currentInitialChar);
		return methodData;
	}

	@Override
	public void execute(CompilationUnit cu, CKNumber result, CKReport report) {
		this.cu = cu;
		cu.accept(this);
	}

	@Override
	public void setResult(CKNumber result) {
		result.setMetricsByMethod(metricsByMethod);
		result.setPackageName(currentPackage);
		result.setInterfaces(currentInterfaces);
	}
    
    
    
}
