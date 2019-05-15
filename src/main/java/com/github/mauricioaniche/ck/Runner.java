package com.github.mauricioaniche.ck;

import org.designroleminer.ClassMetricResult;
import org.designroleminer.MetricReport;

public class Runner {

	public static void main(String[] args) {
		
		if(args==null || args.length == 0) {
			System.out.println("Usage java -jar ck.jar /path/to/project");
			System.exit(1);
		}
		
		String path = args[0];
		MetricReport report = new CK().calculate(path);
		
		System.out.println("class,type,cbo,wmc,dit,noc,rfc,lcom,nom");
		for(ClassMetricResult result : report.all()) {
			System.out.println(
					result.getClassName() + "," +
					result.getType() + "," +
					result.getCbo() + "," +
					result.getWmc() + "," +
					result.getDit() + "," +
					result.getNoc() + "," +
					result.getRfc() + "," +
					result.getLcom() + "," +
					result.getNom()
			);
		}

		
	}
}
