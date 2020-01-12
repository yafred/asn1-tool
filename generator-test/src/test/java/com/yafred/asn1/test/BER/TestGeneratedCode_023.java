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
package com.yafred.asn1.test.BER;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import g_023.ChoiceWithEmptyComp;
import g_023.EmptySequence;
import g_023.EmptySet;



public class TestGeneratedCode_023 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		EmptySet pdu = new EmptySet();
		
		String expectedHexa = "31 00";
		testHelper.writePdu(pdu, EmptySet.class, expectedHexa);

		EmptySet decodedPdu = (EmptySet) testHelper.readPdu(EmptySet.class, EmptySet.class, expectedHexa);
		assertNotNull(decodedPdu);
	}

	@Test
	public void test_2() throws Exception {
		EmptySequence pdu = new EmptySequence();
		
		String expectedHexa = "30 00";
		testHelper.writePdu(pdu, EmptySequence.class, expectedHexa);

		EmptySequence decodedPdu = (EmptySequence) testHelper.readPdu(EmptySequence.class, EmptySequence.class, expectedHexa);
		assertNotNull(decodedPdu);
	}
	
	@Test
	public void test_3() throws Exception {
		ChoiceWithEmptyComp pdu = new ChoiceWithEmptyComp();
		pdu.setSet();
		
		String expectedHexa = "a0 02 a0 00";
		testHelper.writePdu(pdu, ChoiceWithEmptyComp.class, expectedHexa);

		ChoiceWithEmptyComp decodedPdu = (ChoiceWithEmptyComp) testHelper.readPdu(ChoiceWithEmptyComp.class, ChoiceWithEmptyComp.class, expectedHexa);
		assertNull(decodedPdu.getSequence());
		assertNotNull(decodedPdu.getSet());
	}
}
