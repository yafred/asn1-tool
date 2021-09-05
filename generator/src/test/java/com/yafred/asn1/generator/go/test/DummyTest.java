package com.yafred.asn1.generator.go.test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.Test;

public class DummyTest {
	@Test
	public void generateCode() throws Exception {

       	// create output dir
       	String outputPath = System.getProperty("buildDirectory") + File.separator + "generator-output" + File.separator + "go" + File.separator + "dummy";
       	File outputPathFile = new File(outputPath);
       	outputPathFile.mkdirs();
       	
       	// write dummy module
		PrintWriter fileWriter = new PrintWriter(new FileWriter(new File(outputPathFile, "go.mod")));
		
		fileWriter.println("module dummy\r\n" +
		"go 1.17");
		
		fileWriter.close();

		// write dummy file
		fileWriter = new PrintWriter(new FileWriter(new File(outputPathFile, "dummy.go")));
		
		fileWriter.println("package dummy\r\n" + 
				"\r\n" + 
				"// Hello returns string hello\r\n" + 
				"func Hello() string {\r\n" + 
				"	return \"hello\"\r\n" + 
				"}");
		
		fileWriter.close();

	}
}
