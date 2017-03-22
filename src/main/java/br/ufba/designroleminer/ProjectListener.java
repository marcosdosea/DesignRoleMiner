package br.ufba.designroleminer;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ProjectListener extends ASTVisitor {

   private int numberOfMethods;
    
    public ProjectListener() {
        numberOfMethods = 0;
    }
    
    public boolean visit(MethodDeclaration node) {  	
    	numberOfMethods++;
    	return super.visit(node);
    }

	
   public int getNumberOfMethods() {
		return numberOfMethods;
	}

	public void setNumberOfMethods(int numberOfMethods) {
		this.numberOfMethods = numberOfMethods;
	}
      
}
