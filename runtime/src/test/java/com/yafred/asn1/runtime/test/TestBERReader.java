package com.yafred.asn1.runtime.test;

import com.yafred.asn1.runtime.BERDumper;
import com.yafred.asn1.runtime.BERReader;
import com.yafred.asn1.runtime.BERTag;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.BitSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBERReader  {

    private BERReader makeReader(String hexaAsString) {
        byte[] hexa = BERDumper.bytesFromString(hexaAsString);

        ByteArrayInputStream input = new ByteArrayInputStream(hexa);
        BERReader reader = new BERReader(input);

        return reader;
    }

    @Test
    public void test_indefinite_length() {
        BERReader reader = makeReader("80");

        try {
            reader.readLength();
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }

        assertTrue("Length should be infinite form",
            reader.isInfiniteFormLength());
    }

    @Test
    public void test_short_form_length() {
        BERReader reader = makeReader("0f");

        try {
            reader.readLength();
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }

        assertTrue("Length should NOT be infinite form",
            !reader.isInfiniteFormLength());
        assertEquals(reader.getLengthLength(), 1);
        assertEquals(reader.getLengthValue(), 15);
    }

    @Test
    public void test_long_form_length1() {
        BERReader reader = makeReader("81 0a");

        try {
            reader.readLength();
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }

        assertTrue("Length should NOT be infinite form",
            !reader.isInfiniteFormLength());
        assertEquals(reader.getLengthLength(), 2);
        assertEquals(reader.getLengthValue(), 10);
    }

    @Test
    public void test_long_form_length2() {
        BERReader reader = makeReader("82 01 ff");

        try {
            reader.readLength();
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }

        assertTrue("Length should NOT be infinite form",
            !reader.isInfiniteFormLength());
        assertEquals(reader.getLengthLength(), 3);
        assertEquals(reader.getLengthValue(), 511);
    }

    @Test
    public void test_one_byte_tag() {
        String hexaString = "1e";
        BERReader reader = makeReader(hexaString);

        try {
            reader.readTag();
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }

        assertTrue("Tag should be one byte", reader.getTagLength() == 1);
        assertEquals(BERDumper.bytesToString(
                new byte[] { reader.getOneByteTag() }), hexaString);
    }
    
    @Test
    public void test_zero_tag() {
        String hexaString = "00";
        BERReader reader = makeReader(hexaString);

        try {
            reader.readTag();
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }

        assertTrue("Tag should be one byte", reader.getTagLength() == 1);
        assertEquals(reader.getOneByteTag(), 0);
    }
    
    @Test
    /*
    Module DEFINITIONS  ::= 
    BEGIN
      My-Integer ::= [APPLICATION 5] INTEGER
      test-value My-Integer ::= 25
    END
    */
    public void test_explicit_integer() {
    	String hexaString = "6503020119";
        BERReader reader = makeReader(hexaString);
        
        try {
        	reader.readTag();
        	assertEquals("CONSTRUCTED_APPLICATION_5", new BERTag(reader.getTag()).toString());
        	
        	reader.readLength();
        	assertEquals(3, reader.getLengthValue());
        	
        	reader.readTag();
        	assertEquals("PRIMITIVE_UNIVERSAL_2", new BERTag(reader.getTag()).toString());
       	
        	reader.readLength();
        	assertEquals(1, reader.getLengthValue());
        	
        	Integer intValue = reader.readInteger(1);
        	assertEquals(25, intValue.intValue());
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }  	
    }

    @Test
    /*
    Module DEFINITIONS IMPLICIT TAGS  ::= 
    BEGIN
      My-Integer ::= [APPLICATION 200] INTEGER
      test-value My-Integer ::= 25
    END
    */
    public void test_long_tag() {
    	String hexaString = "5F81480119";
        BERReader reader = makeReader(hexaString);
        
        try {
        	reader.readTag();
        	assertEquals("PRIMITIVE_APPLICATION_200", new BERTag(reader.getTag()).toString());
        	
        	reader.readLength();
        	assertEquals(1, reader.getLengthValue());
        	
        	Integer intValue = reader.readInteger(1);
        	assertEquals(25, intValue.intValue());
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }  	
    }
    
    @Test
    /*
    Module DEFINITIONS IMPLICIT TAGS  ::= 
    BEGIN
      Occupation  ::=  BIT STRING
          {
              clerk      (0),
              editor     (1),
              artist     (2),
              publisher  (3)
          }
      test-value Occupation ::= { editor, publisher }
    END
    */
    public void test_bit_string() {
    	String hexaString = "03020450";
        BERReader reader = makeReader(hexaString);
        
        try {
        	reader.readTag();
        	assertEquals("PRIMITIVE_UNIVERSAL_3", new BERTag(reader.getTag()).toString());
        	
        	reader.readLength();
        	assertEquals(2, reader.getLengthValue());
        	
        	BitSet bitStringValue = reader.readBitString(2);
        	assertEquals(false, bitStringValue.get(0));
        	assertEquals(true, bitStringValue.get(1));
        	assertEquals(false, bitStringValue.get(2));
        	assertEquals(true, bitStringValue.get(3));
        } catch (IOException e) {
            assertTrue("Test should succeed", false);
            e.printStackTrace();
        }  	    	
    }
}
