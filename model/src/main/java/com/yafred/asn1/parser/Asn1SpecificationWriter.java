/*******************************************************************************
 * Copyright (C) 2021 Fred D7e (https://github.com/yafred)
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


import java.io.PrintStream;

import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.BitStringType;
import com.yafred.asn1.model.ChoiceValue;
import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.ComponentsOf;
import com.yafred.asn1.model.DefinitiveObjectIdComponent;
import com.yafred.asn1.model.EnumeratedType;
import com.yafred.asn1.model.IntegerType;
import com.yafred.asn1.model.ListOfType;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.ModuleIdentifier;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.NamedType;
import com.yafred.asn1.model.NamedValue;
import com.yafred.asn1.model.NamedValueListValue;
import com.yafred.asn1.model.SelectionType;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.SymbolsFromModule;
import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.TypeReference;
import com.yafred.asn1.model.TypeWithComponents;
import com.yafred.asn1.model.Value;
import com.yafred.asn1.model.ValueAssignment;
import com.yafred.asn1.model.ValueListValue;
import com.yafred.asn1.model.ValueRangeConstraint;

public class Asn1SpecificationWriter {
	static class IndentWriter {
		private int currentIndent = 0;
		private PrintStream printStream;
		
		public IndentWriter(PrintStream printStream) {
			this.printStream = printStream;
		}
		
		public void increaseIndent() {
			currentIndent++;
		}
		
		public void decreaseIndent() {
			currentIndent--;
		}
		
		void println() {
			printStream.println();
			for (int i = 0; i < currentIndent; i++) {
	    	   printStream.print("   ");
	        }
		}
		
		void println(String text) {
			printStream.print(text);
			println();
		}
		
		void print(String text) {
			printStream.print(text);
		}
	}
	
	private IndentWriter out = new IndentWriter(System.out);
	
	
	public Asn1SpecificationWriter(PrintStream printStream) {
		out = new IndentWriter(printStream);
	}

	public void visit(Specification specification) {
		for(ModuleDefinition moduleDefinition : specification.getModuleDefinitionList()) {
			visit(moduleDefinition);
			out.println();
		}
	}
	
	private void visit(ModuleDefinition moduleDefinition) {
		visit(moduleDefinition.getModuleIdentifier());
		out.print(" DEFINITIONS ");
		out.print(moduleDefinition.getTagDefault().toString() + " TAGS");
		if(moduleDefinition.isExtensibilityImplied()) {
			out.print(" EXTENSIBILITY IMPLIED");
		}
		out.println(" ::=");
		out.print("BEGIN");
		out.increaseIndent();
		out.println();
		out.println();
		
		out.print("EXPORTS");
		if(moduleDefinition.isExportAll()) {
			out.print(" ALL");
		}
		else {
			boolean isFirst = true;
			for(String symbol : moduleDefinition.getSymbolsExported()) {
				if(isFirst) {
					out.print(" " + symbol);
					isFirst = false;
				} 
				else {
					out.print(", " + symbol);
				}
			}
		}
		out.println(";");

		out.print("IMPORTS");
		if(moduleDefinition.getSymbolsImported() != null && moduleDefinition.getSymbolsImported().size() != 0) {
			out.increaseIndent();
			boolean isFirst = true;

			out.println();
			for(SymbolsFromModule symbolsFromModule : moduleDefinition.getSymbolsImported()) {
				if(isFirst == false) {
					out.println();
				}
				visit(symbolsFromModule);
				isFirst = false;
			}
			out.println();
			out.decreaseIndent();
		}
		out.println(";");
		
		for(Assignment assignment : moduleDefinition.getAssignmentList()) {
			out.println();
			if(assignment.isTypeAssignment()) {
				visit((TypeAssignment)assignment);
			}
			if(assignment.isValueAssignment()) {
				visit((ValueAssignment)assignment);
			}
			out.println();
		}
		
		out.decreaseIndent();
		out.println();	
		out.println("END");	
	}
	
	private void visit(ModuleIdentifier moduleIdentifier) {
		out.print(moduleIdentifier.getModuleReference());
		if(moduleIdentifier.getDefinitiveObjIdComponents() != null && moduleIdentifier.getDefinitiveObjIdComponents().size() != 0) {
			out.print(" { ");
			for(DefinitiveObjectIdComponent definitiveObjectIdComponent : moduleIdentifier.getDefinitiveObjIdComponents()) {
				if(definitiveObjectIdComponent.getName() != null) {
					out.print(definitiveObjectIdComponent.getName());
					if(definitiveObjectIdComponent.getNumber() != null) {
						out.print("(" + definitiveObjectIdComponent.getNumber() + ")");
					}
				}
				else if(definitiveObjectIdComponent.getNumber() != null) {
					out.print(definitiveObjectIdComponent.getNumber().toString());
				}
				out.print(" ");
			}
			out.print("}");
		}
	}
	
	private void visit(SymbolsFromModule symbolsFromModule) {
		boolean isFirst = true;
		for(String symbol : symbolsFromModule.getSymbolList()) {
			if(isFirst) {
				out.print(symbol);
				isFirst = false;
			} 
			else {
				out.print(", " + symbol);
			}
		}
		out.print(" FROM " + symbolsFromModule.getModuleReference().getName());
	}
	
	public void visit(TypeAssignment typeAssignment) {
		out.print(typeAssignment.getReference() + " ::= ");
		if(typeAssignment.getType() != null) {
			visitType(typeAssignment.getType());
		}
		else {
			out.print("Not-recognized");
		}
	}
	
	private void visitType(Type type) {
		if(type.getTagList() != null) {
			for(Tag tag : type.getTagList()) {
				out.print("[");
				if(tag.getTagClass() != null) {
					out.print(tag.getTagClass().toString());
					out.print(" ");
				}
				if(tag.getNumber() != null) {
					out.print(tag.getNumber() + "] ");					
				}
				else if(tag.getDefinedValue() != null) {
					out.print(tag.getDefinedValue() + "] ");					
				}
				if(tag.getTagMode() != null) {
					out.print(tag.getTagMode().toString());
					out.print(" ");
				}
			}
		}
		out.print(type.getName());
		
		if(type.isBitStringType()) {
			visit((BitStringType)type);
		}
		if(type.isEnumeratedType()) {
			visitEnumeratedType((EnumeratedType)type);
		}
		if(type.isIntegerType()) {
			visitIntegerType((IntegerType)type);
		}
		if(type.isTypeWithComponents()) {
			visit((TypeWithComponents)type);
		}
		if(type.isListOfType()) {
			out.print(" ");
			visitListOfType((ListOfType)type);
		}
		if(type.isSelectionType()) {
			visitSelectionType((SelectionType)type);
		}
	}
	
	private void visit(BitStringType bitStringType) {
		if(bitStringType.getNamedBitList() != null) {
			out.print(" { ");
			boolean isFirst = true;
			for(NamedNumber namedNumber : bitStringType.getNamedBitList()) {
				if(isFirst == false) {
					out.print(", ");
				}
				isFirst = false;
				visit(namedNumber);
			}
			out.print(" }");
		}
	}
	
	private void visitEnumeratedType(EnumeratedType enumeratedType) {
		if(enumeratedType.getRootEnumeration() != null) {
			out.print(" { ");
			boolean isFirst = true;
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				if(isFirst == false) {
					out.print(", ");
				}
				isFirst = false;
				visit(namedNumber);
			}
			if(enumeratedType.getAdditionalEnumeration() != null) {
				out.print(", ...");
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					out.print(", ");
					visit(namedNumber);
				}
			}
			out.print(" }");
		}
	}
	
	private void visit(NamedNumber namedNumber) {
		out.print(namedNumber.getName());
		if(namedNumber.getNumber() != null) {
			out.print("(" + namedNumber.getNumber() + ")");
		}
		else if(namedNumber.getReference() != null) {
			out.print("(" + namedNumber.getReference() + ")");
		}
	}
	
	private void visitIntegerType(IntegerType integerType) {
		if(integerType.getNamedNumberList() != null) {
			out.print(" { ");
			boolean isFirst = true;
			for(NamedNumber namedNumber : integerType.getNamedNumberList()) {
				if(isFirst == false) {
					out.print(", ");
				}
				isFirst = false;
				visit(namedNumber);
			}
			out.print(" }");
		}

		if(integerType.getConstraint() != null && integerType.getConstraint().getConstraintSpec() != null) {
			if(integerType.getConstraint().getConstraintSpec().isValueRangeConstraint()) {
				ValueRangeConstraint valueRangeConstraint = (ValueRangeConstraint)integerType.getConstraint().getConstraintSpec();
				out.print("(");
				if(valueRangeConstraint.getLowerEndValue() == null) {
					out.print("MIN");
				} else {
					out.print(valueRangeConstraint.getLowerEndValue().toString());
				}
				out.print("..");
				if(valueRangeConstraint.getUpperEndValue() == null) {
					out.print("MAX");
				} else {
					out.print(valueRangeConstraint.getUpperEndValue().toString());
				}
				out.print(")");
			}	
		}
	}
	
	private void visit(TypeWithComponents typeWithComponents) {
		int automaticTagCounter = 0;
		Integer automaticTagNumber = null;
		out.increaseIndent();
    	boolean isFirst = true;
    	out.println(" {");
    	if(typeWithComponents.getRootComponentList() != null && typeWithComponents.getRootComponentList().size() != 0) {
	    	for(Component component : typeWithComponents.getRootComponentList()) {
	    		if(isFirst == false) {
	    			out.println(",");
	    		}
	    		isFirst = false;
	    		if(typeWithComponents.isAutomaticTaggingSelected()) {
	    			automaticTagNumber = Integer.valueOf(automaticTagCounter);
	    			automaticTagCounter++;
	    		}
	    		visit(component, automaticTagNumber);
	    	}
    	}
    	if(typeWithComponents.getExtensionComponentList() != null) {
    		if(isFirst == false) {
    			out.println(",");
    		}
    		isFirst = false;
    		out.print("...");
	    	for(Component component : typeWithComponents.getExtensionComponentList()) {
	    		out.println(",");
	    		if(typeWithComponents.isAutomaticTaggingSelected()) {
	    			automaticTagNumber = Integer.valueOf(automaticTagCounter);
	    			automaticTagCounter++;
	    		}
	    		visit(component, automaticTagNumber);
	    	}
    	}
    	if(typeWithComponents.getAdditionalComponentList() != null) {
    		out.println(",");
    		out.print("...");
	    	for(Component component : typeWithComponents.getAdditionalComponentList()) {
	    		out.println(",");
	    		if(typeWithComponents.isAutomaticTaggingSelected()) {
	    			automaticTagNumber = Integer.valueOf(automaticTagCounter);
	    			automaticTagCounter++;
	    		}
	    		visit(component, automaticTagNumber);
	    	}
    	}
    	out.decreaseIndent();
    	out.println();
    	out.print("}");
	}
	
	private void visitListOfType(ListOfType listOfType) {
    	if(listOfType.getElement().getName() != null) {
    		out.print(listOfType.getElement().getName() + " ");
    	}
		visitType(listOfType.getElement().getType());
	}
	
	private void visit(Component component, Integer automaticTagNumber) {
		if(component.isComponentsOf()) {
			out.print("COMPONENTS OF ");
			visitType(((ComponentsOf)component).getForeignContainer());
		}
		if(component.isNamedType()) {
			NamedType namedType = (NamedType)component;
			out.print(namedType.getName() + " ");
			if(automaticTagNumber != null) {
				out.print("[" + automaticTagNumber + "] ");
				if(namedType.getType() != null && namedType.getType().isChoiceType() // component is not tagged (no need to test that)
						|| namedType.getType() != null && namedType.getType().isTypeReference() && ((TypeReference)namedType.getType()).getBuiltinType().isChoiceType() && namedType.getType().getFirstTag() == null ) {
					out.print("EXPLICIT ");					
				}
				else {
					out.print("IMPLICIT ");
				}
			}
			visitType(namedType.getType());
			if(namedType.isOptional()) {
				out.print(" OPTIONAL");
			}
		}
	}
	
	private void visitSelectionType(SelectionType selectionType) {
		out.print(selectionType.getSelection() + " < " );
		visitType(selectionType.getType());
	}
		
	private void visit(ValueAssignment valueAssignment) {
		out.print(valueAssignment.getReference() + " ");
		if(valueAssignment.getValue() != null) {
			visitType(valueAssignment.getValue().getType()); 
			out.print(" ::= ");
			visit(valueAssignment.getValue());
		}
		else {
			out.print("Not-recognized ::= not-recognized");
		}
	}
	
	private void visit(Value value) {
		if(value.isChoiceValue()) {
			visit((ChoiceValue)value);
		}
		else if(value.isNamedValueList()) {
			visit((NamedValueListValue)value);
		}
		else if(value.isValueList()) {
			visit((ValueListValue)value);
		}
		else {
			out.print(value.toString());
		}
	}
	
	private void visit(ChoiceValue choiceValue) {
		out.print(choiceValue.getIdentifier() + " : ");
		visit(choiceValue.getValue());
	}
	
	private void visit(NamedValueListValue namedValueListValue) {
		out.print("{ ");
		boolean isFirst = true;
		for(NamedValue namedValue : namedValueListValue.getValueList()) {
			if(isFirst == false) {
				out.print(", ");
			}
			isFirst = false;
			visit(namedValue);
		}
		out.print(" }");
	}
	
	private void visit(NamedValue namedValue) {
		out.print(namedValue.getIdentifier() + " ");
		visit(namedValue.getValue());
	}
	
	private void visit(ValueListValue valueListValue) {
		out.print("{ ");
		boolean isFirst = true;
		for(Value value : valueListValue.getValueList()) {
			if(isFirst == false) {
				out.print(", ");
			}
			isFirst = false;
			visit(value);
		}
		out.print(" }");
	}
}
