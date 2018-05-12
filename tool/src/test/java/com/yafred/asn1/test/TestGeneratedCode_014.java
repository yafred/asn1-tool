package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import g_014.Audience;
import g_014.Person;




public class TestGeneratedCode_014 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Person item1 = new Person();
		item1.setLast_name("Doe");
		item1.setFirst_name("John");
		item1.setAge(35);

		Person item2 = new Person();
		item2.setLast_name("Dalton");
		item2.setFirst_name("Joe");
		item2.setAge(30);
		
		ArrayList<Person> value = new ArrayList<Person>();
		value.add(item1);
		value.add(item2);

		Audience pdu = new Audience();
		pdu.setValue(value);
				
		String expectedHexa = "30 22 30 0e 16 03 44 6f 65 16 04 4a 6f 68 6e 02 01 23 30 10 16 06 44 61 6c 74 6f 6e 16 03 4a 6f 65 02 01 1e";
		testHelper.writePdu(pdu, Audience.class, expectedHexa);

		// decode
		Audience decodedPdu = (Audience) testHelper.readPdu(Audience.class, Audience.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(decodedPdu.getValue().size(), 2);
		assertEquals(decodedPdu.getValue().get(0).getLast_name(), pdu.getValue().get(0).getLast_name());		
		assertEquals(decodedPdu.getValue().get(1).getFirst_name(), pdu.getValue().get(1).getFirst_name());		

	}
	
}

