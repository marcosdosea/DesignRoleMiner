package com.github.study;

import com.github.designroletool.ClassVisitorDR;
import com.github.designroletool.MethodVisitorCK;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.filter.range.Commits;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;

public class WebBudgetStudy implements Study {

	public static void main(String[] args) {
		new MetricMiner2().start(new WebBudgetStudy());
	}

	public void execute() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/web-budget"))
			.through(Commits.onlyInHead()).withThreads(5)
			.process(new ClassVisitorDR(), new CSVFile("D:/Projetos/_Web/web-budget/web-budget-drs.csv"))
		.mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/_Web/web-budget"))
			.through(Commits.onlyInHead()).withThreads(5)
			.process(new MethodVisitorCK(), new CSVFile("D:/Projetos/_Web/web-budget/web-budget-metrics.csv"))
			.mine();
	}
	
}
