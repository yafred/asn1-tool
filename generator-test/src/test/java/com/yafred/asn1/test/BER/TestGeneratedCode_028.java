/*******************************************************************************
 * Copyright (C) 2021 Fred D7e (https://github.com/yafred)
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

import g_028.MyChoice;
import g_028.MySubChoice;
import g_028.MySubSequence;
import g_028.MySubSet;






public class TestGeneratedCode_028 {
	TestHelper testHelper = new TestHelper();
 
	
	@Test
	public void test_1() throws Exception {	
		
		MySubSequence pdu = new MySubSequence();
		pdu.setOne(Integer.valueOf(1));
		pdu.setFinal("deux");
		
		String expectedHexa = "30 09 80 01 01 81 04 64 65 75 78";
		
		testHelper.writePdu(pdu, MySubSequence.class, expectedHexa);

		MySubSequence decodedPdu = (MySubSequence) testHelper.readPdu(MySubSequence.class, MySubSequence.class, expectedHexa);
		
		assertEquals(pdu.getOne(), decodedPdu.getOne());
		assertEquals(pdu.getFinal(), decodedPdu.getFinal());
	}
	
	
	@Test
	public void test_2() throws Exception {	
		
		MySubSet pdu = new MySubSet();
		pdu.setOne(Integer.valueOf(1));
		pdu.setConst("deux");
		
		String expectedHexa = "31 09 80 01 01 81 04 64 65 75 78";
		
		testHelper.writePdu(pdu, MySubSet.class, expectedHexa);

		MySubSet decodedPdu = (MySubSet) testHelper.readPdu(MySubSet.class, MySubSet.class, expectedHexa);
		
		assertEquals(pdu.getOne(), decodedPdu.getOne());
		assertEquals(pdu.getConst(), decodedPdu.getConst());
	}
	
	
	@Test
	public void test_3() throws Exception {	
		
		MySubChoice pdu = new MySubChoice();
		pdu.setOne(Integer.valueOf(1));
		
		String expectedHexa = "80 01 01";
		
		testHelper.writePdu(pdu, MySubChoice.class, expectedHexa);

		MySubChoice decodedPdu = (MySubChoice) testHelper.readPdu(MySubChoice.class, MySubChoice.class, expectedHexa);
		
		assertEquals(pdu.getOne(), decodedPdu.getOne());
		assertNull(decodedPdu.getTwo());
	}
	
	
	@Test
	public void test_4() throws Exception {	
		
		MyChoice pdu = new MyChoice();
		pdu.setOne(Integer.valueOf(1));
		
		String expectedHexa = "80 01 01";
		
		testHelper.writePdu(pdu, MyChoice.class, expectedHexa);

		MyChoice decodedPdu = (MyChoice) testHelper.readPdu(MyChoice.class, MyChoice.class, expectedHexa);
		
		assertEquals(pdu.getOne(), decodedPdu.getOne());
		assertNull(decodedPdu.getTwo());
	}
	


}
