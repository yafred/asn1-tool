package com.yafred.asn1.model;

/**
 * class of a tag
 * @see Tag
 */
public class TagClass {
    public static final TagClass UNIVERSAL_TAG = new TagClass();
    public static final TagClass APPLICATION_TAG = new TagClass();
    public static final TagClass PRIVATE_TAG = new TagClass();

    private TagClass() {
    }

    @Override
	public String toString() {
        if (this == UNIVERSAL_TAG) {
        	return "UNIVERSAL";
        } else if (this == APPLICATION_TAG) {
        	return "APPLICATION";
        } else if (this == PRIVATE_TAG) {
        	return "PRIVATE";
        } else
        	return "";
    }
}
