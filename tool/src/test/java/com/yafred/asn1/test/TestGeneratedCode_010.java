package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import g_010.ACEI;
import g_010.GpsInfo;
import g_010.MessageFields;



public class TestGeneratedCode_010 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		MessageFields message = new MessageFields();
		message.setMessageSequence(new Integer(1));
		message.setBsId(new Integer(1234));
		message.setNeID(new Integer(5555));
		message.setNelementID(new Integer(6666));
		
		GpsInfo gpsInfo = new GpsInfo();
		gpsInfo.setGpsLat(new Integer(-100));
		gpsInfo.setGpsLong(new Integer(190));
		gpsInfo.setGpsAlt(new Integer(200));
		
		ACEI pdu = new ACEI();
		pdu.setMessage(message);
		pdu.setGpsInfo(gpsInfo);
		pdu.setNeRegNumber(new byte[] { 0x0a, 0x0b });
		pdu.setSiteInfo(new byte[] { 0x0c, 0x0d });
		pdu.setNlementID(new Integer(12444));
		
		
		String expectedHexa = "30 2a a0 0f 80 01 01 81 02 04 d2 82 02 15 b3 83 02 1a 0a 81 02 0a 0b a2 0b 80 01 9c 81 02 00 be 82 02 00 c8 83 02 0c 0d 84 02 30 9c";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		ACEI decodedPdu = (ACEI) testHelper.readPdu(ACEI.class, expectedHexa);
	}
	
	@Test
	public void test_2() throws Exception {
		MessageFields message = new MessageFields();
		message.setMessageSequence(new Integer(6));
		message.setBsId(new Integer(1234));
		message.setNeID(new Integer(5555));
		message.setNelementID(new Integer(226));
		
		GpsInfo gpsInfo = new GpsInfo();
		gpsInfo.setGpsLat(new Integer(1));
		gpsInfo.setGpsLong(new Integer(13));
		gpsInfo.setGpsAlt(new Integer(200));
		
		ACEI pdu = new ACEI();
		pdu.setMessage(message);
		pdu.setGpsInfo(gpsInfo);
		pdu.setNeRegNumber(new byte[] { 0x0a, 0x0b });
		pdu.setSiteInfo(new byte[] { 0x65 });
		pdu.setNlementID(new Integer(12444));
		
		
		String expectedHexa = "30 80 a0 80 80 01 06 83 02 00 e2 00 00 a2 80 80 01 01 81 01 0d 00 00 83 01 65 00 00";
		//testHelper.writePdu(pdu, expectedHexa);

		// decode
		ACEI decodedPdu = (ACEI) testHelper.readPdu(ACEI.class, expectedHexa);
		
		assertNotNull(decodedPdu.getMessage());
		assertEquals(decodedPdu.getMessage().getMessageSequence(), pdu.getMessage().getMessageSequence());
		assertEquals(decodedPdu.getMessage().getNelementID(), pdu.getMessage().getNelementID());
		assertNotNull(decodedPdu.getGpsInfo());
		assertEquals(decodedPdu.getGpsInfo().getGpsLat(), pdu.getGpsInfo().getGpsLat());
		assertEquals(decodedPdu.getGpsInfo().getGpsLong(), pdu.getGpsInfo().getGpsLong());
		assertEquals(decodedPdu.getSiteInfo()[0], pdu.getSiteInfo()[0]);
	}
	
}

