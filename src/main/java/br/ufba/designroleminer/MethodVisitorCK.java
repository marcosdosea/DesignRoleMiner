package br.ufba.designroleminer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;
import com.github.mauricioaniche.ck.MethodData;
import com.github.mauricioaniche.ck.MethodMetrics;
import com.github.mauricioaniche.ck.metric.DesignRole;
import com.github.mauricioaniche.ck.metric.Metric;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;

public class MethodVisitorCK implements CommitVisitor {

	List<String> listaConcerns = new ArrayList<String>();

	public MethodVisitorCK(List<String> listaConcerns) {
		this.listaConcerns = listaConcerns;
	}

	public MethodVisitorCK() {
		super();
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CK ck = new CK();
//			ck.plug(new Callable<Metric>() {
//				
//				@Override
//				public Metric call() throws Exception {
//					// TODO Auto-generated method stub
//					return new DesignRole();
//				}
//			});
			
			CKReport report = ck.calculate(repo.getPath());

			writer.write("Class", "Design Role", "Method", "LOC", "CC", "Efferent", "NOP");

			desconsiderarConcerns(commit, writer, report, 1);

			if (listaConcerns.size() == 0) {
				for (CKNumber classMetrics : report.all()) {
					Map<MethodData, MethodMetrics> metricsByMethod = classMetrics.getMetricsByMethod();
					for (MethodData metodo : metricsByMethod.keySet()) {
						MethodMetrics methodMetrics = metricsByMethod.get(metodo);
						writer.write(classMetrics.getClassName(),
								"\"" + classMetrics.getDesignRole() + "\"", metodo.getNomeMethod(), 
								methodMetrics.getLinesOfCode(), methodMetrics.getComplexity(), 
								methodMetrics.getEfferentCoupling(), methodMetrics.getNumberOfParameters());
					}
				}
			} else {
				for (CKNumber classMetrics : report.all()) {
					if (listaConcerns.contains(classMetrics.getDesignRole())) {
						Map<MethodData, MethodMetrics> metricsByMethod = classMetrics.getMetricsByMethod();
						for (MethodData metodo : metricsByMethod.keySet()) {
							MethodMetrics methodMetrics = metricsByMethod.get(metodo);
							writer.write(classMetrics.getClassName(),
									"\"" + classMetrics.getDesignRole() + "\"", metodo.getNomeMethod(), 
									methodMetrics.getLinesOfCode(), methodMetrics.getComplexity(), 
									methodMetrics.getEfferentCoupling(), methodMetrics.getNumberOfParameters());
						}
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
				classMetrics.setConcern("Util");
		}

	}

	@Override
	public String name() {
		return "ck";
	}

}
