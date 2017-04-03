package br.ufba.designroleminer;

import java.util.concurrent.Callable;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;
import com.github.mauricioaniche.ck.metric.DesignRole;
import com.github.mauricioaniche.ck.metric.Metric;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;

public class ClassVisitorDR implements CommitVisitor {

	public ClassVisitorDR() {
		super();
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CK ck = new CK();
			ck.plug(new Callable<Metric>() {

				@Override
				public Metric call() throws Exception {
					// TODO Auto-generated method stub
					return new DesignRole();
				}
			});

			CKReport report = ck.calculate(repo.getPath());

			writer.write("Class", "Design Role", "SuperClass", "interfaces");

			// desconsiderarConcerns(commit, writer, report, 1);

			for (CKNumber classMetrics : report.all()) {
				String superClass = classMetrics.getSuperClassNameLevel1();
				writer.write(classMetrics.getClassName(), "\"" + classMetrics.getDesignRole() + "\"",
						"\"" + superClass + "\"", "\"" + classMetrics.getInterfaces()+ "\"");
			}

		} finally {
			repo.getScm().reset();
		}
	}

	@Override
	public String name() {
		return "dr";
	}

}
