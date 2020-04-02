/*******************************************************************************
 * Copyright (C) 2020 Fred D7e (https://github.com/yafred)
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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class ASNValueWriter {
    static byte[] bitMask = new byte[] {
            (byte) 0x80, (byte) 0x40, (byte) 0x20, (byte) 0x10, (byte) 0x08, (byte) 0x04,
            (byte) 0x02, (byte) 0x01
        };

    private PrintWriter writer;
    private ArrayList<Sequence> sequences = new ArrayList<Sequence>();
    private boolean isWaitingForChoiceValue = false;

    public ASNValueWriter(PrintWriter writer) {
        this.writer = writer;
    }
    
    public void beginArray(String elementName) {
        indentValue();
        sequences.add(new Sequence(sequences.size() + 1, true, elementName));
        writer.println("{");
    }

    public void endArray() {
        sequences.remove(sequences.size() - 1);
        indent();
        writer.println("}");
    }

    public void beginSequence() {
        indentValue();
        sequences.add(new Sequence(sequences.size() + 1, false));
        writer.println("{");
    }

    public void endSequence() {
        sequences.remove(sequences.size() - 1);
        indent();
        writer.println("}");
    }

    public void writeComponent(String componentName) {
        indentComponent();
        writer.print(componentName + " ");
    }

    public void writeSelection(String selectionName) {
 
        if ((sequences.size() != 0) &&
                ((Sequence) sequences.get(sequences.size() - 1)).isArray &&
                !isWaitingForChoiceValue) {
        	// we indent only in SEQUENCE OF and SET OF
            indentComponent();
        }

        if (sequences.size() != 0) {
            Sequence sequence = (Sequence) sequences.get(sequences.size() - 1);

            if (sequence.isArray() && !sequence.getElementName().equals("")) {
            	writer.print(sequence.getElementName() + " ");
            }
        }

        writer.print(selectionName + " : ");
        isWaitingForChoiceValue = true;
    }

    public void writeInteger(java.lang.Integer value) {
        indentValue();
        writer.println(value);
    }

    public void writeBoolean(java.lang.Boolean value) {
        indentValue();

        boolean boolValue = value.booleanValue();

        if (boolValue) {
            writer.println("TRUE");
        } else {
            writer.println("FALSE");
        }
    }

    public void writeNull() {
        indentValue();
        writer.println("NULL");
    }

    public void writeEnumerated(String enumerated) {
        indentValue();
        writer.println(enumerated);
    }

    public void writeRestrictedCharacterString(String value) {
        indentValue();
        writer.println("\"" + value + "\"");
    }

    public void writeIdentifier(String value) {
        indentValue();
        writer.println(value);
    }

    public void writeOctetString(byte[] value) {
        indentValue();
        writer.print("'" + bytesToString(value) + "'H");
        //writer.print("  -- \"" + new String(value) + "\"");
        writer.println();
    }

    public void writeBitString(BitSet value) {
    	indentValue();
    	
        int size = value.length();
        StringBuffer buffer = new StringBuffer();
        buffer.append("'");

        if(size < 16) { // if bitset is small (this could be configured)
	        for (int i = 0; i < size; i++) {
	            buffer.append(value.get(i) ? "1" : "0");
	        }
	
	        buffer.append("'B");
        }
        else {
        	buffer.append(bytesToString(bitSetToBytes(value)));
	        buffer.append("'H");       	
        }
        writer.println(buffer.toString());
    }
    
    public void writeBitString(List<String> bitList) {
    	indentValue();
    	
    	boolean isFirst = true;
    	writer.print("{ ");
    	for(String bit : bitList) {
    		if(isFirst) {
    			isFirst = false;
    		}
    		else {
    			writer.print(", ");
    		}
    		writer.print(bit);
    	}
    	writer.println(" }");
    }
    
    public void writeObjectIdentifier(long[] value) {
    	// not implemented
    }
    
    public void writeRelativeOID(long[] value) {
    	// not implemented
    }

    public void flush() {
        writer.flush();
    }

    private void indent() {
        if (sequences.size() != 0) {
            writer.print(((Sequence) sequences.get(sequences.size() - 1)).getIndent());
        }
    }

    private void indentComponent() {
        if (sequences.size() != 0) {
            Sequence sequence = (Sequence) sequences.get(sequences.size() - 1);

            if (sequence.isEmpty) {
                writer.print(sequence.getIndent());
            } else {
                writer.print(",");
                writer.print(sequence.getIndent().substring(1));
            }

            sequence.setEmpty(false);
        }
    }

    private void indentValue() {
        if (sequences.size() != 0) {
            Sequence sequence = (Sequence) sequences.get(sequences.size() - 1);

            if (sequence.isArray() && !isWaitingForChoiceValue) {
                if (sequence.isEmpty) {
                    writer.print(sequence.getIndent());
                } else {
                    writer.print(",");
                    writer.print(sequence.getIndent().substring(1));
                }
                if(!sequence.getElementName().equals("")) {
                	writer.print(sequence.getElementName() + " ");
                }

                sequence.setEmpty(false);
            }
        }

        isWaitingForChoiceValue = false;
    }

    private static String bytesToString(byte[] buffer) {
        String text = "";

        for (int i = 0; i < buffer.length; i++) {
            String byteText = Integer.toHexString((int) buffer[i]);

            switch (byteText.length()) {
            case 1:
                byteText = "0" + byteText;

                break;

            case 2:
                break;

            default:
                byteText = byteText.substring(byteText.length() - 2,
                        byteText.length());

                break;
            }

            if (i == 0) {
                text = byteText;
            } else {
                text += (" " + byteText);
            }
        }

        return text;
    }
    
    /*
     * BitSet is not the best way to hold a bitstring
     * We have to scan all the bits of the BitSet
     */
    private static byte[] bitSetToBytes(BitSet value) {
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

        // convert bitset to byte[]
        byte[] buffer = new byte[nBytes];

        int currentIndex = 0;
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

        return buffer;
    }

    private class Sequence {
        private boolean isArray = false;
        private boolean isEmpty = true;
        private String elementName = "";

		private String indent = "";

        public Sequence(int rank, boolean isArray, String elementName) {
            for (int i = 0; i < rank; i++) {
                indent += "  ";
            }

            this.isArray = isArray;
            this.elementName = elementName;
        }

        public Sequence(int rank, boolean isArray) {
            for (int i = 0; i < rank; i++) {
                indent += "  ";
            }

            this.isArray = isArray;
        }

        public String getIndent() {
            return indent;
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public void setEmpty(boolean isEmpty) {
            this.isEmpty = isEmpty;
        }

        public boolean isArray() {
            return isArray;
        }
        
        public String getElementName() {
			return elementName;
		}

    }
}
