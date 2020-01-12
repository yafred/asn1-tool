/*******************************************************************************
 * Copyright (C) 2020 Fred D7e (https://github.com/yafred)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.yafred.asn1.tool;

import java.util.Properties;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.yafred.asn1.generator.java.Generator;
import com.yafred.asn1.grammar.ASNLexer;
import com.yafred.asn1.grammar.ASNParser;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.parser.Asn1ModelValidator;
import com.yafred.asn1.parser.Asn1SpecificationWriter;
import com.yafred.asn1.parser.SpecificationAntlrVisitor;

public class Tool {
	private Specification model;
	
	public static void main(String[] args) throws Exception {
		new Tool().process(args);
	}
	
	
	void process(String[] args) throws Exception {
		Properties gitProperties = new Properties();
		try {
			gitProperties.load(Tool.class.getClassLoader().getResourceAsStream("com/yafred/asn1/tool/git.properties"));
		}
		catch(Exception e) {
		}
		
		boolean hasJavaFormatter = true;
		try {
			Class.forName("com.google.googlejavaformat.java.Formatter");
		} catch (ClassNotFoundException e) {
			hasJavaFormatter = false;
		}
		
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption( "f", "file", true, "File containing ASN.1 modules.");
		options.addOption( "jo", "java-output-dir", true, "Folder where Java code is generated.");
		options.addOption( "jp", "java-output-package", true, "Java package prefix for the Java code.");
		if(hasJavaFormatter) {
			options.addOption( "jb", "java-beautify", false, "Format generated code.");
		}
		options.addOption( "p", "print-asn1", false, "Print the validated model." );

	    // parse the command line arguments
	    CommandLine line = parser.parse( options, args );

      	if(!line.hasOption("f")) {
      		String version = "No version (probably not built from a git checkout)";

	       	if(gitProperties.getProperty("git.tags") != null && !gitProperties.getProperty("git.tags").equals("")) {
	       			version = gitProperties.getProperty("git.tags");
	       	}
	       	else 
		       	if(gitProperties.getProperty("git.commit.id.describe") != null && !gitProperties.getProperty("git.commit.id.describe").equals("")) {
			    	version = gitProperties.getProperty("git.commit.id.describe");	       			
	       	}
	       	
		    String header = "";
	    	String footer = "\nVersion: " + version + "\nPlease report issues at https://github.com/yafred/asn1-tool/issues";
	    	HelpFormatter formatter = new HelpFormatter();
	    	formatter.printHelp( "java -jar asn1-tool.jar", header, options, footer, true );
	    	System.exit(0);
	    }
	    
    	validate(line.getOptionValue("f"));
    	if(line.hasOption("p")) {
    		new Asn1SpecificationWriter(System.out).visit(model);
    	}
    	
    	if(line.hasOption("jo")) {
    		com.yafred.asn1.generator.java.Options generatorOptions = new com.yafred.asn1.generator.java.Options();
           	Generator generator = new Generator();
           	generatorOptions.setOutputPath(line.getOptionValue("jo"));
        	if(line.hasOption("jp")) {
        		generatorOptions.setPackagePrefix(line.getOptionValue("jp"));
        	}
        	if(line.hasOption("jb")) {
        		generatorOptions.setBeautify(true);
        	}
        	else {
        		if(!hasJavaFormatter) {
        			generatorOptions.setBeautify(false);
        		}
        	}
        	generator.setOptions(generatorOptions);
          	generator.processSpecification(model);	
    	}
	}
	

	void validate(String resourceName) throws Exception {
		
		// Parse grammar
        CharStream charStream = CharStreams.fromFileName(resourceName);

        ASNLexer lexer = new ASNLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        ASNParser parser = new ASNParser(tokens);
        ParseTree tree = parser.specification();
        
        if(0 != parser.getNumberOfSyntaxErrors()) {
        	System.exit(1);
        }
        
        // Build model
        SpecificationAntlrVisitor visitor = new SpecificationAntlrVisitor();
        model = visitor.visit(tree);
                  
        // Validate model
        Asn1ModelValidator asn1ModelValidator = new Asn1ModelValidator();
        asn1ModelValidator.visit(model);
        for(String error : asn1ModelValidator.getWarningList()) {
        	System.out.println(error);
        }
        for(String error : asn1ModelValidator.getErrorList()) {
        	System.err.println(error);
        }
        
        if(0 != asn1ModelValidator.getErrorList().size()) {
        	System.exit(1);
        }
        

	}

}
