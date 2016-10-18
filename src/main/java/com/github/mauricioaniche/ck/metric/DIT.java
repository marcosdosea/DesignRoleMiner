package com.github.mauricioaniche.ck.metric;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

public class DIT extends ASTVisitor implements Metric {

	int dit = 1; // Object is the father of everyone!
	String superClassLevel1 = "Object";
	String superClassLevel2 = "";
	String superClassLevel3 = "";

	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding binding = node.resolveBinding();
		
		if (binding.getQualifiedName().contains("XOAuthConsumer"))
			System.out.println("aqui");
		
		if (binding.isMember())
			return false;
		
		if (binding != null)
			calculate(binding);

		return super.visit(node);
	}

	private void calculate(ITypeBinding binding) {
		ITypeBinding father = binding.getSuperclass();

		if (father != null) {
			String fatherName = father.getName();
			if (dit == 1) {
				superClassLevel1 = fatherName;
			} else if (dit == 2) {
				superClassLevel2 = superClassLevel1;
				superClassLevel1 = fatherName;
			} else if (dit >= 3) {
				superClassLevel3 = superClassLevel2;
				superClassLevel2 = superClassLevel1;
				superClassLevel1 = fatherName;
			}

			if (father.isFromSource()) { // classe pai é interna
				dit++;
				calculate(father);
			} else {
				if (!fatherName.endsWith("Object")) {
					dit++;
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
		result.setDit(dit);
		result.setSuperClassNameLevel1(superClassLevel1);
		result.setSuperClassNameLevel2(superClassLevel2);
		result.setSuperClassNameLevel3(superClassLevel3);
	}
}
