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

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.ComponentsOf;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.SymbolsFromModule;
import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.TagClass;
import com.yafred.asn1.model.TagDefault;
import com.yafred.asn1.model.TagMode;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.TypeWithComponents;
import com.yafred.asn1.model.Value;
import com.yafred.asn1.model.ValueAssignment;
import com.yafred.asn1.model.ValueReference;
import com.yafred.asn1.model.type.BitStringType;
import com.yafred.asn1.model.type.ChoiceType;
import com.yafred.asn1.model.type.EnumeratedType;
import com.yafred.asn1.model.type.IntegerType;
import com.yafred.asn1.model.type.ListOfType;
import com.yafred.asn1.model.type.NamedType;
import com.yafred.asn1.model.type.SelectionType;
import com.yafred.asn1.model.type.SequenceType;
import com.yafred.asn1.model.type.SetType;
import com.yafred.asn1.model.type.TypeReference;
import com.yafred.asn1.model.value.IntegerValue;

public class Asn1ModelValidator {
	private ArrayList<String> errorList = new ArrayList<String>();
	private ArrayList<String> warningList = new ArrayList<String>();
	private Map<String, ModuleDefinition> moduleDefinitionMap = new HashMap<String, ModuleDefinition>();  // Key: moduleReference
	private Map<String, Assignment> assignmentMap = new HashMap<String, Assignment>();  // Key: modulereference.typereference or modulereference.valuereference
	private Map<String, Assignment> symbolsExportedMap = new HashMap<String, Assignment>();  // Key: modulereference.typereference or modulereference.valuereference

		
	public void dump() {
        if(assignmentMap != null) {
	        for (Map.Entry<String, Assignment> entry : assignmentMap.entrySet()) {
				Assignment assignment = entry.getValue();
				if(assignment.isTypeAssignment()) {
					Type type = ((TypeAssignment)assignment).getType();
					if(type.isTypeReference()) {
						Type builtinType = ((TypeReference)type).getBuiltinType();
						if(builtinType != null) {
							System.out.println(entry.getKey() + ": " + builtinType.getName());
						}
						else {
							System.out.println(entry.getKey() + ": UNKNOWN TYPE");
						}
					}
				}
				if(assignment.isValueAssignment()) {
					Value value = ((ValueAssignment)assignment).getValue();
					Type type = value.getType();
					System.out.println(entry.getKey() + " has type " + type.getName());
					if(type.isTypeReference()) {
						Type builtinType = ((TypeReference)type).getBuiltinType();
						if(builtinType != null) {
							System.out.println("(eventually a " + builtinType.getName() +")");
						}
						else {
							System.out.println(" (eventually an UNKNOWN TYPE)");
						}
					}
					if(value.isValueReference()) {
						Value referencedValue = value;
						do {
							referencedValue = ((ValueReference)referencedValue).getReferencedValue();
						}
						while(referencedValue != null && referencedValue.isValueReference());
						System.out.println(entry.getKey() + " value is " + ( referencedValue != null ? referencedValue : "UNKNOWN" ));
					}
				}
			}
	        System.out.println();
        }

	}

	public ArrayList<String> getErrorList() {
		return errorList;
	}
	
	public ArrayList<String> getWarningList() {
		return warningList;
	}

	public void visit(Specification specification) {
		
        /*
         *  Check that every module has a distinct name
         */
		for(ModuleDefinition moduleDefinition : specification.getModuleDefinitionList()) {
			String reference = moduleDefinition.getModuleIdentifier().getModuleReference();
			ModuleDefinition alreadyDefined = moduleDefinitionMap.get(reference);
			if(alreadyDefined != null) {
				errorList.add(moduleDefinition.getModuleIdentifier().getModuleReferenceTokenLocation() + ": Trying to redefine '" + reference + "' already defined in " + alreadyDefined.getModuleIdentifier().getModuleReferenceTokenLocation());
			}
			else {
				moduleDefinitionMap.put(reference, moduleDefinition);
			}
		}
		
		/*
		 * Check that every assignment has a distinct name (per module)
		 */
		for (Map.Entry<String, ModuleDefinition> entry : moduleDefinitionMap.entrySet()) {
			
			ModuleDefinition moduleDefinition = entry.getValue();
			String moduleReference = moduleDefinition.getModuleIdentifier().getModuleReference();
			for(Assignment assignment : moduleDefinition.getAssignmentList()) {
				Assignment alreadyDefined = assignmentMap.get(moduleReference + "." + assignment.getReference());
				if(alreadyDefined != null) {
					errorList.add(assignment.getReferenceTokenLocation() + ": Trying to redefine '" + assignment.getReference() + "' already defined in " + alreadyDefined.getReferenceTokenLocation());					
				}
				else {
					assignmentMap.put(moduleReference + "." + assignment.getReference(), assignment);
				}
			}
		}
		
		/*
		 * Populate symbolsExportedMap
		 * Check exported symbols are defined
		 */
		for (Map.Entry<String, ModuleDefinition> entry : moduleDefinitionMap.entrySet()) {
			
			ModuleDefinition moduleDefinition = entry.getValue();
			String moduleReference = moduleDefinition.getModuleIdentifier().getModuleReference();
			if(moduleDefinition.isExportAll() == false) {
				for(String symbolExported : moduleDefinition.getSymbolsExported()) {
					Assignment assignment = assignmentMap.get(moduleReference + "." + symbolExported);
					if(!assignmentMap.containsKey(moduleReference + "." + symbolExported)) {
						errorList.add("Symbol '" + symbolExported + "' exported but not defined in module '" + moduleReference + "'");
					}
					else {
						symbolsExportedMap.put(moduleReference + "." + symbolExported, assignment);
					}
				}
			}
			else {
				for(Assignment assignment : moduleDefinition.getAssignmentList()) {
					symbolsExportedMap.put(moduleReference + "." + assignment.getReference(), assignment);
				}				
			}	
		}
		
		/*
		 * Check imported symbols are defined
		 * Check imported symbols do not redefine an existing assignment
		 */
		for (Map.Entry<String, ModuleDefinition> entry : moduleDefinitionMap.entrySet()) {
			
			ModuleDefinition moduleDefinition = entry.getValue();
			String moduleReference = moduleDefinition.getModuleIdentifier().getModuleReference();
			if(moduleDefinition.getSymbolsImported() != null) {
				for(SymbolsFromModule symbolsFromModule : moduleDefinition.getSymbolsImported()) {
					String fromModuleReference = symbolsFromModule.getModuleReference().getName();
					if(fromModuleReference.equals(moduleReference)) {
						errorList.add("Module '" + fromModuleReference + "' is importing symbols from itself");
						continue;
					}
					for(String symbol : symbolsFromModule.getSymbolList()) {
						if(!symbolsExportedMap.containsKey(fromModuleReference + "." + symbol)) {
							if(moduleDefinitionMap.containsKey(fromModuleReference)) {
								errorList.add("Symbol '" + symbol + "' is imported in module '" + moduleReference + "' but not exported in module '" +  fromModuleReference + "'");
							}
							else {
								errorList.add("Symbol '" + symbol + "' is imported in module '" + moduleReference + "' from an undefined module '" +  fromModuleReference + "'");
							}
						}
						else {
							Assignment assignment = assignmentMap.get(moduleReference + "." + symbol);
							if(assignment != null) {
								errorList.add("Symbol '" + symbol + "' is imported in module '" + moduleReference + "' but already defined at position " + assignment.getReferenceTokenLocation());
							}
						}
					}
				}
			}
		} 
		
		/*
		 * Visit assignments
		 */
		for (Map.Entry<String, ModuleDefinition> entry : moduleDefinitionMap.entrySet()) {			
			ModuleDefinition moduleDefinition = entry.getValue();
			for(Assignment assignment : moduleDefinition.getAssignmentList()) {
				if(assignment.isTypeAssignment()) {				
					TypeAssignment typeAssignment = (TypeAssignment)assignment;
					visitType(typeAssignment.getType(), moduleDefinition);
				}
				if(assignment.isValueAssignment()) {				
					ValueAssignment valueAssignment = (ValueAssignment)assignment;
					visit(valueAssignment.getValue(), moduleDefinition);
				}
			}
		}
	}
	
	/*
	 * Visit type
	 */
	void visitType(Type type, ModuleDefinition moduleDefinition) {
		
		if(type.isTypeReference()) {
			visitTypeReference((TypeReference)type, moduleDefinition);
		}
		if(type.isIntegerType()) {
			IntegerType integerType = (IntegerType)type;
			visit(integerType.getNamedNumberList(), moduleDefinition);
		}
		if(type.isEnumeratedType()) {
			EnumeratedType enumeratedType = (EnumeratedType)type;
			visitEnumeratedType(enumeratedType, moduleDefinition);
		}
		if(type.isBitStringType()) {
			BitStringType bitStringType = (BitStringType)type;
			visit(bitStringType.getNamedBitList(), moduleDefinition);
		}
		if(type.isSequenceType()) {
			SequenceType sequenceType = (SequenceType)type;
			visitSequenceType(sequenceType, moduleDefinition);
		}
		if(type.isListOfType()) {
			ListOfType listOfType = (ListOfType)type;
			visitType(listOfType.getElement().getType(), moduleDefinition);
		}
		if(type.isSetType()) {
			SetType setType = (SetType)type;
			visitSetType(setType, moduleDefinition);
		}
		if(type.isChoiceType()) {
			ChoiceType choiceType = (ChoiceType)type;
			visitChoiceType(choiceType, moduleDefinition);
		}
		if(type.isSelectionType()) {
			SelectionType selectionType = (SelectionType)type;
			visitSelectionType(selectionType, moduleDefinition);
		}
		
		// Check that no UNIVERSAL tag is found
		// Replace TagMode with tagging default when TagMode is absent (we do it now, after visiting ChoiceType and TypeReference)
		ArrayList<Tag> tagList = type.getTagList();
		if(tagList != null) {
			for(Tag tag : tagList) {
				if(tag.getClass() != null && TagClass.UNIVERSAL_TAG == tag.getTagClass()) {
					errorList.add(tag.getTagTokenLocation() + ": Use of UNIVERSAL is reserved for ASN.1 builtin types");
				}
				if(tag.getDefinedValue() != null) {
					errorList.add(tag.getTagTokenLocation() + ": defined values in tags are not supported yet");					
				}
				if(tag.getTagMode() == null) {
					if(moduleDefinition.getTagDefault() == null || moduleDefinition.getTagDefault() == TagDefault.EXPLICIT_TAGS) {
						tag.setTagMode(TagMode.EXPLICIT_TAG);
					}
					else {
						tag.setTagMode(TagMode.IMPLICIT_TAG);
					}
				}
			}
		}

	}
	
	/*
	 * Visit type reference
	 * Check that referenced type is defined (either in the same module or in another module if imported)
	 * Link TypeReference object to defining Type
	 */
	void visitTypeReference(TypeReference typeReference, ModuleDefinition moduleDefinition) {
		if(typeReference.getReferencedType() != null) {
			// already visited
			return;
		}
		TypeAssignment referencedTypeAssignment = null;
		String referencedTypeName = typeReference.getReferencedTypeName();
		String referencedModuleName = typeReference.getReferencedModuleName();
		if(referencedModuleName != null) {
			// Look for an external reference
			referencedTypeAssignment = (TypeAssignment)symbolsExportedMap.get(referencedModuleName + "." + referencedTypeName);
			if(referencedTypeAssignment == null) {
				if(!assignmentMap.containsKey(referencedModuleName + "." + referencedTypeName)) {
					errorList.add(typeReference.getTokenLocation() + " Type '" + referencedModuleName + "." + referencedTypeName + "' not defined");
				}
				else {
					errorList.add(typeReference.getTokenLocation() + " Type '" + referencedModuleName + "." + referencedTypeName + "' not exported");
				}
			}
		}
		else {
			// Look for a type defined in the same module or an imported symbol
			String moduleReference = moduleDefinition.getModuleIdentifier().getModuleReference();
			referencedTypeAssignment = (TypeAssignment)assignmentMap.get(moduleReference + "." + referencedTypeName);
			if(referencedTypeAssignment == null) {
				if(moduleDefinition.getSymbolsImported() != null) {
					for(SymbolsFromModule symbolsFromModule : moduleDefinition.getSymbolsImported()) {
						for(String symbol : symbolsFromModule.getSymbolList()) {
							if(symbol.equals(referencedTypeName)) {
								referencedTypeAssignment = (TypeAssignment)assignmentMap.get(symbolsFromModule.getModuleReference().getName() + "." + referencedTypeName);
								typeReference.setReferencedModuleName(symbolsFromModule.getModuleReference().getName());
								break;
							}
						}
					}
				}
			}
		}
		if(referencedTypeAssignment != null) {
			typeReference.setReferencedType(referencedTypeAssignment.getType());
			if(referencedTypeAssignment.getType().isTypeReference()) {
				visitTypeReference((TypeReference)referencedTypeAssignment.getType(), moduleDefinition);
			}
		}
		else {
			errorList.add(typeReference.getTokenLocation() + " Type '" + referencedTypeName + "' neither defined nor imported");
		}

		// catch CHOICE with IMPLICIT tag here
		if(typeReference.getBuiltinType() != null && typeReference.getBuiltinType().isChoiceType()) {
			ArrayList<Tag> fullTagList = typeReference.getFullTagList();
			if(fullTagList != null && fullTagList.size() != 0) {
				Tag choiceTag = fullTagList.get(fullTagList.size()-1);
				if(choiceTag.getTagMode() == TagMode.IMPLICIT_TAG) {
					errorList.add(choiceTag.getTagTokenLocation() + " IMPLICIT TAG not allowed for CHOICE");
				}
				else {
					choiceTag.setTagMode(TagMode.EXPLICIT_TAG);
				}
			}
		}
	}
	
	/*
	 * Visit value
	 */
	void visit(Value value, ModuleDefinition moduleDefinition) {
		visitType(value.getType(), moduleDefinition);
		if(value.isValueReference()) {
			visit((ValueReference)value, moduleDefinition);
		}
	}

	/*
	 * Visit value reference
	 * Check that referenced value is defined (either in the same module or in another module if imported or as a named number
	 */
	void visit(ValueReference valueReference, ModuleDefinition moduleDefinition) {
		ValueAssignment referencedValueAssignment = null;
		String referencedValueName = valueReference.getReferencedValueName();
		String referencedModuleName = valueReference.getReferencedModuleName();
		if(referencedModuleName != null) {
			// Look for an external reference
			referencedValueAssignment = (ValueAssignment)symbolsExportedMap.get(referencedModuleName + "." + referencedValueName);
			if(referencedValueAssignment == null) {
				if(!assignmentMap.containsKey(referencedModuleName + "." + referencedValueName)) {
					errorList.add(valueReference.getTokenLocation() + " value '" + referencedModuleName + "." + referencedValueName + "' not defined");
				}
				else {
					errorList.add(valueReference.getTokenLocation() + " value '" + referencedModuleName + "." + referencedValueName + "' not exported");
				}
			}
		}
		else {
			// Look for a value defined in the same module or an imported symbol
			String moduleReference = moduleDefinition.getModuleIdentifier().getModuleReference();
			referencedValueAssignment = (ValueAssignment)assignmentMap.get(moduleReference + "." + referencedValueName);
			if(referencedValueAssignment == null) {
				if(moduleDefinition.getSymbolsImported() != null) {
					for(SymbolsFromModule symbolsFromModule : moduleDefinition.getSymbolsImported()) {
						for(String symbol : symbolsFromModule.getSymbolList()) {
							if(symbol.equals(referencedValueName)) {
								referencedValueAssignment = (ValueAssignment)assignmentMap.get(symbolsFromModule.getModuleReference().getName() + "." + referencedValueName);
								break;
							}
						}
					}
				}
			}
		}
		if(referencedValueAssignment != null) {
			valueReference.setReferencedValue(referencedValueAssignment.getValue());
		}
		else {
			errorList.add(valueReference.getTokenLocation() + " value '" + referencedValueName + "' neither defined nor imported");
		}
	}
	
	/*
	 * Validate a namedNumberList
	 * Names must be distinct
	 * Number must be distinct
	 * If number is a reference, it must be defined as an INTEGER
	 */
	void visit(ArrayList<NamedNumber> namedNumberList, ModuleDefinition moduleDefinition) {
		if(namedNumberList == null) return;
		
		Map<String, NamedNumber> namedNumbersByName = new HashMap<String, NamedNumber>();  
		Map<Integer, NamedNumber> namedNumbersByNumber = new HashMap<Integer, NamedNumber>();  

		for(NamedNumber namedNumber : namedNumberList) {
			{
				NamedNumber duplicateName = namedNumbersByName.get(namedNumber.getName());
				if(duplicateName != null) {
					errorList.add(namedNumber.getNameTokenLocation() + " name '" + namedNumber.getName() + "' already used at " + duplicateName.getNameTokenLocation());
				}
				else {
					namedNumbersByName.put(namedNumber.getName(), namedNumber);
				}
			}
			
			if(namedNumber.getNumber() != null) {
				NamedNumber duplicateNumber = namedNumbersByNumber.get(namedNumber.getNumber());				
				if(duplicateNumber != null) {
					errorList.add(namedNumber.getNumberOrReferenceTokenLocation() + " number '" + namedNumber.getNumber() + "' already used at " + duplicateNumber.getNumberOrReferenceTokenLocation());
				}
				else {
					namedNumbersByNumber.put(namedNumber.getNumber(), namedNumber);
				}
			}
			
			if(namedNumber.getReference() != null && namedNumber.getNumber() == null) { 
				// Duplicate of visit(ValueReference, ModuleDefinition)
				String referencedValueName = namedNumber.getReference();
				String moduleReference = moduleDefinition.getModuleIdentifier().getModuleReference();
				ValueAssignment referencedValueAssignment = (ValueAssignment)assignmentMap.get(moduleReference + "." + referencedValueName);
				if(referencedValueAssignment == null) {
					if(moduleDefinition.getSymbolsImported() != null) {
						for(SymbolsFromModule symbolsFromModule : moduleDefinition.getSymbolsImported()) {
							for(String symbol : symbolsFromModule.getSymbolList()) {
								if(symbol.equals(referencedValueName)) {
									referencedValueAssignment = (ValueAssignment)assignmentMap.get(symbolsFromModule.getModuleReference().getName() + "." + referencedValueName);
									break;
								}
							}
						}
					}
				}
				if(referencedValueAssignment == null) {
					errorList.add(namedNumber.getNumberOrReferenceTokenLocation() + "  '" + namedNumber.getReference() + "' neither defined nor imported");
				}
				else {
					Type type = referencedValueAssignment.getValue().getType();
					if(type.isIntegerType() || (type.isTypeReference() && ((TypeReference)type).getBuiltinType().isIntegerType()) ) {
						Value value = referencedValueAssignment.getValue();
						Integer number = null;
						if(value.isIntegerValue()) {
							number = ((IntegerValue)value).getValue();
						}
						else {
							if(value.isValueReference()) {
								Value referencedValue = value;
								do {
									referencedValue = ((ValueReference)referencedValue).getReferencedValue();
								}
								while(referencedValue != null && referencedValue.isValueReference());
								if(referencedValue.isIntegerValue()) {
									number = ((IntegerValue)referencedValue).getValue();
								}

							}
						}
						if(number == null) {
							errorList.add(namedNumber.getNumberOrReferenceTokenLocation() + " cannot find an INTEGER value for  '" + namedNumber.getReference() + "'");
						}
						else {
							namedNumber.setNumber(number);
							NamedNumber duplicateNumber = namedNumbersByNumber.get(namedNumber.getNumber());				
							if(duplicateNumber != null) {
								errorList.add(namedNumber.getNumberOrReferenceTokenLocation() + " number '" + namedNumber.getNumber() + "' already used at " + duplicateNumber.getNumberOrReferenceTokenLocation());
							}
							else {
								namedNumbersByNumber.put(number, namedNumber);
							}
						}
					}
					else {
						errorList.add(namedNumber.getNumberOrReferenceTokenLocation() + "  '" + namedNumber.getReference() + "' must have type INTEGER");
					}
				}
			}
		}
	}
	
	/*
	 * Visit enumeratedType
	 * Check namedNumbers validity
	 * Check that numbers in additional enumerations are ordered (no need to be contiguous)
	 * Assign number when needed starting with the lowest number available in rootEnumeration
	 */
	void visitEnumeratedType(EnumeratedType enumeratedType, ModuleDefinition moduleDefinition) {
		ArrayList<NamedNumber> enumerations = new ArrayList<NamedNumber>();
		enumerations.addAll(enumeratedType.getRootEnumeration());
		if(enumeratedType.getAdditionalEnumeration() != null) {
			enumerations.addAll(enumeratedType.getAdditionalEnumeration());
		}
		
		// Basic namedNumbers check 
		visit(enumerations, moduleDefinition);
		
		Map<Integer, NamedNumber> rootEnumerationMap = new HashMap<Integer, NamedNumber>();
		for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
			rootEnumerationMap.put(namedNumber.getNumber(), namedNumber);
		}
		
		// Assign numbers in rootEnumeration
		int availableNumber = 0;
		for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
			if(namedNumber.getNumber() == null) {
				// find next number available
				for(; rootEnumerationMap.containsKey(Integer.valueOf(availableNumber)); availableNumber++) {}
				namedNumber.setNumber(Integer.valueOf(availableNumber));
				availableNumber++;
			}
		}
		
		// Assign numbers in additionalEnumeration
		if(enumeratedType.getAdditionalEnumeration() != null) {
			NamedNumber previousNamedNumber = null;
			for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
				if(namedNumber.getNumber() == null) {
					// Find next number available
					for(; rootEnumerationMap.containsKey(Integer.valueOf(availableNumber)); availableNumber++) {}
					namedNumber.setNumber(Integer.valueOf(availableNumber));
					availableNumber++;
				}
				else {
					availableNumber = namedNumber.getNumber().intValue() + 1;
				}
				if(previousNamedNumber != null && namedNumber.getNumber().intValue() <= previousNamedNumber.getNumber().intValue()) {
					errorList.add(namedNumber.getNumberOrReferenceTokenLocation() + " number '" + namedNumber.getNumber() + "' should be greater than '" + previousNamedNumber.getNumber() + "' at " + previousNamedNumber.getNumberOrReferenceTokenLocation());
				}
				previousNamedNumber = namedNumber;
			}
		}
		
		// Visit again to see if we have not created duplicates
		// TODO remove duplicate messages in errorList
		visit(enumerations, moduleDefinition);
	}
	
	/*
	 * Visit SequenceType
	 */
	void visitSequenceType(SequenceType sequenceType, ModuleDefinition moduleDefinition) {
		visitTypeWithComponents(sequenceType, moduleDefinition);
		
		ArrayList<Component> rootComponentList = sequenceType.getRootComponentList();
		ArrayList<Component> additionalComponentList = sequenceType.getAdditionalComponentList();
		ArrayList<Component> extensionComponentList = sequenceType.getExtensionComponentList();

		List<ArrayList<Component>> componentLists = new ArrayList<ArrayList<Component>>(asList(rootComponentList, extensionComponentList, additionalComponentList));
		
		if(!sequenceType.isAutomaticTaggingSelected()) {
			// Check tags are unique (following rules of SequenceType)
			ArrayList<NamedType> optionalNamedTypeList = new ArrayList<NamedType>();
			boolean firstNonOptionalInAdditionChecked = false;
			for(int i=0; i<componentLists.size(); i++) {
				ArrayList<Component> componentList = componentLists.get(i);
				if(componentList != null) {
					for(Component component : componentList) {
						if(component.isNamedType()) { // it should be unless there are errors
							NamedType namedType = (NamedType)component;
							Tag tag = namedType.getType().getFirstTag();
							if(tag == null) {  // CHOICE or ANY
								ChoiceType choiceType = null;
								if(namedType.getType().isTypeReference()) {
									TypeReference typeReference = (TypeReference)namedType.getType();
									visitTypeReference(typeReference, moduleDefinition);
									if(typeReference.getBuiltinType().isChoiceType())  {  // could be ANY type
										choiceType = (ChoiceType)typeReference.getBuiltinType();
									}
								}
								else {
									if(namedType.getType().isChoiceType()) { // could be ANY type
										choiceType = (ChoiceType)namedType.getType();
									}
								}
								if(choiceType != null) {
									visitChoiceAlternative(choiceType.getRootAlternativeList(), optionalNamedTypeList, moduleDefinition);
									if(choiceType.getAdditionalAlternativeList() != null) {
										visitChoiceAlternative(choiceType.getAdditionalAlternativeList(), optionalNamedTypeList, moduleDefinition);
									}
								}
							}
							for(NamedType previousNamedType : optionalNamedTypeList) {
								Tag previousTag = previousNamedType.getType().getFirstTag();
								if(tag != null && tag.equals(previousTag)) {
									errorList.add(namedType.getTokenLocation() + ": tag of '" + namedType.getName() + "' is the same as tag of '" + previousNamedType.getName() + "' " + previousNamedType.getTokenLocation()); 					
								}
							}
							if(namedType.isOptional() || namedType.getDefaultValue() != null) {
								optionalNamedTypeList.add(namedType);
							}
							else {
								optionalNamedTypeList = new ArrayList<NamedType>();
								if(componentList == additionalComponentList && extensionComponentList != null && !firstNonOptionalInAdditionChecked) {
									firstNonOptionalInAdditionChecked = true;
									// Make sure all tags in extension are different
									for(Component extensionComponent : extensionComponentList) {
										if(component.isNamedType()) { // it should be unless there are errors
											NamedType extensionNamedType = (NamedType)extensionComponent;
											Tag extensionTag = extensionNamedType.getType().getFirstTag();
											if(tag != null && tag.equals(extensionTag)) {
												errorList.add(namedType.getTokenLocation() + ": tag of '" + namedType.getName() + "' is the same as tag of '" + extensionNamedType.getName() + "' " + extensionNamedType.getTokenLocation() + ". All tags in extension should be different from the first non optional and non default element in addition."); 					
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/*
	 * Visit SetType
	 */
	void visitSetType(SetType setType, ModuleDefinition moduleDefinition) {
		visitTypeWithComponents(setType, moduleDefinition);
		
		ArrayList<Component> rootComponentList = setType.getRootComponentList();
		ArrayList<Component> additionalComponentList = setType.getAdditionalComponentList();
		ArrayList<Component> extensionComponentList = setType.getExtensionComponentList();
		List<ArrayList<Component>> componentLists = new ArrayList<ArrayList<Component>>(asList(rootComponentList, extensionComponentList, additionalComponentList));

		if(!setType.isAutomaticTaggingSelected()) {
			// check tags are distinct 
			ArrayList<NamedType> previousNamedTypeList = new ArrayList<NamedType>();
			for(int i=0; i<componentLists.size(); i++) {
				ArrayList<Component> componentList = componentLists.get(i);
				if(componentList != null) {
					for(Component component : componentList) {
						if(component.isNamedType()) { // it should be unless there are errors
							NamedType namedType = (NamedType)component;
							Tag tag = namedType.getType().getFirstTag();
							if(tag == null) {
								ChoiceType choiceType = null;
								if(namedType.getType().isTypeReference()) {
									TypeReference typeReference = (TypeReference)namedType.getType();
									visitTypeReference(typeReference, moduleDefinition);
									if(typeReference.getBuiltinType().isChoiceType()) {  // could be ANY type
										choiceType = (ChoiceType)typeReference.getBuiltinType();
									}
								}
								else {
									if(namedType.getType().isChoiceType()) {  // could be ANY type
										choiceType = (ChoiceType)namedType.getType();
									}
								}
								if(choiceType != null) {
									visitChoiceAlternative(choiceType.getRootAlternativeList(), previousNamedTypeList, moduleDefinition);
									if(choiceType.getAdditionalAlternativeList() != null) {
										visitChoiceAlternative(choiceType.getAdditionalAlternativeList(), previousNamedTypeList, moduleDefinition);
									}
								}
							}
							for(NamedType previousNamedType : previousNamedTypeList) {
								Tag previousTag = previousNamedType.getType().getFirstTag();
 								if(tag != null && tag.equals(previousTag)) {
									errorList.add(namedType.getTokenLocation() + ": tag of '" + namedType.getName() + "' is the same as tag of '" + previousNamedType.getName() + "' " + previousNamedType.getTokenLocation()); 					
								}
							}
							previousNamedTypeList.add(namedType);
						}
					}
				}
			}
			// check tags in extension are in canonical order
			if(extensionComponentList != null) {
				NamedType previousNamedType = null;
				for(Component component : extensionComponentList) {
					if(component.isNamedType()) { // it should be unless there are errors
						NamedType namedType = (NamedType)component;
						if(previousNamedType != null) {
							Tag previousTag = previousNamedType.getType().getFirstTag();
							Tag tag = namedType.getType().getFirstTag();
							if(previousTag != null && tag != null && previousTag.greaterThan(tag)) {
								errorList.add(namedType.getTokenLocation() + ": tag of '" + namedType.getName() + "' should be canonically greater than tag of '" + previousNamedType.getName() + "' " + previousNamedType.getTokenLocation()); 												
							}
						}
						previousNamedType = namedType;
					}
				}
			}
		}
	}

	/*
	 * Validation common to SequenceType, SetType and ChoiceType
	 */
	void visitTypeWithComponents(TypeWithComponents typeWithComponents, ModuleDefinition moduleDefinition) {
		ArrayList<Component> rootComponentList = typeWithComponents.getRootComponentList();
		ArrayList<Component> additionalComponentList = typeWithComponents.getAdditionalComponentList();
		ArrayList<Component> extensionComponentList = typeWithComponents.getExtensionComponentList();

		// Group lists (keep extensionComponentList last -  when AUTOMATIC TAGS is selected, no tag is allowed in extension)
		List<ArrayList<Component>> componentLists = new ArrayList<ArrayList<Component>>(asList(rootComponentList, additionalComponentList, extensionComponentList));
		List<ArrayList<Component>> transformedComponentLists = new ArrayList<ArrayList<Component>>(asList(new ArrayList<Component>(), new ArrayList<Component>(), new ArrayList<Component>()));
		
		// Visit children
		// Disable automatic tags if a tag is found in the namedtypes
		// Add an error if tag is found only in extension (with AUTOMATIC TAGS selected)
		boolean automaticTaggingSelected = false;
		if(moduleDefinition.getTagDefault() == TagDefault.AUTOMATIC_TAGS) {
			automaticTaggingSelected = true;
		}
		for(ArrayList<Component> componentList : componentLists) {
			if(componentList != null) {
				for(Component component : componentList) {
					if(component.isNamedType()) {
						NamedType namedType = (NamedType)component;
						if(namedType.getType().getTagList() != null && namedType.getType().getTagList().size() != 0) {
							if(automaticTaggingSelected) {
								if(componentList == extensionComponentList) {
									errorList.add(namedType.getTokenLocation() + ": component '" + namedType.getName() + "' cannot be tagged as AUTOMATIC TAGS is selected");
								}
								else {
									automaticTaggingSelected = false;
								}
							}
						}
						visitType(namedType.getType(), moduleDefinition);
					}
					if(component.isComponentsOf()) {
						ComponentsOf componentsOf = (ComponentsOf)component;
						if(componentsOf.getForeignContainer().isSequenceType() || componentsOf.getForeignContainer().isSetType()) {
							visitType(componentsOf.getForeignContainer(), moduleDefinition);
						}
					}
				}
			}
		}
		typeWithComponents.setAutomaticTaggingSelected(automaticTaggingSelected);
		
		// Transform COMPONENTS OF
		for(int i=0; i<componentLists.size(); i++) {
			ArrayList<Component> componentList = componentLists.get(i);
			ArrayList<Component> transformedComponentList = transformedComponentLists.get(i);
			if(componentList != null) {
				for(Component component : componentList) {
					if(component.isNamedType()) {
						transformedComponentList.add(component);
					}
					else {
						if(typeWithComponents.isSequenceType()) {
							transformComponentsOfSequence((ComponentsOf)component, transformedComponentList, moduleDefinition);
						}
						if(typeWithComponents.isSetType()) {
							transformComponentsOfSet((ComponentsOf)component, transformedComponentList, moduleDefinition);
						}
					}
				}
			}
		}
		
		// Replace componentLists
		if(rootComponentList != null) {
			typeWithComponents.setRootComponentList(transformedComponentLists.get(0));
		}
		if(additionalComponentList != null) {
			typeWithComponents.setAdditionalComponentList(transformedComponentLists.get(1));
		}
		if(extensionComponentList != null) {
			typeWithComponents.setExtensionComponentList(transformedComponentLists.get(2));
		}	
		
		// reorder list
		componentLists = new ArrayList<ArrayList<Component>>(asList(typeWithComponents.getRootComponentList(), typeWithComponents.getExtensionComponentList(), typeWithComponents.getAdditionalComponentList()));

		// Check names are unique
		Map<String, NamedType> namedTypesMap = new HashMap<String, NamedType>();
		for(int i=0; i<componentLists.size(); i++) {
			ArrayList<Component> componentList = componentLists.get(i);
			if(componentList != null) {
				for(Component component : componentList) {
					if(component.isNamedType()) { // it should be unless there are errors
						NamedType namedType = (NamedType)component;
						// check name
						NamedType duplicateNamedType =  namedTypesMap.get(namedType.getName());
						if(duplicateNamedType == null) {
							namedTypesMap.put(namedType.getName(), namedType);
						}
						else {
							errorList.add(namedType.getTokenLocation() + ": name '" + namedType.getName() + "' already used at " + duplicateNamedType.getTokenLocation()); 					
						}
					}
				}
			}
		}					
	}
	
	/*
	 * Transform a COMPONENTS OF a SequenceType (recursive)
	 */
	void transformComponentsOfSequence(ComponentsOf componentsOf, ArrayList<Component> transformedComponentList, ModuleDefinition moduleDefinition) {	
		SequenceType foreignSequenceType = null;
		if(componentsOf.getForeignContainer().isTypeReference()) {
			TypeReference typeReference = (TypeReference)componentsOf.getForeignContainer();
			visitTypeReference(typeReference, moduleDefinition);
			if(typeReference.getBuiltinType() != null) {
				if(!typeReference.getBuiltinType().isSequenceType()) {
					errorList.add(componentsOf.getTokenLocation() + ": type in COMPONENTS OF must be a SEQUENCE ('" + typeReference.getName() + "' is a " + typeReference.getBuiltinType().getName() + ")");
				}
				else {
					foreignSequenceType = (SequenceType)typeReference.getBuiltinType();
				}
			}
		}
		else {
			if(!componentsOf.getForeignContainer().isSequenceType()) {
				errorList.add(componentsOf.getTokenLocation() + ": type in COMPONENTS OF must be a SEQUENCE");
			}
			else {
				foreignSequenceType = (SequenceType)componentsOf.getForeignContainer();
			}
		}
		
		// Perform transformation
		// Copy named types from rootComponentLists
		if(foreignSequenceType != null) {
			if(foreignSequenceType.getRootComponentList() != null) {
				for(Component foreignComponent : foreignSequenceType.getRootComponentList()) {
					if(foreignComponent.isNamedType()) {
						transformedComponentList.add(((NamedType)foreignComponent).copy());
					}
					else {
						transformComponentsOfSequence((ComponentsOf)foreignComponent, transformedComponentList, moduleDefinition);
					}
				}
			}						
			if(foreignSequenceType.getAdditionalComponentList() != null) {
				for(Component foreignComponent : foreignSequenceType.getAdditionalComponentList()) {
					if(foreignComponent.isNamedType()) {
						transformedComponentList.add(((NamedType)foreignComponent).copy());
					}
					else {
						transformComponentsOfSequence((ComponentsOf)foreignComponent, transformedComponentList, moduleDefinition);
					}
				}
			}						
		}
		
			
	}
	
	/*
	 * Transform a COMPONENTS OF SetType (recursive)
	 */
	void transformComponentsOfSet(ComponentsOf componentsOf, ArrayList<Component> transformedComponentList, ModuleDefinition moduleDefinition) {	
		SequenceType foreignSequenceType = null;
		if(componentsOf.getForeignContainer().isTypeReference()) {
			TypeReference typeReference = (TypeReference)componentsOf.getForeignContainer();
			visitTypeReference(typeReference, moduleDefinition);
			if(typeReference.getBuiltinType() != null) {
				if(!typeReference.getBuiltinType().isSequenceType()) {
					errorList.add(componentsOf.getTokenLocation() + ": type in COMPONENTS OF must be a SEQUENCE ('" + typeReference.getName() + "' is a " + typeReference.getBuiltinType().getName() + ")");
				}
				else {
					foreignSequenceType = (SequenceType)typeReference.getBuiltinType();
				}
			}
		}
		else {
			if(!componentsOf.getForeignContainer().isSequenceType()) {
				errorList.add(componentsOf.getTokenLocation() + ": type in COMPONENTS OF must be a SEQUENCE");
			}
			else {
				foreignSequenceType = (SequenceType)componentsOf.getForeignContainer();
			}
		}
		
		// Perform transformation
		// Copy named types from rootComponentLists
		if(foreignSequenceType != null) {
			if(foreignSequenceType.getRootComponentList() != null) {
				for(Component foreignComponent : foreignSequenceType.getRootComponentList()) {
					if(foreignComponent.isNamedType()) {
						transformedComponentList.add(((NamedType)foreignComponent).copy());
					}
					else {
						transformComponentsOfSequence((ComponentsOf)foreignComponent, transformedComponentList, moduleDefinition);
					}
				}
			}						
			if(foreignSequenceType.getAdditionalComponentList() != null) {
				for(Component foreignComponent : foreignSequenceType.getAdditionalComponentList()) {
					if(foreignComponent.isNamedType()) {
						transformedComponentList.add(((NamedType)foreignComponent).copy());
					}
					else {
						transformComponentsOfSequence((ComponentsOf)foreignComponent, transformedComponentList, moduleDefinition);
					}
				}
			}						
		}	
	}
	
	/*
	 * Visit ChoiceType
	 */
	void visitChoiceType(ChoiceType choiceType, ModuleDefinition moduleDefinition) {
		if(choiceType.getTagList() != null && choiceType.getTagList().size() !=  0) {
			Tag choiceTag = choiceType.getTagList().get(choiceType.getTagList().size()-1);
			if(choiceTag.getTagMode() == TagMode.IMPLICIT_TAG) {
				errorList.add(choiceTag.getTagTokenLocation() + " IMPLICIT TAG not allowed for CHOICE");
			}
			else {
				choiceTag.setTagMode(TagMode.EXPLICIT_TAG);
			}
		}
		visitTypeWithComponents(choiceType, moduleDefinition);
		
		if(!choiceType.isAutomaticTaggingSelected()) {
			ArrayList<NamedType> alternativeListIncludingNested = new ArrayList<NamedType>();
			visitChoiceAlternative(choiceType.getRootAlternativeList(), alternativeListIncludingNested, moduleDefinition);
			if(choiceType.getAdditionalAlternativeList() != null) {
				visitChoiceAlternative(choiceType.getAdditionalAlternativeList(), alternativeListIncludingNested, moduleDefinition);
			}
			choiceType.setAlternativeListIncludingNested(alternativeListIncludingNested);
		}
	}
	
	/*
	 * Search for duplicate tags in a ChoiceType
	 * Follow untagged choices, selection types and untagged type references.
	 */
	void visitChoiceAlternative(ArrayList<Component>alternativeList, ArrayList<NamedType> alternativeListIncludingNested, ModuleDefinition moduleDefinition) {
		for(Component component : alternativeList) {
			NamedType namedType = (NamedType)component; // always the case for choice alternative
			if(namedType.getType().isTypeReference()) {
				visitTypeReference((TypeReference)namedType.getType(), moduleDefinition);
			}
			Tag tag = namedType.getType().getFirstTag();
			if(tag == null && namedType.getType().isSelectionType()) {
				SelectionType selectionType = (SelectionType)namedType.getType();
				visitSelectionType(selectionType, moduleDefinition);
				if(selectionType.getSelectedType() != null) {
					tag = selectionType.getSelectedType().getFirstTag();
				}
			}
			if(tag != null) {
				int errorsBefore = errorList.size();
				for(NamedType previousNamedType : alternativeListIncludingNested) {
					Tag previousTag = previousNamedType.getType().getFirstTag();
						if(tag != null && tag.equals(previousTag)) {
						errorList.add(namedType.getTokenLocation() + ": tag of '" + namedType.getName() + "' is the same as tag of '" + previousNamedType.getName() + "' " + previousNamedType.getTokenLocation()); 					
					}
				}
				if(errorsBefore == errorList.size()) {
					alternativeListIncludingNested.add(namedType);
				}
			}
			else { // it is either an untagged choice or a reference
				ChoiceType choiceType = null;
				if(namedType.getType().isTypeReference()) {
					TypeReference typeReference = (TypeReference)namedType.getType();
					visitTypeReference(typeReference, moduleDefinition);
					choiceType = (ChoiceType)typeReference.getBuiltinType();
				}
				else {
					choiceType = (ChoiceType)namedType.getType();
				}
				visitChoiceAlternative(choiceType.getRootAlternativeList(), alternativeListIncludingNested, moduleDefinition);
				if(choiceType.getAdditionalAlternativeList() != null) {
					visitChoiceAlternative(choiceType.getAdditionalAlternativeList(), alternativeListIncludingNested, moduleDefinition);
				}
			}
		}
	}

	/*
	 * Validate a selection type
	 */
	void visitSelectionType(SelectionType selectionType, ModuleDefinition moduleDefinition) {
		if(selectionType.getSelectedType() != null) {
			// already visited 
			return;
		}
		
		Type type = selectionType.getType();
		if(type.isTypeReference()) {
			visitTypeReference((TypeReference)type, moduleDefinition);
			if(((TypeReference)type).getReferencedType() == null) {
				errorList.add(selectionType.getTokenLocation() + ": type for selection '" + selectionType.getSelection() + "' is not defined");				
			}
		}
		if(!type.isChoiceType() && !(type.isTypeReference() && ((TypeReference)type).getReferencedType() != null && ((TypeReference)type).getBuiltinType().isChoiceType())) {
			errorList.add(selectionType.getTokenLocation() + ": selection '" + selectionType.getSelection() + "' can only be made from a Choice type");
		}
		else {
			ChoiceType choiceType = type.isChoiceType() ? (ChoiceType)type : (ChoiceType)((TypeReference)type).getBuiltinType();
			ArrayList<Component> alternativeList = choiceType.getRootAlternativeList();
			NamedType selectionAlternative = null;
			for(Component alternative : alternativeList) {
				if(((NamedType)alternative).getName().equals(selectionType.getSelection())) {
					selectionAlternative = (NamedType)alternative;
					break;
				}
			}
			if(selectionAlternative == null) {
				errorList.add(selectionType.getTokenLocation() + ": selection '" + selectionType.getSelection() + "' not found in choice type");				
			}
			else {
				selectionType.setSelectedType(selectionAlternative.getType());
			}
		}
	}
}
