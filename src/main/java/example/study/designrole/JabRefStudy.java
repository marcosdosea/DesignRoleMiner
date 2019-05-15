package example.study.designrole;

import org.designroleminer.visitor.ClassVisitorDesignRole;
import org.designroleminer.visitor.MethodVisitorMetric;
import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class JabRefStudy implements Study {

	public static void main(String[] args) {
		new RepoDriller().start(new JabRefStudy());
	}

	public void execute() {
		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/jabref")).through(Commits.onlyInHead())
				.withThreads(5).process(new ClassVisitorDesignRole(), new CSVFile("D:/Projetos/jabref-drs.csv")).mine();

		new RepositoryMining().in(GitRepository.singleProject("D:/Projetos/jabref")).through(Commits.onlyInHead())
				.withThreads(5).process(new MethodVisitorMetric(), new CSVFile("D:/Projetos/jabref-metrics.csv")).mine();
	}

}
