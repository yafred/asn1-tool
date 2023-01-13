/*******************************************************************************
 * Copyright (C) 2023 Fred D7e (https://github.com/yafred)
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

import org.junit.Test;

import com.yafred.asn1.runtime.ASNValueReader;
import com.yafred.asn1.runtime.ASNValueWriter;

import g_005.Type1;
import g_005.Type2;
import g_005.Type3;
import g_005.Type4;
import g_005.Type5;

public class TestGeneratedCode_005 {

	@Test
	public void test_1() throws Exception {

		String asnValue = "\"test\"";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);

    	Type1 decodedPdu = new Type1();
    	Type1.read(decodedPdu, asnValueReader);
		
		assertEquals("test", decodedPdu.getValue());
		
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Type1.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(asnValue, stringWriter.toString().replaceAll("\\s+",""));  
	}

	@Test
	public void test_2() throws Exception {

		String asnValue = "\"test\"";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);

    	Type2 decodedPdu = new Type2();
    	Type2.read(decodedPdu, asnValueReader);
		
		assertEquals("test", decodedPdu.getValue());		
	}
	
	@Test
	public void test_3() throws Exception {

		String asnValue = "\"test\"";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);

    	Type3 decodedPdu = new Type3();
    	Type3.read(decodedPdu, asnValueReader);
		
		assertEquals("test", decodedPdu.getValue());		
	}
	
	@Test
	public void test_4() throws Exception {

		String asnValue = "\"test\"";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);

    	Type4 decodedPdu = new Type4();
    	Type4.read(decodedPdu, asnValueReader);
		
		assertEquals("test", decodedPdu.getValue());		
	}
	
	@Test
	public void test_5() throws Exception {

		String asnValue = "\"test\"";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);

    	Type5 decodedPdu = new Type5();
    	Type5.read(decodedPdu, asnValueReader);
		
		assertEquals("test", decodedPdu.getValue());		
	}
	
}
