package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_001.ColorType;
import g_001.MyInteger;

public class TestGeneratedCode_001 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MyInteger pdu = new MyInteger();
		pdu.setValue(Integer.valueOf(10));

		String expectedHexa = "02 01 0a";
		testHelper.writePdu(pdu, MyInteger.class, expectedHexa);

		// decode
		MyInteger decodedPdu = (MyInteger) testHelper.readPdu(MyInteger.class, MyInteger.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
	
	@Test
	public void test_2() throws Exception {
		ColorType pdu = new ColorType();
		pdu.setValue(ColorType.NAVY_BLUE);

		String expectedHexa = "02 01 02";
		testHelper.writePdu(pdu, ColorType.class, expectedHexa);

		// decode
		ColorType decodedPdu = (ColorType) testHelper.readPdu(ColorType.class, ColorType.class, expectedHexa);
		assertEquals(pdu.getValue(), decodedPdu.getValue());
	}
}
