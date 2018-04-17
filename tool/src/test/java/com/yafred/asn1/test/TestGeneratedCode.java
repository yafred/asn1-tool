package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.yafred.asn1.runtime.BERDumper;

import g_001.My_integer;
import g_002.My_boolean;
import g_003.My_null;
import g_004.My_octet_string;

public class TestGeneratedCode {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_g_001() throws Exception {
		My_integer my_integer = new My_integer();
		my_integer.setValue(new Integer(10));

		String expectedHexa = "02 01 0a";
		testHelper.writePdu(my_integer, expectedHexa);

		// decode
		My_integer decodedPdu = (My_integer) testHelper.readPdu(My_integer.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), my_integer.getValue());
	}

	@Test
	public void test_g_002() throws Exception {
		My_boolean my_boolean = new My_boolean();
		my_boolean.setValue(Boolean.TRUE);
		
		String expectedHexa = "62 03 01 01 ff";
		testHelper.writePdu(my_boolean, expectedHexa);

		// decode
		My_boolean decodedPdu = (My_boolean) testHelper.readPdu(My_boolean.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), my_boolean.getValue());
	}

	@Test
	public void test_g_003() throws Exception {
		My_null my_null = new My_null();
		my_null.setValue(new Object());
		
		String expectedHexa = "42 00";
		testHelper.writePdu(my_null, expectedHexa);

		// decode
		My_null decodedPdu = (My_null) testHelper.readPdu(My_null.class, expectedHexa);
		
		assertNotNull(decodedPdu.getValue());
	}

	@Test
	public void test_g_004() throws Exception {
		My_octet_string my_octet_string = new My_octet_string();
		my_octet_string.setValue(new byte[] { 10, 11, 12, 13 }); // 0x0A, 0x0B, 0x0C, 0x0D
		
		String expectedHexa = "42 04 0a 0b 0c 0d";
		testHelper.writePdu(my_octet_string, expectedHexa);

		// decode
		My_octet_string decodedPdu = (My_octet_string) testHelper.readPdu(My_octet_string.class, expectedHexa);
		assertEquals(BERDumper.bytesToString(decodedPdu.getValue()), BERDumper.bytesToString(my_octet_string.getValue()));
	}
}
