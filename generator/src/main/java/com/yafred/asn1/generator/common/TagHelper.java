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
package com.yafred.asn1.generator.common;

import java.util.ArrayList;

import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.TagClass;


public class TagHelper {

	Tag tag;
	boolean isPrimitive;
 
    public TagHelper(Tag tag, boolean isPrimitive) {
    	this.tag = tag;
    	this.isPrimitive = isPrimitive;
    }

    public Byte[] getByteList() {

        ArrayList<Byte> result = new ArrayList<Byte>();

        int int1 = isPrimitive ? 0x00 : 0x20;

        if (tag.getTagClass() == TagClass.UNIVERSAL_TAG) {
            int1 |= 0;
        } else if (tag.getTagClass() == TagClass.APPLICATION_TAG) {
            int1 |= 0x40;
        } else if (tag.getTagClass() == TagClass.PRIVATE_TAG) {
            int1 |= 0xc0;
        } else { // CONTEXT
            int1 |= 0x80;
        } 

        int tagNumber = tag.getNumber().intValue();
        if (tagNumber < 31) {
            int1 += tagNumber;
            result.add(Byte.valueOf((byte) int1));
        } else {
            int1 += 31;
            result.add(Byte.valueOf((byte) int1));

            for (int highBits = tagNumber; highBits != 0; highBits = highBits >> 7) {
                int1 = (highBits & 0x7f) | 0x80;
                result.add(1, Byte.valueOf((byte) int1));
            }

            Byte last = result.get(result.size()-1);
            result.remove(last);
            result.add(Byte.valueOf((byte) (last.byteValue() & (byte) 0x7f)));
        }

        // make an array
        return result.toArray(new Byte[0]);
    }

    public byte[] getByteArray() {
        Byte[] tagBytes = getByteList();

        byte[] array = new byte[tagBytes.length];

        for (int i = 0; i < tagBytes.length; i++) {
            array[i] = tagBytes[i].byteValue();
        }

        return array;
    }
       
    public String toString() {
		return (isPrimitive ? "PRIMITIVE_" : "CONSTRUCTED_")
				+ ((tag.getTagClass() == null || tag.getTagClass().toString().equals("")) ? "CONTEXT" : tag.getTagClass().toString()) + "_"
				+ tag.getNumber();
    }



    public String tagBytesAsString() {
        byte[] tagBytes = this.getByteArray();
				
        String tagBytesAsString = ""; 
        
        if(tagBytes.length > 0) {
            tagBytesAsString += tagBytes[0] + " /*" + byteToHexString(tagBytes[0]) + "*/";
        }
        
        for(int i=1; i<tagBytes.length; i++) {
            tagBytesAsString += ", " + tagBytes[i] + " /*" + byteToHexString(tagBytes[i]) + "*/";
        }
        return tagBytesAsString;
    } 

    public String tagBytesAsGoString() {
        byte[] tagBytes = this.getByteArray();
				
        String tagBytesAsString = ""; 
        
        if(tagBytes.length > 0) {
            tagBytesAsString += byteToHexString(tagBytes[0]);
        }
        
        for(int i=1; i<tagBytes.length; i++) {
            tagBytesAsString += ", " + byteToHexString(tagBytes[i]);
        }
        return tagBytesAsString;
    } 

    private String byteToHexString(byte aByte) {
        int intByte = (int)aByte;

        if(intByte < 0) {
            intByte += 256;
        }

        String result = Integer.toHexString(intByte);
        
        if (result.length() == 1) {
            result = "0x0" + result;
        }
        else {
            result = "0x" + result;
        }

        return result;
    }
}
