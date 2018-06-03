/*******************************************************************************
 * Copyright (C) 2018 Fred D7e (https://github.com/yafred)
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;

import g_026.Message;
import g_026.MessageInSequence;
import g_026.MessageInSequenceWithOptional;
import g_026.MessageInSet;
import g_026.MessageList;
import g_026.Request;






public class TestGeneratedCode_026 {
	TestHelper testHelper = new TestHelper();
 
	
	@Test
	public void test_1() throws Exception {	
		
		Request pdu = new Request();
		pdu.setText("Hello");
		
		String expectedHexa = "16 05 48 65 6c 6c 6f";
		
		testHelper.writePdu(pdu, Request.class, expectedHexa);

		Request decodedPdu = (Request) testHelper.readPdu(Request.class, Request.class, expectedHexa);
		
		assertNotNull(decodedPdu.getText());
		assertEquals(pdu.getText(), decodedPdu.getText());
		assertNull(decodedPdu.getNum());
	}
	
	
	@Test
	public void test_2() throws Exception {	
		
		Message pdu = new Message();
		pdu.setRequest().setText("Hello");
		
		String expectedHexa = "16 05 48 65 6c 6c 6f";
		
		testHelper.writePdu(pdu, Message.class, expectedHexa);

		Message decodedPdu = (Message) testHelper.readPdu(Message.class, Message.class, expectedHexa);
		
		assertNotNull(decodedPdu.getRequest());
		assertEquals(pdu.getRequest().getText(), decodedPdu.getRequest().getText());
		assertNull(decodedPdu.getRequest().getNum());
		assertNull(decodedPdu.getResponse());
	}
	
	
	@Test
	public void test_3() throws Exception {	
		
		MessageInSequence pdu = new MessageInSequence();
		pdu.setInfo("info");
		pdu.setRequest().setNum(Integer.valueOf(10));
		pdu.setResponse().setError("Hello");
		
		String expectedHexa = "30 12 a0 06 16 04 69 6e 66 6f 02 01 0a 1a 05 48 65 6c 6c 6f";
		
		testHelper.writePdu(pdu, MessageInSequence.class, expectedHexa);

		MessageInSequence decodedPdu = (MessageInSequence) testHelper.readPdu(MessageInSequence.class, MessageInSequence.class, expectedHexa);
		
		assertEquals(pdu.getInfo(), decodedPdu.getInfo());
		assertNotNull(decodedPdu.getRequest());
		assertEquals(pdu.getRequest().getNum(), decodedPdu.getRequest().getNum());
		assertNotNull(decodedPdu.getResponse());
		assertEquals(pdu.getResponse().getError(), decodedPdu.getResponse().getError());
		assertNull(decodedPdu.getResponse().getInfo());
	}
	
	
	@Test
	public void test_4() throws Exception {	
		
		MessageInSequenceWithOptional pdu = new MessageInSequenceWithOptional();
		pdu.setInfo("info");
		pdu.setResponse().setError("Hello");
		
		String expectedHexa = "30 0f a0 06 16 04 69 6e 66 6f 1a 05 48 65 6c 6c 6f";
		
		testHelper.writePdu(pdu, MessageInSequenceWithOptional.class, expectedHexa);

		MessageInSequenceWithOptional decodedPdu = (MessageInSequenceWithOptional) testHelper.readPdu(MessageInSequenceWithOptional.class, MessageInSequenceWithOptional.class, expectedHexa);
		
		assertEquals(pdu.getInfo(), decodedPdu.getInfo());
		assertNull(decodedPdu.getRequest());
		assertNotNull(decodedPdu.getResponse());
		assertEquals(pdu.getResponse().getError(), decodedPdu.getResponse().getError());
		assertNull(decodedPdu.getResponse().getInfo());
	}

	
	@Test
	public void test_5() throws Exception {	
		
		MessageInSet pdu = new MessageInSet();
		pdu.setInfo("info");
		pdu.setRequest().setNum(Integer.valueOf(10));
		pdu.setResponse().setError("Hello");
		
		String expectedHexa = "31 12 a0 06 16 04 69 6e 66 6f 02 01 0a 1a 05 48 65 6c 6c 6f";
		
		testHelper.writePdu(pdu, MessageInSet.class, expectedHexa);

		MessageInSet decodedPdu = (MessageInSet) testHelper.readPdu(MessageInSet.class, MessageInSet.class, expectedHexa);
		
		assertEquals(pdu.getInfo(), decodedPdu.getInfo());
		assertNotNull(decodedPdu.getRequest());
		assertEquals(pdu.getRequest().getNum(), decodedPdu.getRequest().getNum());
		assertNotNull(decodedPdu.getResponse());
		assertEquals(pdu.getResponse().getError(), decodedPdu.getResponse().getError());
		assertNull(decodedPdu.getResponse().getInfo());
	}
	
	
	@Test
	public void test_6() throws Exception {
		
		MessageList pdu = new MessageList();
		
		Message item1 = new Message();
		item1.setRequest().setNum(10);

		Message item2 = new Message();
		item2.setResponse().setError("none");
		
		ArrayList<Message> list = new ArrayList<Message>();
		list.add(item1);
		list.add(item2);
		
		pdu.setValue(list);
		
		String expectedHexa = "30 09 02 01 0a 1a 04 6e 6f 6e 65";
		
		testHelper.writePdu(pdu, MessageList.class, expectedHexa);

		MessageList decodedPdu = (MessageList) testHelper.readPdu(MessageList.class, MessageList.class, expectedHexa);
		
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		assertEquals(pdu.getValue().get(0).getRequest().getNum(), decodedPdu.getValue().get(0).getRequest().getNum());
		assertEquals(pdu.getValue().get(1).getResponse().getError(), decodedPdu.getValue().get(1).getResponse().getError());		
	}

}
