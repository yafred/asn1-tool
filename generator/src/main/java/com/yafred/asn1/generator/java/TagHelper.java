package com.yafred.asn1.generator.java;

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
            result.add(new Byte((byte) int1));
        } else {
            int1 += 31;
            result.add(new Byte((byte) int1));

            for (int highBits = tagNumber; highBits != 0; highBits = highBits >> 7) {
                int1 = (highBits & 0x7f) | 0x80;
                result.add(1, new Byte((byte) int1));
            }

            Byte last = result.get(result.size()-1);
            result.remove(last);
            result.add(new Byte((byte) (last.byteValue() & (byte) 0x7f)));
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
}
