package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import g_019.Gift;
import g_019.Gift.Car.Family_car;
import g_019.Gift_as_set;
import g_019.Person;
import g_019.Person.Gender;
import g_019.Single_Child_Family;

public class TestGeneratedCode_019 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Single_Child_Family pdu = new Single_Child_Family();
		pdu.setMother().setLast_name("Doe");
		pdu.setMother().setFirst_name("Sarah");
		pdu.setMother().setAge(new Integer(30));
		pdu.setFather().setLast_name("Doe");
		pdu.setFather().setFirst_name("John");
		pdu.setFather().setAge(new Integer(30));
		pdu.setChild().setLast_name("Doe");
		pdu.setChild().setFirst_name("Mary");
		pdu.setChild().setAge(new Integer(5));

		String expectedHexa = "30 31 a0 0f 80 03 44 6f 65 81 05 53 61 72 61 68 82 01 1e a1 0e 80 03 44 6f 65 81 04 4a 6f 68 6e "
				+ "82 01 1e a2 0e 80 03 44 6f 65 81 04 4d 61 72 79 82 01 05";
		testHelper.writePdu(pdu, Single_Child_Family.class, expectedHexa);

		// decode
		Single_Child_Family decodedPdu = (Single_Child_Family) testHelper.readPdu(Single_Child_Family.class,
				Single_Child_Family.class, expectedHexa);

		assertNotNull(decodedPdu.getMother());
		assertNotNull(decodedPdu.getFather());
		assertNotNull(decodedPdu.getChild());

		assertEquals(pdu.getMother().getLast_name(), decodedPdu.getMother().getLast_name());
		assertEquals(pdu.getMother().getFirst_name(), decodedPdu.getMother().getFirst_name());
		assertEquals(pdu.getMother().getAge(), decodedPdu.getMother().getAge());

		assertEquals(pdu.getFather().getLast_name(), decodedPdu.getFather().getLast_name());
		assertEquals(pdu.getFather().getFirst_name(), decodedPdu.getFather().getFirst_name());
		assertEquals(pdu.getFather().getAge(), decodedPdu.getFather().getAge());

		assertEquals(pdu.getChild().getLast_name(), decodedPdu.getChild().getLast_name());
		assertEquals(pdu.getChild().getFirst_name(), decodedPdu.getChild().getFirst_name());
		assertEquals(pdu.getChild().getAge(), decodedPdu.getChild().getAge());
	}

	@Test
	public void test_2() throws Exception {
		Single_Child_Family pdu = new Single_Child_Family();
		pdu.setMother().setLast_name("Doe");
		pdu.setMother().setFirst_name("Sarah");
		pdu.setMother().setAge(new Integer(30));
		pdu.setMother().setInfo().setJob("Seller");
		pdu.setFather().setLast_name("Doe");
		pdu.setFather().setFirst_name("John");
		pdu.setFather().setAge(new Integer(30));
		pdu.setChild().setLast_name("Doe");
		pdu.setChild().setFirst_name("Mary");
		pdu.setChild().setAge(new Integer(5));

		String expectedHexa = "30 3b a0 19 80 03 44 6f 65 81 05 53 61 72 61 68 82 01 1e a3 08 80 06 53 65 6c 6c 65 72 a1 0e 80 "
				+ "03 44 6f 65 81 04 4a 6f 68 6e 82 01 1e a2 0e 80 03 44 6f 65 81 04 4d 61 72 79 82 01 05";
		testHelper.writePdu(pdu, Single_Child_Family.class, expectedHexa);

		// decode
		Single_Child_Family decodedPdu = (Single_Child_Family) testHelper.readPdu(Single_Child_Family.class,
				Single_Child_Family.class, expectedHexa);

		assertNotNull(decodedPdu.getMother());
		assertNotNull(decodedPdu.getMother().getInfo());
		assertNotNull(decodedPdu.getFather());
		assertNotNull(decodedPdu.getChild());

		assertEquals(pdu.getMother().getLast_name(), decodedPdu.getMother().getLast_name());
		assertEquals(pdu.getMother().getFirst_name(), decodedPdu.getMother().getFirst_name());
		assertEquals(pdu.getMother().getAge(), decodedPdu.getMother().getAge());
		assertEquals(pdu.getMother().getInfo().getJob(), decodedPdu.getMother().getInfo().getJob());

		assertEquals(pdu.getFather().getLast_name(), decodedPdu.getFather().getLast_name());
		assertEquals(pdu.getFather().getFirst_name(), decodedPdu.getFather().getFirst_name());
		assertEquals(pdu.getFather().getAge(), decodedPdu.getFather().getAge());

		assertEquals(pdu.getChild().getLast_name(), decodedPdu.getChild().getLast_name());
		assertEquals(pdu.getChild().getFirst_name(), decodedPdu.getChild().getFirst_name());
		assertEquals(pdu.getChild().getAge(), decodedPdu.getChild().getAge());
	}

	@Test
	public void test_3() throws Exception {
		Person pdu = new Person();
		pdu.setName("Bill");
		pdu.setGender(Gender.male);
		pdu.setInfo().setAge(new Integer(20));
		pdu.setInfo().setJob("student");

		String expectedHexa = "31 17 80 04 42 69 6c 6c 81 01 00 a2 0c 80 01 14 81 07 73 74 75 64 65 6e 74";
		testHelper.writePdu(pdu, Person.class, expectedHexa);

		// decode
		Person decodedPdu = (Person) testHelper.readPdu(Person.class, Person.class, expectedHexa);

		assertNotNull(decodedPdu.getInfo());
		assertEquals(pdu.getName(), decodedPdu.getName());
		assertEquals(pdu.getGender(), decodedPdu.getGender());
		assertEquals(pdu.getInfo().getAge(), decodedPdu.getInfo().getAge());
		assertEquals(pdu.getInfo().getJob(), decodedPdu.getInfo().getJob());
	}

	@Test
	public void test_4() throws Exception {
		Gift pdu = new Gift();
		pdu.setCar().setFamily_car(Family_car.peugeot);

		String expectedHexa = "a0 03 81 01 00";
		testHelper.writePdu(pdu, Gift.class, expectedHexa);

		// decode
		Gift decodedPdu = (Gift) testHelper.readPdu(Gift.class, Gift.class, expectedHexa);

		assertNotNull(decodedPdu.getCar());
		assertEquals(pdu.getCar().getFamily_car(), decodedPdu.getCar().getFamily_car());
	}
	
	@Test
	public void test_5() throws Exception {
		Gift_as_set pdu = new Gift_as_set();
		pdu.setCar().setFamily_car(Gift_as_set.Car.Family_car.peugeot);

		String expectedHexa = "31 05 a0 03 81 01 00";
		testHelper.writePdu(pdu, Gift_as_set.class, expectedHexa);

		// decode
		Gift_as_set decodedPdu = (Gift_as_set) testHelper.readPdu(Gift_as_set.class, Gift_as_set.class, expectedHexa);

		assertNotNull(decodedPdu.getCar());
		assertEquals(pdu.getCar().getFamily_car(), decodedPdu.getCar().getFamily_car());
	}
}
