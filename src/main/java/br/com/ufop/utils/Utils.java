package br.com.ufop.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static List<String> readInstance(String filePath) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(filePath));

	    List<String> lines = new ArrayList<String>();
	    	    
	    String line = "";
        while ((line = br.readLine()) != null) {
        	line = line.trim().replaceAll(" +", " ");
            lines.add(line);
        }
        
        br.close();
        
        return lines;
	}

	public static List<String> getInstances(File directory) {
		List<String> instances = new ArrayList<String>();
		
		for (File fileEntry : directory.listFiles()) {
			instances.add(fileEntry.getName());
	    }
		
		return instances;
	}
}
