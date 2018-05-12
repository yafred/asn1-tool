package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import g_011.Card_type;
import g_011.Credit_card;
import g_011.Crooked;
import g_011.Payment_method;


public class TestGeneratedCode_011 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Payment_method pdu = new Payment_method();
		pdu.setCheck("012345678901234");
		
		String expectedHexa = "80 0f 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34";
		testHelper.writePdu(pdu, Payment_method.class, expectedHexa);
		
		// decode
		Payment_method decodedPdu = (Payment_method) testHelper.readPdu(Payment_method.class, Payment_method.class, expectedHexa);
		assertNotNull(decodedPdu.getCheck());
		assertEquals(decodedPdu.getCheck(), pdu.getCheck());
	}
	
	
	@Test
	public void test_2() throws Exception {
		Crooked pdu = new Crooked();
		pdu.setName("bob");
		
		String expectedHexa = "61 05 16 03 62 6f 62";
		testHelper.writePdu(pdu, Crooked.class, expectedHexa);
		
		// decode
		Crooked decodedPdu = (Crooked) testHelper.readPdu(Crooked.class, Crooked.class, expectedHexa);
		assertNotNull(decodedPdu.getName());
		assertEquals(decodedPdu.getName(), pdu.getName());
	}
	
	
	@Test
	public void test_3() throws Exception {
		Payment_method pdu = new Payment_method();
		Credit_card credit_card = new Credit_card();
		credit_card.setType(Card_type.Enum.cb);
		credit_card.setNumber("01234567890123456789");
		credit_card.setExpiry_date("042018");
		pdu.setCredit_card(credit_card);
		
		String expectedHexa = "a1 21 80 01 00 81 14 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 82 06 30 34 32 30 31 38";
		testHelper.writePdu(pdu, Payment_method.class, expectedHexa);
		
		// decode
		Payment_method decodedPdu = (Payment_method) testHelper.readPdu(Payment_method.class, Payment_method.class, expectedHexa);
		assertNotNull(decodedPdu.getCredit_card());
		assertEquals(decodedPdu.getCredit_card().getNumber(), pdu.getCredit_card().getNumber());
		assertEquals(decodedPdu.getCredit_card().getExpiry_date(), pdu.getCredit_card().getExpiry_date());
		assertEquals(decodedPdu.getCredit_card().getType(), pdu.getCredit_card().getType());
	}
		
	@Test
	public void test_4() throws Exception {
		Payment_method pdu = new Payment_method();
		pdu.setCash(new Object());
		
		String expectedHexa = "82 00";
		testHelper.writePdu(pdu, Payment_method.class, expectedHexa);
		
		// decode
		Payment_method decodedPdu = (Payment_method) testHelper.readPdu(Payment_method.class, Payment_method.class, expectedHexa);
		assertNotNull(decodedPdu.getCash());
	}
	
	
}

