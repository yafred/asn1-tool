package com.yafred.asn1.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import g_003.My_null;

public class TestGeneratedCode_003 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		My_null pdu = new My_null();
		pdu.setValue(new Object());
		
		String expectedHexa = "42 00";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_null decodedPdu = (My_null) testHelper.readPdu(My_null.class, expectedHexa);
		
		assertNotNull(decodedPdu.getValue());
	}
}
