package com.yafred.asn1.model;

/**
 * Tagging default of a module
 * @see ModuleDefinition
 */
public class TagDefault  {
    public static final TagDefault EXPLICIT_TAGS = new TagDefault();
    public static final TagDefault IMPLICIT_TAGS = new TagDefault();
    public static final TagDefault AUTOMATIC_TAGS = new TagDefault();

    private TagDefault() {
    }

    @Override
	public String toString() {
        if (this == EXPLICIT_TAGS) {
            return "EXPLICIT";
        } else if (this == IMPLICIT_TAGS) {
            return "IMPLICIT";
        } else if (this == AUTOMATIC_TAGS) {
            return "AUTOMATIC";
        } else {
            return ""; // cannot occur
        }
    }
}
