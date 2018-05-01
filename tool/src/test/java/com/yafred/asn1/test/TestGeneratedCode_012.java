package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import org.junit.Test;

import g_004.My_octet_string;
import g_012.My_integers;
import g_012.My_octet_strings;
import g_012.My_strings;


public class TestGeneratedCode_012 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		My_integers pdu = new My_integers();
		ArrayList<Integer> value = new ArrayList<Integer>();
		value.add(My_integers.ten);
		value.add(My_integers.twenty);
		pdu.setValue(value);
		
		String expectedHexa = "30 06 02 01 0a 02 01 14";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_integers decodedPdu = (My_integers) testHelper.readPdu(My_integers.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), new Integer(10));		
		assertEquals(decodedPdu.getValue().get(1), new Integer(20));		
	}
	
	@Test
	public void test_2() throws Exception {
		My_integers pdu = new My_integers();
		ArrayList<Integer> value = new ArrayList<Integer>();
		value.add(My_integers.ten);
		value.add(My_integers.twenty);
		pdu.setValue(value);
		
		String expectedHexa = "30 80 02 01 0a 02 01 14 00 00";
		//testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_integers decodedPdu = (My_integers) testHelper.readPdu(My_integers.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), new Integer(10));		
		assertEquals(decodedPdu.getValue().get(1), new Integer(20));		
	}
	
	@Test
	public void test_3() throws Exception {
		My_strings pdu = new My_strings();
		ArrayList<String> value = new ArrayList<String>();
		value.add("one");
		value.add("two");
		pdu.setValue(value);
		
		String expectedHexa = "62 10 30 0e a0 05 1a 03 6f 6e 65 a0 05 1a 03 74 77 6f";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_strings decodedPdu = (My_strings) testHelper.readPdu(My_strings.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), "one");		
		assertEquals(decodedPdu.getValue().get(1), "two");		
	}
		
	@Test
	public void test_4() throws Exception {
		My_octet_strings pdu = new My_octet_strings();
		ArrayList<byte[]> value = new ArrayList<byte[]>();
		value.add(new byte[] { 0x0a, 0x0b });
		value.add(new byte[] { 0x01, 0x02 });
		pdu.setValue(value);
		
		String expectedHexa = "62 0a 30 08 80 02 0a 0b 80 02 01 02";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		// My_octet_strings decodedPdu = (My_octet_strings) testHelper.readPdu(My_octet_strings.class, expectedHexa);
	}
}
