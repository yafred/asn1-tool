package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;

import g_026.Message;
import g_026.MessageInSequence;
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

/*		MessageInSequence decodedPdu = (MessageInSequence) testHelper.readPdu(MessageInSequence.class, MessageInSequence.class, expectedHexa);
		
		assertNotNull(decodedPdu.getRequest());
		assertEquals(pdu.getRequest().getNum(), decodedPdu.getRequest().getNum());
		assertNotNull(decodedPdu.getResponse());
		assertEquals(pdu.getResponse().getError(), decodedPdu.getResponse().getError());
		assertNull(decodedPdu.getResponse().getInfo());*/
	}

}
