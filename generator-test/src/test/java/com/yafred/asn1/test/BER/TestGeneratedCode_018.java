/*******************************************************************************
 * Copyright (C) 2022 Fred D7e (https://github.com/yafred)
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

import org.junit.Test;

import g_018.MyObjectId;
import g_018.MyObjectSequence;
import g_018.MyRefObjectSet;
import g_018.MyRelativeObjectId;
import g_018.MyListOfObjects;
import g_018.MyListOfRefObjects;


public class TestGeneratedCode_018 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MyObjectId pdu = new MyObjectId();
		pdu.setValue(new long[] { 1, 20, 2345 }); 
		
		String expectedHexa = "06 03 3c 92 29";
		testHelper.writePdu(pdu, MyObjectId.class, expectedHexa);

		// decode
		MyObjectId decodedPdu = (MyObjectId) testHelper.readPdu(MyObjectId.class, MyObjectId.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().length, decodedPdu.getValue().length);
		assertEquals(pdu.getValue()[0], decodedPdu.getValue()[0]);
		assertEquals(pdu.getValue()[1], decodedPdu.getValue()[1]);
		assertEquals(pdu.getValue()[2], decodedPdu.getValue()[2]);
	}
	
	@Test
	public void test_2() throws Exception {
		MyRelativeObjectId pdu = new MyRelativeObjectId();
		pdu.setValue(new long[] { 10, 20, 2345 }); 
		
		String expectedHexa = "0d 04 0a 14 92 29";
		testHelper.writePdu(pdu, MyRelativeObjectId.class, expectedHexa);

		// decode
		MyRelativeObjectId decodedPdu = (MyRelativeObjectId) testHelper.readPdu(MyRelativeObjectId.class, MyRelativeObjectId.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().length, decodedPdu.getValue().length);
		assertEquals(pdu.getValue()[0], decodedPdu.getValue()[0]);
		assertEquals(pdu.getValue()[1], decodedPdu.getValue()[1]);
		assertEquals(pdu.getValue()[2], decodedPdu.getValue()[2]);
	}
	
	@Test
	public void test_3() throws Exception {
		MyObjectSequence pdu = new MyObjectSequence();
		pdu.setRoot(new long[] { 1, 5, 150 });
		pdu.setObject1(new long[] { 2180 });
		pdu.setObject2(new long[] { 2181 });
		
		String expectedHexa = "30 0d 80 03 2d 81 16 81 02 91 04 82 02 91 05";
		testHelper.writePdu(pdu, MyObjectSequence.class, expectedHexa);

		// decode
		MyObjectSequence decodedPdu = (MyObjectSequence) testHelper.readPdu(MyObjectSequence.class, MyObjectSequence.class, expectedHexa);
		assertNotNull(decodedPdu.getRoot());
		assertEquals(3, decodedPdu.getRoot().length);
		assertEquals(1, decodedPdu.getRoot()[0]);
		assertEquals(5, decodedPdu.getRoot()[1]);
		assertEquals(150, decodedPdu.getRoot()[2]);
		
		assertNotNull(decodedPdu.getObject1());
		assertEquals(1, decodedPdu.getObject1().length);
		assertEquals(2180, decodedPdu.getObject1()[0]);

		assertNotNull(decodedPdu.getObject2());
		assertEquals(1, decodedPdu.getObject2().length);
		assertEquals(2181, decodedPdu.getObject2()[0]);
	}
	
	@Test
	public void test_4() throws Exception {
		MyRefObjectSet pdu = new MyRefObjectSet();
		pdu.setRoot(new long[] { 1, 5, 150 });
		pdu.setObject1(new long[] { 2180 });
		pdu.setObject2(new long[] { 2181 });
		
		String expectedHexa = "31 0d 80 03 2d 81 16 81 02 91 04 82 02 91 05";
		testHelper.writePdu(pdu, MyRefObjectSet.class, expectedHexa);

		// decode
		MyRefObjectSet decodedPdu = (MyRefObjectSet) testHelper.readPdu(MyRefObjectSet.class, MyRefObjectSet.class, expectedHexa);
		assertNotNull(decodedPdu.getRoot());
		assertEquals(3, decodedPdu.getRoot().length);
		assertEquals(1, decodedPdu.getRoot()[0]);
		assertEquals(5, decodedPdu.getRoot()[1]);
		assertEquals(150, decodedPdu.getRoot()[2]);
		
		assertNotNull(decodedPdu.getObject1());
		assertEquals(1, decodedPdu.getObject1().length);
		assertEquals(2180, decodedPdu.getObject1()[0]);

		assertNotNull(decodedPdu.getObject2());
		assertEquals(1, decodedPdu.getObject2().length);
		assertEquals(2181, decodedPdu.getObject2()[0]);
	}
	
	@Test
	public void test_5() throws Exception {
		MyListOfObjects pdu = new MyListOfObjects();
		ArrayList<long[]> value = new ArrayList<long[]>();
		
		value.add(new long[] { 1, 5, 150 });
		value.add(new long[] { 1, 5, 240 });
		value.add(new long[] { 1, 5, 1000 });
		
		pdu.setValue(value);
		
		String expectedHexa = "30 0f 06 03 2d 81 16 06 03 2d 81 70 06 03 2d 87 68";
		testHelper.writePdu(pdu, MyListOfObjects.class, expectedHexa);

		// decode
		MyListOfObjects decodedPdu = (MyListOfObjects) testHelper.readPdu(MyListOfObjects.class, MyListOfObjects.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			assertEquals(pdu.getValue().get(i).length, decodedPdu.getValue().get(i).length);
			assertEquals(pdu.getValue().get(i)[0], decodedPdu.getValue().get(i)[0]);
			assertEquals(pdu.getValue().get(i)[1], decodedPdu.getValue().get(i)[1]);
			assertEquals(pdu.getValue().get(i)[2], decodedPdu.getValue().get(i)[2]);
		}
	}
	
	
	@Test
	public void test_6() throws Exception {
		MyListOfRefObjects pdu = new MyListOfRefObjects();
		ArrayList<long[]> value = new ArrayList<long[]>();
		
		value.add(new long[] { 1, 5, 150 });
		value.add(new long[] { 1, 5, 240 });
		value.add(new long[] { 1, 5, 1000 });
		
		pdu.setValue(value);
		
		String expectedHexa = "31 12 0d 04 01 05 81 16 0d 04 01 05 81 70 0d 04 01 05 87 68";
		testHelper.writePdu(pdu, MyListOfRefObjects.class, expectedHexa);

		// decode
		MyListOfRefObjects decodedPdu = (MyListOfRefObjects) testHelper.readPdu(MyListOfRefObjects.class, MyListOfRefObjects.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			assertEquals(pdu.getValue().get(i).length, decodedPdu.getValue().get(i).length);
			assertEquals(pdu.getValue().get(i)[0], decodedPdu.getValue().get(i)[0]);
			assertEquals(pdu.getValue().get(i)[1], decodedPdu.getValue().get(i)[1]);
			assertEquals(pdu.getValue().get(i)[2], decodedPdu.getValue().get(i)[2]);
		}
	}
}
