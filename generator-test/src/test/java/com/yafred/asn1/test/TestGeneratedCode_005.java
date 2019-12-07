/*******************************************************************************
 * Copyright (C) 2019 Fred D7e (https://github.com/yafred)
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
package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_005.Type1;
import g_005.Type2;
import g_005.Type3;
import g_005.Type4;
import g_005.Type5;

public class TestGeneratedCode_005 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Type1 pdu = new Type1();
		pdu.setValue("Hi!");
		
		String expectedHexa = "1a 03 48 69 21";
		testHelper.writePdu(pdu, Type1.class, expectedHexa);
		
		// decode
		Type1 decodedPdu = (Type1) testHelper.readPdu(Type1.class, Type1.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}

	@Test
	public void test_2() throws Exception {
		Type2 pdu = new Type2();
		pdu.setValue("Hi!");
		
		String expectedHexa = "43 03 48 69 21";
		testHelper.writePdu(pdu, Type2.class, expectedHexa);
		
		// decode
		Type2 decodedPdu = (Type2) testHelper.readPdu(Type2.class, Type2.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}

	@Test
	public void test_3() throws Exception {
		Type3 pdu = new Type3();
		pdu.setValue("Hi!");
		
		String expectedHexa = "a2 05 43 03 48 69 21";
		testHelper.writePdu(pdu, Type3.class,expectedHexa);
		
		// decode
		Type3 decodedPdu = (Type3) testHelper.readPdu(Type3.class, Type3.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}

	@Test
	public void test_4() throws Exception {
		Type4 pdu = new Type4();
		pdu.setValue("Hi!");
		
		String expectedHexa = "67 05 43 03 48 69 21";
		testHelper.writePdu(pdu, Type4.class, expectedHexa);
		
		// decode
		Type4 decodedPdu = (Type4) testHelper.readPdu(Type4.class, Type4.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}

	@Test
	public void test_5() throws Exception {
		Type5 pdu = new Type5();
		pdu.setValue("Hi!");
		
		String expectedHexa = "82 03 48 69 21";
		testHelper.writePdu(pdu, Type5.class, expectedHexa);
		
		// decode
		Type5 decodedPdu = (Type5) testHelper.readPdu(Type5.class, Type5.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}

}
