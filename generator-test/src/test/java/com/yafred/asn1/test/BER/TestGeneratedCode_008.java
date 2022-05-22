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

import java.util.BitSet;

import org.junit.Test;

import g_008.Occupation;



public class TestGeneratedCode_008 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(1);
		bitSet.set(3);
		pdu.setValue(bitSet);

		String expectedHexa = "03 02 04 50";
		testHelper.writePdu(pdu, Occupation.class, expectedHexa);

		// decode
		Occupation decodedPdu = (Occupation) testHelper.readPdu(Occupation.class, Occupation.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_2() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(Occupation.CLERK);
		pdu.setValue(bitSet);

		String expectedHexa = "03 02 07 80";
		testHelper.writePdu(pdu, Occupation.class, expectedHexa);

		// decode
		Occupation decodedPdu = (Occupation) testHelper.readPdu(Occupation.class, Occupation.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_3() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(Occupation.ARTIST);
		bitSet.set(Occupation.CLERK);
		pdu.setValue(bitSet);

		String expectedHexa = "03 02 05 a0";
		testHelper.writePdu(pdu, Occupation.class, expectedHexa);

		// decode
		Occupation decodedPdu = (Occupation) testHelper.readPdu(Occupation.class, Occupation.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_4() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(Occupation.EDITOR);
		bitSet.set(Occupation.PUBLISHER);
		pdu.setValue(bitSet);

		String expectedHexa = "03 02 04 50";
		testHelper.writePdu(pdu, Occupation.class, expectedHexa);

		// decode
		Occupation decodedPdu = (Occupation) testHelper.readPdu(Occupation.class, Occupation.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
}
