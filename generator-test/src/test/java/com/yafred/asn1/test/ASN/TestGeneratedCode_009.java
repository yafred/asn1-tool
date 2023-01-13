/*******************************************************************************
 * Copyright (C) 2023 Fred D7e (https://github.com/yafred)
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import java.util.BitSet;

import org.junit.Test;

import com.yafred.asn1.runtime.ASNValueReader;
import com.yafred.asn1.runtime.ASNValueWriter;

import g_009.Flight;
import g_009.CrewFormat;
import g_009.Flags;
import g_009.Seats;
import g_009.FlightWithOptional;
import g_009.FlightWithRefs;



public class TestGeneratedCode_009 {

	@Test
	public void test_1() throws Exception {
		// with all the components 
		String asnValue = "{\r\n" + 
				"  origin \"Rome\",\r\n" + 
				"  stop1 \"Berlin\",\r\n" + 
				"  stop2 \"Paris\",\r\n" + 
				"  destination \"London\",\r\n" + 
				"  seats ideal,\r\n" + 
				"  cancelled FALSE,\r\n" + 
				"  crew-format eight,\r\n" + 
				"  flags { profitable, high-profile }\r\n" + 
				"}";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Flight decodedPdu = Flight.readPdu(asnValueReader);

    	assertEquals("Rome", decodedPdu.getOrigin());
		assertEquals("London", decodedPdu.getDestination());
		assertEquals(Flight.Seats.IDEAL, decodedPdu.getSeats());
		assertEquals(Flight.CrewFormat.EIGHT, decodedPdu.getCrewFormat());

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Flight.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(asnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  
	}
	
	@Test
	public void test_2() throws Exception {
		String asnValue = "{\r\n" + 
				"  origin \"Rome\",\r\n" + 
				"  stop1 \"Berlin\",\r\n" + 
				"  destination \"London\",\r\n" + 
				"  seats ideal,\r\n" + 
				"  cancelled FALSE,\r\n" + 
				"  crew-format eight,\r\n" + 
				"  flags { profitable, high-profile }\r\n" + 
				"}";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Flight decodedPdu = Flight.readPdu(asnValueReader);

    	assertEquals("Rome", decodedPdu.getOrigin());
		assertEquals("London", decodedPdu.getDestination());
		assertEquals(Flight.Seats.IDEAL, decodedPdu.getSeats());
		assertEquals(Flight.CrewFormat.EIGHT, decodedPdu.getCrewFormat());

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		Flight.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(asnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  
	}
	
	@Test
	public void test_3() throws Exception {
		String asnValue = "{\r\n" + 
				"  origin \"Rome\",\r\n" + 
				"  stop1 \"Berlin\",\r\n" + 
				"  stop2 \"Paris\",\r\n" + 
				"  seats ideal,\r\n" + 
				"  cancelled FALSE,\r\n" + 
				"  crew-format eight,\r\n" + 
				"  flags { profitable, high-profile }\r\n" + 
				"}";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	Flight decodedPdu = new Flight();
    	
    	boolean hasException = false;
    	try {
    		Flight.read(decodedPdu, asnValueReader);
    	}
    	catch(Exception e) {
    		hasException = true;
    	}
    	
    	assertEquals(true, hasException);
	}
	
	@Test
	public void test_4() throws Exception {
		String asnValue = "{}";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	FlightWithOptional.readPdu(asnValueReader);
	}

	@Test
	public void test_5() throws Exception {
		String asnValue = "{ stop \"\"}";
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	FlightWithOptional decodedPdu = new FlightWithOptional();

    	boolean hasException = false;
    	try {
    		FlightWithOptional.read(decodedPdu, asnValueReader);
    	}
    	catch(Exception e) {
    		hasException = true;
    	}
    	
    	assertEquals(true, hasException);
	}
	
	@Test
	public void test_6() throws Exception {
		FlightWithRefs pdu = new FlightWithRefs();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		pdu.setDestination("London");
		pdu.setSeats(Seats.IDEAL);
		pdu.setCrewFormat(CrewFormat.Enum.EIGHT);
		BitSet flags = new BitSet();
		flags.set(Flags.HIGH_PROFILE);
		flags.set(Flags.PROFITABLE);
		pdu.setFlags(flags);
		
		String asnValue = "{\r\n" + 
				"  origin \"Rome\",\r\n" + 
				"  stop1 \"Berlin\",\r\n" + 
				"  destination \"London\",\r\n" + 
				"  seats ideal,\r\n" + 
				"  crew-format eight,\r\n" + 
				"  flags { profitable, high-profile }\r\n" + 
				"}";
		
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	FlightWithRefs decodedPdu = FlightWithRefs.readPdu(asnValueReader);
    	
		assertEquals(pdu.getOrigin(), decodedPdu.getOrigin());
		assertEquals(pdu.getDestination(), decodedPdu.getDestination());
		assertEquals(pdu.getSeats(), decodedPdu.getSeats());
		assertEquals(pdu.getCrewFormat(), decodedPdu.getCrewFormat());
		
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));
		FlightWithRefs.writePdu(decodedPdu, asnValueWriter);
		
		assertEquals(asnValue.replaceAll("\\s+",""), stringWriter.toString().replaceAll("\\s+",""));  
	}
	
	@Test
	public void test_7() throws Exception {
		FlightWithRefs pdu = new FlightWithRefs();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		pdu.setDestination("London");
		pdu.setSeats(Seats.IDEAL);
		pdu.setCrewFormat(CrewFormat.Enum.EIGHT);
		BitSet flags = new BitSet();
		flags.set(Flags.HIGH_PROFILE);
		flags.set(Flags.PROFITABLE);
		pdu.setFlags(flags);
		
		String asnValue = "{" + 
				"  origin \"Rome\"," + 
				"  stop1 \"Berlin\"," + 
				"  destination \"London\"," + 
				"  seats ideal," + 
				"  crew-format eight," + 
				"  flags { profitable, high-profile }" + 
				"}";
		
    	InputStream inputStream = new ByteArrayInputStream(asnValue.getBytes(StandardCharsets.UTF_8));
    	ASNValueReader asnValueReader = new ASNValueReader(inputStream);
    	
    	FlightWithRefs decodedPdu = FlightWithRefs.readPdu(asnValueReader);
    	
		assertEquals(pdu.getOrigin(), decodedPdu.getOrigin());
		assertEquals(pdu.getDestination(), decodedPdu.getDestination());
		assertEquals(pdu.getSeats(), decodedPdu.getSeats());
		assertEquals(pdu.getCrewFormat(), decodedPdu.getCrewFormat());		
	}
}