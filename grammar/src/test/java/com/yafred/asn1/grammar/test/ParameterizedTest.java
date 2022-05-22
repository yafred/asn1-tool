/*******************************************************************************
 * Copyright (C) 2022 Fred D7e (https://github.com/yafred)
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

package com.yafred.asn1.grammar.test;


import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenStream;
import com.yafred.asn1.grammar.ASNLexer;
import com.yafred.asn1.grammar.ASNParser;

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

		Reflections reflections = new Reflections("com.yafred.asn1.test", new ResourcesScanner());
		Set<String> properties = reflections.getResources(Pattern.compile(".*\\.asn"));
		return properties;
	}

	String resourceName;
	
	public ParameterizedTest(String resourceName) {
		this.resourceName = resourceName;
	}
	
	@Test
    public void parseResource() throws Exception {
    	
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
        parser.removeErrorListeners(); // remove ConsoleErrorListener
        parser.addErrorListener(new VerboseListener()); // add ours
        parser.specification(); // begin parsing at specification rule
        
        assertEquals(0, parser.getNumberOfSyntaxErrors());
    }
	
	
	public static class VerboseListener extends BaseErrorListener {
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			List<String> stack = ((ASNParser) recognizer).getRuleInvocationStack();
			Collections.reverse(stack);
			System.err.println("rule stack: " + stack);
			System.err.println("line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg);
		}
	}
}
