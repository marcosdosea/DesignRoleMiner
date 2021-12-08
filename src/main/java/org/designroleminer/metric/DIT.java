package org.designroleminer.metric;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MetricReport;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.github.mauricioaniche.ck.metric.Metric;

public class DIT extends ASTVisitor implements Metric {

	int dit = 1; // Object is the father of everyone!
	String superClassLevel1 = "Object";
	String superClassLevel2 = "";
	String superClassLevel3 = "";

	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding binding = node.resolveBinding();

		if (binding == null)
			return false;
		if (binding.isMember())
			return false;

		if (binding != null)
			calculate(binding);

		return super.visit(node);
	}

	private void calculate(ITypeBinding binding) {
		ITypeBinding father = binding.getSuperclass();

		if ((father != null) && (!father.isMember())) {

			String fatherName = father.getName();

			if (!fatherName.endsWith("Object")) {
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

	public void execute(CompilationUnit cu, ClassMetricResult number, MetricReport report) {
		cu.accept(this);
	}

	public void setResult(ClassMetricResult result) {
		result.setDit(dit);
		result.setSuperClassNameLevel1(superClassLevel1);
		result.setSuperClassNameLevel2(superClassLevel2);
		result.setSuperClassNameLevel3(superClassLevel3);
	}
}
