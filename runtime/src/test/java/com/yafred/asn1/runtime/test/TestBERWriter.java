package com.yafred.asn1.runtime.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yafred.asn1.runtime.BERDumper;
import com.yafred.asn1.runtime.BERTag;

public class TestBERWriter  {

    @Test
    public void test_one_byte_tag() {
    	BERTag tag;
    	String result;
    	
    	tag = new BERTag(BERTag.Class.UNIVERSAL, 16, BERTag.Form.CONSTRUCTED);   	
    	result = BERDumper.bytesToString(tag.getByteArray());
    	assertEquals("30", result);
    	
    	tag = new BERTag(BERTag.Class.CONTEXT, 0, BERTag.Form.PRIMITIVE);
    	result = BERDumper.bytesToString(tag.getByteArray());
    	assertEquals("80", result);    	
    }

    @Test
    public void test_long_tag() {
    	BERTag tag;
    	String result;
    	
    	tag = new BERTag(BERTag.Class.APPLICATION, 200, BERTag.Form.PRIMITIVE);   	
    	result = BERDumper.bytesToString(tag.getByteArray());
    	assertEquals("5f 81 48", result);
    }

}
