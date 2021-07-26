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

import org.junit.Test;

import g_014.Audience;
import g_014.Person;




public class TestGeneratedCode_014 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Person item1 = new Person();
		item1.setLastName("Doe");
		item1.setFirstName("John");
		item1.setAge(35);

		Person item2 = new Person();
		item2.setLastName("Dalton");
		item2.setFirstName("Joe");
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
		assertEquals(decodedPdu.getValue().get(0).getLastName(), pdu.getValue().get(0).getLastName());		
		assertEquals(decodedPdu.getValue().get(1).getFirstName(), pdu.getValue().get(1).getFirstName());		

	}
	
}

