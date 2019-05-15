package org.designroleminer.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.MetricReport;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.MethodData;

public class MethodVisitorMetric implements CommitVisitor {

	List<String> listaDesignRoles = new ArrayList<String>();
	String applicationName;
	Map<String, String> tags = null;
	Map<String, Map<String, Integer>> metricsByTagDesignRole = new HashMap<>();

	public MethodVisitorMetric(List<String> listaDesignRoles, String applicationName) {
		this.listaDesignRoles = listaDesignRoles;
		this.applicationName = applicationName;
	}

	public MethodVisitorMetric(List<String> listaDesignRoles, String applicationName, Map<String, String> tags) {
		this.listaDesignRoles = listaDesignRoles;
		this.applicationName = applicationName;
		this.tags = tags;
	}

	public MethodVisitorMetric(String applicationName, Map<String, String> tags) {
		this.applicationName = applicationName;
		this.tags = tags;
	}

	public MethodVisitorMetric() {
		super();
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CK ck = new CK();

			MetricReport report = ck.calculate(repo.getPath());

			desconsiderarDesignRoles(commit, writer, report, 1);

			if (listaDesignRoles.size() == 0) {
				if (tags == null) {
					writer.write("Class", "Design Role", "Method", "LOC", "CC", "Efferent", "NOP");

					for (ClassMetricResult classMetrics : report.all()) {
						Map<MethodData, MethodMetricResult> metricsByMethod = classMetrics.getMetricsByMethod();
						for (MethodData metodo : metricsByMethod.keySet()) {
							MethodMetricResult methodMetrics = metricsByMethod.get(metodo);
							writer.write(classMetrics.getClassName(), "\"" + classMetrics.getDesignRole() + "\"",
									metodo.getNomeMethod(), methodMetrics.getLinesOfCode(),
									methodMetrics.getComplexity(), methodMetrics.getEfferentCoupling(),
									methodMetrics.getNumberOfParameters());
						}
					}
				} else {
					writer.write("Application", "Tag", "Class", "Design Role", "Method", "LOC", "CC", "Efferent",
							"NOP");
					String tag = tags.get(commit.getHash());
					for (ClassMetricResult classMetrics : report.all()) {

						Map<String, Integer> metricas = metricsByTagDesignRole
								.get("\"" + classMetrics.getDesignRole() + "\"" + "," + tag);
						if (metricas == null) {
							metricas = new HashMap<>();
							metricas.put("LOC", 0);
							metricas.put("CC", 0);
						}
						int loc = metricas.get("LOC");
						int cc = metricas.get("CC");
						Map<MethodData, MethodMetricResult> metricsByMethod = classMetrics.getMetricsByMethod();
						for (MethodData metodo : metricsByMethod.keySet()) {
							MethodMetricResult methodMetrics = metricsByMethod.get(metodo);
							loc += methodMetrics.getLinesOfCode();
							cc += methodMetrics.getComplexity();
							writer.write(applicationName, tag, classMetrics.getClassName(),
									"\"" + classMetrics.getDesignRole() + "\"", metodo.getNomeMethod(),
									methodMetrics.getLinesOfCode(), methodMetrics.getComplexity(),
									methodMetrics.getEfferentCoupling(), methodMetrics.getNumberOfParameters());
						}
						metricas.put("LOC", loc);
						metricas.put("CC", cc);
						metricsByTagDesignRole.put("\"" + classMetrics.getDesignRole() + "\"" + "," + tag, metricas);
					}
					writer.write("DesignRoleTechinique", "Tag", "LOC", "CC");
					for (String designRoleMetric : metricsByTagDesignRole.keySet()) {
						Map<String, Integer> metricas = metricsByTagDesignRole.get(designRoleMetric);
						writer.write(designRoleMetric, metricas.get("LOC"), metricas.get("CC"));
					}
				}
			} else {
				if (tags == null) {
					writer.write("Application", "Class", "Design Role", "Method", "LOC", "CC", "Efferent", "NOP");
					for (ClassMetricResult classMetrics : report.all()) {
						if (listaDesignRoles.contains(classMetrics.getDesignRole())) {
							Map<MethodData, MethodMetricResult> metricsByMethod = classMetrics.getMetricsByMethod();
							for (MethodData metodo : metricsByMethod.keySet()) {
								MethodMetricResult methodMetrics = metricsByMethod.get(metodo);
								writer.write(applicationName, classMetrics.getClassName(),
										"\"" + classMetrics.getDesignRole() + "\"", metodo.getNomeMethod(),
										methodMetrics.getLinesOfCode(), methodMetrics.getComplexity(),
										methodMetrics.getEfferentCoupling(), methodMetrics.getNumberOfParameters());
							}
						}
					}
				} else {
					writer.write("Application", "Tag", "Class", "Design Role", "Method", "LOC", "CC", "Efferent",
							"NOP");
					String tag = tags.get(commit.getHash());
					for (ClassMetricResult classMetrics : report.all()) {
						if (listaDesignRoles.contains(classMetrics.getDesignRole())) {
							Map<MethodData, MethodMetricResult> metricsByMethod = classMetrics.getMetricsByMethod();
							for (MethodData metodo : metricsByMethod.keySet()) {
								MethodMetricResult methodMetrics = metricsByMethod.get(metodo);
								writer.write(applicationName, tag, classMetrics.getClassName(),
										"\"" + classMetrics.getDesignRole() + "\"", metodo.getNomeMethod(),
										methodMetrics.getLinesOfCode(), methodMetrics.getComplexity(),
										methodMetrics.getEfferentCoupling(), methodMetrics.getNumberOfParameters());
							}
						}
					}
				}
			}
		} finally

		{
			repo.getScm().reset();
		}
	}

	private void desconsiderarDesignRoles(Commit commit, PersistenceMechanism writer, MetricReport report,
			int quantidadeCorte) {

		// primeiro conta quantas classes associadas ao conecern
		Map<String, Integer> mapDesignRoles = new HashMap<String, Integer>();
		for (ClassMetricResult classMetrics : report.all()) {
			Integer quantidade = mapDesignRoles.get(classMetrics.getDesignRole());
			if (quantidade != null)
				mapDesignRoles.put(classMetrics.getDesignRole(), ++quantidade);
			else
				mapDesignRoles.put(classMetrics.getDesignRole(), 1);
		}
		// Verifica se conncer deve ser desconsiderado
		for (ClassMetricResult classMetrics : report.all()) {
			Integer quantidade = mapDesignRoles.get(classMetrics.getDesignRole());
			if (quantidade <= quantidadeCorte)
				classMetrics.setConcern("UNDEFINED");
		}

	}

	@Override
	public String name() {
		return "ck";
	}

}
