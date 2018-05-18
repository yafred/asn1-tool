package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_001.ColorType;
import g_001.My_integer;

public class TestGeneratedCode_001 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		My_integer pdu = new My_integer();
		pdu.setValue(Integer.valueOf(10));

		String expectedHexa = "02 01 0a";
		testHelper.writePdu(pdu, My_integer.class, expectedHexa);

		// decode
		My_integer decodedPdu = (My_integer) testHelper.readPdu(My_integer.class, My_integer.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
	
	@Test
	public void test_2() throws Exception {
		ColorType pdu = new ColorType();
		pdu.setValue(ColorType.blue);

		String expectedHexa = "02 01 02";
		testHelper.writePdu(pdu, ColorType.class, expectedHexa);

		// decode
		ColorType decodedPdu = (ColorType) testHelper.readPdu(ColorType.class, ColorType.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
}
