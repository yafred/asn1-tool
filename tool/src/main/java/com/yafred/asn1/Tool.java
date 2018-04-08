package com.yafred.asn1;

import java.io.IOException;
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
	
	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			System.err.println("Input file needed.");
			System.exit(1);
		}
		
		new Tool().build(args[0]);
	}

	public void build(String resourceName) throws IOException {
		
        CharStream charStream = CharStreams.fromFileName(resourceName);

        ASNLexer lexer = new ASNLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        ASNParser parser = new ASNParser(tokens);
        ParseTree tree = parser.specification();
        
        SpecificationAntlrVisitor visitor = new SpecificationAntlrVisitor();
        Specification specification = visitor.visit(tree);
        
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

}
