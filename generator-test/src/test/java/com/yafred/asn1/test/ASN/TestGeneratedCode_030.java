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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.yafred.asn1.runtime.ASNValueReader;

import g_030.Flight;
import g_030.Route;

public class TestGeneratedCode_030 {

	@Test
	public void test_1() throws Exception {
		String asnValue = "{ \"Paris\" , \"London\"  }";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Route decodedPdu = Route.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		Route.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertTrue(pass);
	}

	@Test
	public void test_2() throws Exception {
		String asnValue = "{ \"Paris\" }";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Route decodedPdu = Route.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		Route.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertFalse(pass);
	}
	
	@Test
	public void test_3() throws Exception {
		String asnValue = "{ properties {  { name \"prop1\" , value \"value1\"}, { name \"prop2\" , value \"value2\"} }      }";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Flight decodedPdu = Flight.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		Flight.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertTrue(pass);
	}

	@Test
	public void test_4() throws Exception {
		String asnValue = "{ properties {  { name \"prop1\" , value \"value1\"}, { name \"prop2\" , value \"value2\"}, { name \"prop3\" , value \"value3\"} }      }";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Flight decodedPdu = Flight.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		Flight.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertFalse(pass);
	}

}
