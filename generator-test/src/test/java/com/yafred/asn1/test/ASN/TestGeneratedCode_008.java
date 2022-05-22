/*******************************************************************************
 * Copyright (C) 2022 Fred D7e (https://github.com/yafred)
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
package com.yafred.asn1.test.ASN;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import java.util.BitSet;

import org.junit.Test;

import com.yafred.asn1.runtime.ASNValueReader;
import com.yafred.asn1.runtime.ASNValueWriter;

import g_008.Occupation;


public class TestGeneratedCode_008 {

	@Test
	public void test_1() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(1);
		bitSet.set(3);
		bitSet.set(5);
		pdu.setValue(bitSet);

		String asnValue = "'010101'B";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);

    	Occupation decodedPdu = Occupation.readPdu(asnValueReader);
		
		assertEquals(pdu.getValue(), decodedPdu.getValue());
		
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Occupation.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(asnValue, stringWriter.toString().replaceAll("\\s+",""));  
	}
	
	@Test
	public void test_2() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(0);
		bitSet.set(2);
		bitSet.set(7);
		bitSet.set(8);
		bitSet.set(10);
		bitSet.set(14);
		bitSet.set(15);
		bitSet.set(16); 
		pdu.setValue(bitSet); // '1010 0001 1010 0011 1'B

		String asnValue = "'a1a380'H";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);

    	Occupation decodedPdu = new Occupation();
    	Occupation.read(decodedPdu, asnValueReader);
		
		assertEquals(pdu.getValue(), decodedPdu.getValue());		

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Occupation.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(asnValue, stringWriter.toString().replaceAll("\\s+",""));  	
	}
	
	@Test
	public void test_3() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(1);
		bitSet.set(3);
		pdu.setValue(bitSet);

		String asnValue = "{editor,publisher}";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);

    	Occupation decodedPdu = new Occupation();
    	Occupation.read(decodedPdu, asnValueReader);
		
		assertEquals(pdu.getValue(), decodedPdu.getValue());
		
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Occupation.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(asnValue, stringWriter.toString().replaceAll("\\s+",""));  
	}
	
}