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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import g_024.DirectoryMessage;



public class TestGeneratedCode_024 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		DirectoryMessage pdu = new DirectoryMessage();
		pdu.setTelephone().setInquiry().setMessageNumber("123");
		
		String expectedHexa = "a0 09 a0 07 30 05 84 03 31 32 33";
		testHelper.writePdu(pdu, DirectoryMessage.class, expectedHexa);

		DirectoryMessage decodedPdu = (DirectoryMessage) testHelper.readPdu(DirectoryMessage.class, DirectoryMessage.class, expectedHexa);
		assertNotNull(decodedPdu.getTelephone());
		assertNotNull(decodedPdu.getTelephone().getInquiry());
		assertEquals(pdu.getTelephone().getInquiry().getMessageNumber(), decodedPdu.getTelephone().getInquiry().getMessageNumber());
		assertNull(decodedPdu.getTelephone().getReply());
	}


}
