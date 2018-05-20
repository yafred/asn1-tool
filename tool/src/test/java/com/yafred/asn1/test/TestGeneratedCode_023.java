package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import org.junit.Test;

import g_023.ChoiceWithEmptyComp;
import g_023.EmptySequence;
import g_023.EmptySet;



public class TestGeneratedCode_023 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		EmptySet pdu = new EmptySet();
		
		String expectedHexa = "31 00";
		testHelper.writePdu(pdu, EmptySet.class, expectedHexa);

		EmptySet decodedPdu = (EmptySet) testHelper.readPdu(EmptySet.class, EmptySet.class, expectedHexa);
		assertNotNull(decodedPdu);
	}

	@Test
	public void test_2() throws Exception {
		EmptySequence pdu = new EmptySequence();
		
		String expectedHexa = "30 00";
		testHelper.writePdu(pdu, EmptySequence.class, expectedHexa);

		EmptySequence decodedPdu = (EmptySequence) testHelper.readPdu(EmptySequence.class, EmptySequence.class, expectedHexa);
		assertNotNull(decodedPdu);
	}
	
	@Test
	public void test_3() throws Exception {
		ChoiceWithEmptyComp pdu = new ChoiceWithEmptyComp();
		pdu.setSet();
		
		String expectedHexa = "a0 02 a0 00";
		testHelper.writePdu(pdu, ChoiceWithEmptyComp.class, expectedHexa);

		ChoiceWithEmptyComp decodedPdu = (ChoiceWithEmptyComp) testHelper.readPdu(ChoiceWithEmptyComp.class, ChoiceWithEmptyComp.class, expectedHexa);
		assertNull(decodedPdu.getSequence());
		assertNotNull(decodedPdu.getSet());
	}
}
