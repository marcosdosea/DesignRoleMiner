package org.designroleminer;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;

public class FileLocUtil {
	public static String readFile(File f) {
		try {
			FileInputStream input = new FileInputStream(f);
			String text = IOUtils.toString(input);
			input.close();
			return text;
		} catch (Exception e) {
			throw new RuntimeException("error reading file " + f.getAbsolutePath(), e);
		}
	}
	// http://stackoverflow.com/questions/2850203/count-the-number-of-lines-in-a-java-string
	public static int countLineNumbers(String str) {
		if(str == null || str.isEmpty())
	    {
	        return 0;
	    }
	    int lines = 0;
	    int pos = 0;
	    while ((pos = str.indexOf("\n", pos) + 1) != 0) {
	        	lines++;
	    }
	    return lines;
	}
}
