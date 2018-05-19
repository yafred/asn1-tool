package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import org.junit.Test;

import g_020.Choice_of_lists;
import g_020.Person;
import g_020.Person_2;



public class TestGeneratedCode_020 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Person pdu = new Person();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Bill");
		names.add("Joe");
		pdu.setNames(names);
		ArrayList<Integer> scores = new ArrayList<Integer>();
		scores.add(Integer.valueOf(20));
		scores.add(Integer.valueOf(18));
		pdu.setScores(scores);
		
		String expectedHexa = "31 15 a0 0b 16 04 42 69 6c 6c 16 03 4a 6f 65 a1 06 02 01 14 02 01 12";
		testHelper.writePdu(pdu, Person.class, expectedHexa);

		// decode
		Person decodedPdu = (Person) testHelper.readPdu(Person.class, Person.class, expectedHexa);
		assertNotNull(decodedPdu.getNames());
		assertEquals(pdu.getNames().size(), decodedPdu.getNames().size());
		for(int i=0; i<pdu.getNames().size(); i++) {
			assertEquals(pdu.getNames().get(i), decodedPdu.getNames().get(i));			
		}
		assertNotNull(decodedPdu.getScores());
		assertEquals(pdu.getScores().size(), decodedPdu.getScores().size());
		for(int i=0; i<pdu.getScores().size(); i++) {
			assertEquals(pdu.getScores().get(i), decodedPdu.getScores().get(i));			
		}
	}

	@Test
	public void test_2() throws Exception {
		Person pdu = new Person();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Bill");
		names.add("Joe");
		pdu.setNames(names);
		ArrayList<Integer> scores = new ArrayList<Integer>();
		scores.add(Integer.valueOf(20));
		scores.add(Integer.valueOf(18));
		pdu.setScores(scores);
		
		String expectedHexa = "31 80 a0 80 16 04 42 69 6c 6c 16 03 4a 6f 65 00 00 a1 80 02 01 14 02 01 12 00 00 00 00";
		//testHelper.writePdu(pdu, Person.class, expectedHexa);

		// decode
		Person decodedPdu = (Person) testHelper.readPdu(Person.class, Person.class, expectedHexa);
		assertNotNull(decodedPdu.getNames());
		assertEquals(pdu.getNames().size(), decodedPdu.getNames().size());
		for(int i=0; i<pdu.getNames().size(); i++) {
			assertEquals(pdu.getNames().get(i), decodedPdu.getNames().get(i));			
		}
		assertNotNull(decodedPdu.getScores());
		assertEquals(pdu.getScores().size(), decodedPdu.getScores().size());
		for(int i=0; i<pdu.getScores().size(); i++) {
			assertEquals(pdu.getScores().get(i), decodedPdu.getScores().get(i));			
		}
	}

	@Test
	public void test_3() throws Exception {
		Person_2 pdu = new Person_2();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Bill");
		names.add("Joe");
		pdu.setNames(names);
		ArrayList<Integer> scores = new ArrayList<Integer>();
		scores.add(Integer.valueOf(20));
		scores.add(Integer.valueOf(18));
		pdu.setScores(scores);
		
		String expectedHexa = "30 15 a0 0b 16 04 42 69 6c 6c 16 03 4a 6f 65 a1 06 02 01 14 02 01 12";
		testHelper.writePdu(pdu, Person_2.class, expectedHexa);

		// decode
		Person_2 decodedPdu = (Person_2) testHelper.readPdu(Person_2.class, Person_2.class, expectedHexa);
		assertNotNull(decodedPdu.getNames());
		assertEquals(pdu.getNames().size(), decodedPdu.getNames().size());
		for(int i=0; i<pdu.getNames().size(); i++) {
			assertEquals(pdu.getNames().get(i), decodedPdu.getNames().get(i));			
		}
		assertNotNull(decodedPdu.getScores());
		assertEquals(pdu.getScores().size(), decodedPdu.getScores().size());
		for(int i=0; i<pdu.getScores().size(); i++) {
			assertEquals(pdu.getScores().get(i), decodedPdu.getScores().get(i));			
		}
	}

	@Test
	public void test_4() throws Exception {
		Person_2 pdu = new Person_2();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Bill");
		names.add("Joe");
		pdu.setNames(names);
		ArrayList<Integer> scores = new ArrayList<Integer>();
		scores.add(Integer.valueOf(20));
		scores.add(Integer.valueOf(18));
		pdu.setScores(scores);
		
		String expectedHexa = "30 80 a0 80 16 04 42 69 6c 6c 16 03 4a 6f 65 00 00 a1 80 02 01 14 02 01 12 00 00 00 00";
		//testHelper.writePdu(pdu, Person_2.class, expectedHexa);

		// decode
		Person_2 decodedPdu = (Person_2) testHelper.readPdu(Person_2.class, Person_2.class, expectedHexa);
		assertNotNull(decodedPdu.getNames());
		assertEquals(pdu.getNames().size(), decodedPdu.getNames().size());
		for(int i=0; i<pdu.getNames().size(); i++) {
			assertEquals(pdu.getNames().get(i), decodedPdu.getNames().get(i));			
		}
		assertNotNull(decodedPdu.getScores());
		assertEquals(pdu.getScores().size(), decodedPdu.getScores().size());
		for(int i=0; i<pdu.getScores().size(); i++) {
			assertEquals(pdu.getScores().get(i), decodedPdu.getScores().get(i));			
		}
	}

	@Test
	public void test_5() throws Exception {
		Choice_of_lists pdu = new Choice_of_lists();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Bill");
		names.add("Joe");
		pdu.setNames(names);
		
		String expectedHexa = "a0 0b 16 04 42 69 6c 6c 16 03 4a 6f 65";
		testHelper.writePdu(pdu, Choice_of_lists.class, expectedHexa);

		// decode
		Choice_of_lists decodedPdu = (Choice_of_lists) testHelper.readPdu(Choice_of_lists.class, Choice_of_lists.class, expectedHexa);
		assertNotNull(decodedPdu.getNames());
		assertEquals(pdu.getNames().size(), decodedPdu.getNames().size());
		for(int i=0; i<pdu.getNames().size(); i++) {
			assertEquals(pdu.getNames().get(i), decodedPdu.getNames().get(i));			
		}
		assertNull(decodedPdu.getScores());
	}

}
