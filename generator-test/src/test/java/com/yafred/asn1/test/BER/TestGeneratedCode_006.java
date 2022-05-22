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
package com.yafred.asn1.test.BER;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_006.Type1;
import g_006.Type2;
import g_006.Type3;

import g_006.Type4;
import g_006.Type5;
import g_006.Type6;
import g_006.Type7;


public class TestGeneratedCode_006 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Type1 pdu = new Type1();
		pdu.setValue("12:00:00");
		
		String expectedHexa = "0e 08 31 32 3a 30 30 3a 30 30";
		testHelper.writePdu(pdu, Type1.class, expectedHexa);
		
		// decode
		Type1 decodedPdu = (Type1) testHelper.readPdu(Type1.class, Type1.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_2() throws Exception {
		Type2 pdu = new Type2();
		pdu.setValue("19851106210627.3");
		
		String expectedHexa = "18 10 31 39 38 35 31 31 30 36 32 31 30 36 32 37 2e 33";
		testHelper.writePdu(pdu, Type2.class, expectedHexa);
		
		// decode
		Type2 decodedPdu = (Type2) testHelper.readPdu(Type2.class, Type2.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}

	@Test
	public void test_3() throws Exception {
		Type3 pdu = new Type3();
		pdu.setValue("8201021200Z");
		
		String expectedHexa = "17 0b 38 32 30 31 30 32 31 32 30 30 5a";
		testHelper.writePdu(pdu, Type3.class, expectedHexa);
		
		// decode
		Type3 decodedPdu = (Type3) testHelper.readPdu(Type3.class, Type3.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}

/*	Following are transformed (see X690 201508 8.26) */
	@Test
	public void test_4() throws Exception {
		Type4 pdu = new Type4();
		pdu.setValue("20180418"); // 2018-04-18
		
		String expectedHexa = "1f 1f 08 32 30 31 38 30 34 31 38";
		testHelper.writePdu(pdu, Type4.class, expectedHexa);
		
		// decode
		Type4 decodedPdu = (Type4) testHelper.readPdu(Type4.class, Type4.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_5() throws Exception {
		Type5 pdu = new Type5();
		pdu.setValue("20180418104547"); // 2018-04-18T10:45:47
		
		String expectedHexa = "1f 21 0e 32 30 31 38 30 34 31 38 31 30 34 35 34 37";
		testHelper.writePdu(pdu, Type5.class, expectedHexa);
		
		// decode
		Type5 decodedPdu = (Type5) testHelper.readPdu(Type5.class, Type5.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_6() throws Exception {
		Type6 pdu = new Type6();
		pdu.setValue("0Y29M"); // P0Y29M
		
		String expectedHexa = "1f 22 05 30 59 32 39 4d";
		testHelper.writePdu(pdu, Type6.class, expectedHexa);
		
		// decode
		Type6 decodedPdu = (Type6) testHelper.readPdu(Type6.class, Type6.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_7() throws Exception {
		Type7 pdu = new Type7();
		pdu.setValue("002010");  // 00:20:10
		
		String expectedHexa = "1f 20 06 30 30 32 30 31 30";
		testHelper.writePdu(pdu, Type7.class, expectedHexa);
		
		// decode
		Type7 decodedPdu = (Type7) testHelper.readPdu(Type7.class, Type7.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
}
