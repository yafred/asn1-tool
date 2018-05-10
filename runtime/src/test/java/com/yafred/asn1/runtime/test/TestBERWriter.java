package com.yafred.asn1.runtime.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

import org.junit.Test;

import com.yafred.asn1.runtime.BERWriter;

public class TestBERWriter  {

	@Test
    public void test_byte() {
		
		BERWriter writer = new BERWriter();
		writer.writeByte((byte)0xab);
		
		assertEquals("ab", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
    public void test_boolean() {
		
		BERWriter writer = new BERWriter();
		writer.writeBoolean(Boolean.TRUE);
		
		assertEquals("ff", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
    public void test_boolean2() {
		
		BERWriter writer = new BERWriter();
		writer.writeBoolean(Boolean.FALSE);
		
		assertEquals("00", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
    public void test_restricted_character_string() {
		
		BERWriter writer = new BERWriter();
		writer.writeRestrictedCharacterString("Rome");
		
		assertEquals("52 6f 6d 65", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_integer() {
		
		BERWriter writer = new BERWriter();	
		writer.writeInteger(new Integer(10));
		
		assertEquals("0a", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_bitstring() {
		
		BERWriter writer = new BERWriter();	
		
		BitSet bitSet = new BitSet();
		bitSet.set(10);
		bitSet.set(20);
		writer.writeBitString(bitSet);
		
		assertEquals("03 00 20 08", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	
	@Test
	public void test_increase() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		BERWriter writer = new BERWriter(output, 5, 5);
		
		writer.writeOctetString(new byte[] {0x01, 0x02, 0x03, 0x04, 0x05 });
		assertEquals("01 02 03 04 05", BERDumper.bytesToString(writer.getTraceBuffer()));
		
		writer.writeLength(5);
		assertEquals("05 01 02 03 04 05", BERDumper.bytesToString(writer.getTraceBuffer()));

		writer.writeOctetString(new byte[] { (byte)0x0a });
		assertEquals("0a 05 01 02 03 04 05", BERDumper.bytesToString(writer.getTraceBuffer()));
		
		try {
			writer.flush();
		} catch (IOException e) {
            assertTrue("Test should succeed", false);
			e.printStackTrace();
		}

		assertEquals("0a 05 01 02 03 04 05", BERDumper.bytesToString(output.toByteArray()));		
	}
	
	@Test
	public void test_object_identifier() {
		BERWriter writer = new BERWriter();	
		boolean hasFailed = false;
		
		try {
			writer.writeObjectIdentifier(null);
		} catch(Exception e) {
			hasFailed = true;
		}
		assertTrue(hasFailed);
	}
	
	@Test
	public void test_object_identifier2() {
		BERWriter writer = new BERWriter();	
		boolean hasFailed = false;
		
		try {
			writer.writeObjectIdentifier(new long[] { 1 });
		} catch(Exception e) {
			hasFailed = true;
		}
		assertTrue(hasFailed);
	}
	
	@Test
	public void test_object_identifier3() {
		BERWriter writer = new BERWriter();	
		boolean hasFailed = false;
		
		try {
			writer.writeObjectIdentifier(new long[] { 3, 40 });
		} catch(Exception e) {
			hasFailed = true;
		}
		assertTrue(hasFailed);
	}
	
	@Test
	public void test_object_identifier4() {
		BERWriter writer = new BERWriter();	
		boolean hasFailed = false;
		
		try {
			writer.writeObjectIdentifier(new long[] { 1, 40 });
		} catch(Exception e) {
			hasFailed = true;
		}
		assertTrue(hasFailed);
	}
	
	@Test
	public void test_object_identifier5() {
		BERWriter writer = new BERWriter();	
		
		boolean hasFailed = false;		
		try {
			int length = writer.writeObjectIdentifier(new long[] { 1, 1, 40 });
			assertEquals(2, length);
			assertEquals("29 28", BERDumper.bytesToString(writer.getTraceBuffer()));
		} catch(Exception e) {
			hasFailed = true;
		}
		assertFalse(hasFailed);
	}
	
	@Test
	public void test_object_identifier6() {
		BERWriter writer = new BERWriter();	
		
		boolean hasFailed = false;		
		try {
			int length = writer.writeObjectIdentifier(new long[] { 1, 1, 200 });
			assertEquals(3, length);
			assertEquals("29 81 48", BERDumper.bytesToString(writer.getTraceBuffer()));
		} catch(Exception e) {
			hasFailed = true;
		}
		assertFalse(hasFailed);
	}

	@Test
	public void test_object_identifier7() {
		BERWriter writer = new BERWriter();	
		
		boolean hasFailed = false;		
		try {
			int length = writer.writeObjectIdentifier(new long[] { 2, 2000, 12000 });
			assertEquals(4, length);
			assertEquals("90 20 dd 60", BERDumper.bytesToString(writer.getTraceBuffer()));
		} catch(Exception e) {
			hasFailed = true;
		}
		assertFalse(hasFailed);
	}
	
	@Test
	public void test_relative_object_identifier() {
		BERWriter writer = new BERWriter();	
		
		boolean hasFailed = false;		
		try {
			int length = writer.writeRelativeOID(new long[] { 100, 2000, 12000 });
			assertEquals(5, length);
			assertEquals("64 8f 50 dd 60", BERDumper.bytesToString(writer.getTraceBuffer()));
		} catch(Exception e) {
			hasFailed = true;
		}
		assertFalse(hasFailed);
	}

}
