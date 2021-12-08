package org.designroleminer.visitor;

import java.util.ArrayList;
import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MetricReport;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import com.github.mauricioaniche.ck.CK;

public class ClassVisitorMetric implements CommitVisitor {

	List<String> listaConcerns = new ArrayList<String>();

	public ClassVisitorMetric(List<String> listaConcerns) {
		this.listaConcerns = listaConcerns;
	}

	public ClassVisitorMetric() {
		super();
	}

	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CK ck = new CK();

			MetricReport report = ck.calculate(repo.getPath());

			writer.write("Commit", "Class", "Design Role", "SuperClass", "interfaces", "DIT", "NOM", "CBO", "LCOM",
					"NOC", "RFC", "WMC", "CLOC");

			// desconsiderarConcerns(commit, writer, report, 1);

			if (listaConcerns.size() == 0) {
				for (ClassMetricResult classMetrics : report.all()) {
					String superClass = classMetrics.getSuperClassNameLevel1();
					writer.write(commit.getHash(), classMetrics.getClassName(),
							"\"" + classMetrics.getDesignRole() + "\"", "\"" + superClass + "\"",
							"\"" + classMetrics.getInterfaces() + "\"", classMetrics.getDit(), classMetrics.getNom(),
							classMetrics.getCbo(), classMetrics.getLcom(), classMetrics.getNoc(), classMetrics.getRfc(),
							classMetrics.getWmc());
				}
			} else {
				for (ClassMetricResult classMetrics : report.all()) {
					String superClass = classMetrics.getSuperClassNameLevel1();
					if (listaConcerns.contains(classMetrics.getDesignRole())) {
						writer.write(commit.getHash(), classMetrics.getClassName(),
								"\"" + classMetrics.getDesignRole() + "\"", "\"" + superClass + "\"",
								"\"" + classMetrics.getInterfaces() + "\"", classMetrics.getDit(),
								classMetrics.getNom(), classMetrics.getCbo(), classMetrics.getLcom(),
								classMetrics.getNoc(), classMetrics.getRfc(), classMetrics.getWmc());
					}
				}
			}
		} finally {
			repo.getScm().reset();
		}
	}

	public String name() {
		return "ck";
	}

}
