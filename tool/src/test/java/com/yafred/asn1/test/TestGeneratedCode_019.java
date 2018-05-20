package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import g_019.Gift;
import g_019.Gift.Car.FamilyCar;
import g_019.GiftAsSet;
import g_019.Person;
import g_019.Person.Gender;
import g_019.SingleChildFamily;

public class TestGeneratedCode_019 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		SingleChildFamily pdu = new SingleChildFamily();
		pdu.setMother().setLastName("Doe");
		pdu.setMother().setFirstName("Sarah");
		pdu.setMother().setAge(Integer.valueOf(30));
		pdu.setFather().setLastName("Doe");
		pdu.setFather().setFirstName("John");
		pdu.setFather().setAge(Integer.valueOf(30));
		pdu.setChild().setLastName("Doe");
		pdu.setChild().setFirstName("Mary");
		pdu.setChild().setAge(Integer.valueOf(5));

		String expectedHexa = "30 31 a0 0f 80 03 44 6f 65 81 05 53 61 72 61 68 82 01 1e a1 0e 80 03 44 6f 65 81 04 4a 6f 68 6e "
				+ "82 01 1e a2 0e 80 03 44 6f 65 81 04 4d 61 72 79 82 01 05";
		testHelper.writePdu(pdu, SingleChildFamily.class, expectedHexa);

		// decode
		SingleChildFamily decodedPdu = (SingleChildFamily) testHelper.readPdu(SingleChildFamily.class,
				SingleChildFamily.class, expectedHexa);

		assertNotNull(decodedPdu.getMother());
		assertNotNull(decodedPdu.getFather());
		assertNotNull(decodedPdu.getChild());

		assertEquals(pdu.getMother().getLastName(), decodedPdu.getMother().getLastName());
		assertEquals(pdu.getMother().getFirstName(), decodedPdu.getMother().getFirstName());
		assertEquals(pdu.getMother().getAge(), decodedPdu.getMother().getAge());

		assertEquals(pdu.getFather().getLastName(), decodedPdu.getFather().getLastName());
		assertEquals(pdu.getFather().getFirstName(), decodedPdu.getFather().getFirstName());
		assertEquals(pdu.getFather().getAge(), decodedPdu.getFather().getAge());

		assertEquals(pdu.getChild().getLastName(), decodedPdu.getChild().getLastName());
		assertEquals(pdu.getChild().getFirstName(), decodedPdu.getChild().getFirstName());
		assertEquals(pdu.getChild().getAge(), decodedPdu.getChild().getAge());
	}

	@Test
	public void test_2() throws Exception {
		SingleChildFamily pdu = new SingleChildFamily();
		pdu.setMother().setLastName("Doe");
		pdu.setMother().setFirstName("Sarah");
		pdu.setMother().setAge(Integer.valueOf(30));
		pdu.setMother().setInfo().setJob("Seller");
		pdu.setFather().setLastName("Doe");
		pdu.setFather().setFirstName("John");
		pdu.setFather().setAge(Integer.valueOf(30));
		pdu.setChild().setLastName("Doe");
		pdu.setChild().setFirstName("Mary");
		pdu.setChild().setAge(Integer.valueOf(5));

		String expectedHexa = "30 3b a0 19 80 03 44 6f 65 81 05 53 61 72 61 68 82 01 1e a3 08 80 06 53 65 6c 6c 65 72 a1 0e 80 "
				+ "03 44 6f 65 81 04 4a 6f 68 6e 82 01 1e a2 0e 80 03 44 6f 65 81 04 4d 61 72 79 82 01 05";
		testHelper.writePdu(pdu, SingleChildFamily.class, expectedHexa);

		// decode
		SingleChildFamily decodedPdu = (SingleChildFamily) testHelper.readPdu(SingleChildFamily.class,
				SingleChildFamily.class, expectedHexa);

		assertNotNull(decodedPdu.getMother());
		assertNotNull(decodedPdu.getMother().getInfo());
		assertNotNull(decodedPdu.getFather());
		assertNotNull(decodedPdu.getChild());

		assertEquals(pdu.getMother().getLastName(), decodedPdu.getMother().getLastName());
		assertEquals(pdu.getMother().getFirstName(), decodedPdu.getMother().getFirstName());
		assertEquals(pdu.getMother().getAge(), decodedPdu.getMother().getAge());
		assertEquals(pdu.getMother().getInfo().getJob(), decodedPdu.getMother().getInfo().getJob());

		assertEquals(pdu.getFather().getLastName(), decodedPdu.getFather().getLastName());
		assertEquals(pdu.getFather().getFirstName(), decodedPdu.getFather().getFirstName());
		assertEquals(pdu.getFather().getAge(), decodedPdu.getFather().getAge());

		assertEquals(pdu.getChild().getLastName(), decodedPdu.getChild().getLastName());
		assertEquals(pdu.getChild().getFirstName(), decodedPdu.getChild().getFirstName());
		assertEquals(pdu.getChild().getAge(), decodedPdu.getChild().getAge());
	}

	@Test
	public void test_3() throws Exception {
		Person pdu = new Person();
		pdu.setName("Bill");
		pdu.setGender(Gender.MALE);
		pdu.setInfo().setAge(Integer.valueOf(20));
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
		pdu.setCar().setFamilyCar(FamilyCar.PEUGEOT);

		String expectedHexa = "a0 03 81 01 00";
		testHelper.writePdu(pdu, Gift.class, expectedHexa);

		// decode
		Gift decodedPdu = (Gift) testHelper.readPdu(Gift.class, Gift.class, expectedHexa);

		assertNotNull(decodedPdu.getCar());
		assertEquals(pdu.getCar().getFamilyCar(), decodedPdu.getCar().getFamilyCar());
	}
	
	@Test
	public void test_5() throws Exception {
		GiftAsSet pdu = new GiftAsSet();
		pdu.setCar().setFamilyCar(GiftAsSet.Car.FamilyCar.PEUGEOT);

		String expectedHexa = "31 05 a0 03 81 01 00";
		testHelper.writePdu(pdu, GiftAsSet.class, expectedHexa);

		// decode
		GiftAsSet decodedPdu = (GiftAsSet) testHelper.readPdu(GiftAsSet.class, GiftAsSet.class, expectedHexa);

		assertNotNull(decodedPdu.getCar());
		assertEquals(pdu.getCar().getFamilyCar(), decodedPdu.getCar().getFamilyCar());
	}
}
