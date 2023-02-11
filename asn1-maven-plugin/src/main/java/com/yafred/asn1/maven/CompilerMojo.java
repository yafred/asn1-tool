/*******************************************************************************
 * Copyright (C) 2023 Fred D7e (https://github.com/yafred)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package com.yafred.asn1.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.yafred.asn1.generator.java.Generator;
import com.yafred.asn1.generator.java.Options;
import com.yafred.asn1.grammar.ASNLexer;
import com.yafred.asn1.grammar.ASNParser;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.parser.Asn1ModelValidator;
import com.yafred.asn1.parser.Asn1TypeLabeller;
import com.yafred.asn1.parser.SpecificationAntlrVisitor;

/**
 * Parses ASN.1 files {@code *.asn} and transforms them into Java source files.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.COMPILE)
public class CompilerMojo extends AbstractMojo {

    /**
     * The location of the file that contains the ASN.1 specification (only one file at the moment).
     */
    @Parameter(property = "compile.inputFile")
    private File inputFile;

    /**
     * Specify output directory where the Java files are generated.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/asn")
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException {
        if (!inputFile.isFile()) {
            getLog().info("No such file: "+ inputFile.getAbsolutePath());
            return;
        }

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        try {
            InputStream inputStream = new FileInputStream(inputFile);

            CharStream input = CharStreams.fromStream(inputStream);

            // create a lexer that feeds off of input CharStream
            ASNLexer lexer = new ASNLexer(input);
            // create a buffer of tokens pulled from the lexer
            TokenStream tokens = new CommonTokenStream(lexer);
            // create a parser that feeds off the tokens buffer
            ASNParser parser = new ASNParser(tokens);
            ParseTree tree = parser.specification(); // begin parsing at specification rule
            inputStream.close();

		// create model
		SpecificationAntlrVisitor visitor = new SpecificationAntlrVisitor();
		Specification specification = visitor.visit(tree);

		// validate model
		Asn1ModelValidator asn1ModelValidator = new Asn1ModelValidator();
		asn1ModelValidator.visit(specification);

		// attach labels to types
		new Asn1TypeLabeller(false).visit(specification);

		if(0 != asn1ModelValidator.getErrorList().size()) {
            getLog().info("Errors in ASN.1 specification.");
            return;
        }

		// generate code
		Generator generator = new Generator();
		Options options = new Options();
		options.setOutputPath(outputDirectory.getAbsolutePath());
        getLog().info("Generating Java code in " + outputDirectory.getAbsolutePath());
		generator.setOptions(options);
		generator.processSpecification(specification);

        } catch (Exception e) {

        } 
    }
}
