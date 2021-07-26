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
package com.yafred.asn1.test.ASN;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import com.yafred.asn1.runtime.ASNValueReader;
import com.yafred.asn1.runtime.ASNValueWriter;

import java.util.Arrays;

import org.junit.Test;

import g_019.SingleChildFamily;
import g_019.Person;
import g_019.Gift;
import g_019.GiftAsSet;

public class TestGeneratedCode_019 {

	@Test
	public void test_1() throws Exception {
		String asnValue = "{\r\n" + 
				"mother {\r\n" + 
				"  last-name \"Doe\",\r\n" + 
				"  first-name \"Jane\",\r\n" + 
				"  age 32,\r\n" + 
				"  info {\r\n" + 
				"    job \"IT\" \r\n" + 
				"  }\r\n" + 
				"},\r\n" + 
				"\r\n" + 
				"father {\r\n" + 
				"  last-name \"Doe\",\r\n" + 
				"  first-name \"John\",\r\n" + 
				"  age 32\r\n" + 
				"},\r\n" + 
				"\r\n" + 
				"child {\r\n" + 
				"  last-name \"Doe\",\r\n" + 
				"  first-name \"Jack\",\r\n" + 
				"  age 6\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"}";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	SingleChildFamily decodedPdu = SingleChildFamily.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		SingleChildFamily.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	
	@Test
	public void test_2() throws Exception {
		String asnValue = "{\r\n" + 
				"  name \"Doe\",\r\n" + 
				"  gender male,\r\n" + 
				"  info {\r\n" + 
				"    age 30, \r\n" + 
				"    job \"IT\"\r\n" + 
				"  }\r\n" + 
				"}";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Person decodedPdu = Person.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Person.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}

	@Test
	public void test_3() throws Exception {
		String asnValue = "car : family-car : peugeot";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Gift decodedPdu = Gift.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Gift.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	@Test
	public void test_4() throws Exception {
		String asnValue = "{\r\n" + 
				"  car {\r\n" + 
				"    family-car peugeot\r\n" + 
				"  }\r\n" + 
				"}";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	GiftAsSet decodedPdu = GiftAsSet.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		GiftAsSet.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}

}