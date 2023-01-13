/*******************************************************************************
 * Copyright (C) 2023 Fred D7e (https://github.com/yafred)
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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import g_017.GpsInfo;
import g_017.PaymentMethod;
import g_017.Person;


public class TestGeneratedCode_017 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		
		Person pdu = new Person();
		pdu.setFirstName("John");
		pdu.setLastName("Doe");
		pdu.setAge(Integer.valueOf(25));
				
		String expectedHexa = "30 14 a0 06 16 04 4a 6f 68 6e a1 05 16 03 44 6f 65 a2 03 02 01 19";
		testHelper.writePdu(pdu, Person.class, expectedHexa);

		// decode
		Person decodedPdu = (Person) testHelper.readPdu(Person.class, Person.class, expectedHexa);
		assertEquals(decodedPdu.getFirstName(), pdu.getFirstName());		
		assertEquals(decodedPdu.getLastName(), pdu.getLastName());		
		assertEquals(decodedPdu.getAge(), pdu.getAge());		
	}
	
	@Test
	public void test_2() throws Exception {
		
		Person pdu = new Person();
		pdu.setLastName("Doe");
		pdu.setAge(Integer.valueOf(25));
				
		String expectedHexa = "30 0c a1 05 16 03 44 6f 65 a2 03 02 01 19";
		testHelper.writePdu(pdu, Person.class, expectedHexa);

		// decode
		Person decodedPdu = (Person) testHelper.readPdu(Person.class, Person.class, expectedHexa);
		assertNull(decodedPdu.getFirstName());		
		assertEquals(decodedPdu.getLastName(), pdu.getLastName());		
		assertEquals(decodedPdu.getAge(), pdu.getAge());		
	}
	
	@Test
	public void test_3() throws Exception {
		
		GpsInfo pdu = new GpsInfo();
		pdu.setGpsLat(Integer.valueOf(1));
		pdu.setGpsLong(Integer.valueOf(2));
		pdu.setGpsAlt(Integer.valueOf(3));
				
		String expectedHexa = "31 0f a0 03 02 01 01 a1 03 02 01 02 a2 03 02 01 03";
		testHelper.writePdu(pdu, GpsInfo.class, expectedHexa);

		// decode
		GpsInfo decodedPdu = (GpsInfo) testHelper.readPdu(GpsInfo.class, GpsInfo.class, expectedHexa);
		assertEquals(decodedPdu.getGpsLat(), pdu.getGpsLat());		
		assertEquals(decodedPdu.getGpsLong(), pdu.getGpsLong());		
		assertEquals(decodedPdu.getGpsAlt(), pdu.getGpsAlt());		
	}
	
	@Test
	public void test_4() throws Exception {
		
		GpsInfo pdu = new GpsInfo();
		pdu.setGpsLat(Integer.valueOf(1));
		pdu.setGpsLong(Integer.valueOf(2));
		pdu.setGpsAlt(Integer.valueOf(3));
				
		String expectedHexa = "31 0f a0 03 02 01 01 a1 03 02 01 02 a2 03 02 01 03";
		testHelper.writePdu(pdu, GpsInfo.class, expectedHexa);

		// change order
		expectedHexa = "31 0f a1 03 02 01 02 a0 03 02 01 01 a2 03 02 01 03";

		// decode
		GpsInfo decodedPdu = (GpsInfo) testHelper.readPdu(GpsInfo.class, GpsInfo.class, expectedHexa);
		assertEquals(decodedPdu.getGpsLat(), pdu.getGpsLat());		
		assertEquals(decodedPdu.getGpsLong(), pdu.getGpsLong());		
		assertEquals(decodedPdu.getGpsAlt(), pdu.getGpsAlt());		
	}
	
	@Test
	public void test_5() throws Exception {
		PaymentMethod pdu = new PaymentMethod();
		
		pdu.setCash(new Object());
		
		String expectedHexa = "a2 02 05 00";
		testHelper.writePdu(pdu, PaymentMethod.class, expectedHexa);

		// decode
		PaymentMethod decodedPdu = (PaymentMethod) testHelper.readPdu(PaymentMethod.class, PaymentMethod.class, expectedHexa);
		assertNotNull(decodedPdu.getCash());
		assertNull(decodedPdu.getCheck());
		assertNull(decodedPdu.getCreditCard());
	}
	
	@Test
	public void test_6() throws Exception {
		PaymentMethod pdu = new PaymentMethod();
		
		pdu.setCheck("1234");
		
		String expectedHexa = "a0 06 12 04 31 32 33 34";
		testHelper.writePdu(pdu, PaymentMethod.class, expectedHexa);

		// decode
		PaymentMethod decodedPdu = (PaymentMethod) testHelper.readPdu(PaymentMethod.class, PaymentMethod.class, expectedHexa);
		assertNull(decodedPdu.getCash());
		assertNotNull(decodedPdu.getCheck());
		assertEquals(pdu.getCheck(), decodedPdu.getCheck());
		assertNull(decodedPdu.getCreditCard());
	}
	
	@Test
	public void test_7() throws Exception {
		PaymentMethod pdu = new PaymentMethod();
		
		pdu.setCreditCard("gnu");
		
		String expectedHexa = "a1 05 1a 03 67 6e 75";
		testHelper.writePdu(pdu, PaymentMethod.class, expectedHexa);

		// decode
		PaymentMethod decodedPdu = (PaymentMethod) testHelper.readPdu(PaymentMethod.class, PaymentMethod.class, expectedHexa);
		assertNull(decodedPdu.getCash());
		assertNull(decodedPdu.getCheck());
		assertNotNull(decodedPdu.getCreditCard());
		assertEquals(pdu.getCreditCard(), decodedPdu.getCreditCard());
	}
}

