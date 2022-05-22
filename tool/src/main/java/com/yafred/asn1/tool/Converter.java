/*******************************************************************************
 * Copyright (C) 2022 Fred D7e (https://github.com/yafred)
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
package com.yafred.asn1.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.yafred.asn1.runtime.ASNValueReader;
import com.yafred.asn1.runtime.ASNValueWriter;
import com.yafred.asn1.runtime.BERWriter;
import com.yafred.asn1.runtime.BERDumper;
import com.yafred.asn1.runtime.BERReader;

public class Converter {
	
	final static String OPTION_INPUT_FILE = "i";
	final static String OPTION_INPUT_ENCODING = "dec";
	final static String OPTION_OUTPUT_FILE = "o";
	final static String OPTION_OUTPUT_ENCODING = "enc";
	final static String OPTION_PDU_CLASSNAME = "c";
	
	public static void main(String[] args) throws Exception {
		new Converter().process(args);
	}
	
	
	void process(String[] args) throws Exception {
		Properties gitProperties = new Properties();
		try {
			gitProperties.load(Converter.class.getClassLoader().getResourceAsStream("com/yafred/asn1/tool/git.properties"));
		}
		catch(Exception e) {
		}
				
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption( OPTION_INPUT_FILE, "input-file", true, "File to decode data from.");
		options.addOption( OPTION_INPUT_ENCODING, "decode", true, "Encoding type of the input: ASN, BER.");
		options.addOption( OPTION_OUTPUT_FILE, "output-file", true, "File to encode data to. (default stdout)");
		options.addOption( OPTION_OUTPUT_ENCODING, "encode", true, "Encoding type of the output: BER.");
		options.addOption( OPTION_PDU_CLASSNAME, "classname", true, "Class name (java) of the type to decode/encode. The class must be added to the classpath.");
		options.addOption( "v", "print-version", false, "Print the version text for this converter." );

		// parse the command line arguments
	    CommandLine line = parser.parse( options, args );

	    // version text
 		String version = "No version (probably not built from a git checkout)";

       	if(gitProperties.getProperty("git.tags") != null && !gitProperties.getProperty("git.tags").equals("")) {
       			version = gitProperties.getProperty("git.tags");
       	}
       	else 
	       	if(gitProperties.getProperty("git.commit.id.describe") != null && !gitProperties.getProperty("git.commit.id.describe").equals("")) {
		    	version = gitProperties.getProperty("git.commit.id.describe");	       			
       	}

       	if(line.getOptions().length == 0) {	       	
		    String header = "";
	    	String footer = "\nVersion: " + version + "\nPlease report issues at https://github.com/yafred/asn1-tool/issues";
	    	HelpFormatter formatter = new HelpFormatter();
	    	formatter.printHelp( "java -cp asn1-converter.jar com.yafred.asn1.tool.Converter", header, options, footer, true );
	    	System.exit(0);
	    }
      	
    	if(line.hasOption("v")) {
    		System.out.println(version);
    		System.exit(0);
    	}

		// We need a class name
		if (!line.hasOption(OPTION_PDU_CLASSNAME)) {
			System.err.println("Option -" + OPTION_PDU_CLASSNAME + " is required. (--"
					+ options.getOption(OPTION_PDU_CLASSNAME).getLongOpt() + ": "
					+ options.getOption(OPTION_PDU_CLASSNAME).getDescription() + ")");
			System.exit(1);
		}

    	// class must be in classpath
    	Class<?> pduClass = null;
		try {
			pduClass = Class.forName(line.getOptionValue(OPTION_PDU_CLASSNAME));
		} catch (ClassNotFoundException e) {
    		System.err.println(line.getOptionValue(OPTION_PDU_CLASSNAME) + " is not in the classpath");
        	System.exit(1);
		}
		
      	// Required arguments
		if (!line.hasOption(OPTION_INPUT_FILE)) {
			System.err.println("Option -" + OPTION_INPUT_FILE + " is required. (--"
					+ options.getOption(OPTION_INPUT_FILE).getLongOpt() + ": "
					+ options.getOption(OPTION_INPUT_FILE).getDescription() + ")");
			System.exit(1);
		}
		if (!line.hasOption(OPTION_INPUT_ENCODING)) {
			System.err.println("Option -" + OPTION_INPUT_ENCODING + " is required. (--"
					+ options.getOption(OPTION_INPUT_ENCODING).getLongOpt() + ": "
					+ options.getOption(OPTION_INPUT_ENCODING).getDescription() + ")");
			System.exit(1);
		}
		if (!line.hasOption(OPTION_OUTPUT_ENCODING)) {
			System.err.println("Option -" + OPTION_OUTPUT_ENCODING + " is required. (--"
					+ options.getOption(OPTION_OUTPUT_ENCODING).getLongOpt() + ": "
					+ options.getOption(OPTION_OUTPUT_ENCODING).getDescription() + ")");
			System.exit(1);
		}
		
    	// perform conversion
    	try {
    		Object pdu = null;
    		
    		InputStream inputStream = new FileInputStream(line.getOptionValue(OPTION_INPUT_FILE));
    		
			switch (line.getOptionValue(OPTION_INPUT_ENCODING)) {
			case "ASN":
				pdu = readASN(pduClass, inputStream);
				break;
			case "BER":
				pdu = readBER(pduClass, inputStream);
				break;
			default:
				System.err.println("Valid option -" + OPTION_INPUT_ENCODING + " is required. (--"
						+ options.getOption(OPTION_INPUT_ENCODING).getLongOpt() + ": "
						+ options.getOption(OPTION_INPUT_ENCODING).getDescription() + ")");
				System.exit(1);				
			}
			
			validate(pdu);
			
    		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	    String utf8 = StandardCharsets.UTF_8.name();
    	    PrintStream printStream = new PrintStream(byteArrayOutputStream, true, utf8);

			switch (line.getOptionValue(OPTION_OUTPUT_ENCODING)) {
			case "ASN":
				writeASN(pdu, printStream);
				break;
			case "BER":
				writeBER(pdu, printStream);
				break;
			default:
				System.err.println("Valid option -" + OPTION_OUTPUT_ENCODING + " is required. (--"
						+ options.getOption(OPTION_OUTPUT_ENCODING).getLongOpt() + ": "
						+ options.getOption(OPTION_OUTPUT_ENCODING).getDescription() + ")");
				System.exit(1);				
			}	
			
			System.out.println(byteArrayOutputStream.toString(utf8));
    
		} catch (Exception e) {
    		e.printStackTrace();
        	System.exit(1);
		}
	}
	
	private Object readASN(Class<?> pduClass, InputStream inputStream) throws Exception {
		ASNValueReader reader = new ASNValueReader(inputStream);
		Method method = pduClass.getMethod("readPdu", ASNValueReader.class);
		return method.invoke(null, new Object[] { reader });
	}
	
	private Object readBER(Class<?> pduClass, InputStream inputStream) throws Exception {
		Method method = pduClass.getMethod("readPdu", new Class[] { BERReader.class });

		byte[] hexa = BERDumper.bytesFromString(inputStreamToString(inputStream, 128));

		ByteArrayInputStream input = new ByteArrayInputStream(hexa);
		BERReader reader = new BERReader(input);
		Object ret = method.invoke(null, new Object[] { reader });
		return ret;
	}	

	private void writeASN(Object pdu, PrintStream printStream) throws Exception {
		Class<?> pduClass = pdu.getClass();
		Method method = pduClass.getMethod("writePdu", new Class[] { pduClass, ASNValueWriter.class });
		ASNValueWriter writer = new ASNValueWriter(new PrintWriter(printStream));
		method.invoke(null, new Object[] { pdu, writer });
		writer.flush();
	}
	
	private void writeBER(Object pdu, PrintStream printStream) throws Exception {
		Class<?> pduClass = pdu.getClass();
		Method method = pduClass.getMethod("writePdu", new Class[] { pduClass, BERWriter.class });
		ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
		BERWriter writer = new BERWriter(bufferOut);

		method.invoke(null, new Object[] { pdu, writer });

		byte[] result = writer.getTraceBuffer();

		// dump
		printStream.println();
		// dump byte form
		printStream.println(result.length + " bytes: " + BERDumper.bytesToString(result));

		// dump TLV form
		ByteArrayInputStream bufferIn = new ByteArrayInputStream(bufferOut.toByteArray());
		new BERDumper(new PrintWriter(printStream)).dump(bufferIn);		
	}
	

	private void validate(Object pdu) throws Exception {
		Class<?> pduClass = pdu.getClass();
		Method method = pduClass.getMethod("validate", new Class[] { pduClass });
		method.invoke(null, new Object[] { pdu });
	}

	public static String inputStreamToString(final InputStream is, final int bufferSize) {
	    final char[] buffer = new char[bufferSize];
	    final StringBuilder out = new StringBuilder();
	    try (Reader in = new InputStreamReader(is, "UTF-8")) {
	        for (;;) {
	            int rsz = in.read(buffer, 0, buffer.length);
	            if (rsz < 0)
	                break;
	            out.append(buffer, 0, rsz);
	        }
	    }
	    catch (UnsupportedEncodingException ex) {
	        /* ... */
	    }
	    catch (IOException ex) {
	        /* ... */
	    }
	    return out.toString();
	}
}
