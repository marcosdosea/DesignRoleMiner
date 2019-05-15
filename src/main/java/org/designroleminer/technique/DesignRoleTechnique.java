package org.designroleminer.technique;

import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class DesignRoleTechnique extends AbstractTechnique {

	/**
	 * Generate sheet with design role assigned to each class
	 * 
	 * @param classes
	 * @param fileResultado
	 */
	@Override
	public void generate(List<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("Classe                              ;DesignRoleTechnique                        ;Concorda?;");

		for (ClassMetricResult classe : classes) {
			pm.write(classe.getClassName() + ";" + classe.getDesignRole() + ";" + "SIM;");
		}
	}

}
