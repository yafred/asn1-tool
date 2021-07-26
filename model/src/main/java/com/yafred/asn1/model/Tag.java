/*******************************************************************************
 * Copyright (C) 2021 Fred D7e (https://github.com/yafred)
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
    private TokenLocation tagTokenLocation = null;

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

	public TokenLocation getTagTokenLocation() {
		return tagTokenLocation;
	}

	public void setTagTokenLocation(TokenLocation tagTokenLocation) {
		this.tagTokenLocation = tagTokenLocation;
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
