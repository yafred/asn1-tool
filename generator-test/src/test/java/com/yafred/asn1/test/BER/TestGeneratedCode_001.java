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
package com.yafred.asn1.test.BER;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_001.ColorType;
import g_001.MyInteger;

public class TestGeneratedCode_001 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MyInteger pdu = new MyInteger();
		pdu.setValue(Integer.valueOf(10));

		String expectedHexa = "02 01 0a";
		testHelper.writePdu(pdu, MyInteger.class, expectedHexa);

		// decode
		MyInteger decodedPdu = (MyInteger) testHelper.readPdu(MyInteger.class, MyInteger.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_2() throws Exception {
		ColorType pdu = new ColorType();
		pdu.setValue(ColorType.NAVY_BLUE);

		String expectedHexa = "02 01 02";
		testHelper.writePdu(pdu, ColorType.class, expectedHexa);

		// decode
		ColorType decodedPdu = (ColorType) testHelper.readPdu(ColorType.class, ColorType.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
}
