package org.designroleminer.study;

import org.designroleminer.visitor.ClassVisitorDesignRole;
import org.designroleminer.visitor.MethodVisitorMetric;
import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class MotivatingStudy implements Study {

	public static void main(String[] args) {
		new RepoDriller().start(new MotivatingStudy());
	}

	public void execute() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.single("f2e700f3739ce38d008100c3d515fce3f0755369")).withThreads(5) // Commit 09.11.2016
				.process(new ClassVisitorDesignRole(), new CSVFile("D:/Projetos/_Web/libreplan-drs.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/libreplan"))
				.through(Commits.single("f2e700f3739ce38d008100c3d515fce3f0755369")).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/libreplan-metrics.csv")).mine();
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/web-budget"))
				.through(Commits.single("fe9873b4c88c2ea28a1adf4c173e10561efd0788")).withThreads(5) // Commit 20.10.2016
				.process(new ClassVisitorDesignRole(), new CSVFile("D:/Projetos/_Web/web-budget-drs.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/web-budget"))
				.through(Commits.single("fe9873b4c88c2ea28a1adf4c173e10561efd0788")).withThreads(5)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/web-budget-metrics.csv")).mine();

	}

}
