package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_005.Type1;
import g_005.Type2;
import g_005.Type3;
import g_005.Type4;
import g_005.Type5;

public class TestGeneratedCode_005 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Type1 pdu = new Type1();
		pdu.setValue("Hi!");
		
		String expectedHexa = "1a 03 48 69 21";
		testHelper.writePdu(pdu, expectedHexa);
		
		// decode
		Type1 decodedPdu = (Type1) testHelper.readPdu(Type1.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}

	@Test
	public void test_2() throws Exception {
		Type1 pdu = new Type2();
		pdu.setValue("Hi!");
		
		String expectedHexa = "43 03 48 69 21";
		testHelper.writePdu(pdu, expectedHexa);
		
		// decode
		Type1 decodedPdu = (Type2) testHelper.readPdu(Type2.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}

	@Test
	public void test_3() throws Exception {
		Type1 pdu = new Type3();
		pdu.setValue("Hi!");
		
		String expectedHexa = "a2 05 43 03 48 69 21";
		testHelper.writePdu(pdu, expectedHexa);
		
		// decode
		Type1 decodedPdu = (Type3) testHelper.readPdu(Type3.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}

	@Test
	public void test_4() throws Exception {
		Type1 pdu = new Type4();
		pdu.setValue("Hi!");
		
		String expectedHexa = "67 05 43 03 48 69 21";
		testHelper.writePdu(pdu, expectedHexa);
		
		// decode
		Type1 decodedPdu = (Type4) testHelper.readPdu(Type4.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}

	@Test
	public void test_5() throws Exception {
		Type1 pdu = new Type5();
		pdu.setValue("Hi!");
		
		String expectedHexa = "82 03 48 69 21";
		testHelper.writePdu(pdu, expectedHexa);
		
		// decode
		Type1 decodedPdu = (Type5) testHelper.readPdu(Type5.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}

}
