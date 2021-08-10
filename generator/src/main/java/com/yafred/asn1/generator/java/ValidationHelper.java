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
package com.yafred.asn1.generator.java;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.constraint.ValueRange;
import com.yafred.asn1.model.type.ChoiceType;
import com.yafred.asn1.model.type.ListOfType;
import com.yafred.asn1.model.type.NamedType;
import com.yafred.asn1.model.type.SequenceType;
import com.yafred.asn1.model.type.SetType;
import com.yafred.asn1.model.type.TypeReference;

public class ValidationHelper {
	Generator generator;
	File packageDirectory;
	PrintWriter output;

	
	ValidationHelper(Generator generator) {
		this.generator = generator;
	}
	

	void processType(Type type, String className, boolean isInnerType) throws Exception {
		this.output = generator.output; // for now, write encoding/decoding methods in the POJO class
		      
		output.println("public static void validate(" + className + " instance) throws Exception {");

		// switch
		if (type.isTypeReference()) {
			// nothing
		} 
		else if (!type.isTypeWithComponents() && !type.isListOfType()) {
			processComponent(type, "value", className);
		}
		else if (type.isSequenceType()) {
			processSequenceTypeAssignment((SequenceType)type, className);
		}
		else if (type.isSetType()) {
			processSetTypeAssignment((SetType)type, className);
		}
		else if (type.isListOfType()) {
			processListOfTypeAssignment((ListOfType)type, className);
		}
		else if (type.isChoiceType()) {
			processChoiceTypeAssignment((ChoiceType)type, className);
		}

		output.println("}");
	}
	
	
	private void processSequenceTypeAssignment(SequenceType sequenceType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, sequenceType.getRootComponentList());
		Utils.addAllIfNotNull(componentList, sequenceType.getExtensionComponentList());
		Utils.addAllIfNotNull(componentList, sequenceType.getAdditionalComponentList());
		
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			processComponent(namedType.getType(), componentName, componentClassName);
		}
	}


	private void processSetTypeAssignment(SetType setType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, setType.getRootComponentList());
		Utils.addAllIfNotNull(componentList, setType.getExtensionComponentList());
		Utils.addAllIfNotNull(componentList, setType.getAdditionalComponentList());
		
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			processComponent(namedType.getType(), componentName, componentClassName);
		}
	}


	private void processListOfTypeAssignment(ListOfType listOfType, String className) throws Exception {
		Type elementType = listOfType.getElement().getType();
		String elementClassName = "";
		if(listOfType.getElement().getType().isTypeReference()) {
			elementClassName = Utils.normalizeJavaType((TypeReference)listOfType.getElement().getType(), generator.options.getPackagePrefix());
			elementType = ((TypeReference)listOfType.getElement().getType()).getBuiltinType();
		}
		else if(Utils.isConstructed(listOfType.getElement().getType())) {
			elementClassName = "Item";
			if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
				elementClassName = Utils.uNormalize(listOfType.getElement().getName());
			}
		}		

		output.println("if(instance.getValue() != null) {");
		output.println("for(int i=0; i<instance.getValue().size(); i++) {");
		
		processListElement(elementType, elementClassName, "value");

		output.println("}");
		output.println("}");
	}


	private void processChoiceTypeAssignment(ChoiceType choiceType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, choiceType.getRootAlternativeList());
		Utils.addAllIfNotNull(componentList, choiceType.getAdditionalAlternativeList());
		
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			output.println("if(" + componentGetter + "!=null){");
			processComponent(namedType.getType(), componentName, componentClassName);
			output.println("}");
		}
	}

	
	private void processComponent(Type type, String componentName, String componentClassName) throws Exception {
		String referencedClassName = "";
		Type builtinType = type;
		if(builtinType.isTypeReference()) {
			referencedClassName = Utils.normalizeJavaType((TypeReference) builtinType, generator.options.getPackagePrefix());
			builtinType = ((TypeReference)builtinType).getBuiltinType();
		}
		
		String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";

		if(builtinType.isRestrictedCharacterStringType()) {
		}
		else if(builtinType.isIntegerType()) {
			if(builtinType.getConstraint() != null && builtinType.getConstraint().getConstraintElement() != null && builtinType.getConstraint().getConstraintElement().isValueRange()) {
				ValueRange valueRangeConstraint = (ValueRange)builtinType.getConstraint().getConstraintElement();
				if(valueRangeConstraint.getLowerEndValue() != null) {
					output.println("if(" + componentGetter + "<" + valueRangeConstraint.getLowerEndValue() + "){");
					output.println("throw new Exception();");
					output.println("}");
				}
				if(valueRangeConstraint.getUpperEndValue() != null) {
					output.println("if(" + componentGetter + ">" + valueRangeConstraint.getUpperEndValue() + "){");
					output.println("throw new Exception();");
					output.println("}");
				}
			}
		}
		else if(builtinType.isBooleanType()) {
		}	
		else if(builtinType.isBitStringType()) {
		}
		else if(builtinType.isOctetStringType()) {
		}
		else if(builtinType.isObjectIdentifierType()) {
		}
		else if(builtinType.isRelativeOIDType()) {
		}
		else if(builtinType.isNullType()) {
		}
		else if(builtinType.isEnumeratedType()) {
		}
		else if(type.isTypeReference()) {
			output.println(referencedClassName + ".validate(" + componentGetter + ");");		
		}
		else if(type.isTypeWithComponents()) {
			output.println(Utils.uNormalize(componentName) + ".validate(" + componentGetter + ");");		
		}
		else if(type.isListOfType()) {
			ListOfType listOfType = (ListOfType)type;
			Type elementType = listOfType.getElement().getType();
			String elementClassName = "";
			if(listOfType.getElement().getType().isTypeReference()) {
				elementClassName = Utils.uNormalize(listOfType.getElement().getType().getName());
				elementType = ((TypeReference)listOfType.getElement().getType()).getBuiltinType();
			}
			else if(listOfType.getElement().getType().isTypeWithComponents() || listOfType.getElement().getType().isListOfType()) {
				elementClassName = "Item";
				if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
					elementClassName = Utils.uNormalize(listOfType.getElement().getName());
				}
			}
			output.println("if(" + componentGetter + " != null) {");
			output.println("for(int i=0;i<" + componentGetter + ".size(); i++) {");
			processListElement(elementType, elementClassName, componentName);
			output.println("}");
			output.println("}");
		}
		else {
			throw new Exception("ValidationHelper.processComponent: Code generation not supported for Type " + type.getName());
		}
	}
	
	
	private void processListElement(Type elementType, String elementClassName, String componentName) throws Exception {
		String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";
	
		if(elementType.isRestrictedCharacterStringType()) {
		}
		else if(elementType.isIntegerType()) {
			if(elementType.getConstraint() != null && elementType.getConstraint().getConstraintElement() != null && elementType.getConstraint().getConstraintElement().isValueRange()) {
				ValueRange valueRangeConstraint = (ValueRange)elementType.getConstraint().getConstraintElement();
				if(valueRangeConstraint.getLowerEndValue() != null) {
					output.println("if(" + componentGetter + ".get(i) <" + valueRangeConstraint.getLowerEndValue() + "){");
					output.println("throw new Exception();");
					output.println("}");
				}
				if(valueRangeConstraint.getUpperEndValue() != null) {
					output.println("if(" + componentGetter + ".get(i) >" + valueRangeConstraint.getUpperEndValue() + "){");
					output.println("throw new Exception();");
					output.println("}");
				}
			}
		}
		else if(elementType.isBooleanType()) {
		}
		else if(elementType.isBitStringType()) {
		}
		else if(elementType.isOctetStringType()) {
		}
		else if(elementType.isObjectIdentifierType()) {
		}
		else if(elementType.isRelativeOIDType()) {
		}
		else if(elementType.isNullType()) {
		}
		else if(elementType.isEnumeratedType()) {
		}
		else if(Utils.isConstructed(elementType)) {
			output.println(elementClassName + ".validate(" + componentGetter + ".get(i));");						
		}
		else {
			throw new Exception("ValidationHelper.processListElement: Code generation not supported for Type " + elementType.getName());
		}
	}

}
