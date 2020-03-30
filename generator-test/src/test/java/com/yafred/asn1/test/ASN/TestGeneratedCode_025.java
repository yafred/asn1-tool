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

import g_025.Board1;
import g_025.Board2;
import g_025.Board3;
import g_025.Game;
import g_025.Game2;
import g_025.Row;



public class TestGeneratedCode_025 {

	@Test
	public void test_1() throws Exception {
		String asnValue = "{\r\n" + 
				"  {\r\n" + 
				"    \"X\",\r\n" + 
				"    \"0\",\r\n" + 
				"    \"-\"\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"-\",\r\n" + 
				"    \"-\",\r\n" + 
				"    \"-\"\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"X\",\r\n" + 
				"    \"-\",\r\n" + 
				"    \"0\"\r\n" + 
				"  }\r\n" + 
				"}";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Board1 decodedPdu = Board1.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Board1.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	@Test
	public void test_2() throws Exception {
		String asnValue = "{\r\n" + 
				"  {\r\n" + 
				"    \"X\",\r\n" + 
				"    \"0\",\r\n" + 
				"    \"-\"\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"-\",\r\n" + 
				"    \"-\",\r\n" + 
				"    \"-\"\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"X\",\r\n" + 
				"    \"-\",\r\n" + 
				"    \"0\"\r\n" + 
				"  }\r\n" + 
				"}";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Board2 decodedPdu = Board2.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Board2.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	
	@Test
	public void test_3() throws Exception {
		String asnValue = "{\r\n" + 
				"  board \r\n" + 
				"  {\r\n" + 
				"    {\r\n" + 
				"      \"X\",\r\n" + 
				"      \"0\",\r\n" + 
				"      \"-\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"-\",\r\n" + 
				"      \"-\",\r\n" + 
				"      \"-\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"X\",\r\n" + 
				"      \"-\",\r\n" + 
				"      \"0\"\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				", info \"coucou\" }\r\n" + 
				"";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Game decodedPdu = Game.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Game.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	
	@Test
	public void test_4() throws Exception {
		String asnValue = "{\r\n" + 
				"  board \r\n" + 
				"  {\r\n" + 
				"    {\r\n" + 
				"      \"X\",\r\n" + 
				"      \"0\",\r\n" + 
				"      \"-\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"-\",\r\n" + 
				"      \"-\",\r\n" + 
				"      \"-\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"X\",\r\n" + 
				"      \"-\",\r\n" + 
				"      \"0\"\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"";
		
		String expectedAsnValue = asnValue;

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Game2 decodedPdu = Game2.readPdu(asnValueReader);
    	
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Game2.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	


}