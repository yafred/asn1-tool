package com.yafred.asn1.model;

public class TagMode {
    public static final TagMode EXPLICIT_TAG = new TagMode();
    public static final TagMode IMPLICIT_TAG = new TagMode();

    private TagMode() {
    }

	@Override
	public String toString() {
        if (this == EXPLICIT_TAG) {
            return "EXPLICIT";
        } else {
        	return "IMPLICIT";
        }
	}
}
