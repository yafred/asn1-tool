// https://github.com/junit-team/junit4/wiki/parameterized-tests

// Reflections: needs reflection and guava (see pom.xml)

package com.yafred.asn1.generator.java.test;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.util.Set;
import java.util.regex.Pattern;

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

import com.yafred.asn1.generator.java.Generator;
import com.yafred.asn1.generator.java.Options;
import com.yafred.asn1.grammar.ASNLexer;
import com.yafred.asn1.grammar.ASNParser;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.parser.Asn1ModelValidator;
import com.yafred.asn1.parser.SpecificationAntlrVisitor;

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
        
        SpecificationAntlrVisitor visitor = new SpecificationAntlrVisitor();
        Specification specification = visitor.visit(tree);
        
        Asn1ModelValidator asn1ModelValidator = new Asn1ModelValidator();
       	asn1ModelValidator.visit(specification);
       	
       	assertEquals(0, asn1ModelValidator.getErrorList().size());
       	
       	String ouputPath = System.getProperty("buildDirectory");
       	Options options = new Options();
       	options.setOutputDir(ouputPath + File.separator + "generated-test-sources");
       	options.setOverwriteAllowed(false);
       	new Generator().processSpecification(specification, options);	
	}
}
