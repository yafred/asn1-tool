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
import static org.junit.Assert.assertNull;

import java.util.BitSet;

import org.junit.Test;

import g_009.Flight;
import g_009.CrewFormat;
import g_009.Flags;
import g_009.Seats;
import g_009.FlightModern;
import g_009.FlightWithOptional;
import g_009.FlightWithRefs;
import g_009.FlightWithoutOptional;



public class TestGeneratedCode_009 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Flight pdu = new Flight();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		pdu.setDestination("London");
		pdu.setSeats(Flight.Seats.IDEAL);
		pdu.setCrewFormat(Flight.CrewFormat.EIGHT);
		BitSet flags = new BitSet();
		flags.set(Flight.Flags.HIGH_PROFILE);
		flags.set(Flight.Flags.PROFITABLE);
		pdu.setFlags(flags);
		
		String expectedHexa = "30 21 16 04 52 6f 6d 65 80 06 42 65 72 6c 69 6e 16 06 4c 6f 6e 64 6f 6e 02 02 00 b4 0a 01 08 03 02 04 30";
		testHelper.writePdu(pdu, Flight.class, expectedHexa);

		// decode
		Flight decodedPdu = (Flight) testHelper.readPdu(Flight.class, Flight.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getDestination(), pdu.getDestination());
	}
	
	
	@Test
	public void test_2() throws Exception {
		FlightModern pdu = new FlightModern();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		pdu.setDestination("London");
		pdu.setSeats(FlightModern.Seats.IDEAL);
		pdu.setCrewFormat(FlightModern.CrewFormat.EIGHT);
		BitSet flags = new BitSet();
		flags.set(FlightModern.Flags.HIGH_PROFILE);
		flags.set(FlightModern.Flags.PROFITABLE);
		pdu.setFlags(flags);
		
		String expectedHexa = "30 21 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e 83 06 4c 6f 6e 64 6f 6e 84 02 00 b4 86 01 01 87 02 04 30";
		testHelper.writePdu(pdu, FlightModern.class, expectedHexa);

		// decode
		FlightModern decodedPdu = (FlightModern) testHelper.readPdu(FlightModern.class, FlightModern.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getDestination(), pdu.getDestination());
		assertEquals(decodedPdu.getCrewFormat(), pdu.getCrewFormat());
	}
	
	@Test
	public void test_3() throws Exception {
		FlightWithOptional pdu = new FlightWithOptional();
		
		String expectedHexa = "30 00";
		testHelper.writePdu(pdu, FlightWithOptional.class, expectedHexa);

		// decode
		FlightWithOptional decodedPdu = (FlightWithOptional) testHelper.readPdu(FlightWithOptional.class, FlightWithOptional.class, expectedHexa);
		assertNull(decodedPdu.getOrigin());
	}
	
	@Test
	public void test_4() throws Exception {
		FlightWithOptional pdu = new FlightWithOptional();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		
		String expectedHexa = "30 0e 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e";
		testHelper.writePdu(pdu, FlightWithOptional.class, expectedHexa);

		// decode
		FlightWithOptional decodedPdu = (FlightWithOptional) testHelper.readPdu(FlightWithOptional.class, FlightWithOptional.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getStop1(), pdu.getStop1());
	}
	
	@Test
	public void test_5() throws Exception {
		FlightModern pdu = new FlightModern();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		pdu.setDestination("London");
		pdu.setSeats(FlightModern.Seats.IDEAL);
		pdu.setCrewFormat(FlightModern.CrewFormat.EIGHT);
		BitSet flags = new BitSet();
		flags.set(FlightModern.Flags.HIGH_PROFILE);
		flags.set(FlightModern.Flags.PROFITABLE);
		pdu.setFlags(flags);
		
		String expectedHexa = "30 80 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e 83 06 4c 6f 6e 64 6f 6e 84 02 00 b4 86 01 01 87 02 04 30 00 00";
		//testHelper.writePdu(pdu, FlightModern.class, expectedHexa);

		// decode
		FlightModern decodedPdu = (FlightModern) testHelper.readPdu(FlightModern.class, FlightModern.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getDestination(), pdu.getDestination());
		assertEquals(decodedPdu.getCrewFormat(), pdu.getCrewFormat());
	}
	
	@Test
	public void test_6() throws Exception {
		FlightWithOptional pdu = new FlightWithOptional();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		
		String expectedHexa = "30 80 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e 00 00";
		//testHelper.writePdu(pdu, FlightWithOptional.class, expectedHexa);

		// decode
		FlightWithOptional decodedPdu = (FlightWithOptional) testHelper.readPdu(FlightWithOptional.class, FlightWithOptional.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getStop1(), pdu.getStop1());
	}
	
	@Test
	public void test_7() throws Exception {
		FlightWithoutOptional pdu = new FlightWithoutOptional();
		pdu.setOrigin("Rome");
		pdu.setDestination("Berlin");
		
		String expectedHexa = "30 80 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e 00 00";
		//testHelper.writePdu(pdu, FlightWithoutOptional.class, expectedHexa);

		// decode
		FlightWithoutOptional decodedPdu = (FlightWithoutOptional) testHelper.readPdu(FlightWithoutOptional.class, FlightWithoutOptional.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getDestination(), pdu.getDestination());
	}
	
	@Test
	public void test_8() throws Exception {
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
		
		String expectedHexa = "30 21 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e 83 06 4c 6f 6e 64 6f 6e 84 02 00 b4 86 01 01 87 02 04 30";
		testHelper.writePdu(pdu, FlightWithRefs.class, expectedHexa);

		// decode
		FlightWithRefs decodedPdu = (FlightWithRefs) testHelper.readPdu(FlightWithRefs.class, FlightWithRefs.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getDestination(), pdu.getDestination());
		assertEquals(decodedPdu.getCrewFormat(), pdu.getCrewFormat());
	}



}
