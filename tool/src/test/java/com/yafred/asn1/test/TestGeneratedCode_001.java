package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_001.My_integer;

public class TestGeneratedCode_001 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		My_integer pdu = new My_integer();
		pdu.setValue(new Integer(10));

		String expectedHexa = "02 01 0a";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_integer decodedPdu = (My_integer) testHelper.readPdu(My_integer.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
}
