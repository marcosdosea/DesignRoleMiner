package org.designroleminer.threshold;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.FileLocUtil;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.MetricReport;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitService;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.MethodData;

public class TechniqueExecutor {

	static Logger logger = LoggerFactory.getLogger(TechniqueExecutor.class);
	private Repository repository;
	private GitService gitService;

	public TechniqueExecutor() {
		repository = null;
		gitService = null;
	}
	
	public TechniqueExecutor(Repository repository, GitService gitService) {
		this.repository = repository;
		this.gitService = gitService;
	}

	public void execute(Collection<ClassMetricResult> classes, String fileResultado, AbstractTechnique techinique) {
		techinique.generate(classes, fileResultado);
	}

	public MetricReport getMetricsFromProjects(Collection<String> projetos, String pathResultado, String commit) {

		ExtractSaveMetricsToFiles(projetos, pathResultado, commit);
		MetricReport report = LoadMetricsFromFiles(projetos, pathResultado, commit);

		return report;
	}

	public void saveDesignRoles(Collection<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("Classe                              ;DesignRoleTechnique                        ;Concorda?;");

		for (ClassMetricResult classe : classes) {
			pm.write(classe.getClassName() + ";" + classe.getDesignRole() + ";" + "SIM;");
		}
	}
	
	public void saveArchitecturalRoles(Collection<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("Classe                              ;ArchitecturalRole                        ;");

		for (ClassMetricResult classe : classes) {
			if (classe.isArchitecturalRole())
				pm.write(classe.getClassName() + ";" + classe.getDesignRole() + ";");
			else
				pm.write(classe.getClassName() + ";UNINDENTIFIED;");
		}
	}

	private MetricReport LoadMetricsFromFiles(Collection<String> projetos, String pathResultado, String commit) {

		MetricReport report = new MetricReport();

		for (String path : projetos) {

			if (projetos.size() > 1 || commit.isEmpty()) {
				Repository repository;
				try {
					repository = Git.open(new File(path)).getRepository();
					ObjectId lastCommitId = repository.resolve(Constants.HEAD);
					commit = lastCommitId.getName();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			int lastIndex = path.lastIndexOf("\\");
			String nameLastFolder = path.substring(lastIndex + 1) + "-" + commit;
			
			String filePathMethods = pathResultado + nameLastFolder + "-methods.csv";
			String filePathClasses = pathResultado + nameLastFolder + "-classes.csv";
			String filePathProject = pathResultado + nameLastFolder + "-project.csv";

			File fileMethods = new File(filePathMethods);
			File fileClasses = new File(filePathClasses);
			File fileProject = new File(filePathProject);

			int numberOfMethods = 0;
			int systemLOC = 0;
			int numberOfClasses = 0;
			try {
				String line;
				BufferedReader brProject = new BufferedReader(new FileReader(fileProject));
				int count = 0;
				while ((line = brProject.readLine()) != null) {
					if (count > 0) {
						String[] values = line.split(",");
						numberOfClasses = Integer.parseInt(values[0]);
						systemLOC = Integer.parseInt(values[1]);
						numberOfMethods = Integer.parseInt(values[2]);
					}
					count++;
				}
				BufferedReader brClasses = new BufferedReader(new FileReader(fileClasses));
				count = 0;
				while ((line = brClasses.readLine()) != null) {
					if (count > 0) {
						String[] values = line.split(",");
						String file = values[10];
						String className = values[1];
						String type = values[11];
						ClassMetricResult classMetric = new ClassMetricResult(file, className, type);
						classMetric.setDesignRole(values[0]);
						classMetric.setNom(Integer.parseInt(values[2]));
						classMetric.setDit(Integer.parseInt(values[3]));
						classMetric.setCbo(Integer.parseInt(values[4]));
						classMetric.setLcom(Integer.parseInt(values[5]));
						classMetric.setNoc(Integer.parseInt(values[6]));
						classMetric.setNom(Integer.parseInt(values[7]));
						classMetric.setRfc(Integer.parseInt(values[8]));
						classMetric.setWmc(Integer.parseInt(values[9]));
						classMetric.setArchitecturalRole(Boolean.parseBoolean(values[12]));
						if (values.length > 13 && !values[13].isEmpty())
							classMetric.setLoc(Integer.parseInt(values[13]));
						else
							classMetric.setLoc(0);
						classMetric.setMetricsByMethod(new HashMap<>());
						report.add(classMetric);
					}
					count++;
				}
				BufferedReader brMethods = new BufferedReader(new FileReader(fileMethods));
				count = 0;
				while ((line = brMethods.readLine()) != null) {
					if (count > 0) {
						String[] values = line.split(",");

						MethodData methodData = new MethodData();
						methodData.setNomeClasse(values[1]);
						methodData.setNomeMethod(values[2]);
						methodData.setInitialChar(Integer.parseInt(values[8]));

						MethodMetricResult metrics = new MethodMetricResult();
						metrics.setCohesion(Integer.parseInt(values[7]));
						metrics.setComplexity(Integer.parseInt(values[4]));
						metrics.setEfferentCoupling(Integer.parseInt(values[5]));
						metrics.setLinesOfCode(Integer.parseInt(values[3]));
						metrics.setNumberOfParameters(Integer.parseInt(values[6]));
						ClassMetricResult classMetric = report.get(values[9]); // name file
						Map<MethodData, MethodMetricResult> map = classMetric.getMetricsByMethod();
						map.put(methodData, metrics);
						classMetric.setMetricsByMethod(map);
					}
					count++;
				}
				brClasses.close();
				brMethods.close();
				brProject.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			report.setNumberOfClasses(numberOfClasses);
			report.setNumberOfMethods(numberOfMethods);
			report.setSystemLOC(systemLOC);
		}
		return report;
	}

	private void ExtractSaveMetricsToFiles(Collection<String> projetos, String pathResultado, String commit) {
		for (String path : projetos) {
			int lastIndex = path.lastIndexOf("\\");
			
			if (projetos.size() > 1 || commit.isEmpty()) {
				Repository repository;
				try {
					repository = Git.open(new File(path)).getRepository();
					ObjectId lastCommitId = repository.resolve(Constants.HEAD);
					commit = lastCommitId.getName();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			String nameLastFolder = path.substring(lastIndex + 1) + "-" + commit;
			String filePathMethods = pathResultado + nameLastFolder + "-methods.csv";
			String filePathClasses = pathResultado + nameLastFolder + "-classes.csv";
			String filePathProject = pathResultado + nameLastFolder + "-project.csv";

			File dir = new File(pathResultado);
			if (!dir.exists())
				dir.mkdir();

			File fileMethods = new File(filePathMethods);
			File fileClasses = new File(filePathClasses);
			File fileProject = new File(filePathProject);

			long totalMetodos = 0;
			long totalLoc = 0;
			long totalClasses = 0;

			if (!fileMethods.exists() && !fileClasses.exists() && !fileProject.exists()) {
				
				try {
					if ((repository != null) && (gitService != null))
						gitService.checkout(repository, commit);
				} catch (Exception e) {
					logger.error("Erro inesperado ao realizar checkout do projeto no commit " + commit);
					e.printStackTrace();
				}
				
				
				MetricReport report = new CK().calculate(path);
				Collection<ClassMetricResult> metricasClasses = report.all();

				PersistenceMechanism pmMethods = new CSVFile(filePathMethods, false);
				PersistenceMechanism pmClasses = new CSVFile(filePathClasses, false);
				PersistenceMechanism pmProject = new CSVFile(filePathProject, false);

				pmMethods.write("DesignRole", "Classe", "Método", "LOC", "CC", "Efferent", "NOP", "Cohesion",
						"InitialChar", "File");
				pmClasses.write("DesignRole", "Classe", "NOM", "DIT", "CBO", "LCom", "NOC", "NOM", "RFC", "WMC", "File",
						"Type", "IsArchitecturalRole", "LOC");
				pmProject.write("Number of Classes", "LOC", "NOM", "Commit");
				for (ClassMetricResult ckNumber : metricasClasses) {
					if (!ckNumber.getType().equals("class"))
						continue;
					int locClass = FileLocUtil.countLineNumbers(FileLocUtil.readFile(new File(ckNumber.getFile())));
					pmClasses.write(ckNumber.getDesignRole(), ckNumber.getClassName(), ckNumber.getNom(),
							ckNumber.getDit(), ckNumber.getCbo(), ckNumber.getLcom(), ckNumber.getNoc(),
							ckNumber.getNom(), ckNumber.getRfc(), ckNumber.getWmc(), ckNumber.getFile(),
							ckNumber.getType(), ckNumber.isArchitecturalRole(), locClass);
					if (ckNumber.getDesignRole() != null) {
						totalMetodos += ckNumber.getNom();
						totalLoc += locClass;
						// listaClasses.add(ckNumber);
					} else {
						System.out.println("Design role is null");
					}

					for (MethodData method : ckNumber.getMetricsByMethod().keySet()) {
						MethodMetricResult methodMetrics = ckNumber.getMetricsByMethod().get(method);
						if (!method.isConstructor()) {
							pmMethods.write(ckNumber.getDesignRole(), ckNumber.getClassName(), method.getNomeMethod(),
									methodMetrics.getLinesOfCode(), methodMetrics.getComplexity(),
									methodMetrics.getEfferentCoupling(), methodMetrics.getNumberOfParameters(),
									methodMetrics.getCohesion(), method.getInitialChar(), ckNumber.getFile());
						}
					}
					totalClasses++;
				}
				pmProject.write(totalClasses, totalLoc, totalMetodos, commit);
				// logger.info("Number of classes: " + totalClasses);
				// logger.info("Number of methods: " + totalMetodos);
				// logger.info("Total Lines of Code: " + totalLoc);
				pmClasses.close();
				pmMethods.close();
				pmProject.close();
			}
		}
	}

	public ArrayList<String> lerProjetos(String nomeArquivo) {
		ArrayList<String> projetos = new ArrayList<>();
		String arquivo = System.getProperty("user.dir") + "\\" + nomeArquivo;
		File file = new File(arquivo);
		try {
			if (file.exists()) {
				Scanner scanner = new Scanner(file);
				while (scanner.hasNext()) {
					projetos.add(scanner.next());
				}
				scanner.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrados: " + arquivo);
		}
		return projetos;
	}
}
