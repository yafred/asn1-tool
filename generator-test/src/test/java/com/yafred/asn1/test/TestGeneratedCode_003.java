package com.yafred.asn1.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import g_003.MyNull;

public class TestGeneratedCode_003 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MyNull pdu = new MyNull();
		pdu.setValue(new Object());
		
		String expectedHexa = "42 00";
		testHelper.writePdu(pdu, MyNull.class, expectedHexa);

		// decode
		MyNull decodedPdu = (MyNull) testHelper.readPdu(MyNull.class, MyNull.class, expectedHexa);
		
		assertNotNull(decodedPdu.getValue());
	}
}
