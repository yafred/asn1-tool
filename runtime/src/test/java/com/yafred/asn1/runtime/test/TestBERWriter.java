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
package com.yafred.asn1.runtime.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

import org.junit.Test;

import com.yafred.asn1.runtime.BERDumper;
import com.yafred.asn1.runtime.BERWriter;

public class TestBERWriter  {

	@Test
    public void test_byte() {
		
		BERWriter writer = new BERWriter();
		int nBytes = writer.writeByte((byte)0xab);
		assertEquals(1, nBytes);
		
		assertEquals("ab", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
    public void test_boolean() {
		
		BERWriter writer = new BERWriter();
		int nBytes = writer.writeBoolean(Boolean.TRUE);
		assertEquals(1, nBytes);
		
		assertEquals("ff", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
    public void test_boolean2() {
		
		BERWriter writer = new BERWriter();
		int nBytes = writer.writeBoolean(Boolean.FALSE);
		assertEquals(1, nBytes);
		
		assertEquals("00", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
    public void test_restricted_character_string() {
		
		BERWriter writer = new BERWriter();
		int nBytes = writer.writeRestrictedCharacterString("Rome");
		assertEquals(4, nBytes);
		
		assertEquals("52 6f 6d 65", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_1b_integer() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(10));
		assertEquals(1, nBytes);
		
		assertEquals("0a", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_1b_integer2() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(127));
		assertEquals(1, nBytes);
		
		assertEquals("7f", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_1b_integer3() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(-128));
		assertEquals(1, nBytes);
		
		assertEquals("80", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_2b_integer() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(500));
		assertEquals(2, nBytes);
		
		assertEquals("01 f4", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_3b_integer() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(500000));
		assertEquals(3, nBytes);
		
		assertEquals("07 a1 20", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_4b_integer() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(80000000));
		assertEquals(4, nBytes);
		
		assertEquals("04 c4 b4 00", BERDumper.bytesToString(writer.getTraceBuffer()));
	}

	@Test
	public void test_4b_biginteger() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(java.math.BigInteger.valueOf(80000000));
		assertEquals(4, nBytes);
		
		assertEquals("04 c4 b4 00", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_4b_integer2() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(25000000));
		assertEquals(4, nBytes);
		
		assertEquals("01 7d 78 40", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_4b_integer3() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(-25000000));
		assertEquals(4, nBytes);
		
		assertEquals("fe 82 87 c0", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_4b_biginteger3() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(java.math.BigInteger.valueOf(-25000000));
		assertEquals(4, nBytes);
		
		assertEquals("fe 82 87 c0", BERDumper.bytesToString(writer.getTraceBuffer()));
	}

	@Test
	public void test_1b_negative_integer() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(-10));
		assertEquals(1, nBytes);
		
		assertEquals("f6", BERDumper.bytesToString(writer.getTraceBuffer()));
	}

	@Test
	public void test_2b_negative_integer() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(-500));
		assertEquals(2, nBytes);
		
		assertEquals("fe 0c", BERDumper.bytesToString(writer.getTraceBuffer()));
	}

	@Test
	public void test_3b_negative_integer() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(-500000));
		assertEquals(3, nBytes);
		
		assertEquals("f8 5e e0", BERDumper.bytesToString(writer.getTraceBuffer()));
	}

	@Test
	public void test_4b_negative_integer() {
		
		BERWriter writer = new BERWriter();	
		int nBytes = writer.writeInteger(Integer.valueOf(-80000000));
		assertEquals(4, nBytes);
		
		assertEquals("fb 3b 4c 00", BERDumper.bytesToString(writer.getTraceBuffer()));
	}

	@Test
	public void test_bitstring() {
		
		BERWriter writer = new BERWriter();	
		
		BitSet bitSet = new BitSet();
		bitSet.set(10);
		bitSet.set(20);
		int nBytes = writer.writeBitString(bitSet);
		assertEquals(4, nBytes);
		
		assertEquals("03 00 20 08", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_length_short() {
		
		BERWriter writer = new BERWriter();	
		
		int nBytes = writer.writeLength(100); // short form: 0 to 127
		assertEquals(1, nBytes);
		
		assertEquals("64", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_1b_length() {
		
		BERWriter writer = new BERWriter();	
		
		int nBytes = writer.writeLength(201); // long form: > 127
		assertEquals(2, nBytes);
		
		assertEquals("81 c9", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_2b_length() {
		
		BERWriter writer = new BERWriter();	
		
		int nBytes = writer.writeLength(500); // long form: > 127
		assertEquals(3, nBytes);
		
		assertEquals("82 01 f4", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_3b_length() {
		
		BERWriter writer = new BERWriter();	
		
		int nBytes = writer.writeLength(500000); // long form: > 127
		assertEquals(4, nBytes);
		
		assertEquals("83 07 a1 20", BERDumper.bytesToString(writer.getTraceBuffer()));
	}
	
	@Test
	public void test_4b_length() {
		
		BERWriter writer = new BERWriter();	
		
		int nBytes = writer.writeLength(80000000); // long form: > 127
		assertEquals(5, nBytes);
		
		assertEquals("84 04 c4 b4 00", BERDumper.bytesToString(writer.getTraceBuffer()));
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
