package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_002.My_boolean;

public class TestGeneratedCode_002 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		My_boolean pdu = new My_boolean();
		pdu.setValue(Boolean.TRUE);
		
		String expectedHexa = "62 03 01 01 ff";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_boolean decodedPdu = (My_boolean) testHelper.readPdu(My_boolean.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
}
