# ASN.1 Tool

[![Build Status](https://travis-ci.org/yafred/asn1-tool.svg?branch=master)](https://travis-ci.org/yafred/asn1-tool)

## Test the tool (as a user)
  
  * Download [latest release](https://github.com/yafred/asn1-tool/releases) 
  * java -classpath asn1Tool-vX.Y.Z.jar com.yafred.asn1.Tool input.asn
  
## Test the tool (as a developer)

  * mvn package

## Parsing

ASN.1 specifications are transformed in a Java model using an [Antlr](http://www.antlr.org/) visitor.

  * Grammar: _grammar/src/main/antlr4/com/yafred/asn1/grammar/ASN.g4_
  * Java Model: _model/src/main/java/com/yafred/asn1/model_
  * Visitor: _model/src/main/java/com/yafred/asn1/parser/SpecificationAntlrVisitor.java_
  * Building the Java model: _model/src/main/java/com/yafred/asn1/parser/Asn1ModelBuilder.java_
  * Using the Java model to write an ASN.1 specification: _model/src/main/java/com/yafred/asn1/parser/Asn1SpecificationWriter.java_
  * Test resources: _testdata/src/test/resources/com/yafred/asn1/test_
  * JUnit tests: _grammar/src/test/java/com/yafred/asn1/grammar/test/ParameterizedTest.java_
  * JUnit tests: _model/src/test/java/com/yafred/asn1/model/test/ParameterizedTest.java_

## Validation

Check that the Java model complies with the rules described in ITU-T X.680 (08/2015) 

  * Validating the Java model: _model/src/main/java/com/yafred/asn1/parser/Asn1ModelValidator.java_
  
## Code generation

Generate source code to encode and decode values from types described in a ASN.1 specification

  * Java code generator for encoding/decoding values from the specification (Not yet)
  * Java Runtime libraries to help running the generated code (Not yet)


