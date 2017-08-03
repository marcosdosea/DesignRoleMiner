package br.ufba.designroleminer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;

public class ClassVisitorDR implements CommitVisitor {

	List<String> listaConcerns = new ArrayList<String>();
	String applicationName;

	public ClassVisitorDR(List<String> listaConcerns, String applicationName) {
		this.listaConcerns = listaConcerns;
		this.applicationName = applicationName;
	}

	public ClassVisitorDR() {
		super();
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CK ck = new CK();

			CKReport report = ck.calculate(repo.getPath());

			
			desconsiderarConcerns(commit, writer, report, 1);
			if (listaConcerns.size() == 0) {
				writer.write("Class", "Design Role");
				for (CKNumber classMetrics : report.all()) {
					writer.write(classMetrics.getClassName(), "\"" + classMetrics.getDesignRole() + "\"");
				}
			} else {
				writer.write("Application", "Class", "Design Role");

				for (CKNumber classMetrics : report.all()) {
					if (listaConcerns.contains(classMetrics.getDesignRole())) {
						writer.write(applicationName, classMetrics.getClassName(), "\"" + classMetrics.getDesignRole() + "\"");
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
				classMetrics.setConcern("Undefined");
		}
	}

	@Override
	public String name() {
		return "dr";
	}

}
