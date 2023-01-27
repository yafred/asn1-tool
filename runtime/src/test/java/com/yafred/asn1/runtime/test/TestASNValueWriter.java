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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.BitSet;

import org.junit.Test;

import com.yafred.asn1.runtime.ASNValueWriter;

public class TestASNValueWriter {

	@Test
	public void test_bitset() throws Exception {

		BitSet value = new BitSet();
		value.set(0);
		value.set(2);
		value.set(7);
		value.set(8);
		value.set(10);
		value.set(14);
		value.set(15);
		value.set(16);

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));

		asnValueWriter.writeBitString(value);

		String expectedAsnValue = "'a1a380'H";

		assertEquals(expectedAsnValue.replaceAll("\\s+", ""), stringWriter.toString().replaceAll("\\s+", ""));
	}

	@Test
	public void test_bitset_namedBits() throws Exception {

		ArrayList<String> value = new ArrayList<String>();
		value.add("one");
		value.add("two");

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));

		asnValueWriter.writeBitString(value);

		String expectedAsnValue = "{ one, two }";

		assertEquals(expectedAsnValue.replaceAll("\\s+", ""), stringWriter.toString().replaceAll("\\s+", ""));

	}

	@Test
	public void test_array() throws Exception {

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));

		asnValueWriter.beginArray("item");
		asnValueWriter.writeRestrictedCharacterString("hello");
		asnValueWriter.writeRestrictedCharacterString("world");
		asnValueWriter.endArray();

		String expectedAsnValue = "{ item \"hello\", item \"world\" }";

		assertEquals(expectedAsnValue.replaceAll("\\s+", ""), stringWriter.toString().replaceAll("\\s+", ""));
	}

	@Test
	public void test_array_of_choices() throws Exception {

		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));

		asnValueWriter.beginArray("item");
		asnValueWriter.writeSelection("word1");
		asnValueWriter.writeRestrictedCharacterString("hello");
		asnValueWriter.writeSelection("word2");
		asnValueWriter.writeRestrictedCharacterString("world");
		asnValueWriter.endArray();

		String expectedAsnValue = "{ item word1 : \"hello\", item word2 : \"world\" }";

		assertEquals(expectedAsnValue.replaceAll("\\s+", ""), stringWriter.toString().replaceAll("\\s+", ""));
	}

	@Test
	public void test_object_identifier() throws Exception {
		StringWriter stringWriter = new StringWriter(100);
		ASNValueWriter asnValueWriter = new ASNValueWriter(new PrintWriter(stringWriter));

		asnValueWriter.writeObjectIdentifier(new long[] { 1, 2, 3, 4, 5 });

		String expectedAsnValue = "{ 1 2 3 4 5 }";

		assertEquals(expectedAsnValue.replaceAll("\\s+", ""), stringWriter.toString().replaceAll("\\s+", ""));
	}
}