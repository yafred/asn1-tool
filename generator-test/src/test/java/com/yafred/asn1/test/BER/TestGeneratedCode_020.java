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
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import org.junit.Test;

import g_020.ChoiceOfLists;
import g_020.Person;
import g_020.Person2;



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
		Person2 pdu = new Person2();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Bill");
		names.add("Joe");
		pdu.setNames(names);
		ArrayList<Integer> scores = new ArrayList<Integer>();
		scores.add(Integer.valueOf(20));
		scores.add(Integer.valueOf(18));
		pdu.setScores(scores);
		
		String expectedHexa = "30 15 a0 0b 16 04 42 69 6c 6c 16 03 4a 6f 65 a1 06 02 01 14 02 01 12";
		testHelper.writePdu(pdu, Person2.class, expectedHexa);

		// decode
		Person2 decodedPdu = (Person2) testHelper.readPdu(Person2.class, Person2.class, expectedHexa);
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
		Person2 pdu = new Person2();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Bill");
		names.add("Joe");
		pdu.setNames(names);
		ArrayList<Integer> scores = new ArrayList<Integer>();
		scores.add(Integer.valueOf(20));
		scores.add(Integer.valueOf(18));
		pdu.setScores(scores);
		
		String expectedHexa = "30 80 a0 80 16 04 42 69 6c 6c 16 03 4a 6f 65 00 00 a1 80 02 01 14 02 01 12 00 00 00 00";
		//testHelper.writePdu(pdu, Person2.class, expectedHexa);

		// decode
		Person2 decodedPdu = (Person2) testHelper.readPdu(Person2.class, Person2.class, expectedHexa);
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
		ChoiceOfLists pdu = new ChoiceOfLists();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Bill");
		names.add("Joe");
		pdu.setNames(names);
		
		String expectedHexa = "a0 0b 16 04 42 69 6c 6c 16 03 4a 6f 65";
		testHelper.writePdu(pdu, ChoiceOfLists.class, expectedHexa);

		// decode
		ChoiceOfLists decodedPdu = (ChoiceOfLists) testHelper.readPdu(ChoiceOfLists.class, ChoiceOfLists.class, expectedHexa);
		assertNotNull(decodedPdu.getNames());
		assertEquals(pdu.getNames().size(), decodedPdu.getNames().size());
		for(int i=0; i<pdu.getNames().size(); i++) {
			assertEquals(pdu.getNames().get(i), decodedPdu.getNames().get(i));			
		}
		assertNull(decodedPdu.getScores());
	}

}
