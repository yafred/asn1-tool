package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.BitSet;

import org.junit.Test;

import g_015.My_bitstring_list;
import g_015.My_boolean_list;
import g_015.My_enumerated_list;
import g_015.My_enumerated_list2;
import g_015.My_integer_list;
import g_015.My_octet_string_list;
import g_015.My_string_list;


public class TestGeneratedCode_015 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		My_integer_list pdu = new My_integer_list();
		ArrayList<Integer> value = new ArrayList<Integer>();
		value.add(My_integer_list.ten);
		value.add(My_integer_list.twenty);
		pdu.setValue(value);
		
		String expectedHexa = "31 06 02 01 0a 02 01 14";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_integer_list decodedPdu = (My_integer_list) testHelper.readPdu(My_integer_list.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), new Integer(10));		
		assertEquals(decodedPdu.getValue().get(1), new Integer(20));		
	}
	
	@Test
	public void test_2() throws Exception {
		My_integer_list pdu = new My_integer_list();
		ArrayList<Integer> value = new ArrayList<Integer>();
		value.add(My_integer_list.ten);
		value.add(My_integer_list.twenty);
		pdu.setValue(value);
		
		String expectedHexa = "31 80 02 01 0a 02 01 14 00 00";
		//testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_integer_list decodedPdu = (My_integer_list) testHelper.readPdu(My_integer_list.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), new Integer(10));		
		assertEquals(decodedPdu.getValue().get(1), new Integer(20));		
	}
	
	@Test
	public void test_3() throws Exception {
		My_string_list pdu = new My_string_list();
		ArrayList<String> value = new ArrayList<String>();
		value.add("one");
		value.add("two");
		pdu.setValue(value);
		
		String expectedHexa = "62 0a 80 03 6f 6e 65 80 03 74 77 6f";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_string_list decodedPdu = (My_string_list) testHelper.readPdu(My_string_list.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));		
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}
		
	@Test
	public void test_4() throws Exception {
		My_octet_string_list pdu = new My_octet_string_list();
		ArrayList<byte[]> value = new ArrayList<byte[]>();
		value.add(new byte[] { 0x0a, 0x0b });
		value.add(new byte[] { 0x01, 0x02 });
		pdu.setValue(value);
		
		String expectedHexa = "62 08 80 02 0a 0b 80 02 01 02";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_octet_string_list decodedPdu = (My_octet_string_list) testHelper.readPdu(My_octet_string_list.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(BERDumper.bytesToString(decodedPdu.getValue().get(0)), BERDumper.bytesToString(pdu.getValue().get(0)));	
		assertEquals(BERDumper.bytesToString(decodedPdu.getValue().get(1)), BERDumper.bytesToString(pdu.getValue().get(1)));		
	}
	
	@Test
	public void test_5() throws Exception {
		BitSet item1 = new BitSet();
		item1.set(My_bitstring_list.artist);
		item1.set(My_bitstring_list.clerk);
		BitSet item2 = new BitSet();
		item2.set(My_bitstring_list.editor);
		item2.set(My_bitstring_list.publisher);
		
		ArrayList<BitSet> value = new ArrayList<BitSet>();
		value.add(item1);
		value.add(item2);
		
		My_bitstring_list pdu = new My_bitstring_list();
		pdu.setValue(value);
		
		String expectedHexa = "31 08 03 02 05 a0 03 02 04 50";
		testHelper.writePdu(pdu, expectedHexa);
		
		// decode
		My_bitstring_list decodedPdu = (My_bitstring_list) testHelper.readPdu(My_bitstring_list.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));	
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));
	}
	
	@Test
	public void test_6() throws Exception {
		ArrayList<My_enumerated_list.Enum> value = new ArrayList<My_enumerated_list.Enum>();
		value.add(My_enumerated_list.Enum.apple);
		value.add(My_enumerated_list.Enum.banana);
		
		My_enumerated_list pdu = new My_enumerated_list();
		pdu.setValue(value);
		
		String expectedHexa = "31 06 0a 01 01 0a 01 00";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_enumerated_list decodedPdu = (My_enumerated_list) testHelper.readPdu(My_enumerated_list.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));	
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}
	
	@Test
	public void test_7() throws Exception {
		ArrayList<My_enumerated_list2.Enum> value = new ArrayList<My_enumerated_list2.Enum>();
		value.add(My_enumerated_list2.Enum.apple);
		value.add(My_enumerated_list2.Enum.banana);
		
		My_enumerated_list2 pdu = new My_enumerated_list2();
		pdu.setValue(value);
		
		String expectedHexa = "31 06 0a 01 01 0a 01 00";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_enumerated_list2 decodedPdu = (My_enumerated_list2) testHelper.readPdu(My_enumerated_list2.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));	
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}
	
	@Test
	public void test_8() throws Exception {
		My_boolean_list pdu = new My_boolean_list();
		ArrayList<Boolean> value = new ArrayList<Boolean>();
		value.add(Boolean.TRUE);
		value.add(Boolean.FALSE);
		pdu.setValue(value);
		
		String expectedHexa = "31 06 01 01 ff 01 01 00";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_boolean_list decodedPdu = (My_boolean_list) testHelper.readPdu(My_boolean_list.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));	
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}
}
