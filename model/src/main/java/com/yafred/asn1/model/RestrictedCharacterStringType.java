/*******************************************************************************
 * Copyright (C) 2020 Fred D7e (https://github.com/yafred)
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
