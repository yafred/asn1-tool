/*******************************************************************************
 * Copyright (C) 2020 Fred D7e (https://github.com/yafred)
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
package com.yafred.asn1.test.ASN;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import com.yafred.asn1.runtime.ASNValueReader;
import com.yafred.asn1.runtime.ASNValueWriter;

import java.util.Arrays;

import org.junit.Test;

import g_016.ACEI;




public class TestGeneratedCode_016 {

	@Test
	public void test_1() throws Exception {
		String asnValue = "{\r\n" + 
				"  message \r\n" + 
				"  {\r\n" + 
				"    messageSequence 1,\r\n" + 
				"    bsId 1234,\r\n" + 
				"    neID 5555,\r\n" + 
				"    nelementID 6666\r\n" + 
				"  },\r\n" + 
				"  neRegNumber '0A0B'H,\r\n" + 
				"  gpsInfo \r\n" + 
				"  {\r\n" + 
				"    gpsLat -100,\r\n" + 
				"    gpsLong 190,\r\n" + 
				"    gpsAlt 200\r\n" + 
				"  },\r\n" + 
				"  siteInfo '0C0D'H,\r\n" + 
				"  nlementID 12444\r\n" + 
				"}";
		
		String expectedAsnValue = "{\r\n" + 
				"  message \r\n" + 
				"  {\r\n" + 
				"    messageSequence 1,\r\n" + 
				"    bsId 1234,\r\n" + 
				"    neID 5555,\r\n" + 
				"    nelementID 6666\r\n" + 
				"  },\r\n" + 
				"  neRegNumber '0a0b'H,\r\n" + 
				"  gpsInfo \r\n" + 
				"  {\r\n" + 
				"    gpsLat -100,\r\n" + 
				"    gpsLong 190,\r\n" + 
				"    gpsAlt 200\r\n" + 
				"  },\r\n" + 
				"  siteInfo '0c0d'H,\r\n" + 
				"  nlementID 12444\r\n" + 
				"}";
				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	ACEI decodedPdu = ACEI.readPdu(asnValueReader);
    	
		assertNotNull(decodedPdu.getMessage());
		assertNotNull(decodedPdu.getMessage().getMessageSequence());
		assertEquals(Integer.valueOf(1), decodedPdu.getMessage().getMessageSequence());
		assertTrue(Arrays.equals(new byte[] { 0x0a, 0x0b }, decodedPdu.getNeRegNumber()));

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		ACEI.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	@Test
	public void test_2() throws Exception {
		String asnValue = "{" + 
				"  neRegNumber '0A0B'H," + 
				"  gpsInfo " + 
				"  {" + 
				"    gpsLat -100," + 
				"    gpsLong 190," + 
				"    gpsAlt 200" + 
				"  }," + 
				"  message " + 
				"  {" + 
				"    messageSequence 1," + 
				"    bsId 1234," + 
				"    neID 5555," + 
				"    nelementID 6666" + 
				"  }," + 
				"  siteInfo '0C0D'H," + 
				"  nlementID 12444" + 
				"}";
		
		String expectedAsnValue = "{\r\n" + 
				"  message \r\n" + 
				"  {\r\n" + 
				"    messageSequence 1,\r\n" + 
				"    bsId 1234,\r\n" + 
				"    neID 5555,\r\n" + 
				"    nelementID 6666\r\n" + 
				"  },\r\n" + 
				"  neRegNumber '0a0b'H,\r\n" + 
				"  gpsInfo \r\n" + 
				"  {\r\n" + 
				"    gpsLat -100,\r\n" + 
				"    gpsLong 190,\r\n" + 
				"    gpsAlt 200\r\n" + 
				"  },\r\n" + 
				"  siteInfo '0c0d'H,\r\n" + 
				"  nlementID 12444\r\n" + 
				"}";

				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	ACEI decodedPdu = ACEI.readPdu(asnValueReader);
    	
		assertNotNull(decodedPdu.getMessage());
		assertNotNull(decodedPdu.getMessage().getMessageSequence());
		assertEquals(Integer.valueOf(1), decodedPdu.getMessage().getMessageSequence());
		assertTrue(Arrays.equals(new byte[] { 0x0a, 0x0b }, decodedPdu.getNeRegNumber()));

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		ACEI.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(expectedAsnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}
	
	@Test
	public void test_3() throws Exception {
		String asnValue = "{\r\n" + 
				"  message \r\n" + 
				"  {\r\n" + 
				"    messageSequence 6,\r\n" + 
				"    nelementID 226\r\n" + 
				"  },\r\n" + 
				"  gpsInfo \r\n" + 
				"  {\r\n" + 
				"    gpsLat 1,\r\n" + 
				"    gpsLong 13\r\n" + 
				"  },\r\n" + 
				"  siteInfo '65'H\r\n" + 
				"}";
				
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	ACEI decodedPdu = ACEI.readPdu(asnValueReader);
    	
		assertNotNull(decodedPdu.getMessage());
		assertNotNull(decodedPdu.getMessage().getMessageSequence());
		assertEquals(Integer.valueOf(6), decodedPdu.getMessage().getMessageSequence());
		assertTrue(Arrays.equals(new byte[] { 0x65 }, decodedPdu.getSiteInfo()));

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		ACEI.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(asnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  		
	}

}