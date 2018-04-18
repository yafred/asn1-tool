package com.yafred.asn1.generator.java.test;

import org.junit.Test;

import com.yafred.asn1.generator.java.TagHelper;
import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.TagClass;

public class TagHelperTest {

	@Test
	public void test_1() {
		Tag tag = new Tag(new Integer(32), TagClass.UNIVERSAL_TAG, null);
		TagHelper tagHelper = new TagHelper(tag, true);
		
		byte[] encoded = tagHelper.getByteArray();
		String tagComment = tagHelper.toString();

	}
	
}
