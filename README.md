# ASN.1 Tool

[![Build Status](https://travis-ci.org/yafred/asn1-tool.svg?branch=master)](https://travis-ci.org/yafred/asn1-tool)
[![Coverage Status](https://coveralls.io/repos/github/yafred/asn1-tool/badge.svg?branch=master)](https://coveralls.io/github/yafred/asn1-tool?branch=master)

## Test the tool (as a user)
  
  * Download [latest release](https://github.com/yafred/asn1-tool/releases) 
  * java -jar asn1-tool.jar
  * You can create an alias with alias on Linux or doskey on Windows 
  
## Test the tool (as a developer)

  * mvn clean package

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

  * Java code generator for encoding/decoding values from the specification: _generator/src/main/java/com/yafred/asn1/generator/java_
  * Java Runtime libraries to help running the generated code: _runtime/src/main/java/com/yafred/asn1/runtime_


