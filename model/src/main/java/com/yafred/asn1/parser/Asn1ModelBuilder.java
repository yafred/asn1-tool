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
package com.yafred.asn1.parser;

import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import com.yafred.asn1.grammar.ASNLexer;
import com.yafred.asn1.grammar.ASNParser;
import com.yafred.asn1.model.Specification;

public class Asn1ModelBuilder {
	
	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			System.err.println("Needs an argument (path to a file in the filesystem or resource in the classpath). "
					+ "For example: com/yafred/asn1/test/unit/a_000.asn");
			System.exit(1);
		}
		
		new Asn1ModelBuilder().build(args[0]);
	}

	public void build(String resourceName) throws IOException {
		
        System.out.println("=================  BUILD ====================================================================");
		System.out.println(resourceName);

		// load test data (needs parser/target/test-classes in path
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(resourceName);

        CharStream charStream = null;
        if (inStream != null) {
        	charStream = CharStreams.fromStream(inStream);
        }
        else {
        	charStream = CharStreams.fromFileName(resourceName);
        }

        ASNLexer lexer = new ASNLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        ASNParser parser = new ASNParser(tokens);
        ParseTree tree = parser.specification();
        
        SpecificationAntlrVisitor visitor = new SpecificationAntlrVisitor();
        Specification specification = visitor.visit(tree);
        
        if(inStream != null) {
	        inStream.close();
	        inStream = getClass().getClassLoader().getResourceAsStream(resourceName);  // reload
	
	        System.out.println("-----------------  TEST DATA  ---------------------------------------------------------------");
	        
	        System.out.println(convertStreamToString(inStream));
        }
        
        System.out.println("-----------------  DUMP MODEL ---------------------------------------------------------------");
          
        new Asn1SpecificationWriter(System.out).visit(specification);

        System.out.println("-----------------  VALIDATE MODEL -----------------------------------------------------------");
        
        Asn1ModelValidator asn1ModelValidator = new Asn1ModelValidator();
        asn1ModelValidator.visit(specification);
        for(String error : asn1ModelValidator.getWarningList()) {
        	System.out.println(error);
        }
        for(String error : asn1ModelValidator.getErrorList()) {
        	System.err.println(error);
        }
        
        System.out.println("-----------------  VALIDATED MODEL INFO -----------------------------------------------------");
        asn1ModelValidator.dump();
        
        System.out.println("-----------------  DUMP MODEL AGAIN ---------------------------------------------------------");
        
        new Asn1SpecificationWriter(System.out).visit(specification);

	}

	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is);
	    s.useDelimiter("\\A");
	    String ret = s.hasNext() ? s.next() : "";
	    s.close();
	    return ret;
	}

}
