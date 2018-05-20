package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_004.MyOctetString;

public class TestGeneratedCode_004 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MyOctetString pdu = new MyOctetString();
		pdu.setValue(new byte[] { 10, 11, 12, 13 }); // 0x0A, 0x0B, 0x0C, 0x0D
		
		String expectedHexa = "42 04 0a 0b 0c 0d";
		testHelper.writePdu(pdu, MyOctetString.class, expectedHexa);

		// decode
		MyOctetString decodedPdu = (MyOctetString) testHelper.readPdu(MyOctetString.class, MyOctetString.class, expectedHexa);
		assertEquals(BERDumper.bytesToString(decodedPdu.getValue()), BERDumper.bytesToString(pdu.getValue()));
	}
}
