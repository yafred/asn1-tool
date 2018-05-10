package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import g_018.My_Object_Id;
import g_018.My_Object_Sequence;
import g_018.My_Ref_Object_Set;
import g_018.My_Relative_Object_Id;
import g_018.My_list_of_objects;
import g_018.My_list_of_ref_objects;


public class TestGeneratedCode_018 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		My_Object_Id pdu = new My_Object_Id();
		pdu.setValue(new long[] { 1, 20, 2345 }); 
		
		String expectedHexa = "06 03 3c 92 29";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_Object_Id decodedPdu = (My_Object_Id) testHelper.readPdu(My_Object_Id.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().length, decodedPdu.getValue().length);
		assertEquals(pdu.getValue()[0], decodedPdu.getValue()[0]);
		assertEquals(pdu.getValue()[1], decodedPdu.getValue()[1]);
		assertEquals(pdu.getValue()[2], decodedPdu.getValue()[2]);
	}
	
	@Test
	public void test_2() throws Exception {
		My_Relative_Object_Id pdu = new My_Relative_Object_Id();
		pdu.setValue(new long[] { 10, 20, 2345 }); 
		
		String expectedHexa = "0d 04 0a 14 92 29";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_Relative_Object_Id decodedPdu = (My_Relative_Object_Id) testHelper.readPdu(My_Relative_Object_Id.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().length, decodedPdu.getValue().length);
		assertEquals(pdu.getValue()[0], decodedPdu.getValue()[0]);
		assertEquals(pdu.getValue()[1], decodedPdu.getValue()[1]);
		assertEquals(pdu.getValue()[2], decodedPdu.getValue()[2]);
	}
	
	@Test
	public void test_3() throws Exception {
		My_Object_Sequence pdu = new My_Object_Sequence();
		pdu.setRoot(new long[] { 1, 5, 150 });
		pdu.setObject1(new long[] { 2180 });
		pdu.setObject2(new long[] { 2181 });
		
		String expectedHexa = "30 0d 80 03 2d 81 16 81 02 91 04 82 02 91 05";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_Object_Sequence decodedPdu = (My_Object_Sequence) testHelper.readPdu(My_Object_Sequence.class, expectedHexa);
		assertNotNull(decodedPdu.getRoot());
		assertEquals(3, decodedPdu.getRoot().length);
		assertEquals(1, decodedPdu.getRoot()[0]);
		assertEquals(5, decodedPdu.getRoot()[1]);
		assertEquals(150, decodedPdu.getRoot()[2]);
		
		assertNotNull(decodedPdu.getObject1());
		assertEquals(1, decodedPdu.getObject1().length);
		assertEquals(2180, decodedPdu.getObject1()[0]);

		assertNotNull(decodedPdu.getObject2());
		assertEquals(1, decodedPdu.getObject2().length);
		assertEquals(2181, decodedPdu.getObject2()[0]);
	}
	
	@Test
	public void test_4() throws Exception {
		My_Ref_Object_Set pdu = new My_Ref_Object_Set();
		pdu.setRoot(new long[] { 1, 5, 150 });
		pdu.setObject1(new long[] { 2180 });
		pdu.setObject2(new long[] { 2181 });
		
		String expectedHexa = "31 0d 80 03 2d 81 16 81 02 91 04 82 02 91 05";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_Ref_Object_Set decodedPdu = (My_Ref_Object_Set) testHelper.readPdu(My_Ref_Object_Set.class, expectedHexa);
		assertNotNull(decodedPdu.getRoot());
		assertEquals(3, decodedPdu.getRoot().length);
		assertEquals(1, decodedPdu.getRoot()[0]);
		assertEquals(5, decodedPdu.getRoot()[1]);
		assertEquals(150, decodedPdu.getRoot()[2]);
		
		assertNotNull(decodedPdu.getObject1());
		assertEquals(1, decodedPdu.getObject1().length);
		assertEquals(2180, decodedPdu.getObject1()[0]);

		assertNotNull(decodedPdu.getObject2());
		assertEquals(1, decodedPdu.getObject2().length);
		assertEquals(2181, decodedPdu.getObject2()[0]);
	}
	
	@Test
	public void test_5() throws Exception {
		My_list_of_objects pdu = new My_list_of_objects();
		ArrayList<long[]> value = new ArrayList<long[]>();
		
		value.add(new long[] { 1, 5, 150 });
		value.add(new long[] { 1, 5, 240 });
		value.add(new long[] { 1, 5, 1000 });
		
		pdu.setValue(value);
		
		String expectedHexa = "30 0f 06 03 2d 81 16 06 03 2d 81 70 06 03 2d 87 68";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_list_of_objects decodedPdu = (My_list_of_objects) testHelper.readPdu(My_list_of_objects.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			assertEquals(pdu.getValue().get(i).length, decodedPdu.getValue().get(i).length);
			assertEquals(pdu.getValue().get(i)[0], decodedPdu.getValue().get(i)[0]);
			assertEquals(pdu.getValue().get(i)[1], decodedPdu.getValue().get(i)[1]);
			assertEquals(pdu.getValue().get(i)[2], decodedPdu.getValue().get(i)[2]);
		}
	}
	
	
	@Test
	public void test_6() throws Exception {
		My_list_of_ref_objects pdu = new My_list_of_ref_objects();
		ArrayList<long[]> value = new ArrayList<long[]>();
		
		value.add(new long[] { 1, 5, 150 });
		value.add(new long[] { 1, 5, 240 });
		value.add(new long[] { 1, 5, 1000 });
		
		pdu.setValue(value);
		
		String expectedHexa = "31 12 0d 04 01 05 81 16 0d 04 01 05 81 70 0d 04 01 05 87 68";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		My_list_of_ref_objects decodedPdu = (My_list_of_ref_objects) testHelper.readPdu(My_list_of_ref_objects.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			assertEquals(pdu.getValue().get(i).length, decodedPdu.getValue().get(i).length);
			assertEquals(pdu.getValue().get(i)[0], decodedPdu.getValue().get(i)[0]);
			assertEquals(pdu.getValue().get(i)[1], decodedPdu.getValue().get(i)[1]);
			assertEquals(pdu.getValue().get(i)[2], decodedPdu.getValue().get(i)[2]);
		}
	}
}
