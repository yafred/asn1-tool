package com.yafred.asn1.model;

import java.util.ArrayList;

/**
 * Abstract base for all ASN.1 types
 */
abstract public class Type {
	
	ArrayList<Tag> tagList;
	
	TokenLocation tokenLocation;
	
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
