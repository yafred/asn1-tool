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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.yafred.asn1.runtime.ASNValueReader;

import g_029.ListOfPositive;
import g_029.ListOfSmallInteger;
import g_029.Plane;
import g_029.SmallInteger;

public class TestGeneratedCode_029 {

	@Test
	public void test_1() throws Exception {
		String asnValue = "5";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	SmallInteger decodedPdu = SmallInteger.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		SmallInteger.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertTrue(pass);
	}

	@Test
	public void test_2() throws Exception {
		String asnValue = "50";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	SmallInteger decodedPdu = SmallInteger.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		SmallInteger.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertFalse(pass);
	}

	@Test
	public void test_3() throws Exception {
		String asnValue = "{ 0, 1, 2}";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	ListOfSmallInteger decodedPdu = ListOfSmallInteger.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		ListOfSmallInteger.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertTrue(pass);
	}

	@Test
	public void test_4() throws Exception {
		String asnValue = "{ 0, 1, 2, -50}";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	ListOfSmallInteger decodedPdu = ListOfSmallInteger.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		ListOfSmallInteger.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertFalse(pass);
	}

	@Test
	public void test_5() throws Exception {
		String asnValue = "{ 0, 1, 2, 50}";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	ListOfPositive decodedPdu = ListOfPositive.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		ListOfPositive.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertTrue(pass);
	}

	
	@Test
	public void test_6() throws Exception {
		String asnValue = "{ 0, -1, 2, 50}";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	ListOfPositive decodedPdu = ListOfPositive.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		ListOfPositive.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertFalse(pass);
	}

	
	@Test
	public void test_7() throws Exception {
		String asnValue = "{ seats 120}";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Plane decodedPdu = Plane.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		Plane.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertTrue(pass);
	}

	
	@Test
	public void test_8() throws Exception {
		String asnValue = "{ seats 1120}";

    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Plane decodedPdu = Plane.readPdu(asnValueReader);

    	boolean pass = true;
    	
    	try {
    		Plane.validate(decodedPdu);
    	}
    	catch(Exception e) {
    		pass = false;
    	}
    	
    	assertFalse(pass);
	}

}
