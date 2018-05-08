package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import g_004.My_octet_string;

public class TestGeneratedCode_004 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		My_octet_string pdu = new My_octet_string();
		pdu.setValue(new byte[] { 10, 11, 12, 13 }); // 0x0A, 0x0B, 0x0C, 0x0D
		
		String expectedHexa = "42 04 0a 0b 0c 0d";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_octet_string decodedPdu = (My_octet_string) testHelper.readPdu(My_octet_string.class, expectedHexa);
		assertEquals(BERDumper.bytesToString(decodedPdu.getValue()), BERDumper.bytesToString(pdu.getValue()));
	}
}
