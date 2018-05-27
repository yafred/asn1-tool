package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.BitSet;

import org.junit.Test;

import g_013.MyEnumeratedList;
import g_013.MyEnumeratedList2;
import g_013.MyEnumeration;
import g_013.MyEnumeration2;
import g_013.MyBooleanList;
import g_013.MyBitstring;
import g_013.MyBitstringList;
import g_013.MyOctetStringList;
import g_013.MyInteger;
import g_013.MyIntegerList;
import g_013.MyStringList;


public class TestGeneratedCode_013 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MyIntegerList pdu = new MyIntegerList();
		ArrayList<Integer> value = new ArrayList<Integer>();
		value.add(MyInteger.TEN);
		value.add(MyInteger.TWENTY);
		pdu.setValue(value);
		
		String expectedHexa = "30 0a 60 03 02 01 0a 60 03 02 01 14";
		testHelper.writePdu(pdu, MyIntegerList.class, expectedHexa);

		// decode
		MyIntegerList decodedPdu = (MyIntegerList) testHelper.readPdu(MyIntegerList.class, MyIntegerList.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), Integer.valueOf(10));		
		assertEquals(decodedPdu.getValue().get(1), Integer.valueOf(20));		
	}
	
	@Test
	public void test_2() throws Exception {
		MyStringList pdu = new MyStringList();
		ArrayList<String> value = new ArrayList<String>();
		value.add("one");
		value.add("two");
		pdu.setValue(value);
		
		String expectedHexa = "62 0c 30 0a 41 03 6f 6e 65 41 03 74 77 6f";
		testHelper.writePdu(pdu, MyStringList.class, expectedHexa);

		// decode
		MyStringList decodedPdu = (MyStringList) testHelper.readPdu(MyStringList.class, MyStringList.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));		
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}
	
	@Test
	public void test_3() throws Exception {
		MyOctetStringList pdu = new MyOctetStringList();
		ArrayList<byte[]> value = new ArrayList<byte[]>();
		value.add(new byte[] { 0x0a, 0x0b });
		value.add(new byte[] { 0x01, 0x02 });
		pdu.setValue(value);
		
		String expectedHexa = "62 0a 30 08 80 02 0a 0b 80 02 01 02";
		testHelper.writePdu(pdu, MyOctetStringList.class, expectedHexa);

		// decode
		MyOctetStringList decodedPdu = (MyOctetStringList) testHelper.readPdu(MyOctetStringList.class, MyOctetStringList.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(BERDumper.bytesToString(decodedPdu.getValue().get(0)), BERDumper.bytesToString(pdu.getValue().get(0)));	
		assertEquals(BERDumper.bytesToString(decodedPdu.getValue().get(1)), BERDumper.bytesToString(pdu.getValue().get(1)));		
	}

	@Test
	public void test_4() throws Exception {
		BitSet item1 = new BitSet();
		item1.set(MyBitstring.ARTIST);
		item1.set(MyBitstring.CLERK);
		BitSet item2 = new BitSet();
		item2.set(MyBitstring.EDITOR);
		item2.set(MyBitstring.PUBLISHER);
		
		ArrayList<BitSet> value = new ArrayList<BitSet>();
		value.add(item1);
		value.add(item2);
		
		MyBitstringList pdu = new MyBitstringList();
		pdu.setValue(value);
		
		String expectedHexa = "30 08 03 02 05 a0 03 02 04 50";
		testHelper.writePdu(pdu, MyBitstringList.class, expectedHexa);
		
		// decode
		MyBitstringList decodedPdu = (MyBitstringList) testHelper.readPdu(MyBitstringList.class, MyBitstringList.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));	
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));
	}
	
	@Test
	public void test_5() throws Exception {
		MyBooleanList pdu = new MyBooleanList();
		ArrayList<Boolean> value = new ArrayList<Boolean>();
		value.add(Boolean.TRUE);
		value.add(Boolean.FALSE);
		pdu.setValue(value);
		
		String expectedHexa = "30 06 01 01 ff 01 01 00";
		testHelper.writePdu(pdu, MyBooleanList.class, expectedHexa);

		// decode
		MyBooleanList decodedPdu = (MyBooleanList) testHelper.readPdu(MyBooleanList.class, MyBooleanList.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));	
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}

	@Test
	public void test_6() throws Exception {
		ArrayList<MyEnumeration.Enum> value = new ArrayList<MyEnumeration.Enum>();
		value.add(MyEnumeration.Enum.APPLE);
		value.add(MyEnumeration.Enum.BANANA);
		
		MyEnumeratedList pdu = new MyEnumeratedList();
		pdu.setValue(value);
		
		String expectedHexa = "30 06 0a 01 01 0a 01 00";
		testHelper.writePdu(pdu, MyEnumeratedList.class, expectedHexa);

		// decode
		MyEnumeratedList decodedPdu = (MyEnumeratedList) testHelper.readPdu(MyEnumeratedList.class, MyEnumeratedList.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));	
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}
	
	@Test
	public void test_7() throws Exception {
		ArrayList<MyEnumeration2.Enum> value = new ArrayList<MyEnumeration2.Enum>();
		value.add(MyEnumeration2.Enum.APPLE);
		value.add(MyEnumeration2.Enum.BANANA);
		
		MyEnumeratedList2 pdu = new MyEnumeratedList2();
		pdu.setValue(value);
		
		String expectedHexa = "30 06 0a 01 01 0a 01 00";
		testHelper.writePdu(pdu, MyEnumeratedList2.class, expectedHexa);

		// decode
		MyEnumeratedList2 decodedPdu = (MyEnumeratedList2) testHelper.readPdu(MyEnumeratedList2.class, MyEnumeratedList2.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));	
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}

}
