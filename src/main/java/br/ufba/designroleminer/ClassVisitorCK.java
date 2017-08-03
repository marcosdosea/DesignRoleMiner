package br.ufba.designroleminer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class ClassVisitorCK implements CommitVisitor {

	List<String> listaConcerns = new ArrayList<String>();

	public ClassVisitorCK(List<String> listaConcerns) {
		this.listaConcerns = listaConcerns;
	}

	public ClassVisitorCK() {
		super();
	}
	
	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CK ck = new CK();

			CKReport report = ck.calculate(repo.getPath());

			writer.write("Commit", "Class", "Design Role", "SuperClass", "interfaces", "DIT", "NOM", "CBO", "LCOM", "NOC",
					"RFC", "WMC");

			desconsiderarConcerns(commit, writer, report, 1);

			if (listaConcerns.size() == 0) {
				for (CKNumber classMetrics : report.all()) {
					String superClass = classMetrics.getSuperClassNameLevel1();
					writer.write(commit.getHash(), classMetrics.getClassName(), "\"" + classMetrics.getDesignRole() + "\"",
							"\"" + superClass + "\"", "\"" + classMetrics.getInterfaces() + "\"", classMetrics.getDit(),
							classMetrics.getNom(), classMetrics.getCbo(), classMetrics.getLcom(), classMetrics.getNoc(),
							classMetrics.getRfc(), classMetrics.getWmc());
				}
			} else {
				for (CKNumber classMetrics : report.all()) {
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

	private void desconsiderarConcerns(Commit commit, PersistenceMechanism writer, CKReport report,
			int quantidadeCorte) {

		// primeiro conta quantas classes associadas ao conecern
		Map<String, Integer> mapConcerns = new HashMap<String, Integer>();
		for (CKNumber classMetrics : report.all()) {
			Integer quantidade = mapConcerns.get(classMetrics.getDesignRole());
			if (quantidade != null)
				mapConcerns.put(classMetrics.getDesignRole(), ++quantidade);
			else
				mapConcerns.put(classMetrics.getDesignRole(), 1);
		}
		// Verifica se conncer deve ser desconsiderado
		for (CKNumber classMetrics : report.all()) {
			Integer quantidade = mapConcerns.get(classMetrics.getDesignRole());
			if (quantidade <= quantidadeCorte)
				classMetrics.setConcern("util");
		}

	}

	@Override
	public String name() {
		return "ck";
	}

}
