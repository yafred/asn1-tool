package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import org.junit.Test;

import g_020.Person;



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
	

}
