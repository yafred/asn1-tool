package com.yafred.asn1.parser;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.yafred.asn1.grammar.ASNBaseVisitor;
import com.yafred.asn1.grammar.ASNParser.AlternativeTypeListContext;
import com.yafred.asn1.grammar.ASNParser.AssignmentContext;
import com.yafred.asn1.grammar.ASNParser.BitStringTypeContext;
import com.yafred.asn1.grammar.ASNParser.BuiltinTypeContext;
import com.yafred.asn1.grammar.ASNParser.CharacterStringTypeContext;
import com.yafred.asn1.grammar.ASNParser.ChoiceTypeContext;
import com.yafred.asn1.grammar.ASNParser.ComponentTypeContext;
import com.yafred.asn1.grammar.ASNParser.ComponentTypeListContext;
import com.yafred.asn1.grammar.ASNParser.DefinedTypeContext;
import com.yafred.asn1.grammar.ASNParser.DefinitiveObjIdComponentContext;
import com.yafred.asn1.grammar.ASNParser.EnumeratedTypeContext;
import com.yafred.asn1.grammar.ASNParser.EnumerationContext;
import com.yafred.asn1.grammar.ASNParser.EnumerationItemContext;
import com.yafred.asn1.grammar.ASNParser.ExportsContext;
import com.yafred.asn1.grammar.ASNParser.ExtensionAdditionAlternativeContext;
import com.yafred.asn1.grammar.ASNParser.ExtensionAdditionAlternativesListContext;
import com.yafred.asn1.grammar.ASNParser.ExtensionAdditionContext;
import com.yafred.asn1.grammar.ASNParser.ExtensionAdditionListContext;
import com.yafred.asn1.grammar.ASNParser.ExtensionsAndAdditionsContext;
import com.yafred.asn1.grammar.ASNParser.ExtensionsOnlyContext;
import com.yafred.asn1.grammar.ASNParser.GlobalModuleReferenceContext;
import com.yafred.asn1.grammar.ASNParser.ImportsContext;
import com.yafred.asn1.grammar.ASNParser.IntegerTypeContext;
import com.yafred.asn1.grammar.ASNParser.ModuleDefinitionContext;
import com.yafred.asn1.grammar.ASNParser.ModuleIdentifierContext;
import com.yafred.asn1.grammar.ASNParser.NamedBitContext;
import com.yafred.asn1.grammar.ASNParser.NamedBitListContext;
import com.yafred.asn1.grammar.ASNParser.NamedNumberContext;
import com.yafred.asn1.grammar.ASNParser.NamedNumberListContext;
import com.yafred.asn1.grammar.ASNParser.NamedTypeContext;
import com.yafred.asn1.grammar.ASNParser.NamedValueContext;
import com.yafred.asn1.grammar.ASNParser.ReferencedTypeContext;
import com.yafred.asn1.grammar.ASNParser.RootComponentOnlyContext;
import com.yafred.asn1.grammar.ASNParser.RootComponentsAndExtensionsAndAdditionsContext;
import com.yafred.asn1.grammar.ASNParser.RootComponentsAndExtensionsContext;
import com.yafred.asn1.grammar.ASNParser.SelectionTypeContext;
import com.yafred.asn1.grammar.ASNParser.SequenceOfTypeContext;
import com.yafred.asn1.grammar.ASNParser.SequenceTypeContext;
import com.yafred.asn1.grammar.ASNParser.SetOfTypeContext;
import com.yafred.asn1.grammar.ASNParser.SetTypeContext;
import com.yafred.asn1.grammar.ASNParser.SpecificationContext;
import com.yafred.asn1.grammar.ASNParser.SymbolContext;
import com.yafred.asn1.grammar.ASNParser.SymbolListContext;
import com.yafred.asn1.grammar.ASNParser.SymbolsFromModuleContext;
import com.yafred.asn1.grammar.ASNParser.SymbolsFromModuleListContext;
import com.yafred.asn1.grammar.ASNParser.TagDefaultContext;
import com.yafred.asn1.grammar.ASNParser.TaggedTypeContext;
import com.yafred.asn1.grammar.ASNParser.TypeAssignmentContext;
import com.yafred.asn1.grammar.ASNParser.TypeContext;
import com.yafred.asn1.grammar.ASNParser.UsefulTypeContext;
import com.yafred.asn1.grammar.ASNParser.ValueAssignmentContext;
import com.yafred.asn1.grammar.ASNParser.ValueContext;
import com.yafred.asn1.grammar.ASNParser.ValueReferenceContext;
import com.yafred.asn1.grammar.ASNParser.Value_BOOLEANContext;
import com.yafred.asn1.grammar.ASNParser.Value_BSTRINGContext;
import com.yafred.asn1.grammar.ASNParser.Value_CSTRINGContext;
import com.yafred.asn1.grammar.ASNParser.Value_ChoiceContext;
import com.yafred.asn1.grammar.ASNParser.Value_EmptyListContext;
import com.yafred.asn1.grammar.ASNParser.Value_HSTRINGContext;
import com.yafred.asn1.grammar.ASNParser.Value_IntegerContext;
import com.yafred.asn1.grammar.ASNParser.Value_NULLContext;
import com.yafred.asn1.grammar.ASNParser.Value_NamedValueListContext;
import com.yafred.asn1.grammar.ASNParser.Value_ValueListContext;
import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.BMPStringType;
import com.yafred.asn1.model.BitStringType;
import com.yafred.asn1.model.BinaryStringValue;
import com.yafred.asn1.model.BooleanType;
import com.yafred.asn1.model.BooleanValue;
import com.yafred.asn1.model.CharacterStringType;
import com.yafred.asn1.model.CharacterStringValue;
import com.yafred.asn1.model.ChoiceType;
import com.yafred.asn1.model.ChoiceValue;
import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.ComponentsOf;
import com.yafred.asn1.model.DateTimeType;
import com.yafred.asn1.model.DateType;
import com.yafred.asn1.model.TypeReference;
import com.yafred.asn1.model.DefinitiveObjectIdComponent;
import com.yafred.asn1.model.DurationType;
import com.yafred.asn1.model.EmbeddedPDVType;
import com.yafred.asn1.model.EmptyListValue;
import com.yafred.asn1.model.EnumeratedType;
import com.yafred.asn1.model.ExternalType;
import com.yafred.asn1.model.GeneralStringType;
import com.yafred.asn1.model.GeneralizedTimeType;
import com.yafred.asn1.model.GlobalModuleReference;
import com.yafred.asn1.model.GraphicStringType;
import com.yafred.asn1.model.IA5StringType;
import com.yafred.asn1.model.IRIType;
import com.yafred.asn1.model.ISO646StringType;
import com.yafred.asn1.model.IntegerType;
import com.yafred.asn1.model.IntegerValue;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.ModuleIdentifier;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.NamedType;
import com.yafred.asn1.model.NamedValue;
import com.yafred.asn1.model.NamedValueListValue;
import com.yafred.asn1.model.NullType;
import com.yafred.asn1.model.NullValue;
import com.yafred.asn1.model.NumericStringType;
import com.yafred.asn1.model.ObjectDescriptorType;
import com.yafred.asn1.model.ObjectIdentifierType;
import com.yafred.asn1.model.OctetStringType;
import com.yafred.asn1.model.HexaStringValue;
import com.yafred.asn1.model.PrintableStringType;
import com.yafred.asn1.model.RealType;
import com.yafred.asn1.model.RelativeIRIType;
import com.yafred.asn1.model.RelativeOIDType;
import com.yafred.asn1.model.SelectionType;
import com.yafred.asn1.model.SequenceOfType;
import com.yafred.asn1.model.SequenceType;
import com.yafred.asn1.model.SetOfType;
import com.yafred.asn1.model.SetType;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.SymbolsFromModule;
import com.yafred.asn1.model.T61StringType;
import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.TagClass;
import com.yafred.asn1.model.TagDefault;
import com.yafred.asn1.model.TagMode;
import com.yafred.asn1.model.TeletexStringType;
import com.yafred.asn1.model.TimeOfDayType;
import com.yafred.asn1.model.TimeType;
import com.yafred.asn1.model.TokenLocation;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.UTCTimeType;
import com.yafred.asn1.model.UTF8StringType;
import com.yafred.asn1.model.UniversalStringType;
import com.yafred.asn1.model.Value;
import com.yafred.asn1.model.ValueAssignment;
import com.yafred.asn1.model.ValueListValue;
import com.yafred.asn1.model.ValueReference;
import com.yafred.asn1.model.VideotexStringType;
import com.yafred.asn1.model.VisibleStringType;

public class SpecificationAntlrVisitor extends ASNBaseVisitor<Specification> {
	
	@Override
	public Specification visitSpecification(SpecificationContext ctx) {
		ArrayList<ModuleDefinition> moduleDefinitionList = new ArrayList<ModuleDefinition>();
		for ( ModuleDefinitionContext moduleDefinitionContext : ctx.moduleDefinition()) {
			moduleDefinitionList.add(moduleDefinitionContext.accept(new ModuleDefinitionVisitor()));
		}
		return new Specification(moduleDefinitionList);
	}
	
	static class ModuleDefinitionVisitor extends ASNBaseVisitor<ModuleDefinition> {
		@Override
		public ModuleDefinition visitModuleDefinition(ModuleDefinitionContext ctx) {
			ModuleIdentifier moduleIdentifier = ctx.moduleIdentifier().accept(new ModuleIdentifierVisitor());
			TagDefault tagDefault = TagDefault.EXPLICIT_TAGS;
			if(ctx.tagDefault() != null) {
				tagDefault = ctx.tagDefault().accept(new TagDefaultVisitor());
			}
			boolean extensibilityImplied = false;
			if(ctx.extensionDefault() != null) {
				extensibilityImplied = true;
			}
			ArrayList<String> symbolsExported = null;
			if(ctx.exports() != null) {
				if(ctx.exports().ALL_LITERAL() == null) {
					symbolsExported = ctx.exports().accept(new ExportsVisitor());
				}
			}			
			ArrayList<SymbolsFromModule> symbolsImported = null;
			if(ctx.imports() != null) {
				symbolsImported = ctx.imports().accept(new ImportsVisitor());
			}
			ArrayList<Assignment> assignments = null;
			if(ctx.assignment() != null) {
				assignments = new ArrayList<Assignment>();
				for(AssignmentContext assignmentContext : ctx.assignment()) {
					Assignment assignment = null;
					if(assignmentContext.typeAssignment() != null) {
						assignment = assignmentContext.typeAssignment().accept(new TypeAssignmentVisitor());
					}
					if(assignmentContext.valueAssignment() != null) {
						assignment = assignmentContext.valueAssignment().accept(new ValueAssignmentVisitor());
					}
					if(assignment != null) {
						assignments.add(assignment);
					}
				}
			}
			
			return new ModuleDefinition(moduleIdentifier, tagDefault, extensibilityImplied, symbolsExported, symbolsImported, assignments);
		}
	}
	
	static class ModuleIdentifierVisitor extends ASNBaseVisitor<ModuleIdentifier> {
		@Override
		public ModuleIdentifier visitModuleIdentifier(ModuleIdentifierContext ctx) {
			ModuleIdentifier moduleIdentifier = new ModuleIdentifier();
			String moduleReference = ctx.UCASE_ID().getText();
			moduleIdentifier.setModuleReference(moduleReference);
			moduleIdentifier.setModuleReferenceTokenLocation(new TokenLocation(ctx.UCASE_ID().getSymbol().getLine(), ctx.UCASE_ID().getSymbol().getCharPositionInLine()+1));
			
			ArrayList<DefinitiveObjectIdComponent> definitiveObjIdComponentList = new ArrayList<DefinitiveObjectIdComponent>();
			if(ctx.definitiveOID() != null) {
				DefinitiveObjIdComponentVisitor definitiveObjIdComponentVisitor = new DefinitiveObjIdComponentVisitor();
				for(DefinitiveObjIdComponentContext definitiveObjIdComponentContext : ctx.definitiveOID().definitiveObjIdComponent()) {
					definitiveObjIdComponentList.add(definitiveObjIdComponentContext.accept(definitiveObjIdComponentVisitor));
				}
			}
			moduleIdentifier.setDefinitiveObjIdComponents(definitiveObjIdComponentList);

			return moduleIdentifier;
		}
	}
	
	static class DefinitiveObjIdComponentVisitor extends ASNBaseVisitor<DefinitiveObjectIdComponent> {
		@Override
		public DefinitiveObjectIdComponent visitDefinitiveObjIdComponent(DefinitiveObjIdComponentContext ctx) {
			String name = null;
			Integer number = null;
			if(ctx.LCASE_ID() != null) {
				name = ctx.LCASE_ID().getText();
			}
			if(ctx.NUMBER() != null) {
				number = Integer.parseInt(ctx.NUMBER().getText());
			}
			return new DefinitiveObjectIdComponent(name, number);
		}
	}
	
	static class TagDefaultVisitor extends ASNBaseVisitor<TagDefault> {
		@Override
		public TagDefault visitTagDefault(TagDefaultContext ctx) {
			if(ctx.EXPLICIT_LITERAL() != null) return TagDefault.EXPLICIT_TAGS;
			if(ctx.IMPLICIT_LITERAL() != null) return TagDefault.IMPLICIT_TAGS;
			if(ctx.AUTOMATIC_LITERAL() != null) return TagDefault.AUTOMATIC_TAGS;
			return null;
		}
	}
	
	static class ExportsVisitor extends ASNBaseVisitor<ArrayList<String>> {
		@Override
		public ArrayList<String> visitExports(ExportsContext ctx) {
			ArrayList<String> symbolsExported = new ArrayList<String>();
			if(ctx.symbolList() != null) {
				symbolsExported = ctx.symbolList().accept(new SymbolListVisitor());
			}
			return symbolsExported; 
		}
	}
	
	static class SymbolListVisitor extends ASNBaseVisitor<ArrayList<String>> {
		@Override
		public ArrayList<String> visitSymbolList(SymbolListContext ctx) {
			ArrayList<String> symbols = new ArrayList<String>();
			for(SymbolContext symbolContext : ctx.symbol()) {
				symbols.add(symbolContext.accept(new SymbolVisitor()));
			}
			return symbols;
		}
	}
	
	static class SymbolVisitor extends ASNBaseVisitor<String> {
		@Override
		public String visitSymbol(SymbolContext ctx) {
			String symbol = null;
			if(ctx.UCASE_ID() != null) {
				symbol = ctx.UCASE_ID().getText();
			}
			else {
				symbol = ctx.LCASE_ID().getText();
			}
			return symbol;
		}
	}
	
	static class ImportsVisitor extends ASNBaseVisitor<ArrayList<SymbolsFromModule>> {
		@Override
		public ArrayList<SymbolsFromModule> visitImports(ImportsContext ctx) {
			ArrayList<SymbolsFromModule> symbolsImported = null;
			if(ctx.symbolsFromModuleList() != null) {
				symbolsImported = ctx.symbolsFromModuleList().accept(new SymbolsFromModuleListVisitor());
			}
			else {
				symbolsImported = new ArrayList<SymbolsFromModule>();
			}
			return symbolsImported; 
		}
	}
	
	static class SymbolsFromModuleListVisitor extends ASNBaseVisitor<ArrayList<SymbolsFromModule>> {
		@Override
		public ArrayList<SymbolsFromModule> visitSymbolsFromModuleList(SymbolsFromModuleListContext ctx) {
			ArrayList<SymbolsFromModule> symbolsFromModuleList = new ArrayList<SymbolsFromModule>();
			for(SymbolsFromModuleContext symbolsFromModuleContext : ctx.symbolsFromModule()) {
				symbolsFromModuleList.add(symbolsFromModuleContext.accept(new SymbolsFromModuleVisitor()));
			}
			return symbolsFromModuleList;
		}
	}
	
	static class SymbolsFromModuleVisitor extends ASNBaseVisitor<SymbolsFromModule> {
		@Override
		public SymbolsFromModule visitSymbolsFromModule(SymbolsFromModuleContext ctx) {
			ArrayList<String> symbolList = ctx.symbolList().accept(new SymbolListVisitor());
			GlobalModuleReference globalModuleReference = ctx.globalModuleReference().accept(new GlobalModuleReferenceVisitor());
			return new SymbolsFromModule(symbolList, globalModuleReference);
		}
	}
	
	static class GlobalModuleReferenceVisitor extends ASNBaseVisitor<GlobalModuleReference> {
		@Override
		public GlobalModuleReference visitGlobalModuleReference(GlobalModuleReferenceContext ctx) {
			String name = ctx.UCASE_ID().getText();
			return new GlobalModuleReference(name);
		}
	}

	static class TypeAssignmentVisitor extends ASNBaseVisitor<TypeAssignment> {
		@Override
		public TypeAssignment visitTypeAssignment(TypeAssignmentContext ctx) {
			String reference = ctx.UCASE_ID().getText();
			Type type = ctx.type().accept(new TypeVisitor());
			TypeAssignment typeAssignment = new TypeAssignment();
			typeAssignment.setReference(reference);
			typeAssignment.setReferenceTokenLocation(new TokenLocation(ctx.UCASE_ID().getSymbol().getLine(), ctx.UCASE_ID().getSymbol().getCharPositionInLine()+1));
			typeAssignment.setType(type);
			return typeAssignment;
		}
	}

	static class TypeVisitor extends ASNBaseVisitor<Type> {
		@Override
		public Type visitType(TypeContext ctx) {
			Type type = null;
			if(ctx.builtinType() != null) {
				type = ctx.builtinType().accept(new BuiltinTypeVisitor());
			}
			if(ctx.referencedType() != null) {
				type = ctx.referencedType().accept(new ReferencedTypeVisitor());
			}
			type.setTokenLocation(new TokenLocation(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()+1));
			return type;
		}
	}

	static class ReferencedTypeVisitor extends ASNBaseVisitor<Type> {
		@Override
		public Type visitReferencedType(ReferencedTypeContext ctx) {
			Type type = null;
			if(ctx.definedType() != null) {
				type = ctx.definedType().accept(new DefinedTypeVisitor());
			}
			if(ctx.usefulType() != null) {
				type = ctx.usefulType().accept(new UsefulTypeVisitor());
			}
			if(ctx.selectionType() != null) {
				type = ctx.selectionType().accept(new SelectionTypeVisitor());
			}
			return type;
		}
	}
	
	static class DefinedTypeVisitor extends ASNBaseVisitor<Type> {
		@Override
		public Type visitDefinedType(DefinedTypeContext ctx) {
			TypeReference type = new TypeReference();
			if(ctx.UCASE_ID().size() == 2) {
				type.setReferencedModuleName(ctx.UCASE_ID(0).getText());
				type.setReferencedTypeName(ctx.UCASE_ID(1).getText());
			}
			else {
				type.setReferencedTypeName(ctx.UCASE_ID(0).getText());
			}
			type.setTokenLocation(new TokenLocation(ctx.UCASE_ID(0).getSymbol().getLine(), ctx.UCASE_ID(0).getSymbol().getCharPositionInLine()+1));
			return type;
		}
	}
	
	static class UsefulTypeVisitor extends ASNBaseVisitor<Type> {
		@Override
		public Type visitUsefulType(UsefulTypeContext ctx) {
			Type type = null;
			if(ctx.GeneralizedTime_LITERAL() != null) {
				type = new GeneralizedTimeType();
			}
			if(ctx.UTCTime_LITERAL() != null) {
				type = new UTCTimeType();
			}
			if(ctx.ObjectDescriptor_LITERAL() != null) {
				type = new ObjectDescriptorType();
			}
			return type;
		}
	}
	
	static class SelectionTypeVisitor extends ASNBaseVisitor<SelectionType> {
		@Override
		public SelectionType visitSelectionType(SelectionTypeContext ctx) {
			SelectionType selectionType = new SelectionType(ctx.LCASE_ID().getText(), ctx.type().accept(new TypeVisitor()));
			selectionType.setTokenLocation(new TokenLocation(ctx.LCASE_ID().getSymbol().getLine(), ctx.LCASE_ID().getSymbol().getCharPositionInLine()+1));
			return selectionType;
		}
	}
	
	static class BuiltinTypeVisitor extends ASNBaseVisitor<Type> {
		@Override
		public Type visitBuiltinType(BuiltinTypeContext ctx) {
			Type type = null;
			if(ctx.choiceType() != null) {
				type = ctx.choiceType().accept(new ChoiceTypeVisitor());
			}
			if(ctx.bitStringType() != null) {
				type = ctx.bitStringType().accept(new BitStringTypeVisitor());
			}
			if(ctx.BOOLEAN_LITERAL() != null) {
				type = new BooleanType();
			}
			if(ctx.DATE_LITERAL() != null) {
				type = new DateType();
			}
			if(ctx.DATE_TIME_LITERAL() != null) {
				type = new DateTimeType();
			}
			if(ctx.DURATION_LITERAL() != null) {
				type = new DurationType();
			}			
			if(ctx.EMBEDDED_LITERAL() != null) {
				type = new EmbeddedPDVType();
			}			
			if(ctx.enumeratedType() != null) {
				type = ctx.enumeratedType().accept(new EnumeratedTypeVisitor());
			}
			if(ctx.EXTERNAL_LITERAL() != null) {
				type = new ExternalType();
			}			
			if(ctx.characterStringType() != null) {
				type = ctx.characterStringType().accept(new CharacterStringTypeVisitor());
			}
			if(ctx.integerType() != null) {
				type = ctx.integerType().accept(new IntegerTypeVisitor());
			}
			if(ctx.OID_IRI_LITERAL() != null) {
				type = new IRIType();
			}			
			if(ctx.NULL_LITERAL() != null) {
				type = new NullType();
			}
			if(ctx.OCTET_LITERAL() != null) {
				type = new OctetStringType();
			}
			if(ctx.REAL_LITERAL() != null) {
				type = new RealType();
			}
			if(ctx.OBJECT_LITERAL() != null) {
				type = new ObjectIdentifierType();
			}			
			if(ctx.RELATIVE_OID_LITERAL() != null) {
				type = new RelativeOIDType();
			}			
			if(ctx.RELATIVE_OID_IRI_LITERAL() != null) {
				type = new RelativeIRIType();
			}			
			if(ctx.sequenceType() != null) {
				type = ctx.sequenceType().accept(new SequenceTypeVisitor());
			}
			if(ctx.setType() != null) {
				type = ctx.setType().accept(new SetTypeVisitor());
			}
			if(ctx.sequenceOfType() != null) {
				type = ctx.sequenceOfType().accept(new SequenceOfTypeVisitor());
			}
			if(ctx.setOfType() != null) {
				type = ctx.setOfType().accept(new SetOfTypeVisitor());
			}
			if(ctx.TIME_LITERAL() != null) {
				type = new TimeType();
			}
			if(ctx.TIME_OF_DAY_LITERAL() != null) {
				type = new TimeOfDayType();
			}
			if(ctx.prefixedType() != null) {
				type = ctx.prefixedType().taggedType().accept(new TaggedTypeVisitor());
			}
			return type;
		}
	}
	
	static class CharacterStringTypeVisitor extends ASNBaseVisitor<Type> {
		@Override
		public Type visitCharacterStringType(CharacterStringTypeContext ctx) {
			Type type = null;
			if(ctx.BMPString_LITERAL() != null) {
				type = new BMPStringType();
			}
			if(ctx.GeneralString_LITERAL() != null) {
				type = new GeneralStringType();
			}
			if(ctx.GraphicString_LITERAL() != null) {
				type = new GraphicStringType();
			}
			if(ctx.IA5String_LITERAL() != null) {
				type = new IA5StringType();
			}			
			if(ctx.ISO646String_LITERAL() != null) {
				type = new ISO646StringType();
			}			
			if(ctx.NumericString_LITERAL() != null) {
				type = new NumericStringType();
			}			
			if(ctx.PrintableString_LITERAL() != null) {
				type = new PrintableStringType();
			}			
			if(ctx.TeletexString_LITERAL() != null) {
				type = new TeletexStringType();
			}			
			if(ctx.T61String_LITERAL() != null) {
				type = new T61StringType();
			}			
			if(ctx.UniversalString_LITERAL() != null) {
				type = new UniversalStringType();
			}			
			if(ctx.UTF8String_LITERAL() != null) {
				type = new UTF8StringType();
			}			
			if(ctx.VideotexString_LITERAL() != null) {
				type = new VideotexStringType();
			}			
			if(ctx.VisibleString_LITERAL() != null) {
				type = new VisibleStringType();
			}			
			if(ctx.CHARACTER_LITERAL() != null) {
				type = new CharacterStringType();
			}			
			return type;
		}
	}
	
	static class BitStringTypeVisitor extends ASNBaseVisitor<BitStringType> {
		@Override
		public BitStringType visitBitStringType(BitStringTypeContext ctx) {
			BitStringType type = new BitStringType();
			if(ctx.namedBitList() != null) {
				type.setNamedBitList(ctx.namedBitList().accept(new NamedBitListVisitor()));
			}
			return type;
		}
	}
	
	static class NamedBitListVisitor extends ASNBaseVisitor<ArrayList<NamedNumber>> {
		@Override
		public ArrayList<NamedNumber> visitNamedBitList(NamedBitListContext ctx) {
			ArrayList<NamedNumber> namedBitList = new ArrayList<NamedNumber>();
			for(NamedBitContext namedBitContext : ctx.namedBit()) {
			    TokenLocation numberOrReferenceToken = null;
			    NamedNumber namedBit = null;
			    if(namedBitContext.LCASE_ID().size() == 2) {
			    	numberOrReferenceToken = new TokenLocation(namedBitContext.LCASE_ID(1).getSymbol().getLine(), namedBitContext.LCASE_ID(1).getSymbol().getCharPositionInLine()+1);
					namedBit = new NamedNumber(namedBitContext.LCASE_ID(0).getText(), namedBitContext.LCASE_ID(1).getText());
					namedBit.setNumberOrReferenceTokenLocation(numberOrReferenceToken);
				}
				else {
			    	numberOrReferenceToken = new TokenLocation(namedBitContext.NUMBER().getSymbol().getLine(), namedBitContext.NUMBER().getSymbol().getCharPositionInLine()+1);
					namedBit = new NamedNumber(namedBitContext.LCASE_ID(0).getText(), Integer.parseInt(namedBitContext.NUMBER().getText()));					
					namedBit.setNumberOrReferenceTokenLocation(numberOrReferenceToken);
				}
			    TokenLocation nameToken = new TokenLocation(namedBitContext.LCASE_ID(0).getSymbol().getLine(), namedBitContext.LCASE_ID(0).getSymbol().getCharPositionInLine()+1);
			    namedBit.setNameTokenLocation(nameToken);
			    namedBitList.add(namedBit);
			}
			return namedBitList;
		}
	}
	
	static class EnumeratedTypeVisitor extends ASNBaseVisitor<EnumeratedType> {
		@Override
		public EnumeratedType visitEnumeratedType(EnumeratedTypeContext ctx) {
			ArrayList<NamedNumber> rootEnumeration = ctx.enumeration(0).accept(new EnumerationTypeVisitor());
			ArrayList<NamedNumber> additionalEnumeration = null;
			if(ctx.ELLIPSIS() != null) {
				additionalEnumeration = new ArrayList<NamedNumber>();
			}
			if(ctx.enumeration().size() == 2) {
				additionalEnumeration = ctx.enumeration(1).accept(new EnumerationTypeVisitor());
			}
			return new EnumeratedType(rootEnumeration, additionalEnumeration);
		}
	}

	static class EnumerationTypeVisitor extends ASNBaseVisitor<ArrayList<NamedNumber>> {
		@Override
		public ArrayList<NamedNumber> visitEnumeration(EnumerationContext ctx) {
			ArrayList<NamedNumber> namedNumberList = new ArrayList<NamedNumber>();
			for (EnumerationItemContext enumerationItemContext : ctx.enumerationItem() ) {
				if(enumerationItemContext.LCASE_ID() != null) {
					NamedNumber namedNumber = new NamedNumber(enumerationItemContext.LCASE_ID().getText());
				    TokenLocation nameToken = new TokenLocation(enumerationItemContext.LCASE_ID().getSymbol().getLine(), enumerationItemContext.LCASE_ID().getSymbol().getCharPositionInLine()+1);
					namedNumber.setNameTokenLocation(nameToken);
					namedNumber.setNumberOrReferenceTokenLocation(nameToken);
					namedNumberList.add(namedNumber);
				}
				else {
					namedNumberList.add(enumerationItemContext.namedNumber().accept(new NamedNumberVisitor()));
				}
			}
			return namedNumberList;
		}
	}
	
	static class IntegerTypeVisitor extends ASNBaseVisitor<IntegerType> {
		@Override
		public IntegerType visitIntegerType(IntegerTypeContext ctx) {
			IntegerType type = new IntegerType();
			if(ctx.namedNumberList() != null) {
				type.setNamedNumberList(ctx.namedNumberList().accept(new NamedNumberListVisitor()));
			}
			return type;
		}
	}
	
	static class NamedNumberListVisitor extends ASNBaseVisitor<ArrayList<NamedNumber>> {
		@Override
		public ArrayList<NamedNumber> visitNamedNumberList(NamedNumberListContext ctx) {
			ArrayList<NamedNumber> namedNumberList = new ArrayList<NamedNumber>();
			for(NamedNumberContext namedNumberContext : ctx.namedNumber()) {
				namedNumberList.add(namedNumberContext.accept(new NamedNumberVisitor()));
			}
			return namedNumberList;
		}
	}
	
	static class NamedNumberVisitor extends ASNBaseVisitor<NamedNumber> {
		@Override
		public NamedNumber visitNamedNumber(NamedNumberContext ctx) {
			NamedNumber namedNumber;
			TerminalNode numberOrReferenceNode = null;
		    TokenLocation nameToken = new TokenLocation(ctx.LCASE_ID(0).getSymbol().getLine(), ctx.LCASE_ID(0).getSymbol().getCharPositionInLine()+1);
			if(ctx.LCASE_ID().size() == 2) {
				namedNumber = new NamedNumber(ctx.LCASE_ID(0).getText(), ctx.LCASE_ID(1).getText());
				numberOrReferenceNode = ctx.LCASE_ID(1);
			}
			else {
				String number = "";
				if(ctx.signedNumber().MINUS() != null) {
					number = "-";
					numberOrReferenceNode = ctx.signedNumber().MINUS();
				}
				number = number + ctx.signedNumber().NUMBER().getText();
				namedNumber = new NamedNumber(ctx.LCASE_ID(0).getText(), Integer.parseInt(number));		
				if(numberOrReferenceNode == null) {
					numberOrReferenceNode = ctx.signedNumber().NUMBER();
				}
			}
			namedNumber.setNameTokenLocation(nameToken);
		    TokenLocation numberOrReferenceToken = new TokenLocation(numberOrReferenceNode.getSymbol().getLine(), numberOrReferenceNode.getSymbol().getCharPositionInLine()+1);
			namedNumber.setNumberOrReferenceTokenLocation(numberOrReferenceToken);
			return namedNumber;
		}
	}
	
	static class ChoiceTypeVisitor extends ASNBaseVisitor<ChoiceType> {
		@Override
		public ChoiceType visitChoiceType(ChoiceTypeContext ctx) {
			ChoiceType choiceType = null;
			ArrayList<Component> rootAlternativeTypeList = ctx.alternativeTypeList().accept(new AlternativeTypeListVisitor());
			ArrayList<Component> additionalAlternativeList = null;
			if(ctx.extensionAndException() != null) {
				additionalAlternativeList = new ArrayList<Component>();
			}
			if(ctx.extensionAdditionAlternatives() != null) {
				additionalAlternativeList = ctx.extensionAdditionAlternatives().extensionAdditionAlternativesList().accept(new ExtensionAdditionAlternativesListVisitor());
			}
			choiceType = new ChoiceType(rootAlternativeTypeList, additionalAlternativeList);
			return choiceType;
		}
	}
	
	static class AlternativeTypeListVisitor extends ASNBaseVisitor<ArrayList<Component>> {
		@Override
		public ArrayList<Component> visitAlternativeTypeList(AlternativeTypeListContext ctx) {
			ArrayList<Component> alternativeTypeList = new ArrayList<Component>();
			for (NamedTypeContext namedTypeContext : ctx.namedType()) {
				NamedType namedType = new NamedType(namedTypeContext.LCASE_ID().getText(), namedTypeContext.type().accept(new TypeVisitor()));
				namedType.setTokenLocation(new TokenLocation(namedTypeContext.LCASE_ID().getSymbol().getLine(), namedTypeContext.LCASE_ID().getSymbol().getCharPositionInLine()+1));				
				alternativeTypeList.add(namedType);
			}
			return alternativeTypeList;
		}
	}
	
	static class ExtensionAdditionAlternativesListVisitor extends ASNBaseVisitor<ArrayList<Component>> {
		@Override
		public ArrayList<Component> visitExtensionAdditionAlternativesList(
				ExtensionAdditionAlternativesListContext ctx) {
			ArrayList<Component> alternativeTypeList = new ArrayList<Component>();
			for(ExtensionAdditionAlternativeContext extensionAdditionAlternativeContext : ctx.extensionAdditionAlternative()) {
				if(extensionAdditionAlternativeContext.namedType() != null) {
					alternativeTypeList.add(new NamedType(extensionAdditionAlternativeContext.namedType().LCASE_ID().getText(), extensionAdditionAlternativeContext.namedType().type().accept(new TypeVisitor())));									
				}
				if(extensionAdditionAlternativeContext.extensionAdditionAlternativesGroup() != null) {
					alternativeTypeList.addAll(extensionAdditionAlternativeContext.extensionAdditionAlternativesGroup().alternativeTypeList().accept(new AlternativeTypeListVisitor()));					
				}
			}
			return alternativeTypeList;
		}
	}
		
	static class SequenceTypeVisitor extends ASNBaseVisitor<SequenceType> {
		@Override
		public SequenceType visitSequenceType(SequenceTypeContext ctx) {
			ComponentTypeLists componentTypeLists = new ComponentTypeLists();
			componentTypeLists.rootComponentList = new ArrayList<Component>();
			if(ctx.componentTypeLists() != null) {
				componentTypeLists = ctx.componentTypeLists().accept(new ComponentTypeListsVisitor());
			}
			return new SequenceType(componentTypeLists.rootComponentList, componentTypeLists.extensionComponentList, componentTypeLists.additionalComponentList);
		}
	}

	static class SetTypeVisitor extends ASNBaseVisitor<SetType> {
		@Override
		public SetType visitSetType(SetTypeContext ctx) {
			ComponentTypeLists componentTypeLists = null;
			if(ctx.componentTypeLists() != null) {
				componentTypeLists = ctx.componentTypeLists().accept(new ComponentTypeListsVisitor());
			}
			return new SetType(componentTypeLists.rootComponentList, componentTypeLists.extensionComponentList, componentTypeLists.additionalComponentList);
		}
	}
				
	static class ComponentTypeLists {
		public ArrayList<Component> rootComponentList;
		public ArrayList<Component> extensionComponentList;
		public ArrayList<Component> additionalComponentList;		
	}
	
	static class ComponentTypeListsVisitor extends ASNBaseVisitor<ComponentTypeLists> {
		
		@Override
		public ComponentTypeLists visitRootComponentOnly(RootComponentOnlyContext ctx) {
			ArrayList<Component> rootComponentList = new ArrayList<Component>();
			for(ComponentTypeContext componentTypeContext :  ctx.componentTypeList().componentType()) {
				rootComponentList.add(componentTypeContext.accept(new ComponentTypeVisitor()));
			}
			ComponentTypeLists componentTypeLists = new ComponentTypeLists();
			componentTypeLists.rootComponentList = rootComponentList;
			return componentTypeLists;
		}
		
		@Override
		public ComponentTypeLists visitRootComponentsAndExtensions(RootComponentsAndExtensionsContext ctx) {
			ComponentTypeLists componentTypeLists = new ComponentTypeLists();
			componentTypeLists.rootComponentList = ctx.componentTypeList().accept(new ComponentTypeListVisitor());
			componentTypeLists.extensionComponentList = new ArrayList<Component>();
			if(ctx.extensionAdditions() != null) {
				componentTypeLists.extensionComponentList = ctx.extensionAdditions().extensionAdditionList().accept(new ExtensionAdditionListVisitor());
			}
			return componentTypeLists;
		}
		
		@Override
		public ComponentTypeLists visitRootComponentsAndExtensionsAndAdditions(
				RootComponentsAndExtensionsAndAdditionsContext ctx) {
			ComponentTypeLists componentTypeLists = new ComponentTypeLists();
			componentTypeLists.rootComponentList = ctx.componentTypeList(0).accept(new ComponentTypeListVisitor());
			componentTypeLists.extensionComponentList = new ArrayList<Component>();
			if(ctx.extensionAdditions() != null) {
				componentTypeLists.extensionComponentList = ctx.extensionAdditions().extensionAdditionList().accept(new ExtensionAdditionListVisitor());
			}
			componentTypeLists.additionalComponentList = ctx.componentTypeList(1).accept(new ComponentTypeListVisitor());
			return componentTypeLists;
		}
		
		@Override
		public ComponentTypeLists visitExtensionsAndAdditions(ExtensionsAndAdditionsContext ctx) {
			ComponentTypeLists componentTypeLists = new ComponentTypeLists();
			componentTypeLists.extensionComponentList = new ArrayList<Component>();
			if(ctx.extensionAdditions() != null) {
				componentTypeLists.extensionComponentList = ctx.extensionAdditions().extensionAdditionList().accept(new ExtensionAdditionListVisitor());
			}
			componentTypeLists.additionalComponentList = ctx.componentTypeList().accept(new ComponentTypeListVisitor());
			return componentTypeLists;
		}
		
		@Override
		public ComponentTypeLists visitExtensionsOnly(ExtensionsOnlyContext ctx) {
			ComponentTypeLists componentTypeLists = new ComponentTypeLists();
			componentTypeLists.extensionComponentList = new ArrayList<Component>();
			if(ctx.extensionAdditions() != null) {
				componentTypeLists.extensionComponentList = ctx.extensionAdditions().extensionAdditionList().accept(new ExtensionAdditionListVisitor());
			}
			componentTypeLists.additionalComponentList = new ArrayList<Component>();
			return componentTypeLists;
		}
	}
	
	static class ExtensionAdditionListVisitor extends ASNBaseVisitor<ArrayList<Component>> {
		@Override
		public ArrayList<Component> visitExtensionAdditionList(ExtensionAdditionListContext ctx) {
			ArrayList<Component> extensionComponentList = new ArrayList<Component>();
			for(ExtensionAdditionContext extensionAdditionContext : ctx.extensionAddition()) {
				if(extensionAdditionContext.componentType() != null) {
					extensionComponentList.add(extensionAdditionContext.componentType().accept(new ComponentTypeVisitor()));
				}
				if(extensionAdditionContext.extensionAdditionGroup() != null) {
					for(ComponentTypeContext componentTypeContext : extensionAdditionContext.extensionAdditionGroup().componentTypeList().componentType()) {
						extensionComponentList.add(componentTypeContext.accept(new ComponentTypeVisitor()));
					}
				}				
			}
			return extensionComponentList;
		}
	}
	
	static class ComponentTypeListVisitor extends ASNBaseVisitor<ArrayList<Component>> {
		@Override
		public ArrayList<Component> visitComponentTypeList(ComponentTypeListContext ctx) {
			ArrayList<Component> componentTypeList = new ArrayList<Component>();
			for(ComponentTypeContext componentTypeContext :  ctx.componentType()) {
				componentTypeList.add(componentTypeContext.accept(new ComponentTypeVisitor()));
			}
			return componentTypeList;
		}
	}
	
	static class ComponentTypeVisitor extends ASNBaseVisitor<Component> {
		@Override
		public Component visitComponentType(ComponentTypeContext ctx) {
			Component component = null;
			boolean isOptional = false;
			if(ctx.OPTIONAL_LITERAL() != null) {
				isOptional = true;
			}
			if(ctx.namedType() != null) {
				String name = ctx.namedType().LCASE_ID().getText();
				Type type = ctx.namedType().type().accept(new TypeVisitor());
				component = new NamedType(name, type, isOptional);
				component.setTokenLocation(new TokenLocation(ctx.namedType().LCASE_ID().getSymbol().getLine(), ctx.namedType().LCASE_ID().getSymbol().getCharPositionInLine()+1));
			}
			if(ctx.COMPONENTS_LITERAL() != null) {
				component = new ComponentsOf(ctx.type().accept(new TypeVisitor()));
				component.setTokenLocation(new TokenLocation(ctx.COMPONENTS_LITERAL().getSymbol().getLine(), ctx.COMPONENTS_LITERAL().getSymbol().getCharPositionInLine()+1));
			}
			return component;
		}
	}
	
	static class SequenceOfTypeVisitor extends ASNBaseVisitor<SequenceOfType> {
		@Override
		public SequenceOfType visitSequenceOfType(SequenceOfTypeContext ctx) {
			SequenceOfType sequenceOfType = null;
			if(ctx.namedType() != null) {
				String name = ctx.namedType().LCASE_ID().getText();
				Type type = ctx.namedType().type().accept(new TypeVisitor());
				sequenceOfType = new SequenceOfType(name, type);
			}
			else {
				Type type = ctx.type().accept(new TypeVisitor());
				sequenceOfType = new SequenceOfType(null, type);
			}
			return sequenceOfType;
		}
	}	
	
	static class SetOfTypeVisitor extends ASNBaseVisitor<SetOfType> {
		@Override
		public SetOfType visitSetOfType(SetOfTypeContext ctx) {
			SetOfType setOfType = null;
			if(ctx.namedType() != null) {
				String name = ctx.namedType().LCASE_ID().getText();
				Type type = ctx.namedType().type().accept(new TypeVisitor());
				setOfType = new SetOfType(name, type);
			}
			else {
				Type type = ctx.type().accept(new TypeVisitor());
				setOfType = new SetOfType(null, type);
			}
			return setOfType;
		}
	}	
	
	static class TaggedTypeVisitor extends ASNBaseVisitor<Type> {
		@Override
		public Type visitTaggedType(TaggedTypeContext ctx) {
			Type type = ctx.type().accept(new TypeVisitor());
			TagClass tagClass = null;
			if(ctx.PRIVATE_LITERAL() != null) {
				tagClass = TagClass.PRIVATE_TAG;
			}
			if(ctx.APPLICATION_LITERAL() != null) {
				tagClass = TagClass.APPLICATION_TAG;
			}
			if(ctx.UNIVERSAL_LITERAL() != null) {
				tagClass = TagClass.UNIVERSAL_TAG;
			}

			TagMode tagMode = null;
			if(ctx.EXPLICIT_LITERAL() != null) {
				tagMode = TagMode.EXPLICIT_TAG;
			}
			if(ctx.IMPLICIT_LITERAL() != null) {
				tagMode = TagMode.IMPLICIT_TAG;
			}
			
			Tag tag = null;
			if(ctx.NUMBER() != null) {
				Integer tagNumber = Integer.parseInt(ctx.NUMBER().getText());
				tag = new Tag(tagNumber, tagClass, tagMode);
			}
			
			if(ctx.LCASE_ID() != null) {
				tag = new Tag(ctx.LCASE_ID().getText(), tagClass, tagMode);
			}
			
			TokenLocation tagToken = new TokenLocation(ctx.LBRACKET().getSymbol().getLine(), ctx.LBRACKET().getSymbol().getCharPositionInLine()+1);
			tag.setTagTokenLocation(tagToken);
			type.insertTag(tag);
			
			return type;
		}
	}
	
	static class ValueAssignmentVisitor extends ASNBaseVisitor<ValueAssignment> {
		@Override
		public ValueAssignment visitValueAssignment(ValueAssignmentContext ctx) {
			String reference = ctx.LCASE_ID().getText();
			Value value = ctx.value().accept(new ValueVisitor());
			value.setType(ctx.type().accept(new TypeVisitor()));
			ValueAssignment valueAssignment = new ValueAssignment();
			valueAssignment.setReference(reference);
			valueAssignment.setReferenceTokenLocation(new TokenLocation(ctx.LCASE_ID().getSymbol().getLine(), ctx.LCASE_ID().getSymbol().getCharPositionInLine()+1));
			valueAssignment.setValue(value);
			return valueAssignment;
		}
	}

	static class ValueVisitor extends ASNBaseVisitor<Value> {
		@Override
		public Value visitValue_BOOLEAN(Value_BOOLEANContext ctx) {
			BooleanValue value = null;
			if(ctx.TRUE_LITERAL() != null) {
				value = new BooleanValue(true);
			}
			else {
				value = new BooleanValue(false);
			}
			return value;
		}
		
		@Override
		public Value visitValue_NULL(Value_NULLContext ctx) {
			return new NullValue();
		}
		
		@Override
		public Value visitValue_Integer(Value_IntegerContext ctx) {
			return new IntegerValue(Integer.parseInt(ctx.getText()));
		}

		@Override
		public Value visitValueReference(ValueReferenceContext ctx) {
			ValueReference value = new ValueReference();
			if(ctx.UCASE_ID() != null) {
				value.setReferencedModuleName(ctx.UCASE_ID().getText());
				value.setTokenLocation(new TokenLocation(ctx.UCASE_ID().getSymbol().getLine(), ctx.UCASE_ID().getSymbol().getCharPositionInLine()+1));
			}
			else {
				value.setTokenLocation(new TokenLocation(ctx.LCASE_ID().getSymbol().getLine(), ctx.LCASE_ID().getSymbol().getCharPositionInLine()+1));				
			}
			value.setReferencedValueName(ctx.LCASE_ID().getText());
			return value;

		}
		
		@Override
		public Value visitValue_CSTRING(Value_CSTRINGContext ctx) {
			return new CharacterStringValue(ctx.getText());
		}
		
		@Override
		public Value visitValue_BSTRING(Value_BSTRINGContext ctx) {
			return new BinaryStringValue(ctx.getText());
		}
		
		@Override
		public Value visitValue_HSTRING(Value_HSTRINGContext ctx) {
			return new HexaStringValue(ctx.getText());
		}
		
		@Override
		public Value visitValue_Choice(Value_ChoiceContext ctx) {
			return new ChoiceValue(ctx.LCASE_ID().getText(), ctx.value().accept(new ValueVisitor()));
		}
		
		@Override
		public Value visitValue_EmptyList(Value_EmptyListContext ctx) {
			return new EmptyListValue();
		}
		
		@Override
		public Value visitValue_NamedValueList(Value_NamedValueListContext ctx) {
			ArrayList<NamedValue> valueList = new ArrayList<NamedValue>();
			for(NamedValueContext valueContext : ctx.namedValue()) {
				NamedValue namedValue = new NamedValue(valueContext.LCASE_ID().getText(), valueContext.accept(new ValueVisitor()));
				valueList.add(namedValue);
			}
			return new NamedValueListValue(valueList);
		}
		
		@Override
		public Value visitValue_ValueList(Value_ValueListContext ctx) {
			ArrayList<Value> valueList = new ArrayList<Value>();
			for(ValueContext valueContext : ctx.value()) {
				valueList.add(valueContext.accept(new ValueVisitor()));
			}
			return new ValueListValue(valueList);
		}	
	}
}

