package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_007.Fruit;



public class TestGeneratedCode_007 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Fruit pdu = new Fruit();
		pdu.setValue(Fruit.Enum.APPLE);

		String expectedHexa = "0a 01 01";
		testHelper.writePdu(pdu, Fruit.class, expectedHexa);

		// decode
		Fruit decodedPdu = (Fruit) testHelper.readPdu(Fruit.class, Fruit.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
}
