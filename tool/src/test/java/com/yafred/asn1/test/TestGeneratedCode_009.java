package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;

import org.junit.Test;

import g_009.Flight;
import g_009.Flight_modern;
import g_009.Flight_with_optional;



public class TestGeneratedCode_009 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Flight pdu = new Flight();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		pdu.setDestination("London");
		pdu.setSeats(Flight.Seats.ideal);
		pdu.setCrew_format(Flight.Crew_format.eight);
		BitSet flags = new BitSet();
		flags.set(Flight.Flags.high_profile);
		flags.set(Flight.Flags.profitable);
		pdu.setFlags(flags);
		
		String expectedHexa = "30 21 16 04 52 6f 6d 65 80 06 42 65 72 6c 69 6e 16 06 4c 6f 6e 64 6f 6e 02 02 00 b4 0a 01 08 03 02 04 30";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		Flight decodedPdu = (Flight) testHelper.readPdu(Flight.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getDestination(), pdu.getDestination());
	}
	
	
	@Test
	public void test_2() throws Exception {
		Flight_modern pdu = new Flight_modern();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		pdu.setDestination("London");
		pdu.setSeats(Flight.Seats.ideal);
		pdu.setCrew_format(Flight_modern.Crew_format.eight);
		BitSet flags = new BitSet();
		flags.set(Flight.Flags.high_profile);
		flags.set(Flight.Flags.profitable);
		pdu.setFlags(flags);
		
		String expectedHexa = "30 21 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e 83 06 4c 6f 6e 64 6f 6e 84 02 00 b4 86 01 01 87 02 04 30";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		Flight_modern decodedPdu = (Flight_modern) testHelper.readPdu(Flight_modern.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getDestination(), pdu.getDestination());
		assertEquals(decodedPdu.getCrew_format(), pdu.getCrew_format());
	}
	
	@Test
	public void test_3() throws Exception {
		Flight_with_optional pdu = new Flight_with_optional();
		
		String expectedHexa = "30 00";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		Flight_modern decodedPdu = (Flight_modern) testHelper.readPdu(Flight_modern.class, expectedHexa);
	}
	
	@Test
	public void test_4() throws Exception {
		Flight_with_optional pdu = new Flight_with_optional();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		
		String expectedHexa = "30 0e 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e";
		testHelper.writePdu(pdu, expectedHexa);

		// decode
		Flight_modern decodedPdu = (Flight_modern) testHelper.readPdu(Flight_modern.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getStop1(), pdu.getStop1());
	}
	
	@Test
	public void test_5() throws Exception {
		Flight_modern pdu = new Flight_modern();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		pdu.setDestination("London");
		pdu.setSeats(Flight.Seats.ideal);
		pdu.setCrew_format(Flight_modern.Crew_format.eight);
		BitSet flags = new BitSet();
		flags.set(Flight.Flags.high_profile);
		flags.set(Flight.Flags.profitable);
		pdu.setFlags(flags);
		
		String expectedHexa = "30 80 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e 83 06 4c 6f 6e 64 6f 6e 84 02 00 b4 86 01 01 87 02 04 30 00 00";
		//testHelper.writePdu(pdu, expectedHexa);

		// decode
		Flight_modern decodedPdu = (Flight_modern) testHelper.readPdu(Flight_modern.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getDestination(), pdu.getDestination());
		assertEquals(decodedPdu.getCrew_format(), pdu.getCrew_format());
	}
	
	@Test
	public void test_6() throws Exception {
		Flight_with_optional pdu = new Flight_with_optional();
		pdu.setOrigin("Rome");
		pdu.setStop1("Berlin");
		
		String expectedHexa = "30 80 80 04 52 6f 6d 65 81 06 42 65 72 6c 69 6e 00 00";
		//testHelper.writePdu(pdu, expectedHexa);

		// decode
		Flight_modern decodedPdu = (Flight_modern) testHelper.readPdu(Flight_modern.class, expectedHexa);
		assertEquals(decodedPdu.getOrigin(), pdu.getOrigin());
		assertEquals(decodedPdu.getStop1(), pdu.getStop1());
	}
}
