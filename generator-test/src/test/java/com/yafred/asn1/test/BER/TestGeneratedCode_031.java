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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import g_031.Plane;


public class TestGeneratedCode_031 {
	TestHelper testHelper = new TestHelper();
 
	
	@Test
	public void test_1() throws Exception {	
		
		Plane pdu = new Plane();
		pdu.setUniqueId(new java.math.BigInteger("123456789123456789123456789"));
		pdu.setSeats(Short.parseShort("100"));
		
		String expectedHexa = "30 10 80 0b 66 1e fd f2 e3 b1 9f 7c 04 5f 15 81 01 64";
		
		testHelper.writePdu(pdu, Plane.class, expectedHexa);

		Plane decodedPdu = (Plane) testHelper.readPdu(Plane.class, Plane.class, expectedHexa);

	}

}
