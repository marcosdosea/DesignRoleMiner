package org.designroleminer.study;

import org.designroleminer.visitor.ClassVisitorDesignRole;
import org.designroleminer.visitor.MethodVisitorMetric;
import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class WebBudgetStudy implements Study {

	public static void main(String[] args) {
		new RepoDriller().start(new WebBudgetStudy());
	}

	public void execute() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/web-budget"))
				.through(Commits.onlyInHead()).withThreads(1)
				.process(new ClassVisitorDesignRole(), new CSVFile("D:/Projetos/_Web/web-budget/web-budget-drs.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/web-budget"))
				.through(Commits.onlyInHead()).withThreads(1)
				.process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/_Web/web-budget/web-budget-metrics.csv"))
				.mine();
	}

}
