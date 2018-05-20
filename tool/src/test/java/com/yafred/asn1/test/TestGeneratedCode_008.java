package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;

import org.junit.Test;

import g_008.Occupation;



public class TestGeneratedCode_008 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(1);
		bitSet.set(3);
		pdu.setValue(bitSet);

		String expectedHexa = "03 02 04 50";
		testHelper.writePdu(pdu, Occupation.class, expectedHexa);

		// decode
		Occupation decodedPdu = (Occupation) testHelper.readPdu(Occupation.class, Occupation.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
	
	@Test
	public void test_2() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(Occupation.CLERK);
		pdu.setValue(bitSet);

		String expectedHexa = "03 02 07 80";
		testHelper.writePdu(pdu, Occupation.class, expectedHexa);

		// decode
		Occupation decodedPdu = (Occupation) testHelper.readPdu(Occupation.class, Occupation.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
	
	@Test
	public void test_3() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(Occupation.ARTIST);
		bitSet.set(Occupation.CLERK);
		pdu.setValue(bitSet);

		String expectedHexa = "03 02 05 a0";
		testHelper.writePdu(pdu, Occupation.class, expectedHexa);

		// decode
		Occupation decodedPdu = (Occupation) testHelper.readPdu(Occupation.class, Occupation.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
	
	@Test
	public void test_4() throws Exception {
		Occupation pdu = new Occupation();
		BitSet bitSet = new BitSet();
		bitSet.set(Occupation.EDITOR);
		bitSet.set(Occupation.PUBLISHER);
		pdu.setValue(bitSet);

		String expectedHexa = "03 02 04 50";
		testHelper.writePdu(pdu, Occupation.class, expectedHexa);

		// decode
		Occupation decodedPdu = (Occupation) testHelper.readPdu(Occupation.class, Occupation.class, expectedHexa);
		assertEquals(decodedPdu.getValue(), pdu.getValue());
	}
}
