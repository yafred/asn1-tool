package com.yafred.asn1.model;

/**
 * Tag of a type
 * @see Type
 */
public class Tag {
    private Integer number = null;

    /**
     * Reference to an INTEGER value
     */
    private String definedValue = null;
    private TagClass tagClass = null;
    private TagMode tagMode = null;
    private Token tagToken = null;

    public Tag(Integer number, TagClass tagClass, TagMode tagMode) {
        this.number = number;
        this.tagClass = tagClass;
        this.tagMode = tagMode;
    }

    public Tag(String definedValue, TagClass tagClass, TagMode tagMode) {
        this.definedValue = definedValue;
        this.tagClass = tagClass;
        this.tagMode = tagMode;
    }

 
    public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getDefinedValue() {
		return definedValue;
	}

	public void setDefinedValue(String definedValue) {
		this.definedValue = definedValue;
	}

	public TagClass getTagClass() {
		return tagClass;
	}

	public void setTagClass(TagClass tagClass) {
		this.tagClass = tagClass;
	}

	public TagMode getTagMode() {
		return tagMode;
	}

	public void setTagMode(TagMode tagMode) {
		this.tagMode = tagMode;
	}

	public Token getTagToken() {
		return tagToken;
	}

	public void setTagToken(Token tagToken) {
		this.tagToken = tagToken;
	}

	/**
     * @see java.lang.Object#equals(Object)
     */
    @Override
	public boolean equals(Object obj) {
        if (!(obj instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) obj;
        
        boolean tagClassIdentical = false;
        tagClassIdentical = (tagClass == null && otherTag.tagClass == null);
        if(!tagClassIdentical && tagClass != null) {
        	tagClassIdentical = tagClass.equals(otherTag.tagClass);
        }
        

        return number.equals(otherTag.number) && tagClassIdentical;
    }
    
    public int getCanonicalClassRank() {
        if (tagClass == TagClass.UNIVERSAL_TAG) {
        	return 0;
        } else if (tagClass == TagClass.APPLICATION_TAG) {
        	return 1;
        } else if (tagClass == TagClass.PRIVATE_TAG) {
        	return 3;
        } else
        	return 2; // CONTEXT_TAG
    }

    /*
     * Canonical order
     */
    public boolean greaterThan(Tag tag) {
    	if(getCanonicalClassRank() == tag.getCanonicalClassRank()) {
    		return number > tag.getNumber();
    	}
    	else {
    		return getCanonicalClassRank() > tag.getCanonicalClassRank();
    	}
    }
}