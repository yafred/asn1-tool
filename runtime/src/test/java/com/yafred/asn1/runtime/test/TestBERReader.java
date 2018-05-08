package com.yafred.asn1.runtime.test;

import com.yafred.asn1.runtime.BERReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.BitSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
            reader.isIndefiniteFormLength());
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
            !reader.isIndefiniteFormLength());
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

        assertTrue("Length should NOT be indefinite form",
            !reader.isIndefiniteFormLength());
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
            !reader.isIndefiniteFormLength());
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
        try {
			reader.mustMatchTag(new byte[] {0x1e});
		} catch (Exception e) {
			assertTrue("Tag should be " + hexaString, false);
		}
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
        try {
			reader.mustMatchTag(new byte[] {0});
		} catch (Exception e) {
			assertTrue("Tag should be 0", false);
		}
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
        	assertTrue(reader.matchTag(new byte[] { 0x65 }));
        	
        	reader.readLength();
        	assertEquals(3, reader.getLengthValue());
        	
        	reader.readTag();
        	assertTrue(reader.matchTag(new byte[] { 0x02 }));
       	
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
    Module DEFINITIONS  ::= 
    BEGIN
      My-Integer ::= [APPLICATION 5] INTEGER
      test-value My-Integer ::= 25
    END
    */
    public void test_trace() {
    	String hexaString = "65 03 02 01 19";
        BERReader reader = makeReader(hexaString);
        reader.setTraceBufferEnabled(true);
        
        try {
        	reader.readTag();
        	assertTrue(reader.matchTag(new byte[] { 0x65 }));
        	
        	reader.readLength();
        	assertEquals(3, reader.getLengthValue());
        	
        	reader.readTag();
        	assertTrue(reader.matchTag(new byte[] { 0x02 }));
       	
        	reader.readLength();
        	assertEquals(1, reader.getLengthValue());
        	
        	Integer intValue = reader.readInteger(1);
        	assertEquals(25, intValue.intValue());
        	
        	assertEquals(5, reader.getTraceLength());
        	assertEquals(hexaString, BERDumper.bytesToString(reader.getTraceBuffer()));
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
    	String hexaString = "5f 81 48 01 19";
        BERReader reader = makeReader(hexaString);
        
        try {
        	reader.readTag();
        	assertTrue(reader.matchTag(new byte[] { (byte)0x5f, (byte)0x81, (byte)0x48 } ));
        	
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
      My-Integer ::= [APPLICATION 100] INTEGER
      test-value My-Integer ::= 25
    END
    */
    public void test_long_tag2() {
    	String hexaString = "5f 64 01 19";
        BERReader reader = makeReader(hexaString);
        
        try {
        	reader.readTag();
        	assertTrue(reader.matchTag(new byte[] { (byte)0x5f, (byte)0x64 } ));
        	
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
        	assertTrue(reader.matchTag(new byte[] { 0x03 }));
        	
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
    
    @Test
    public void test_string() {
    	String hexaString = "52 6f 6d 65";
        BERReader reader = makeReader(hexaString);

        try {
			String text = reader.readRestrictedCharacterString(4);
			assertEquals("Rome", text);
		} catch (IOException e) {
	           assertTrue("Test should succeed", false);
	           e.printStackTrace();
		}
    }
    
    @Test
    public void test_boolean() {
    	String hexaString = "ff";
        BERReader reader = makeReader(hexaString);

        try {
			Boolean result = reader.readBoolean(1);
			assertTrue(result);
		} catch (IOException e) {
	           assertTrue("Test should succeed", false);
	           e.printStackTrace();
		}
    }
    
    @Test
    public void test_boolean2() {
    	String hexaString = "01";
        BERReader reader = makeReader(hexaString);

        try {
			Boolean result = reader.readBoolean(1);
			assertTrue(result);
		} catch (IOException e) {
	           assertTrue("Test should succeed", false);
	           e.printStackTrace();
		}
    }
    
    @Test
    public void test_boolean3() {
    	String hexaString = "00";
        BERReader reader = makeReader(hexaString);

        try {
			Boolean result = reader.readBoolean(1);
			assertFalse(result);
		} catch (IOException e) {
	           assertTrue("Test should succeed", false);
	           e.printStackTrace();
		}
    }
    
    @Test
    public void test_hexa() {
    	String hexaString = "00 01 a0 b0";
        BERReader reader = makeReader(hexaString);

        try {
			byte[] result = reader.readOctetString(4);
			assertEquals(hexaString, BERDumper.bytesToString(result));
		} catch (IOException e) {
	           assertTrue("Test should succeed", false);
	           e.printStackTrace();
		}
    }
}
