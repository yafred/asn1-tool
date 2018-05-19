# ASN.1 Tool

[![Build Status](https://travis-ci.org/yafred/asn1-tool.svg?branch=master)](https://travis-ci.org/yafred/asn1-tool)
[![Coverage Status](https://coveralls.io/repos/github/yafred/asn1-tool/badge.svg?branch=master)](https://coveralls.io/github/yafred/asn1-tool?branch=master)

## Using the tool
  
  * Download [latest release](https://github.com/yafred/asn1-tool/releases) 
  * java -jar asn1-tool.jar
     * -f \<input file> ASN.1 specification.
     * -p just print the validated model.
     * -jo \<path> generate Java code in the given folder.
     * -jp \<package> use this package as a prefix for generated Java code (ASN.1 module names are added to this prefix to get the full package name).
  * You can create an alias with alias on Linux or doskey on Windows 

## Warning

This tool is in development
  
## Example

### Write your ASN.1 specification

```
G-009 DEFINITIONS AUTOMATIC TAGS ::= 
BEGIN 

Flight ::= SEQUENCE {
   origin             IA5String,
   destination        IA5String,
   seats  INTEGER,
   crew-format ENUMERATED { six, eight, ten }
}

END
```

### Validate it

```
java -jar asn1-tool.jar -f your_spec.asn -p

G-009 DEFINITIONS AUTOMATIC TAGS ::=
BEGIN

   EXPORTS ALL;
   IMPORTS;

   Flight ::= SEQUENCE {
      origin [0] IMPLICIT IA5String,
      destination [1] IMPLICIT IA5String,
      seats [2] IMPLICIT INTEGER,
      crew-format [3] IMPLICIT ENUMERATED { six(0), eight(1), ten(2) }
   }

END
```

### Generate the java code 

```
java -jar asn1-tool.jar -f your_spec.asn -jo -jo your_ouput_folder
```

### Integrate the java bindings in your code

```
Flight obj = new Flight();
obj.setOrigin("Rome");
obj.setDestination("London");
obj.setSeats(Integer.valueOf(250));
obj.setCrew_format(Flight.Crew_format.eight);
```

### Use the BER encoders/decoders to serialize/deserialize your objects

```
// encode a Flight
ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
BERWriter writer = new BERWriter(bufferOut);
Flight.writePdu(obj, writer);

byte[] berEncoded = bufferOut.toByteArray(); 

// decode a Flight
ByteArrayInputStream input = new ByteArrayInputStream(berEncoded);
BERReader reader = new BERReader(input);
obj = Flight.readPdu(reader);
```
