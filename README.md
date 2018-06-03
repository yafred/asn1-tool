#-------------------------------------------------------------------------------
# Copyright (C) 2018 Fred D7e (https://github.com/yafred)
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#-------------------------------------------------------------------------------
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
java -jar asn1-tool.jar -f your_spec.asn -jo your_ouput_folder
```

### Integrate the java bindings in your code

```
Flight obj = new Flight();
obj.setOrigin("Rome");
obj.setDestination("London");
obj.setSeats(Integer.valueOf(250));
obj.setCrewFormat(Flight.CrewFormat.EIGHT);
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
