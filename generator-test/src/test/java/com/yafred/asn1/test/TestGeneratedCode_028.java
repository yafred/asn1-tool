package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;

import g_028.MySubChoice;
import g_028.MyChoice;
import g_028.MySubSequence;
import g_028.MySubSet;






public class TestGeneratedCode_028 {
	TestHelper testHelper = new TestHelper();
 
	
	@Test
	public void test_1() throws Exception {	
		
		MySubSequence pdu = new MySubSequence();
		pdu.setOne(Integer.valueOf(1));
		pdu.setTwo("deux");
		
		String expectedHexa = "30 09 80 01 01 81 04 64 65 75 78";
		
		testHelper.writePdu(pdu, MySubSequence.class, expectedHexa);

		MySubSequence decodedPdu = (MySubSequence) testHelper.readPdu(MySubSequence.class, MySubSequence.class, expectedHexa);
		
		assertEquals(pdu.getOne(), decodedPdu.getOne());
		assertEquals(pdu.getTwo(), decodedPdu.getTwo());
	}
	
	
	@Test
	public void test_2() throws Exception {	
		
		MySubSet pdu = new MySubSet();
		pdu.setOne(Integer.valueOf(1));
		pdu.setTwo("deux");
		
		String expectedHexa = "31 09 80 01 01 81 04 64 65 75 78";
		
		testHelper.writePdu(pdu, MySubSet.class, expectedHexa);

		MySubSet decodedPdu = (MySubSet) testHelper.readPdu(MySubSet.class, MySubSet.class, expectedHexa);
		
		assertEquals(pdu.getOne(), decodedPdu.getOne());
		assertEquals(pdu.getTwo(), decodedPdu.getTwo());
	}
	
	
	@Test
	public void test_3() throws Exception {	
		
		MySubChoice pdu = new MySubChoice();
		pdu.setOne(Integer.valueOf(1));
		
		String expectedHexa = "80 01 01";
		
		testHelper.writePdu(pdu, MySubChoice.class, expectedHexa);

		MySubChoice decodedPdu = (MySubChoice) testHelper.readPdu(MySubChoice.class, MySubChoice.class, expectedHexa);
		
		assertEquals(pdu.getOne(), decodedPdu.getOne());
		assertNull(decodedPdu.getTwo());
	}
	
	
	@Test
	public void test_4() throws Exception {	
		
		MyChoice pdu = new MyChoice();
		pdu.setOne(Integer.valueOf(1));
		
		String expectedHexa = "80 01 01";
		
		testHelper.writePdu(pdu, MyChoice.class, expectedHexa);

		MyChoice decodedPdu = (MyChoice) testHelper.readPdu(MyChoice.class, MyChoice.class, expectedHexa);
		
		assertEquals(pdu.getOne(), decodedPdu.getOne());
		assertNull(decodedPdu.getTwo());
	}
	


}
