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
package com.yafred.asn1.runtime;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.BitSet;

public class ASNValueReader {
	
	static private String skipChars = " \t\r\n";
	static private String tokens = ",{}:";
	private Reader reader;
	private String putAsideToken = "";
	
	public ASNValueReader(InputStream in) {
		reader = new BufferedReader(new InputStreamReader(in));
    }
		
	public String lookAheadToken() throws Exception {
		String ret = "";
		
		reader.mark(1000);

		int c;
		// find first significant character
		do {
			c = reader.read();
			if(c == -1) return ""; // end of stream
		} while(-1 != skipChars.indexOf(c));

		ret = ret + (char)c;
		
		reader.reset();
		
		return ret;
	}
	
	public String readToken() throws Exception {
		String token = "";
		
		if(!putAsideToken.contentEquals("")) {
			token = putAsideToken;
			putAsideToken = "";
		}
		else {
			int c;
			// find first significant character
			do {
				c = reader.read();
				if(c == -1) return ""; // end of stream
			} while(-1 != skipChars.indexOf(c));
			
			if(-1 == tokens.indexOf(c)) {
				throw new Exception("Expecting a token, read '" + (char)c + "'");
			}
			token = token + (char)c;
		}
		
		return token;
	}

	public String lookAheadIdentifier() throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		int c = -1;
		
		reader.mark(1000);

		// find first significant character
		do {
			c = reader.read();
			if(c == -1) return ""; // end of stream
		} while(-1 != skipChars.indexOf(c));
		
		// read until space or token
		while(-1 == skipChars.indexOf(c) && -1 == tokens.indexOf(c) && c != -1) {
			stringBuffer.append((char)c);
			c = reader.read();
		} 
		
		reader.reset();
		
		return stringBuffer.toString();
	}
	
	public String readIdentifier() throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		int c = -1;
		
		// find first significant character
		do {
			c = reader.read();
			if(c == -1) return ""; // end of stream
		} while(-1 != skipChars.indexOf(c));
		
		// read until space or token
		while(-1 == skipChars.indexOf(c) && -1 == tokens.indexOf(c) && c != -1) {
			stringBuffer.append((char)c);
			c = reader.read();
		} 
		
		// keep token if one was found
		if(-1 != tokens.indexOf(c)) {
			putAsideToken = "" + (char)c;
		}		
		
		return stringBuffer.toString();
	}
	
	public java.lang.Integer readInteger() throws Exception {
		String integerAsString = readIdentifier(); 
		return Integer.valueOf(integerAsString);
	}

	// readBitString assumes 'xxx'B or 'xxx'H is in the reader
	// lookAheadToken must be used to evacuate { bit1, bit3 } notation
	public BitSet readBitString() throws Exception {
		BitSet ret = null;
	
		StringBuffer stringBuffer = new StringBuffer();
		int c = -1;
		
		// find first significant character
		do {
			c = reader.read();
			if(c == -1) return null; // end of stream
		} while(-1 != skipChars.indexOf(c));

		// must be a ' (single quote)
		if((char)c != '\'') {
			throw new Exception("BITSTRING value must start with \'");
		}
		
		// read until ' (single quote)
		c = reader.read();
		while(c != '\'' && c != -1) {
			stringBuffer.append((char)c);
			c = reader.read();
		} 

		if((char)c != '\'') {
			throw new Exception("BITSTRING value must end with \'H or \'B");
		}
		
		c = reader.read();
		switch((char)c) {
		case 'B':
			ret = bitStringToBitSet(stringBuffer.toString());
			break;
			
		case 'H':
			ret = octetStringToBitSet(stringBuffer.toString());
			break;
			
		default:
			throw new Exception("BITSTRING value must end with \'H or \'B");
		}	

		return ret;
	}

	public java.lang.Boolean readBoolean() throws Exception {
		Boolean ret = null;
		String booleanAsString = readIdentifier(); 
		switch(booleanAsString) {
		case "TRUE":
			ret = Boolean.TRUE;
			break;
		case "FALSE":
			ret = Boolean.FALSE;
			break;
		default:
			throw new Exception("Boolean must be either TRUE or FALSE");
		}
		return ret;
	}
	
	public java.lang.Object readNull() throws Exception {
		Object ret = null;
		String NullAsTring = readIdentifier(); 
		switch(NullAsTring) {
		case "NULL":
			ret = new Object();
			break;
		default:
			throw new Exception("Expected NULL");
		}
		return ret;
	}

	public String readRestrictedCharacterString() throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		int c = -1;
		
		// find first significant character
		do {
			c = reader.read();
			if(c == -1) return ""; // end of stream
		} while(-1 != skipChars.indexOf(c));

		// must be a " (double quote)
		if((char)c != '"') {
			throw new Exception("String value must start with \"");
		}
		
		// read until " (double quote)
		c = reader.read();
		while(c != '"' && c != -1) {
			stringBuffer.append((char)c);
			c = reader.read();
		} 

		if((char)c != '"') {
			throw new Exception("String value must end with \"");
		}
	
		return stringBuffer.toString();
	}

	public byte[] readOctetString() throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		int c = -1;
		
		// find first significant character
		do {
			c = reader.read();
			if(c == -1) return null; // end of stream
		} while(-1 != skipChars.indexOf(c));

		// must be a ' (single quote)
		if((char)c != '\'') {
			throw new Exception("OCTET STRING value must start with \'");
		}
		
		// read until ' (single quote)
		c = reader.read();
		while(c != '\'' && c != -1) {
			stringBuffer.append((char)c);
			c = reader.read();
		} 

		if((char)c != '\'') {
			throw new Exception("OCTET STRING value must end with \'H");
		}
		
		c = reader.read();
		if((char)c != 'H') {
			throw new Exception("OCTET STRING value must end with \'H");
		}	
		
		return  hexStringToByteArray(stringBuffer.toString());		
	}

	public long[] readObjectIdentifier() throws Exception {
		return null;		
	}

	public long[] readRelativeOID() throws Exception {
		return null;		
	}
	
	static private byte[] hexStringToByteArray(String s) {
		// we should get rid of spaces before
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	static private BitSet bitStringToBitSet(String binary) {
		// we should get rid of spaces before
	    BitSet bitset = new BitSet(binary.length());
	    for (int i = 0; i < binary.length(); i++) {
	        if (binary.charAt(i) == '1') {
	            bitset.set(i);
	        }
	    }
	    return bitset;
	}
	
	static private BitSet octetStringToBitSet(String hexString) {
		// we should get rid of spaces before
	    BitSet bitset = new BitSet(4*hexString.length());
	    for (int i = 0; i < hexString.length(); i++) {
	    	byte aByte = Byte.parseByte(hexString.substring(i, i+1), 16);
	    	if((aByte & 0x08) != 0x00) bitset.set(4*i);
	    	if((aByte & 0x04) != 0x00) bitset.set(4*i + 1);
	    	if((aByte & 0x02) != 0x00) bitset.set(4*i + 2);
	    	if((aByte & 0x01) != 0x00) bitset.set(4*i + 3);
	    }
	    return bitset;
	}

}
