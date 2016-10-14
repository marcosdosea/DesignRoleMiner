package br.ufba.concernminer;

import java.util.Map;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;
import com.github.mauricioaniche.ck.MethodData;
import com.github.mauricioaniche.ck.MethodMetrics;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;

public class MethodVisitorCK implements CommitVisitor{

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CKReport report = new CK().calculate(repo.getPath());
			
			writer.write("Commit", "Class","Concern", "SuperClass","interfaces", "DIT", "NOM", "Method", "LOC", "CC", "Efferent", "NOP" );
			
			for(CKNumber classMetrics: report.all()) {
				
				Map<MethodData, MethodMetrics> metricsByMethod = classMetrics.getMetricsByMethod();
				
				String superClass = classMetrics.getSuperClassNameLevel1();
				
				for(MethodData metodo: metricsByMethod.keySet()) {
					MethodMetrics methodMetrics = metricsByMethod.get(metodo);
					writer.write(commit.getHash(), classMetrics.getClassName(), "\"" + classMetrics.getConcern() + "\"", "\"" + superClass +"\"", 
							"\""+classMetrics.getInterfaces() + "\"",  classMetrics.getDit(), classMetrics.getNom(), 
							metodo.getNomeMethod(), methodMetrics.getLinesOfCode(), methodMetrics.getComplexity(), 
							methodMetrics.getEfferentCoupling(), methodMetrics.getNumberOfParameters() );
				}
			}
		} finally {
			repo.getScm().reset();
		}
	}

	@Override
	public String name() {
		return "ck";
	}

}
