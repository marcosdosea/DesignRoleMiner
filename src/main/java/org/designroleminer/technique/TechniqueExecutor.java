package org.designroleminer.technique;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.FileLocUtil;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.MetricReport;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.MethodData;

public class TechniqueExecutor {

	static Logger logger = LoggerFactory.getLogger(TechniqueExecutor.class);
	AbstractTechnique techinique;

	public TechniqueExecutor(AbstractTechnique techinique) {
		this.techinique = techinique;
	}

	public void execute(List<ClassMetricResult> classes, String fileResultado) {
		techinique.generate(classes, fileResultado);
	}

	public ArrayList<ClassMetricResult> getMetricsFromProjects(ArrayList<String> projetos) {
		ArrayList<ClassMetricResult> listaClasses = new ArrayList<ClassMetricResult>();
		for (String path : projetos) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
			String dataHora = sf.format(Calendar.getInstance().getTime());

			logger.info("[" + dataHora + "] Extracting metrics from project " + path + "...");

			int lastIndex = path.lastIndexOf("\\");
			String nameLastFolder = path.substring(lastIndex + 1);
			String filePathMethods = path + "\\" + nameLastFolder + "-methods.csv";
			String filePathClasses = path + "\\" + nameLastFolder + "-classes.csv";
			String filePathProject = path + "\\" + nameLastFolder + "-project.csv";

			File fileMethods = new File(filePathMethods);
			File fileClasses = new File(filePathClasses);
			File fileProject = new File(filePathProject);

			long totalMetodos = 0;
			long totalLoc = 0;

			if (fileMethods.exists() && !fileMethods.isDirectory()) {
				try {
					String line;
					BufferedReader brProject = new BufferedReader(new FileReader(fileProject));
					int count = 0;
					while ((line = brProject.readLine()) != null) {
						if (count > 0) {
							String[] values = line.split(",");
							totalLoc = Integer.parseInt(values[0]);
							totalMetodos = Integer.parseInt(values[1]);
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
							classMetric.setMetricsByMethod(new HashMap<>());
							listaClasses.add(classMetric);
						}
						count++;
					}
					BufferedReader brMethods = new BufferedReader(new FileReader(fileMethods));
					count = 0;
					while ((line = brMethods.readLine()) != null) {
						if (count > 0) {
							String[] values = line.split(",");

							MethodData methodData = new MethodData();
							methodData.setNomeMethod(values[1]);
							methodData.setInitialChar(Integer.parseInt(values[8]));

							MethodMetricResult metrics = new MethodMetricResult();
							metrics.setCohesion(Integer.parseInt(values[7]));
							metrics.setComplexity(Integer.parseInt(values[4]));
							metrics.setEfferentCoupling(Integer.parseInt(values[5]));
							metrics.setLinesOfCode(Integer.parseInt(values[3]));
							metrics.setNumberOfParameters(Integer.parseInt(values[6]));
							ClassMetricResult classMetricResult = new ClassMetricResult(values[9], "", "");
							int index = listaClasses.indexOf(classMetricResult);
							ClassMetricResult classMetric = listaClasses.get(index);
							Map<MethodData, MethodMetricResult> map = classMetric.getMetricsByMethod();
							map.put(methodData, metrics);
							classMetric.setMetricsByMethod(map);
						}
						count++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				MetricReport report = new CK().calculate(path);
				Collection<ClassMetricResult> metricasClasses = report.all();

				PersistenceMechanism pmMethods = new CSVFile(filePathMethods, false);
				PersistenceMechanism pmClasses = new CSVFile(filePathClasses, false);
				PersistenceMechanism pmProject = new CSVFile(filePathProject, false);

				pmMethods.write("DesignRole", "Classe", "Método", "LOC", "CC", "Efferent", "NOP", "Cohesion",
						"InitialChar", "File");
				pmClasses.write("DesignRole", "Classe", "NOM", "DIT", "CBO", "LCom", "NOC", "NOM", "RFC", "WMC", "File",
						"Type");
				pmProject.write("LOC", "NOM");
				for (ClassMetricResult ckNumber : metricasClasses) {
					pmClasses.write(ckNumber.getDesignRole(), ckNumber.getClassName(), ckNumber.getNom(),
							ckNumber.getDit(), ckNumber.getCbo(), ckNumber.getLcom(), ckNumber.getNoc(),
							ckNumber.getNom(), ckNumber.getRfc(), ckNumber.getWmc(), ckNumber.getFile(),
							ckNumber.getType());
					if (ckNumber.getDesignRole() != null) {
						totalMetodos += ckNumber.getNom();
						totalLoc += FileLocUtil.countLineNumbers(FileLocUtil.readFile(new File(ckNumber.getFile())));
						listaClasses.add(ckNumber);
					}
					for (MethodData method : ckNumber.getMetricsByMethod().keySet()) {
						MethodMetricResult methodMetrics = ckNumber.getMetricsByMethod().get(method);
						pmMethods.write(ckNumber.getDesignRole(), ckNumber.getClassName(), method.getNomeMethod(),
								methodMetrics.getLinesOfCode(), methodMetrics.getComplexity(),
								methodMetrics.getEfferentCoupling(), methodMetrics.getNumberOfParameters(),
								methodMetrics.getCohesion(), method.getInitialChar(), ckNumber.getFile());
					}
				}
				pmProject.write(totalLoc, totalMetodos);
			}

			logger.info("Number of classes: " + listaClasses.size());
			logger.info("Number of methods: " + totalMetodos);
			logger.info("Total Lines of Code: " + totalLoc);

			// listaClasses.addAll(report.all());
		}

		return listaClasses;
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

	/**
	 * set strategy
	 * 
	 * @param techinique
	 */
	public void setTechinique(AbstractTechnique techinique) {
		this.techinique = techinique;
	}

}
