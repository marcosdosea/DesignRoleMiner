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
			CKReport report = new CK().calculate(repo.getPath());

			writer.write("Commit", "Class", "Concern", "SuperClass", "interfaces", "DIT", "NOM", "CBO", "LCOM", "NOC",
					"RFC", "WMC");

			desconsiderarConcerns(commit, writer, report, 1);

			if (listaConcerns.size() == 0) {
				for (CKNumber classMetrics : report.all()) {
					String superClass = classMetrics.getSuperClassNameLevel1();
					writer.write(commit.getHash(), classMetrics.getClassName(), "\"" + classMetrics.getConcern() + "\"",
							"\"" + superClass + "\"", "\"" + classMetrics.getInterfaces() + "\"", classMetrics.getDit(),
							classMetrics.getNom(), classMetrics.getCbo(), classMetrics.getLcom(), classMetrics.getNoc(),
							classMetrics.getRfc(), classMetrics.getWmc());
				}
			} else {
				for (CKNumber classMetrics : report.all()) {
					String superClass = classMetrics.getSuperClassNameLevel1();
					if (listaConcerns.contains(classMetrics.getConcern())) {
						writer.write(commit.getHash(), classMetrics.getClassName(),
								"\"" + classMetrics.getConcern() + "\"", "\"" + superClass + "\"",
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
			Integer quantidade = mapConcerns.get(classMetrics.getConcern());
			if (quantidade != null)
				mapConcerns.put(classMetrics.getConcern(), ++quantidade);
			else
				mapConcerns.put(classMetrics.getConcern(), 1);
		}
		// Verifica se conncer deve ser desconsiderado
		for (CKNumber classMetrics : report.all()) {
			Integer quantidade = mapConcerns.get(classMetrics.getConcern());
			if (quantidade <= quantidadeCorte)
				classMetrics.setConcern("util");
		}

	}

	@Override
	public String name() {
		return "ck";
	}

}
