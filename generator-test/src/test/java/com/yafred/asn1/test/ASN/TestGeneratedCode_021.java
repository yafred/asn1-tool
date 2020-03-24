/*******************************************************************************
 * Copyright (C) 2020 Fred D7e (https://github.com/yafred)
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

import g_021.Players;
import g_021.Races;
import g_021.Traffic;
import g_021.Traffic2;


public class TestGeneratedCode_021 {

	@Test
	public void test_1() throws Exception {
		String asnValue = "{\r\n" + 
				" \r\n" + 
				"{\r\n" + 
				"name \"Smith\",\r\n" + 
				"age 20\r\n" + 
				"},\r\n" + 
				"\r\n" + 
				"{\r\n" + 
				"name \"Doe\",\r\n" + 
				"age 22\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"}";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Players decodedPdu = Players.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Players.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	@Test
	public void test_2() throws Exception {
		String asnValue = "{\r\n" + 
				"{ name \"Marathon\", distance 42, difficulty high },\r\n" + 
				"{ name \"Baby\", distance 1, difficulty low }\r\n" + 
				"}";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Races decodedPdu = Races.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Races.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	
	@Test
	public void test_3() throws Exception {
		String asnValue = "{\r\n" + 
				"request : { id 1, text \"James\" },\r\n" + 
				"request : { id 2, text \"Jane\" },\r\n" + 
				"response : { id 1, errorMessage \"Hello James\" },\r\n" + 
				"response : { id 2, errorMessage \"Hello Jane\"}\r\n" + 
				"}\r\n" + 
				"";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Traffic decodedPdu = Traffic.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Traffic.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}	
	
	@Test
	public void test_4() throws Exception {
		String asnValue = "{\r\n" + 
				"request : { id 1, text \"James\" },\r\n" + 
				"request : { id 2, text \"Jane\" },\r\n" + 
				"response : { id 1, errorMessage \"Hello James\" },\r\n" + 
				"response : { id 2, errorMessage \"Hello Jane\"}\r\n" + 
				"}\r\n" + 
				"";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Traffic2 decodedPdu = Traffic2.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Traffic2.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}	
}