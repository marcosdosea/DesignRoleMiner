package com.github.drminer.visitor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.RepositoryFile;
import org.repodriller.scm.SCMRepository;

import com.github.drminer.FileLocUtil;

public class ProjectVisitor implements CommitVisitor {

	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {

		try {
			repo.getScm().checkout(commit.getHash());
			List<RepositoryFile> files = repo.getScm().files();

			int totalLoc = 0;
			int totalFiles = 0;
			ProjectListener visitor = new ProjectListener();
			for (RepositoryFile file : files) {
				if (!file.fileNameEndsWith("java"))
					continue;
				File soFile = file.getFile();

				// new JDTRunner().visit(visitor, new
				// ByteArrayInputStream(FileLocUtil.readFile(soFile).getBytes()));
				int loc = FileLocUtil.countLineNumbers(FileLocUtil.readFile(soFile));
				totalLoc += loc;
				totalFiles++;
			}

			writer.write(commit.getHash(), new SimpleDateFormat("YYYY-MM-dd").format(commit.getDate().getTime()),
					visitor.getNumberOfMethods(), totalLoc, totalFiles);
		} finally {
			repo.getScm().reset();
		}
	}

	public String name() {
		return "nom";
	}

}
