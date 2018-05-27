package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_002.MyBoolean;

public class TestGeneratedCode_002 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MyBoolean pdu = new MyBoolean();
		pdu.setValue(Boolean.TRUE);
		
		String expectedHexa = "62 03 01 01 ff";
		testHelper.writePdu(pdu, MyBoolean.class, expectedHexa);

		// decode
		MyBoolean decodedPdu = (MyBoolean) testHelper.readPdu(MyBoolean.class, MyBoolean.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
}
