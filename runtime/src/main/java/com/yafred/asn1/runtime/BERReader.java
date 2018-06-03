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
package com.yafred.asn1.runtime;

import java.io.*;

import java.util.*;


public class BERReader {
    private static byte[] bitMask = new byte[] {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10, (byte) 0x08,
            (byte) 0x04, (byte) 0x02, (byte) 0x01
        };
    private java.io.InputStream in = null;

    /**
     * Length
     */
    int lengthLength;
    int lengthValue;
    boolean isIndefiniteFormLength;

    /**
     * Tag
     */
    private int tagNumBytes;
    private byte[] tagBuffer = new byte[10];
    private boolean tagMatched = true;
    
    /**
     * Trace
     */
    private byte[] traceBuffer = null;
    private int traceIndex = 0;
    private boolean isTraceBufferEnabled = false;
    private int traceBufferIncrement = 500;

    public BERReader(java.io.InputStream in) {
        this.in = in;
    }

    /**
     * Returns number of bytes received since last call to reset() (or creation of this object).
     */
    public int getTraceLength() {
        return traceIndex;
    }

    private int readChar() throws IOException {
        int value = in.read();

        if (value == -1) {
            throw new EOFException();
        }

        if (isTraceBufferEnabled) {
            checkTraceBufferSize();
            traceBuffer[traceIndex] = (byte) value;
        }

        traceIndex++;

        return value;
    }


    public void mustMatchTag(byte[]tag) throws Exception {
    	if(tagNumBytes != tag.length) {
    		throw new Exception("Expected size: " + tag.length +
                    ", actual: " + tagNumBytes);
    	}
    	for(int i=0; i<tag.length; i++) {
			if(tag[i] != tagBuffer[i]) {
				throw new Exception("Unexpected byte (expected: " + tag[i] +
		                ", actual: " + tagBuffer[i]);
			}
		}
    	this.tagMatched = true;
    }
    
    
    public boolean lookAheadTag(byte[][]tags) {
    	boolean foundMatch = false;
    	
    	for(int k=0; k<tags.length && !foundMatch; k++) {
    		byte[]tag = tags[k];
    		foundMatch = false;
        	if(tagNumBytes == tag.length) {
        		foundMatch = true;
        		for(int i=0; i<tag.length; i++) {
        			if(tag[i] != tagBuffer[i]) {
        				foundMatch = false;
        				break;
        			}
        		}
        	}
    	}
    	
    	return foundMatch;
    }
    
    
    public boolean matchTag(byte[]tag)  {
    	tagMatched = false;
    	if(tagNumBytes == tag.length) {
        	tagMatched = true;
    		for(int i=0; i<tag.length; i++) {
    			if(tag[i] != tagBuffer[i]) {
    				tagMatched = false;
    				break;
    			}
    		}
    	}
     	return tagMatched;
    }

    /**
     *
     * @throws IOException
     */
    public void readTag() throws IOException {
     	
        boolean isLastByte = false;
        tagNumBytes = 1;

        // read first byte
        tagBuffer[0] = (byte) readChar();

        if ((tagBuffer[0] & 0x1F) != 0x1F) { // short form
            isLastByte = true;
        }

        for (int i = 1; !isLastByte; i++) {
            tagBuffer[i] = (byte) readChar();
            tagNumBytes++;

            if ((tagBuffer[i] & 0x80) == 0) {
                isLastByte = true;
            }
        }
        
        // switch toggle (will be set again when length is read ... meaning that tag has been matched)
        tagMatched = false;   
     }

    /**
     *
     * @return
     */
    public int getTagLength() {
        return tagNumBytes;
    }

    /**
     *
     * @return
     */
    public byte[] getTag() {
        byte[] ret = new byte[tagNumBytes];
        System.arraycopy(tagBuffer, 0, ret, 0, tagNumBytes);

        return ret;
    }

    public void mustReadZeroLength() throws Exception {
    	readLength();
    	if(getLengthLength() != 1 || getLengthValue() != 0) {
    		throw new Exception("Expecting 0 length here");
    	}
    }
   
    
    /**
     * @throws IOException
     */
    public void readLength() throws IOException {
     	// if we read a length, this means that preceding tag has been recognized
    	tagMatched = true;
    	
        lengthLength = 0; // length of length
        lengthValue = 0; // value of length
        isIndefiniteFormLength = false;

        int aByte = readChar();

        if (aByte == 0x80) {
            lengthLength = 1;
            lengthValue = -1;
            isIndefiniteFormLength = true;
        } else {
            if (aByte > 0x7f) { // long form

                int nBytes = (aByte & 0x7f);

                if (nBytes > 4) {
                    throw new RuntimeException(
                        "Length over 4 bytes not supported");
                }

                lengthLength = nBytes + 1;
                lengthValue = 0;

                for (int i = nBytes; i > 0; i--) {
                    aByte = readChar();
                    lengthValue += (aByte << ((i - 1) * 8));
                }
            } else { // short form
                lengthLength = 1;
                lengthValue = aByte;
            }
        }
    }


    public Integer readInteger(int nBytes) throws IOException {
        if (nBytes > 4) {
            throw new RuntimeException("Integers over 4 bytes not supported");
        }

        int result = 0;
        int resultMask = 0;

        int aByte = readChar();

        if ((aByte & 0x80) == 0x80) { // negative number

            switch (nBytes) {
            case 1:
                resultMask = 0xffffff00;

                break;

            case 2:
                resultMask = 0xffff0000;

                break;

            case 3:
                resultMask = 0xff000000;

                break;
            }
        }

        result += (aByte << ((nBytes - 1) * 8));

        for (int i = nBytes - 1; i > 0; i--) {
            aByte = readChar();
            result += (aByte << ((i - 1) * 8));
        }

        return Integer.valueOf(result | resultMask);
    }

    public String readRestrictedCharacterString(int nBytes) throws IOException {
        byte[] buffer = new byte[nBytes];

        for (int i = 0; i < nBytes; i++) {
            buffer[i] = (byte) readChar();
        }

        return new String(buffer);
    }

    /**
     * Reads a bitstring encoded as primitive value
     */
    public BitSet readBitString(int nBytes) throws IOException {
        if (nBytes < 1) {
            throw new RuntimeException(
                "Length of a bitstring cannot be less than one");
        }

        if (nBytes == 1) {
            return new BitSet(0);
        }

        byte[] copy = new byte[nBytes];

        for (int i = 0; i < nBytes; i++) {
            copy[i] = (byte) readChar();
        }

        // first byte is number of padding bits
        int numSignificantBitInLastByte = 8 - copy[0];

        BitSet result = new BitSet();

        int bitIndex = 0;
        int byteIndex = 0;

        // handle full bytes
        for (byteIndex = 1; byteIndex < (nBytes - 1); byteIndex++) {
            for (int k = 0; k < 8; k++, bitIndex++) {
                if ((copy[byteIndex] & bitMask[k]) != 0x00) {
                    result.set(bitIndex);
                }
            }
        }

        // handle last byte
        for (int k = 0; k < numSignificantBitInLastByte; k++, bitIndex++) {
            if ((copy[byteIndex] & bitMask[k]) != 0x00) {
                result.set(bitIndex);
            }
        }

        return result;
    }
    
    public long[] readObjectIdentifier(int nBytes) throws IOException {
    	ArrayList<Long> objectIdentifier = new ArrayList<Long>();
    	byte[] buffer = readOctetString(nBytes);
    	long arc = -1;
    	long mult = 1;
        for (int i = nBytes-1; i >= 0; i--) {	
            if((buffer[i] & 0x80) == 0x00) {
            	if(arc != -1) {
            		objectIdentifier.add(0, new Long(arc));
            	}
            	arc = buffer[i];
            	mult = 1;
            }
            else {
              	// mult *= 128;
            	mult = Math.multiplyExact(mult, 128);
            	// arc += mult * (buffer[i] & 0x7F);
            	arc = Math.addExact(arc, Math.multiplyExact(mult, (buffer[i] & 0x7F))); // detect overflow
            }
        }
        if(arc < 40) {
            objectIdentifier.add(0, new Long(arc));
            objectIdentifier.add(0, new Long(0));                  	
        }
        else if(arc < 80) {
            objectIdentifier.add(0, new Long(arc-40));
            objectIdentifier.add(0, new Long(1));                  	
        }
        else {
            objectIdentifier.add(0, new Long(arc-80));
            objectIdentifier.add(0, new Long(2));                  	
        }    
        
        long[] ret = new long[objectIdentifier.size()];
        int i=0;
        for(Long arcAsLong : objectIdentifier) {
        	ret[i] = arcAsLong.longValue();
        	i++;
        }
        return ret;
    }
    
    public long[] readRelativeOID(int nBytes) throws IOException {
    	ArrayList<Long> objectIdentifier = new ArrayList<Long>();
    	byte[] buffer = readOctetString(nBytes);
    	long arc = -1;
    	long mult = 1;
        for (int i = nBytes-1; i >= 0; i--) {	
            if((buffer[i] & 0x80) == 0x00) {
            	if(arc != -1) {
            		objectIdentifier.add(0, new Long(arc));
            	}
            	arc = buffer[i];
            	mult = 1;
            }
            else {
              	// mult *= 128;
            	mult = Math.multiplyExact(mult, 128);
            	// arc += mult * (buffer[i] & 0x7F);
            	arc = Math.addExact(arc, Math.multiplyExact(mult, (buffer[i] & 0x7F))); // detect overflow
            }
        }
        objectIdentifier.add(0, new Long(arc));
        
        long[] ret = new long[objectIdentifier.size()];
        int i=0;
        for(Long arcAsLong : objectIdentifier) {
        	ret[i] = arcAsLong.longValue();
        	i++;
        }
        return ret;
    }

    public byte[] readOctetString(int nBytes) throws IOException {
        byte[] result = new byte[nBytes];

        for (int i = 0; i < nBytes; i++) {
            result[i] = (byte) readChar();
        }

        return result;
    }

    public Boolean readBoolean(int Bytes) throws IOException {
        int value = readChar();

        if (value == 0) {
            return new Boolean(false);
        } else {
            return new Boolean(true);
        }
    }

    /**
     * Enables bufferization for trace purposes.
     * Keeps all bytes received until reset() is called.
     */
    public void setTraceBufferEnabled(boolean state) {
        this.isTraceBufferEnabled = state;
    }

    /**
     * Provides a buffer containing all the bytes that have been received since enableTraceBuffer(true) or reset() was called.
     * Returns null if nothing has been traced.
     */
    public byte[] getTraceBuffer() {
        byte[] copy = null;

        if (traceIndex > 0) {
            copy = new byte[traceIndex];
            System.arraycopy(traceBuffer, 0, copy, 0, traceIndex);
        }

        return copy;
    }

    /**
     * Resets index and hence the trace.
     */
    public void reset() {
        traceIndex = 0;
    }

    /**
     * Checks traceBuffer is big enough. Enlarges it otherwise.
     */
    private void checkTraceBufferSize() {
        if (traceBuffer == null) {
            traceBuffer = new byte[traceBufferIncrement];
        } else {
            if (traceIndex >= traceBuffer.length) {
                byte[] old = traceBuffer;
                traceBuffer = new byte[old.length + traceBufferIncrement];
                System.arraycopy(old, 0, traceBuffer, 0, old.length);
            }
        }
    }

    public boolean isIndefiniteFormLength() {
        return isIndefiniteFormLength;
    }

    public int getLengthLength() {
        return lengthLength;
    }

    public int getLengthValue() {
        return lengthValue;
    }

    /*
     * False when a tag has just been read
     * True when a length has been read or a match method has been successful
     */
	public boolean isTagMatched() {
		return tagMatched;
	}
	
	public void setTagMatched(boolean tagMatched) {
		this.tagMatched = tagMatched;
	}

}
