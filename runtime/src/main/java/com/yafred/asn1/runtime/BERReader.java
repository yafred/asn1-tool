package com.yafred.asn1.runtime;

import java.io.*;

import java.util.*;


public class BERReader {
    private static byte[] bitMask = new byte[] {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10, (byte) 0x08,
            (byte) 0x04, (byte) 0x02, (byte) 0x01
        };
    private java.io.InputStream in = null;
    private boolean readingUntaggedChoice = false;

    /**
     * Length
     */
    int lengthLength;
    int lengthValue;
    boolean isInfiniteFormLength;

    /**
     * Tag
     */
    private int tagNumBytes;
    private byte[] tagBuffer = new byte[10];

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
     * Returns true if array1 and array2 have same size and content
     */
    static public boolean match(byte[] array1, byte[] array2) {
        boolean match = true;

        if (array1.length == array2.length) {
            int i = 0;

            for (i = 0; (i < array1.length) && (array1[i] == array2[i]); i++)
                ;

            if (i < array1.length) {
                match = false;
            }
        } else {
            match = false;
        }

        return match;
    }

    /**
     * Returns number of bytes received since last call to reset() (or creation of this object).
     */
    public int getNumReceived() {
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

    /**
     * @param expectedByte
     * @throws Exception
     */
    public void expectByte(byte expectedByte) throws Exception {
        byte actualByte = (byte) readChar();

        if (actualByte != expectedByte) {
            throw new Exception("Unexpected byte (expected: " + expectedByte +
                ", actual: " + actualByte);
        }
    }

    public byte readByte() throws Exception {
        return (byte) readChar();
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
    }
    
    public boolean matchTag(byte[]tag)  {
    	boolean ret = true;
    	if(tagNumBytes == tag.length) {
    		for(int i=0; i<tag.length; i++) {
    			if(tag[i] != tagBuffer[i]) {
    				ret = false;
    				break;
    			}
    		}
    	}
    	return ret;
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
     * @throws Exception
     */
    public byte getOneByteTag() {
        return tagBuffer[0];
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

    /**
     * @throws IOException
     */
    public void readLength() throws IOException {
        lengthLength = 0; // length of length
        lengthValue = 0; // value of length
        isInfiniteFormLength = false;

        int aByte = readChar();

        if (aByte == 0x80) {
            lengthLength = 1;
            lengthValue = 0;
            isInfiniteFormLength = true;
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

    public long skip(long length) throws IOException {
        for (int i = 0; i < length; i++) {
            readChar();
        }

        return length;
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

        return new Integer(result | resultMask);
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

    public boolean isReadingUntaggedChoice() {
        return readingUntaggedChoice;
    }

    public void setReadingUntaggedChoice(boolean readingUntaggedChoice) {
        this.readingUntaggedChoice = readingUntaggedChoice;
    }

    public boolean isInfiniteFormLength() {
        return isInfiniteFormLength;
    }

    public int getLengthLength() {
        return lengthLength;
    }

    public int getLengthValue() {
        return lengthValue;
    }
}
