package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import g_017.GpsInfo;
import g_017.Payment_method;
import g_017.Person;


public class TestGeneratedCode_017 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		
		Person pdu = new Person();
		pdu.setFirst_name("John");
		pdu.setLast_name("Doe");
		pdu.setAge(new Integer(25));
				
		String expectedHexa = "30 14 a0 06 16 04 4a 6f 68 6e a1 05 16 03 44 6f 65 a2 03 02 01 19";
		testHelper.writePdu(pdu, Person.class, expectedHexa);

		// decode
		Person decodedPdu = (Person) testHelper.readPdu(Person.class, Person.class, expectedHexa);
		assertEquals(decodedPdu.getFirst_name(), pdu.getFirst_name());		
		assertEquals(decodedPdu.getLast_name(), pdu.getLast_name());		
		assertEquals(decodedPdu.getAge(), pdu.getAge());		
	}
	
	@Test
	public void test_2() throws Exception {
		
		Person pdu = new Person();
		pdu.setLast_name("Doe");
		pdu.setAge(new Integer(25));
				
		String expectedHexa = "30 0c a1 05 16 03 44 6f 65 a2 03 02 01 19";
		testHelper.writePdu(pdu, Person.class, expectedHexa);

		// decode
		Person decodedPdu = (Person) testHelper.readPdu(Person.class, Person.class, expectedHexa);
		assertNull(decodedPdu.getFirst_name());		
		assertEquals(decodedPdu.getLast_name(), pdu.getLast_name());		
		assertEquals(decodedPdu.getAge(), pdu.getAge());		
	}
	
	@Test
	public void test_3() throws Exception {
		
		GpsInfo pdu = new GpsInfo();
		pdu.setGpsLat(new Integer(1));
		pdu.setGpsLong(new Integer(2));
		pdu.setGpsAlt(new Integer(3));
				
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
		pdu.setGpsLat(new Integer(1));
		pdu.setGpsLong(new Integer(2));
		pdu.setGpsAlt(new Integer(3));
				
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
		Payment_method pdu = new Payment_method();
		
		pdu.setCash(new Object());
		
		String expectedHexa = "a2 02 05 00";
		testHelper.writePdu(pdu, Payment_method.class, expectedHexa);

		// decode
		Payment_method decodedPdu = (Payment_method) testHelper.readPdu(Payment_method.class, Payment_method.class, expectedHexa);
		assertNotNull(decodedPdu.getCash());
		assertNull(decodedPdu.getCheck());
		assertNull(decodedPdu.getCredit_card());
	}
	
	@Test
	public void test_6() throws Exception {
		Payment_method pdu = new Payment_method();
		
		pdu.setCheck("1234");
		
		String expectedHexa = "a0 06 12 04 31 32 33 34";
		testHelper.writePdu(pdu, Payment_method.class, expectedHexa);

		// decode
		Payment_method decodedPdu = (Payment_method) testHelper.readPdu(Payment_method.class, Payment_method.class, expectedHexa);
		assertNull(decodedPdu.getCash());
		assertNotNull(decodedPdu.getCheck());
		assertEquals(pdu.getCheck(), decodedPdu.getCheck());
		assertNull(decodedPdu.getCredit_card());
	}
	
	@Test
	public void test_7() throws Exception {
		Payment_method pdu = new Payment_method();
		
		pdu.setCredit_card("gnu");
		
		String expectedHexa = "a1 05 1a 03 67 6e 75";
		testHelper.writePdu(pdu, Payment_method.class, expectedHexa);

		// decode
		Payment_method decodedPdu = (Payment_method) testHelper.readPdu(Payment_method.class, Payment_method.class, expectedHexa);
		assertNull(decodedPdu.getCash());
		assertNull(decodedPdu.getCheck());
		assertNotNull(decodedPdu.getCredit_card());
		assertEquals(pdu.getCredit_card(), decodedPdu.getCredit_card());
	}
}

