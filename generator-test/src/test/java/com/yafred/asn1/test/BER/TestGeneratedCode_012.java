/*******************************************************************************
 * Copyright (C) 2021 Fred D7e (https://github.com/yafred)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.yafred.asn1.test.BER;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.BitSet;

import org.junit.Test;

import g_012.MyBitstringList;
import g_012.MyBooleanList;
import g_012.MyEnumeratedList;
import g_012.MyEnumeratedList2;
import g_012.MyIntegerList;
import g_012.MyOctetStringList;
import g_012.MyStringList;


public class TestGeneratedCode_012 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MyIntegerList pdu = new MyIntegerList();
		ArrayList<Integer> value = new ArrayList<Integer>();
		value.add(MyIntegerList.TEN);
		value.add(MyIntegerList.TWENTY);
		pdu.setValue(value);
		
		String expectedHexa = "30 06 02 01 0a 02 01 14";
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
		MyIntegerList pdu = new MyIntegerList();
		ArrayList<Integer> value = new ArrayList<Integer>();
		value.add(MyIntegerList.TEN);
		value.add(MyIntegerList.TWENTY);
		pdu.setValue(value);
		
		String expectedHexa = "30 80 02 01 0a 02 01 14 00 00";
		//testHelper.writePdu(pdu, MyIntegerList.class, expectedHexa);

		// decode
		MyIntegerList decodedPdu = (MyIntegerList) testHelper.readPdu(MyIntegerList.class, MyIntegerList.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), Integer.valueOf(10));		
		assertEquals(decodedPdu.getValue().get(1), Integer.valueOf(20));		
	}
	
	@Test
	public void test_3() throws Exception {
		MyStringList pdu = new MyStringList();
		ArrayList<String> value = new ArrayList<String>();
		value.add("one");
		value.add("two");
		pdu.setValue(value);
		
		String expectedHexa = "62 10 30 0e a0 05 1a 03 6f 6e 65 a0 05 1a 03 74 77 6f";
		testHelper.writePdu(pdu, MyStringList.class, expectedHexa);

		// decode
		MyStringList decodedPdu = (MyStringList) testHelper.readPdu(MyStringList.class, MyStringList.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0), pdu.getValue().get(0));		
		assertEquals(decodedPdu.getValue().get(1), pdu.getValue().get(1));		
	}
		
	@Test
	public void test_4() throws Exception {
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
	public void test_5() throws Exception {
		BitSet item1 = new BitSet();
		item1.set(MyBitstringList.ARTIST);
		item1.set(MyBitstringList.CLERK);
		BitSet item2 = new BitSet();
		item2.set(MyBitstringList.EDITOR);
		item2.set(MyBitstringList.PUBLISHER);
		
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
	public void test_6() throws Exception {
		ArrayList<MyEnumeratedList.Enum> value = new ArrayList<MyEnumeratedList.Enum>();
		value.add(MyEnumeratedList.Enum.APPLE);
		value.add(MyEnumeratedList.Enum.BANANA);
		
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
		ArrayList<MyEnumeratedList2.Enum> value = new ArrayList<MyEnumeratedList2.Enum>();
		value.add(MyEnumeratedList2.Enum.APPLE);
		value.add(MyEnumeratedList2.Enum.BANANA);
		
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
	
	@Test
	public void test_8() throws Exception {
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
}
