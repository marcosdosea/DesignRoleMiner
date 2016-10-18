package br.ufba.concernminer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.mauricioaniche.ck.MethodData;
import com.github.mauricioaniche.ck.MethodMetrics;
import com.github.mauricioaniche.ck.Utils;
import com.github.mauricioaniche.ck.metric.MethodMetric;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.parser.jdt.JDTRunner;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.RepositoryFile;
import br.com.metricminer2.scm.SCMRepository;

public class MethodVisitor implements CommitVisitor {

	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {

		try {
			repo.getScm().checkout(commit.getHash());
			List<RepositoryFile> files = repo.getScm().files();

			writer.write("Commit", "Package", "SuperClass/[interfaces]", "Class", "Method", "Complexity", "LOC", "NOP",
					"Efferent Coupling");

			// CKReport report = new CK().calculate();

			for (RepositoryFile file : files) {
				if (!file.fileNameEndsWith("java"))
					continue;
				File soFile = file.getFile();

				MethodMetric visitor = new MethodMetric();

				new JDTRunner().visit(visitor, new ByteArrayInputStream(Utils.readFile(soFile).getBytes()));

				Map<MethodData, MethodMetrics> mapMetricsByMethod = visitor.getMetricsByMethod();

				// for (MethodData metodo : mapMetricsByMethod.keySet()) {
				// MethodMetrics metrics = mapMetricsByMethod.get(metodo);
				// if (Strings.isNullOrEmpty(metodo.getNomeSuperclasse())) {
				// writer.write(commit.getHash(), metodo.getNomePackage(), "\""
				// + metodo.getNomeInterfaces() + "\"",
				// metodo.getNomeType(), metodo.getNomeMethod(),
				// metrics.getComplexity(), metrics.getLinesOfCode(),
				// metrics.getNumberOfParameters(),
				// metrics.getEfferentCoupling());
				// } else {
				// writer.write(commit.getHash(), metodo.getNomePackage(), "\""
				// + metodo.getNomeSuperclasse() + "\"",
				// metodo.getNomeType(), metodo.getNomeMethod(),
				// metrics.getComplexity(), metrics.getLinesOfCode(),
				// metrics.getNumberOfParameters(),
				// metrics.getEfferentCoupling());
				// }
				//
				// }
			}
		} finally {
			repo.getScm().reset();
		}

	}

	public String name() {
		return "cc";
	}

}
