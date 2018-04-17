package com.yafred.asn1.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class SampleGenerator {
	private File outputDir;
	private String outputPath;
	private String packageName;
	private String className = "Test";
	
	public SampleGenerator(String outputPath, String packageName) {
		this.outputPath = outputPath;
		this.packageName = packageName;
	}
	
	public void generateCode() throws Exception {
		if (outputPath == null || outputPath.equals("")) {
			throw new Exception("No output directory specified in options");
		}

		// check if output dir exists
		outputDir = new File(outputPath);

		if (!outputDir.exists()) {
			throw new Exception("Output directory does not exist: " + outputPath);
		}

		if (!outputDir.canWrite()) {
			throw new Exception("Cannot write output directory: " + outputPath);
		}
		
		// create a folder
		File packageDirectory = new File(outputDir, packageName);

        if (packageDirectory.exists()) {
            packageDirectory.delete();
        } else {
            packageDirectory.mkdir();
        }
        
        // create a class
        PrintWriter output = new PrintWriter(new FileWriter(
                new File(packageDirectory, className + ".java")));
        
        output.println("package " + packageName + ";");
        output.println("public class " + className);
        output.println("{");
        output.println("public String sayHello(String name) { return \"Hello \" + name; }");
        output.println("}");
        output.close();
	}

	public static void main(String[] args) {
		try {
			new SampleGenerator(args[0], args[1]).generateCode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
