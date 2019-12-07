/*******************************************************************************
 * Copyright (C) 2019 Fred D7e (https://github.com/yafred)
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

public class NamedNumber {
    private String name = null;
    private Integer number = null;
    private String reference = null; // reference to an integer value
    
    private TokenLocation nameTokenLocation;
    private TokenLocation numberOrReferenceTokenLocation;

    public NamedNumber(String name) { // for enumeratedType only
        this.name = name;
        this.number = null;
    }
    public NamedNumber(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    public NamedNumber(String name, String reference) {
        this.name = name;
        this.reference = reference;
    }

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public TokenLocation getNameTokenLocation() {
		return nameTokenLocation;
	}
	
	public void setNameTokenLocation(TokenLocation nameTokenLocation) {
		this.nameTokenLocation = nameTokenLocation;
	}
	
	public TokenLocation getNumberOrReferenceTokenLocation() {
		return numberOrReferenceTokenLocation;
	}
	
	public void setNumberOrReferenceTokenLocation(TokenLocation numberOrReferenceTokenLocation) {
		this.numberOrReferenceTokenLocation = numberOrReferenceTokenLocation;
	}
}
