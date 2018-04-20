package com.yafred.asn1;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.yafred.asn1.grammar.ASNLexer;
import com.yafred.asn1.grammar.ASNParser;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.parser.Asn1ModelValidator;
import com.yafred.asn1.parser.Asn1SpecificationWriter;
import com.yafred.asn1.parser.SpecificationAntlrVisitor;

public class Tool {
	
	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			System.err.println("Parse and validate ASN.1 specification ...");
			System.err.println("Input file needed.");
			System.exit(1);
		}
		
		new Tool().build(args[0]);
	}

	public void build(String resourceName) throws Exception {
		
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
        Specification specification = visitor.visit(tree);
                  
        // Validate model
        Asn1ModelValidator asn1ModelValidator = new Asn1ModelValidator();
        asn1ModelValidator.visit(specification);
        for(String error : asn1ModelValidator.getWarningList()) {
        	System.out.println(error);
        }
        for(String error : asn1ModelValidator.getErrorList()) {
        	System.err.println(error);
        }
        
        if(0 != asn1ModelValidator.getErrorList().size()) {
        	System.exit(1);
        }
        
		new Asn1SpecificationWriter(System.out).visit(specification);
	}

}
