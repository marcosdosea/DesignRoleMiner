package org.designroleminer.techinique;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.FileLocUtil;
import org.designroleminer.MethodMetricResult;
import org.designroleminer.MetricReport;
import org.designroleminer.smelldetector.model.LimiarMetrica;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

import com.github.mauricioaniche.ck.CK;

public class TechiniqueExecutor {

	ITechinique techinique;
	
	public TechiniqueExecutor(ITechinique techinique) {
		this.techinique = techinique;
	}
	
	public void execute(List<ClassMetricResult> classes, String fileResultado) {
		techinique.generate(classes, fileResultado);
	}

	public ArrayList<ClassMetricResult> getMetricsFromProjects(ArrayList<String> projetos) {
		ArrayList<ClassMetricResult> listaClasses = new ArrayList<>();
		for (String path : projetos) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
			String dataHora = sf.format(Calendar.getInstance().getTime());

			System.out.println("[" + dataHora + "] Extracting metrics from project " + path + "...");
			MetricReport report = new CK().calculate(path);

			Collection<ClassMetricResult> metricasClasses = report.all();
			long totalMetodos = 0;
			long totalLoc = 0;
			for (ClassMetricResult ckNumber : metricasClasses) {
				totalMetodos += ckNumber.getNom();
				totalLoc += FileLocUtil.countLineNumbers(FileLocUtil.readFile(new File(ckNumber.getFile())));
			}

			System.out.println("Number of classes: " + report.all().size());
			System.out.println("Number of methods: " + totalMetodos);
			System.out.println("Total Lines of Code: " + totalLoc);

			listaClasses.addAll(report.all());
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
	 * @param techinique
	 */
	public void setTechinique(ITechinique techinique) {
		this.techinique = techinique;
	}

}
