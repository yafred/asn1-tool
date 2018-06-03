/*******************************************************************************
 * Copyright (C) 2018 Fred D7e (https://github.com/yafred)
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
		message.setMessageSequence(Integer.valueOf(1));
		message.setBsId(Integer.valueOf(1234));
		message.setNeID(Integer.valueOf(5555));
		message.setNelementID(Integer.valueOf(6666));
		
		GpsInfo gpsInfo = new GpsInfo();
		gpsInfo.setGpsLat(Integer.valueOf(-100));
		gpsInfo.setGpsLong(Integer.valueOf(190));
		gpsInfo.setGpsAlt(Integer.valueOf(200));
		
		ACEI pdu = new ACEI();
		pdu.setMessage(message);
		pdu.setGpsInfo(gpsInfo);
		pdu.setNeRegNumber(new byte[] { 0x0a, 0x0b });
		pdu.setSiteInfo(new byte[] { 0x0c, 0x0d });
		pdu.setNlementID(Integer.valueOf(12444));
		
		String expectedHexa = "30 2a a0 0f 80 01 01 81 02 04 d2 82 02 15 b3 83 02 1a 0a 81 02 0a 0b a2 0b 80 01 9c 81 02 00 be 82 02 00 c8 83 02 0c 0d 84 02 30 9c";
		testHelper.writePdu(pdu, ACEI.class, expectedHexa);

		// decode
		ACEI decodedPdu = (ACEI) testHelper.readPdu(ACEI.class, ACEI.class, expectedHexa);
		assertNotNull(decodedPdu.getMessage());
		assertEquals(decodedPdu.getMessage().getMessageSequence(), pdu.getMessage().getMessageSequence());
		assertEquals(decodedPdu.getMessage().getNelementID(), pdu.getMessage().getNelementID());
		assertNotNull(decodedPdu.getGpsInfo());
		assertEquals(decodedPdu.getGpsInfo().getGpsLat(), pdu.getGpsInfo().getGpsLat());
		assertEquals(decodedPdu.getGpsInfo().getGpsLong(), pdu.getGpsInfo().getGpsLong());
		assertEquals(BERDumper.bytesToString(decodedPdu.getSiteInfo()), BERDumper.bytesToString(pdu.getSiteInfo()));
	}
	
	@Test
	public void test_2() throws Exception {
		MessageFields message = new MessageFields();
		message.setMessageSequence(Integer.valueOf(6));
		message.setBsId(Integer.valueOf(1234));
		message.setNeID(Integer.valueOf(5555));
		message.setNelementID(Integer.valueOf(226));
		
		GpsInfo gpsInfo = new GpsInfo();
		gpsInfo.setGpsLat(Integer.valueOf(1));
		gpsInfo.setGpsLong(Integer.valueOf(13));
		gpsInfo.setGpsAlt(Integer.valueOf(200));
		
		ACEI pdu = new ACEI();
		pdu.setMessage(message);
		pdu.setGpsInfo(gpsInfo);
		pdu.setNeRegNumber(new byte[] { 0x0a, 0x0b });
		pdu.setSiteInfo(new byte[] { 0x65 });
		pdu.setNlementID(Integer.valueOf(12444));
		
		
		String expectedHexa = "30 80 a0 80 80 01 06 83 02 00 e2 00 00 a2 80 80 01 01 81 01 0d 00 00 83 01 65 00 00";
		//testHelper.writePdu(pdu, ACEI.class, expectedHexa);

		// decode
		ACEI decodedPdu = (ACEI) testHelper.readPdu(ACEI.class, ACEI.class, expectedHexa);
		
		assertNotNull(decodedPdu.getMessage());
		assertEquals(decodedPdu.getMessage().getMessageSequence(), pdu.getMessage().getMessageSequence());
		assertEquals(decodedPdu.getMessage().getNelementID(), pdu.getMessage().getNelementID());
		assertNotNull(decodedPdu.getGpsInfo());
		assertEquals(decodedPdu.getGpsInfo().getGpsLat(), pdu.getGpsInfo().getGpsLat());
		assertEquals(decodedPdu.getGpsInfo().getGpsLong(), pdu.getGpsInfo().getGpsLong());
		assertEquals(BERDumper.bytesToString(decodedPdu.getSiteInfo()), BERDumper.bytesToString(pdu.getSiteInfo()));
	}
	
}

