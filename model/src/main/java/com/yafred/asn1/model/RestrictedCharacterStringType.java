package com.yafred.asn1.model;

abstract public class RestrictedCharacterStringType extends Type {
	
	public boolean isRestrictedCharacterStringType() {
		return true;
	}
	
	public boolean isBMPStringType() {
		return false;
	}
	
	public boolean isGeneralStringType() {
		return false;		
	}
	
	public boolean isGraphicStringType() {
		return false;		
	}
	
	public boolean isIA5StringType() {
		return false;		
	}
	
	public boolean isISO646StringType() {
		return false;		
	}
	
	public boolean isNumericStringType() {
		return false;		
	}
	
	public boolean isPrintableStringType() {
		return false;		
	}
	
	public boolean isTeletexStringType() {
		return false;		
	}
	
	public boolean isT61StringType() {
		return false;		
	}
	
	public boolean isUniversalStringType() {
		return false;		
	}
	
	public boolean isUTF8StringType() {
		return false;		
	}
	
	public boolean isVideotexStringType() {
		return false;		
	}
	
	public boolean isVisibleStringType() {
		return false;		
	}
}
