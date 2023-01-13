/*******************************************************************************
 * Copyright (C) 2023 Fred D7e (https://github.com/yafred)
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
// https://github.com/junit-team/junit4/wiki/parameterized-tests

// Reflections: needs reflection and guava (see pom.xml)

package com.yafred.asn1.generator.go.test;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Set;
import java.util.regex.Pattern;

import com.yafred.asn1.generator.go.Generator;
import com.yafred.asn1.generator.go.Options;
import com.yafred.asn1.grammar.ASNLexer;
import com.yafred.asn1.grammar.ASNParser;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.parser.Asn1ModelValidator;
import com.yafred.asn1.parser.SpecificationAntlrVisitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

@RunWith(Parameterized.class)
public class ParameterizedTest {
	@Parameters(name = "{0}")
	public static Iterable<? extends Object> data() {

		Reflections reflections = new Reflections("com.yafred.asn1.test.generator", new ResourcesScanner());
		Set<String> properties = reflections.getResources(Pattern.compile(".*\\.asn"));
		return properties;
	}

	String resourceName;
	
	public ParameterizedTest(String resourceName) {
		this.resourceName = resourceName;
	}
	
	@Test
    public void generateCode() throws Exception {
    	// load test data
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(resourceName);

        System.out.println(resourceName);
        
        if (inStream == null) {
            throw new RuntimeException("Resource not found: " + resourceName);
        }

        // create a CharStream that reads from standard input
        CharStream input = CharStreams.fromStream(inStream);
        
        // create a lexer that feeds off of input CharStream
        ASNLexer lexer = new ASNLexer(input);
        // create a buffer of tokens pulled from the lexer
        TokenStream tokens = new CommonTokenStream(lexer);
        // create a parser that feeds off the tokens buffer
        ASNParser parser = new ASNParser(tokens);
        ParseTree tree = parser.specification(); // begin parsing at specification rule
        inStream.close();
       
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        
        // create model
        SpecificationAntlrVisitor visitor = new SpecificationAntlrVisitor();
        Specification specification = visitor.visit(tree);
        
        // validate model
        Asn1ModelValidator asn1ModelValidator = new Asn1ModelValidator();
       	asn1ModelValidator.visit(specification);
       	
       	assertEquals(0, asn1ModelValidator.getErrorList().size());
       	
       	// create output dir
       	String outputPath = System.getProperty("buildDirectory") + File.separator + "generator-output" + File.separator + "go";
       	File outputPathFile = new File(outputPath);
       	outputPathFile.mkdirs();
        	
		// create module
		PrintWriter fileWriter = new PrintWriter(new FileWriter(new File(outputPathFile, "go.mod")));

		fileWriter.println("module generated-code\r\n" +
		"require github.com/yafred/asn1-go v0.0.5\r\n" +
		"go 1.17");
		
		fileWriter.close();
		  
       	// generate code
       	Generator generator = new Generator();
       	Options options = new Options();
       	options.setOutputPath(outputPath);
       	generator.setOptions(options);
       	generator.processSpecification(specification);		   
	}
}
