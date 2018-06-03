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
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import com.yafred.asn1.runtime.BERReader;
import com.yafred.asn1.runtime.BERWriter;

public class TestHelper {

	Object readPdu(Class valueClass, Class berClass, String hexaAsString) throws Exception {
		Method method = berClass.getMethod("readPdu", new Class[] { BERReader.class });

		byte[] hexa = BERDumper.bytesFromString(hexaAsString);

		ByteArrayInputStream input = new ByteArrayInputStream(hexa);
		BERReader reader = new BERReader(input);
		Object ret = method.invoke(null, new Object[] { reader });

		assertEquals(valueClass, ret.getClass());

		return ret;
	}

	void writePdu(Object pdu, Class berClass, String expectedHexa) throws Exception {
		Method method = berClass.getMethod("writePdu", new Class[] { pdu.getClass(), BERWriter.class });
		ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
		BERWriter writer = new BERWriter(bufferOut);

		method.invoke(null, new Object[] { pdu, writer });

		byte[] result = writer.getTraceBuffer();

		// dump to sysout
		System.out.println();
		System.out.println(">>>> " + pdu.getClass().getName());

		// dump byte form
		System.out.println(result.length + " bytes: " + BERDumper.bytesToString(result));

		// dump TLV form
		ByteArrayInputStream bufferIn = new ByteArrayInputStream(bufferOut.toByteArray());
		new BERDumper(new PrintWriter(System.out)).dump(bufferIn);

		// compare with expected
		String resultAsString = BERDumper.bytesToString(result);

		boolean isEqual = (resultAsString.equals(expectedHexa));

		if (!isEqual) {
			System.out.println("Expected: " + expectedHexa);
			System.out.println("Actual:   " + resultAsString);
		}

		assertTrue(isEqual);
	}

}
