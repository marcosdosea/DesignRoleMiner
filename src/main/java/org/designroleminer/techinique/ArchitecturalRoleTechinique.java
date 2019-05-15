package org.designroleminer.techinique;

import java.util.List;

import org.designroleminer.ClassMetricResult;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

public class ArchitecturalRoleTechinique extends ITechinique {

	/**
	 * Generate sheet with architectural roles assigned to each class
	 * 
	 * @param classes
	 * @param fileResultado
	 */
	@Override
	public void generate(List<ClassMetricResult> classes, String fileResultado) {
		PersistenceMechanism pm = new CSVFile(fileResultado);
		pm.write("Classe                              ;ArchitecturalRole                        ;");

		for (ClassMetricResult classe : classes) {
			if (classe.isArchitecturalRole())
				pm.write(classe.getClassName() + ";" + classe.getDesignRole() + ";");
			else
				pm.write(classe.getClassName() + ";UNINDENTIFIED;");
		}
	}

}
