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

	Object readPdu(Class valueClass, String hexaAsString) throws Exception {
		Class berClass = valueClass;
		Method method = berClass.getMethod("readPdu", new Class[] { BERReader.class });

		byte[] hexa = BERDumper.bytesFromString(hexaAsString);

		ByteArrayInputStream input = new ByteArrayInputStream(hexa);
		BERReader reader = new BERReader(input);
		Object ret = method.invoke(null, new Object[] { reader });

		assertEquals(valueClass, ret.getClass());

		return ret;
	}

	void writePdu(Object pdu, String expectedHexa) throws Exception {
		Class berClass = pdu.getClass();
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
