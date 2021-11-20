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


/*
Built with Antlr 4.7.1 : java org.antlr.v4.Tool ASN.g
Tested with Antlr 4.7.1 : java org.antlr.v4.gui.TestRig

Credits: http://www.antlr.org/

Guidance: Rec. ITU-T X.680 (08/2015)
*/


grammar ASN;

/*--------------------- Specification (content of an asn.1 specification file) ------*/

specification:
    configComments ?
    moduleDefinition*
;

/*--------------------- Module definition -------------------------------------------*/

/* ModuleDefinition (see 13 in ITU-T X.680 (08/2015) */
moduleDefinition:  
    moduleIdentifier
    DEFINITIONS_LITERAL
    // encodingReferenceDefault?
    tagDefault?
    extensionDefault?
    ASSIGN
    BEGIN_LITERAL
    ( exports? imports? assignment assignment* )?   // moduleBody
    // encodingControlSections?
    END_LITERAL
;

moduleIdentifier: 
    UCASE_ID  // modulereference
    ( definitiveOID  /* iRIValue? */ )?    // DefinitiveIdentification
;

definitiveOID:
    LCURLY
    definitiveObjIdComponent definitiveObjIdComponent*
    RCURLY
;

definitiveObjIdComponent:
    LCASE_ID     
    | NUMBER     
    | ( LCASE_ID LPAREN NUMBER RPAREN )
;

tagDefault:
    ( EXPLICIT_LITERAL | IMPLICIT_LITERAL | AUTOMATIC_LITERAL ) TAGS_LITERAL
;

extensionDefault:
    EXTENSIBILITY_LITERAL IMPLIED_LITERAL
;

exports:
    EXPORTS_LITERAL ( symbolList? | ALL_LITERAL ) SEMI 
;

imports:
    IMPORTS_LITERAL symbolsFromModuleList? SEMI
;

symbolsFromModuleList:
    symbolsFromModule symbolsFromModule*
;

symbolsFromModule:
    symbolList FROM_LITERAL globalModuleReference
;

globalModuleReference:
    UCASE_ID assignedIdentifier?
;

assignedIdentifier:
    definitiveOID  
    | LCASE_ID // definedValue
;

symbolList:
    symbol ( COMMA symbol )*
;

symbol:
    // reference | parameterizedReference (we simplify)
    UCASE_ID | LCASE_ID
;

assignment:
    typeAssignment
    | valueAssignment
//  | xMLValueAssignment
//  | valueSetTypeAssignment
//  | objectClassAssignment
//  | objectAssignment
//  | objectSetAssignment
//  | parameterizedAssignment
;


/*--------------------- Types -------------------------------------------------------*/


/* TypeAssignment (see 16.1 in ITU-T X.680 (08/2015) */
typeAssignment:
    UCASE_ID // typereference
    ASSIGN
    type
;

/* BuiltinType (see 17.1 in ITU-T X.680 (08/2015) */
type:
    builtinType constraint?
    | referencedType constraint?
    //| constrainedType 
;

builtinType:
    bitStringType
    | BOOLEAN_LITERAL // BooleanType
    | characterStringType
    | choiceType
    | DATE_LITERAL  // DateType
    | DATE_TIME_LITERAL // DateTimeType
    | DURATION_LITERAL  // DurationType
    | EMBEDDED_LITERAL PDV_LITERAL // EmbeddedPDVType
    | enumeratedType
    | EXTERNAL_LITERAL // ExternalType
//  | InstanceOfType (Rec. ITU-T X.681 | ISO/IEC 8824-2, Annex C)
    | integerType
    | OID_IRI_LITERAL // IRIType
    | NULL_LITERAL // NullType
//  | ObjectClassFieldType (Rec. ITU-T X.681 | ISO/IEC 8824-2, 14.1)
    | OBJECT_LITERAL IDENTIFIER_LITERAL // ObjectIdentifierType
    | OCTET_LITERAL STRING_LITERAL // OctetStringType
    | REAL_LITERAL  // RealType
    | RELATIVE_OID_IRI_LITERAL // RelativeIRIType
    | RELATIVE_OID_LITERAL // RelativeOIDType
    | sequenceType
    | sequenceOfType
    | setType
    | setOfType
    | prefixedType 
    | TIME_LITERAL // TimeType
    | TIME_OF_DAY_LITERAL // TimeOfDayType
;

/* ReferencedType (see 17.3 in ITU-T X.680 (08/2015) */
referencedType:
    usefulType
    | definedType
    | selectionType
//  | TypeFromObject
//  | ValueSetFromObjects
;

definedType:
    UCASE_ID  // typereference
    | ( UCASE_ID DOT UCASE_ID )  // modulereference.typereference
;

usefulType:
   GeneralizedTime_LITERAL
   | UTCTime_LITERAL
   | ObjectDescriptor_LITERAL
;

/* SelectionType (see 30 in ITU-T X.680 (08/2015) */
selectionType:
    LCASE_ID LESS_THAN type
;

/* BitStringType (see 22.1 in ITU-T X.680 (08/2015) */
bitStringType:
    BIT_LITERAL STRING_LITERAL
    ( LCURLY namedBitList RCURLY )?
;

namedBitList:
    namedBit ( COMMA namedBit )*
;

namedBit:
    LCASE_ID LPAREN NUMBER RPAREN
    | LCASE_ID LPAREN LCASE_ID RPAREN
;

/* EnumeratedType (see 20.1 in ITU-T X.680 (08/2015) */
enumeratedType:
    ENUMERATED_LITERAL
    LCURLY 
    enumeration
    ( COMMA ELLIPSIS /* exceptionSpec? */ ( COMMA enumeration )? )? 
    RCURLY 
;

enumeration:
    enumerationItem ( COMMA enumerationItem )*
;

enumerationItem:
    LCASE_ID | namedNumber
;

/* IntegerType (see 19.1 in ITU-T X.680 (08/2015) */
integerType:
    INTEGER_LITERAL 
    ( LCURLY namedNumberList RCURLY )?
;

namedNumberList:
    namedNumber ( COMMA namedNumber )*
;

namedNumber:
    LCASE_ID LPAREN signedNumber RPAREN
    | LCASE_ID LPAREN LCASE_ID RPAREN   // definedValue
;

signedNumber:
    MINUS? NUMBER
;


/* CharacterStringType (see 40.1 in ITU-T X.680 (08/2015) */
characterStringType:
    // RestrictedCharacterString types
    BMPString_LITERAL
    | GeneralString_LITERAL
    | GraphicString_LITERAL
    | IA5String_LITERAL
    | ISO646String_LITERAL
    | NumericString_LITERAL
    | PrintableString_LITERAL
    | TeletexString_LITERAL
    | T61String_LITERAL
    | UniversalString_LITERAL
    | UTF8String_LITERAL
    | VideotexString_LITERAL
    | VisibleString_LITERAL
    // UnrestrictedCharacterString type
    | CHARACTER_LITERAL STRING_LITERAL
;

/* ChoiceType (see 29.1 in ITU-T X.680 (08/2015) */
choiceType:
    CHOICE_LITERAL 
    LCURLY  
    alternativeTypeList ( COMMA extensionAndException extensionAdditionAlternatives? extensionMarker? )?
    RCURLY
;

extensionAdditionAlternatives:
    COMMA extensionAdditionAlternativesList
;

extensionAdditionAlternativesList:
    extensionAdditionAlternative ( COMMA extensionAdditionAlternative )*
;

extensionAdditionAlternative:
    extensionAdditionAlternativesGroup
    | namedType
;

extensionAdditionAlternativesGroup:
    DOUBLE_LBRACKET ( NUMBER COLON )? alternativeTypeList DOUBLE_RBRACKET
;

alternativeTypeList:
   namedType ( COMMA namedType )*
;

namedType:
    LCASE_ID type
;

/* SequenceType (see 25 in ITU-T X.680 (08/2015) */
sequenceType:
    SEQUENCE_LITERAL LCURLY componentTypeLists? RCURLY
;

/* SetType (see 27 in ITU-T X.680 (08/2015) */
setType:
    SET_LITERAL LCURLY componentTypeLists? RCURLY
;

componentTypeLists:
    componentTypeList  # rootComponentOnly
    | componentTypeList COMMA extensionAndException extensionAdditions? extensionMarker? # rootComponentsAndExtensions
    | componentTypeList COMMA extensionAndException extensionAdditions? extensionMarker COMMA componentTypeList # rootComponentsAndExtensionsAndAdditions
    | extensionAndException extensionAdditions? extensionMarker COMMA componentTypeList # extensionsAndAdditions
    | extensionAndException extensionAdditions? extensionMarker? # extensionsOnly
;

extensionAndException:
    ELLIPSIS exceptionSpec? 
;

exceptionSpec:
    EXCLAMATION exceptionIdentification
;

exceptionIdentification:
    signedNumber
    | LCASE_ID  // definedValue
    | type COLON value
;

extensionMarker:
    COMMA ELLIPSIS
;

extensionAdditions:
    COMMA extensionAdditionList
;

extensionAdditionList:
    extensionAddition ( COMMA extensionAddition )*
;

extensionAddition:
    componentType
    | extensionAdditionGroup
;

extensionAdditionGroup:
    DOUBLE_LBRACKET ( NUMBER COLON )? componentTypeList DOUBLE_RBRACKET
;

componentTypeList:
    componentType ( COMMA componentType )*
;

componentType:
    namedType
    | namedType OPTIONAL_LITERAL
    | namedType DEFAULT_LITERAL value
    | COMPONENTS_LITERAL OF_LITERAL type
;

/* SequenceOfType (see 26 in ITU-T X.680 (08/2015) */
sequenceOfType:
    SEQUENCE_LITERAL ( SIZE_LITERAL? constraint )? OF_LITERAL ( namedType | type )
;

/* SetOfType (see 28 in ITU-T X.680 (08/2015) */
setOfType:
    SET_LITERAL ( SIZE_LITERAL? constraint )? OF_LITERAL ( namedType | type )
;

/* PrefixedType  (see 31 in ITU-T X.680 (08/2015) */
prefixedType:
    taggedType
//  | encodingPrefixedType
;

taggedType:
    LBRACKET 
    ( UCASE_ID COLON )?   /* encodingreference (see 12.25 in ITU-T X.680 (08/2015) should be all uppercase but it creates ambiguities */
    ( UNIVERSAL_LITERAL | APPLICATION_LITERAL | PRIVATE_LITERAL )?   // Class
    ( NUMBER | LCASE_ID ) // ClassNumber
    RBRACKET 
    ( IMPLICIT_LITERAL | EXPLICIT_LITERAL )?
    type
;

/*--------------------- Values ------------------------------------------------------*/

valueAssignment:
    LCASE_ID
    type
    ASSIGN
    value
;

/* For values, we can only recognize patterns. Validation can only be done in context */
value:
    CSTRING                                                    # value_CSTRING
    | BSTRING                                                  # value_BSTRING  
    | HSTRING                                                  # value_HSTRING    
    | LCURLY RCURLY                                            # value_EmptyList    
    | (TRUE_LITERAL | FALSE_LITERAL)                           # value_BOOLEAN
    | LCASE_ID COLON value                                     # value_Choice    
    | LCURLY namedValue ( COMMA namedValue )* RCURLY           # value_NamedValueList
    | LCURLY objIdComponents  objIdComponents* RCURLY          # value_ObjectIdentifier
    | LCURLY value ( COMMA value )* RCURLY                     # value_ValueList
    | MINUS? ( NUMBER | NUMBER_WITH_DECIMALS )                 # value_Number
    | NULL_LITERAL                                             # value_NULL
    | valueReference                                           # value_ReferencedValue
;

namedValue:
    LCASE_ID value
;

valueReference:
    LCASE_ID                          // valuereference                      
    | ( UCASE_ID DOT LCASE_ID )       // modulereference.valuereference                       
;

objIdComponents:
    LCASE_ID
    | NUMBER
    | LCASE_ID LPAREN NUMBER RPAREN
;


/*--------------------- Constraints -------------------------------------------------*/

/* Constraint (see 49.6 in ITU-T X.680 (08/2015) */
/* Constraint ::= "(" ConstraintSpec ExceptionSpec ")" */
constraint:
    LPAREN 
    subtypeConstraint
//  ( EXCLAMATION exceptionIdentification )?
    RPAREN
;

subtypeConstraint:
    elementSetSpec ( COMMA ELLIPSIS elementSetSpec? )?
;

elementSetSpec:
    unions
	| ALL_LITERAL EXCEPT_LITERAL elements   // exclusions
;

unions:
    intersections ( ( PIPE | UNION_LITERAL ) intersections )*
;

intersections:
    intersectionElements ( ( POWER | INTERSECTION_LITERAL ) intersectionElements)*
;

intersectionElements:
    elements ( EXCEPT_LITERAL elements)?
;

elements:
    subtypeElements
    | LPAREN elementSetSpec RPAREN
;

/* Subtype elements (see 51 in ITU-T X.680 (08/2015) */
subtypeElements:
    value # singleValue
    | INCLUDES_LITERAL? type # containedSubType
    | valueRange # range // including DurationRange, TimePointRange, RecurrenceRange 
    | SIZE_LITERAL constraint # size
    | type # typeConstraint
    | FROM_LITERAL constraint # permittedAlphabet
    | WITH_LITERAL COMPONENT_LITERAL constraint # innerType
    | WITH_LITERAL COMPONENTS_LITERAL LCURLY ( ELLIPSIS COMMA )? typeConstraints RCURLY # innerTypeMultiple
    | PATTERN_LITERAL CSTRING # pattern
 // | SETTINGS_LITERAL propertyAndSettingPair propertyAndSettingPair*
;

valueRange:
    ( value | MIN_LITERAL ) RANGE ( value | MAX_LITERAL ) # closedRange
    | value LESS_THAN # lowerEndPoint
    | LESS_THAN value # upperEndPoint
;

typeConstraints:
    namedConstraint (COMMA namedConstraint)*
;

namedConstraint:
    LCASE_ID constraint? ( PRESENT_LITERAL | ABSENT_LITERAL | OPTIONAL_LITERAL )?
;

/* Additions */
configComments:
    CONFIG_COMMENT CONFIG_COMMENT*
;

/*--------------------- LITERAL -----------------------------------------------------*/

DEFINITIONS_LITERAL:
    'DEFINITIONS'
;

BEGIN_LITERAL:
    'BEGIN'
;

END_LITERAL:
    'END'
;

INTEGER_LITERAL:
    'INTEGER'
;

EXPLICIT_LITERAL:
    'EXPLICIT'
;

IMPLICIT_LITERAL:
    'IMPLICIT'
;

AUTOMATIC_LITERAL:
    'AUTOMATIC'
;

TAGS_LITERAL:
    'TAGS'
;

EXTENSIBILITY_LITERAL:
    'EXTENSIBILITY'
;

IMPLIED_LITERAL:
    'IMPLIED'
;

EXPORTS_LITERAL:
    'EXPORTS'
;

IMPORTS_LITERAL:
    'IMPORTS'
;

FROM_LITERAL:
    'FROM'
;

ALL_LITERAL:
    'ALL'
;

BOOLEAN_LITERAL:
    'BOOLEAN'
;

NULL_LITERAL:
    'NULL'
;

BMPString_LITERAL:
    'BMPString'
;

GeneralString_LITERAL:
    'GeneralString'
;

GraphicString_LITERAL:
    'GraphicString'
;

IA5String_LITERAL:
    'IA5String'
;

ISO646String_LITERAL:
    'ISO646String'
;

NumericString_LITERAL:
    'NumericString'
;

PrintableString_LITERAL:
    'PrintableString'
;

TeletexString_LITERAL:
    'TeletexString'
;

T61String_LITERAL:
    'T61String'
;

UniversalString_LITERAL:
    'UniversalString'
;

UTF8String_LITERAL:
    'UTF8String'
;

VideotexString_LITERAL:
    'VideotexString'
;

VisibleString_LITERAL:
    'VisibleString'
;

CHARACTER_LITERAL:
    'CHARACTER'
;

OCTET_LITERAL:
    'OCTET'
;

STRING_LITERAL:
    'STRING'
;

REAL_LITERAL:
    'REAL'
;

BIT_LITERAL:
    'BIT'
;

DATE_LITERAL:
    'DATE'
;

DATE_TIME_LITERAL:
    'DATE-TIME'
;

DURATION_LITERAL:
    'DURATION'
;

TIME_LITERAL:
    'TIME'
;

TIME_OF_DAY_LITERAL:
    'TIME-OF-DAY'
;

ENUMERATED_LITERAL:
    'ENUMERATED'
;

EMBEDDED_LITERAL:
    'EMBEDDED'
;

PDV_LITERAL:
    'PDV'
;

EXTERNAL_LITERAL:
    'EXTERNAL'
;

OID_IRI_LITERAL:
    'OID-IRI'
;

OBJECT_LITERAL:
    'OBJECT'
;

IDENTIFIER_LITERAL:
    'IDENTIFIER'
;

RELATIVE_OID_IRI_LITERAL:
    'RELATIVE-OID-IRI'
;

RELATIVE_OID_LITERAL:
    'RELATIVE-OID'
;

CHOICE_LITERAL:
    'CHOICE'
;

SEQUENCE_LITERAL:
    'SEQUENCE'
;

SET_LITERAL:
    'SET'
;

OF_LITERAL:
    'OF'
;

UNIVERSAL_LITERAL:
    'UNIVERSAL'
;

APPLICATION_LITERAL:
    'APPLICATION'
;

PRIVATE_LITERAL:
    'PRIVATE'
;

OPTIONAL_LITERAL:
    'OPTIONAL'
;

COMPONENTS_LITERAL:
    'COMPONENTS'
;

PATTERN_LITERAL:
    'PATTERN'
;

TRUE_LITERAL:
    'TRUE'
;

FALSE_LITERAL:
    'FALSE'
;

SIZE_LITERAL:
    'SIZE'
;

EXCEPT_LITERAL:
    'EXCEPT'
;

UNION_LITERAL:
    'UNION'
;

INTERSECTION_LITERAL:
    'INTERSECTION'
;

MIN_LITERAL:
    'MIN'
;

MAX_LITERAL:
    'MAX'
;

INCLUDES_LITERAL:
    'INCLUDES'
;

WITH_LITERAL:
    'WITH'
;

COMPONENT_LITERAL:
    'COMPONENT'
;

PRESENT_LITERAL:
    'PRESENT'
;

ABSENT_LITERAL:
    'ABSENT'
;

DEFAULT_LITERAL:
    'DEFAULT'
;

GeneralizedTime_LITERAL:
    'GeneralizedTime'
;

UTCTime_LITERAL:
    'UTCTime'
;

ObjectDescriptor_LITERAL:
    'ObjectDescriptor'
;

/*--------------------- Lexical Items -----------------------------------------------*/

ASSIGN:
    '::='
;

LCURLY:
    '{'
;

RCURLY:
    '}'
;

LBRACKET:
    '['
;

RBRACKET:
    ']'
;

DOUBLE_LBRACKET:
    '[['
;

DOUBLE_RBRACKET:
    ']]'
;

LPAREN:
    '('
;

RPAREN:
    ')'
;

SEMI:
    ';'
;

COMMA:
    ','
;

MINUS:
    '-'
;

DOT:
    '.'
;

LESS_THAN:
    '<'
;

COLON:
    ':'
;

APOSTROPHE:
    '\''
;

ELLIPSIS:
    '...'
;

RANGE:
    '..'
;

POWER:	
    '^'
;

PIPE:
    '|'
;

EXCLAMATION:
    '!'
;





BSTRING: 
    APOSTROPHE ('0'..'1')* '\'B'
;

HSTRING:
    APOSTROPHE ('0'..'9'|'a'..'f'|'A'..'F')* '\'H'
;

CSTRING:  
    '"' ( EscapeSequence | ~('\\'|'"') )* '"'
;

fragment EscapeSequence:   
    '\\' ('b'|'t'|'n'|'f'|'r'|'"'|APOSTROPHE|'\\')
;

/* typereference (see 12.2 in ITU-T X.680 (08/2015) */
UCASE_ID:
    ('A'..'Z') ('-'('a'..'z'|'A'..'Z'|'0'..'9')|('a'..'z'|'A'..'Z'|'0'..'9'))* 
;

LCASE_ID:
    ('a'..'z') ('-'('a'..'z'|'A'..'Z'|'0'..'9')|('a'..'z'|'A'..'Z'|'0'..'9'))* 
;
   
/* comments (see 12.6 in ITU-T X.680 (08/2015) */
CONFIG_COMMENT: 
    '-->' ~('\n'|'\r')* '\r'? '\n' 
;

LINE_COMMENT: 
    '--' ~('\n'|'\r')* '\r'? '\n' ->skip
; 

/* white-space (see 12.1.6 in ITU-T X.680 (08/2015) */
WS:
    (' '|'\r'|'\t'|'\n') -> skip
;

/* number (see 12.8 in ITU-T X.680 (08/2015) */
NUMBER:
    '0' | ('1'..'9')('0'..'9')*
;

NUMBER_WITH_DECIMALS:
    ('0'..'9')('0'..'9')* '.' ('0'..'9')('0'..'9')*
;

OTHER:
    .
;
