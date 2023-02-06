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
package com.yafred.asn1.model;

import java.util.ArrayList;

import com.yafred.asn1.model.type.SelectionType;
import com.yafred.asn1.model.type.TypeReference;

/**
 * Abstract base for all ASN.1 types
 */
abstract public class Type {
	
	ArrayList<Tag> tagList;
		
	TokenLocation tokenLocation;

    String label; 
	
	Constraint constraint;
	
    public ArrayList<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<Tag> tagList) {
		this.tagList = tagList;
	}	

	public TokenLocation getTokenLocation() {
		return tokenLocation;
	}

	public void setTokenLocation(TokenLocation tokenLocation) {
		this.tokenLocation = tokenLocation;
	}
	
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

	public Constraint getConstraint() {
		return constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	public boolean isBitStringType() {
        return false;
    }

    public boolean isCharacterStringType() {
        return false;
    }
    
	public boolean isRestrictedCharacterStringType() {
		return false;
	}

    public boolean isOctetStringType() {
        return false;
    }

    public boolean isEnumeratedType() {
        return false;
    }

    public boolean isIntegerType() {
        return false;
    }

    public boolean isBooleanType() {
        return false;
    }

    public boolean isNullType() {
        return false;
    }

    public boolean isObjectIdentifierType() {
        return false;
    }
    
    public boolean isRelativeOIDType() {
        return false;
    }

    public boolean isRealType() {
        return false;
    }

    public boolean isEmbeddedPDVType() {
        return false;
    }

    public boolean isChoiceType() {
        return false;
    }

    public boolean isSetType() {
        return false;
    }

    public boolean isSequenceType() {
        return false;
    }

    public boolean isSetOfType() {
        return false;
    }

    public boolean isSequenceOfType() {
        return false;
    }

    public boolean isSelectionType() {
        return false;
    }
    
    public boolean isTypeWithComponents() {
    	return false;
    }
    
    public boolean isListOfType() {
    	return false;
    }
    
    public boolean isUTCTimeType() {
        return false;
    }

    public boolean isDateTimeType() {
        return false;
    }
    
    public boolean isDateType() {
        return false;
    }
    
    public boolean isDurationType() {
        return false;
    }
    
    public boolean isExternalType() {
        return false;
    }
    
    public boolean isGeneralizedTimeType() {
        return false;
    }
    
    public boolean isIRIType() {
        return false;
    }

    public boolean isObjectDescriptorType() {
        return false;
    }

    public boolean isTypeReference() {
        return false;
    }

    public boolean isAnyType() {
        return false;
    }
    
    abstract public String getName();
    
    /*
     * 3 types return null: TypeReference, ChoiceType and SelectionType
     */
    abstract public Tag getUniversalTag();
    
    /*
     * Get the first tag of this type (to validate Sequence and Set components)
     * Note that Choice does not have a UNIVERSAL tag and may return null
     */
    public Tag getFirstTag() {
    	Tag ret = null;
    	if(tagList != null && tagList.size() != 0) {
    		ret = tagList.get(0);
    	}
    	else {
    		if(isTypeReference()) {
    			TypeReference typeReference = (TypeReference)this;
    			ret = typeReference.getReferencedType().getFirstTag();
    		}
    		else if(isSelectionType()) {
    			SelectionType selectionType = (SelectionType)this;
    			ret = selectionType.getType().getFirstTag();
    		}
    		else {
    			ret = getUniversalTag();
    			if(ret != null) {
    				ret.setTagTokenLocation(getTokenLocation());
    			}
    		}
    	}

    	return ret;
    }
 	
    public void insertTag(Tag tag) {
        if(tagList == null) {
        	tagList = new ArrayList<Tag>();
        }
        tagList.add(0, tag); // insert at first position
    }
    
}
