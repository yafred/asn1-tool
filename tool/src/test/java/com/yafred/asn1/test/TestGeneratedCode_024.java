package com.yafred.asn1.test;

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
