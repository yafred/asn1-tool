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
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.BitSet;

import org.junit.Test;

import com.yafred.asn1.runtime.ASNValueReader;

public class TestASNValueReader {

	@Test
	public void test_read_identifier_1() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("\n \t mySeq ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals("mySeq", asnValueReader.readIdentifier());
	}

	@Test
	public void test_read_identifier_2() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("aSeq{ ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals("aSeq", asnValueReader.readIdentifier());
	}

	@Test
	public void test_read_integer_1() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("\n \t 123 ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals(Integer.valueOf(123), asnValueReader.readInteger());
	}

	@Test
	public void test_read_integer_2() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("123, ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals(Integer.valueOf(123), asnValueReader.readInteger());
	}

	@Test
	public void test_read_restrictedCharacterString_1() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("mySeq".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		boolean exceptionCaught = false;
		try {
			asnValueReader.readRestrictedCharacterString();
		} catch (Exception e) {
			exceptionCaught = true;
		}
		assertTrue("Must throw an exception", exceptionCaught);
	}

	@Test
	public void test_read_restrictedCharacterString_2() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("\"some text".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		boolean exceptionCaught = false;
		try {
			asnValueReader.readRestrictedCharacterString();
		} catch (Exception e) {
			exceptionCaught = true;
		}
		assertTrue("Must throw an exception", exceptionCaught);
	}

	@Test
	public void test_read_restrictedCharacterString_3() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("\"some text \"".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals("some text ", asnValueReader.readRestrictedCharacterString());
	}

	@Test
	public void test_read_restrictedCharacterString_4() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("\"\"".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals("", asnValueReader.readRestrictedCharacterString());
	}

	@Test
	public void test_read_token_1() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("bla,".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals("bla", asnValueReader.readIdentifier());
		assertEquals(",", asnValueReader.readToken());
	}

	@Test
	public void test_read_token_2() throws Exception {
		InputStream inputStream = new ByteArrayInputStream(" : ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals(":", asnValueReader.readToken());
	}

	@Test
	public void test_read_boolean() throws Exception {
		InputStream inputStream = new ByteArrayInputStream(" TRUE ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals(Boolean.TRUE, asnValueReader.readBoolean());
	}

	@Test
	public void test_read_null() throws Exception {
		InputStream inputStream = new ByteArrayInputStream(" NULL ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		asnValueReader.readNull();
	}

	@Test
	public void test_read_OctetString_1() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("0102'H".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		boolean exceptionCaught = false;
		try {
			asnValueReader.readOctetString();
		} catch (Exception e) {
			exceptionCaught = true;
		}
		assertTrue("Must throw an exception", exceptionCaught);
	}

	@Test
	public void test_read_OctetString_2() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("'0102'".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		boolean exceptionCaught = false;
		try {
			asnValueReader.readOctetString();
		} catch (Exception e) {
			exceptionCaught = true;
		}
		assertTrue("Must throw an exception", exceptionCaught);
	}

	@Test
	public void test_read_OctetString_3() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("'0102'H".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		byte[] value = null;
		value = asnValueReader.readOctetString();

		assertEquals(value.length, 2);
		assertEquals("byte should be 0x01", 0x01, value[0]);
		assertEquals("byte should be 0x02", 0x02, value[1]);
	}

	@Test
	public void test_read_OctetString_4() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("'0a0B'H".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		byte[] value = null;
		value = asnValueReader.readOctetString();

		assertEquals(value.length, 2);
		assertEquals("byte should be 0x0a", 0x0a, value[0]);
		assertEquals("byte should be 0x0b", 0x0b, value[1]);
	}

	@Test
	public void test_look_ahead() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("  mySeq ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		assertEquals("m", asnValueReader.lookAheadToken());
		assertEquals("mySeq", asnValueReader.readIdentifier());
	}

	@Test
	public void test_read_BitString_1() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("0101'B".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		boolean exceptionCaught = false;
		try {
			asnValueReader.readBitString();
		} catch (Exception e) {
			exceptionCaught = true;
		}
		assertTrue("Must throw an exception", exceptionCaught);
	}

	@Test
	public void test_read_BitString_2() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("'0101'".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		boolean exceptionCaught = false;
		try {
			asnValueReader.readBitString();
		} catch (Exception e) {
			exceptionCaught = true;
		}
		assertTrue("Must throw an exception", exceptionCaught);
	}

	@Test
	public void test_read_BitString_3() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("'0101'B".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		BitSet decodedValue = null;
		decodedValue = asnValueReader.readBitString();

		BitSet value = new BitSet(4);
		value.set(1);
		value.set(3);

		assertEquals(value, decodedValue);
	}

	@Test
	public void test_read_BitString_4() throws Exception {
		InputStream inputStream = new ByteArrayInputStream("'501'H".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);
		BitSet decodedValue = null;
		decodedValue = asnValueReader.readBitString();

		BitSet value = new BitSet(12);
		value.set(1);
		value.set(3);
		value.set(11);

		assertEquals(value, decodedValue);
	}

	@Test
	public void test_named_list_element() throws Exception {
		InputStream inputStream = new ByteArrayInputStream(" item \"toto\" ".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);

		assertEquals("item", asnValueReader.lookAheadIdentifier());
		assertEquals("item", asnValueReader.readIdentifier());
		assertEquals("toto", asnValueReader.readRestrictedCharacterString());
	}

	@Test
	public void test_object_identifier() throws Exception {
		InputStream inputStream = new ByteArrayInputStream(" { 1 2 3 4 5}".getBytes(StandardCharsets.UTF_8));
		ASNValueReader asnValueReader = new ASNValueReader(inputStream);

		long[] decodedValue = asnValueReader.readObjectIdentifier();
		long[] expectedValue = new long[] {1,2,3,4,5};

		assertTrue("Should be the same", Arrays.equals(decodedValue, expectedValue));
	}
}