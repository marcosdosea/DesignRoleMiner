package br.ufba.concernminer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import com.github.mauricioaniche.ck.Utils;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.parser.jdt.JDTRunner;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.RepositoryFile;
import br.com.metricminer2.scm.SCMRepository;

public class ProjectVisitor implements CommitVisitor {

	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		
		try {
			repo.getScm().checkout(commit.getHash());
			List<RepositoryFile> files = repo.getScm().files();

			int totalLoc = 0;
			int totalFiles = 0;
			ProjectListener visitor = new ProjectListener();
			for(RepositoryFile file : files) {
				if(!file.fileNameEndsWith("java")) continue;
				File soFile = file.getFile();
				
				new JDTRunner().visit(visitor, new ByteArrayInputStream(Utils.readFile(soFile).getBytes()));
				int loc = Utils.countLineNumbers(Utils.readFile(soFile));
				totalLoc += loc;
				totalFiles++;
			}
			
			writer.write(commit.getHash(), new SimpleDateFormat("YYYY-MM-dd").format(commit.getDate().getTime()), visitor.getNumberOfMethods(), totalLoc, totalFiles);
		} finally {
			repo.getScm().reset();
		}
	}

	public String name() {
		return "nom";
	}

}
