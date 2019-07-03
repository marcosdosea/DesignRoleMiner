package org.designroleminer.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MetricReport;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import com.github.mauricioaniche.ck.CK;

public class ClassVisitorDesignRole implements CommitVisitor {

	List<String> listaConcerns = new ArrayList<String>();
	String applicationName;

	public ClassVisitorDesignRole(List<String> listaConcerns, String applicationName) {
		this.listaConcerns = listaConcerns;
		this.applicationName = applicationName;
	}

	public ClassVisitorDesignRole() {
		super();
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CK ck = new CK();

			MetricReport report = ck.calculate(repo.getPath());

			desconsiderarConcerns(commit, writer, report, 1);
			if (listaConcerns.size() == 0) {
				writer.write("Class", "Design Role", "Architectural Role?");
				for (ClassMetricResult classMetrics : report.all()) {
					writer.write(classMetrics.getClassName(), "\"" + classMetrics.getDesignRole() + "\"",
							classMetrics.isArchitecturalRole());
				}
			} else {
				writer.write("Application", "Class", "Design Role", "Architectural Role?");

				for (ClassMetricResult classMetrics : report.all()) {
					if (listaConcerns.contains(classMetrics.getDesignRole())) {
						writer.write(applicationName, classMetrics.getClassName(),
								"\"" + classMetrics.getDesignRole() + "\"", classMetrics.isArchitecturalRole());
					}
				}
			}

		} finally {
			repo.getScm().reset();
		}
	}

	private void desconsiderarConcerns(Commit commit, PersistenceMechanism writer, MetricReport report,
			int quantidadeCorte) {

		// primeiro conta quantas classes associadas ao conecern
		Map<String, Integer> mapConcerns = new HashMap<String, Integer>();
		for (ClassMetricResult classMetrics : report.all()) {
			Integer quantidade = mapConcerns.get(classMetrics.getDesignRole());
			if (quantidade != null)
				mapConcerns.put(classMetrics.getDesignRole(), ++quantidade);
			else
				mapConcerns.put(classMetrics.getDesignRole(), 1);
		}
		// Verifica se conncer deve ser desconsiderado
		for (ClassMetricResult classMetrics : report.all()) {
			Integer quantidade = mapConcerns.get(classMetrics.getDesignRole());
			if (quantidade <= quantidadeCorte)
				classMetrics.setDesignRole("Undefined");
		}
	}

	@Override
	public String name() {
		return "dr";
	}

}
