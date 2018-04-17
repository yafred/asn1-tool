package com.yafred.asn1.runtime;

import java.io.IOException;
import java.io.OutputStream;

import java.util.BitSet;


public class BERWriter {
    static byte[] bitMask = new byte[] {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10, (byte) 0x08, (byte) 0x04,
            (byte) 0x02, (byte) 0x01
        };
    OutputStream out = null;
    byte[] buffer = null;
    int dataSize = 0;
    boolean flushFlag = false; // true when buffer has been written to out and no data has been encoded yet.
    int increment;

    public BERWriter(OutputStream out, int initialSize, int increment) {
        this.out = out;
        this.increment = increment;
        buffer = new byte[initialSize];
    }

    public BERWriter(OutputStream out) {
        this(out, 100, 100);
    }

    public BERWriter() {
        this(null);
    }

    public void flush() throws IOException {
        if (out != null) {
            out.write(buffer, buffer.length - dataSize, dataSize);
         }

        flushFlag = true;
    }

    void increaseDataSize(int nBytes) {
        if (flushFlag) {
            flushFlag = false;
            dataSize = 0;
        }

        if ((dataSize + nBytes) > buffer.length) {
            byte[] tempBuffer = new byte[buffer.length + increment + nBytes];
            System.arraycopy(buffer, buffer.length - dataSize, tempBuffer,
                tempBuffer.length - dataSize, dataSize);
            buffer = tempBuffer;
        }

        dataSize += nBytes;
    }

    public int writeInteger(java.lang.Integer value) {
        int intValue = value.intValue();
        int nBytes = 1; // bytes needed to write integer

        if (intValue >= 0) {
            if (intValue < 0x80) {
                nBytes = 1;
            } else if (intValue < 0x8000) {
                nBytes = 2;
            } else if (intValue < 0x800000) {
                nBytes = 3;
            } else {
                nBytes = 4;
            }
        } else {
            if (intValue > 0xffffff80) {
                nBytes = 1;
            } else if (intValue > 0xffff8000) {
                nBytes = 2;
            } else if (intValue > 0xff800000) {
                nBytes = 3;
            } else {
                nBytes = 4;
            }
        }

        increaseDataSize(nBytes);

        int begin = buffer.length - dataSize;
        int end = (begin + nBytes) - 1;

        for (int i = end; i >= begin; i--) {
            buffer[i] = (byte) intValue;
            intValue >>= 8;
        }

        return nBytes;
    }

    public int writeBitString(BitSet value) {
        // find last true bit
        int significantBitNumber = value.length();

        // count number of bytes
        int nBytes = 0;
        int nPadding = 0; // bit string is left aligned 

        if (significantBitNumber == 0) {
            nBytes = 0;
        } else {
            nBytes = significantBitNumber / 8;
            nPadding = significantBitNumber % 8;

            if (nPadding != 0) {
                nBytes += 1;
            }
        }

        // put into writer
        increaseDataSize(nBytes);

        int currentIndex = buffer.length - dataSize;
        int maskId = 0;

        for (int i = 0; i < significantBitNumber; i++) {
            if (value.get(i)) {
                buffer[currentIndex] |= bitMask[maskId];
            }

            if (maskId == 7) {
                currentIndex++;
                maskId = 0;
            } else {
                maskId++;
            }
        }

        // leading byte is number of unused bit in last byte
        increaseDataSize(1);

        if (nPadding == 0) {
            buffer[buffer.length - dataSize] = 0;
        } else {
            buffer[buffer.length - dataSize] = (byte) (8 - nPadding);
        }

        return nBytes + 1; // bit string bytes + leading byte (containing num of padding bits)
    }

    public int writeBoolean(java.lang.Boolean value) {
        boolean boolValue = value.booleanValue();
        increaseDataSize(1);

        if (boolValue) {
            buffer[buffer.length - dataSize] = (byte)0xFF;
        } else {
            buffer[buffer.length - dataSize] = 0x00;
        }

        return 1;
    }

    public int writeRestrictedCharacterString(String value) {
        byte[] bytes = value.getBytes();

        return writeOctetString(bytes);
    }

    public int writeLength(int value) {
        if (value < 0) {
            throw new RuntimeException("negative length");
        }

        int nBytes = 0;

        if (value > 0xFFFFFF) {
            nBytes = 5;
        } else if (value > 0xFFFF) {
            nBytes = 4;
        } else if (value > 0xFF) {
            nBytes = 3;
        } else if (value > 0x7F) {
            nBytes = 2;
        } else {
            nBytes = 1;
        }

        int nShift = 0;

        for (int i = nBytes; i > 1; i--, nShift += 8) {
            increaseDataSize(1);
            buffer[buffer.length - dataSize] = (byte) (value >> nShift);
        }

        // first byte is either number of subsequent bytes or the length itself
        increaseDataSize(1);

        if (nBytes > 1) {
            buffer[buffer.length - dataSize] = (byte) ((nBytes - 1) | 0x80);
        } else {
            buffer[buffer.length - dataSize] = (byte) (value);
        }

        return nBytes;
    }

    public int writeOctetString(byte[] value) {
        increaseDataSize(value.length);
        System.arraycopy(value, 0, buffer, buffer.length - dataSize, value.length);

        return value.length;
    }
	
	public int writeByte(byte value) {
		increaseDataSize(1);
        buffer[buffer.length - dataSize] = value;
		
		return 1;
	}

    /**
     * Provides a buffer containing the encoded data.
     * Returns null if no data has been encoded since the buffer has been flushed.
     */
    public byte[] getTraceBuffer() {
        byte[] copy = null;

        if (dataSize > 0) {
            copy = new byte[dataSize];
            System.arraycopy(buffer, buffer.length - dataSize, copy, 0, dataSize);
        }

        return copy;
    }
}
