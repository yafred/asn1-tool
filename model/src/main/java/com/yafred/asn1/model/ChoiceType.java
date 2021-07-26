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

import java.util.ArrayList;


public class ChoiceType extends TypeWithComponents {
	private ArrayList<NamedType> alternativeListIncludingNested = null ; 

    public ChoiceType(ArrayList<Component> rootAlternativeList,
    		ArrayList<Component> additionalAlternativeList) {

        super(rootAlternativeList, additionalAlternativeList, null);
    }

    public ArrayList<Component> getRootAlternativeList() {
		return getRootComponentList();
	}
    
    public void setRootAlternativeList(ArrayList<Component> rootAlternativeList) {
    	setRootComponentList(rootAlternativeList);
    }

	public ArrayList<Component> getAdditionalAlternativeList() {
		return getExtensionComponentList();
	}
	
	public void setAdditionalAlternativeList(ArrayList<Component> additionalAlternativeList) {
		setExtensionComponentList(additionalAlternativeList);
	}

	public ArrayList<NamedType> getAlternativeListIncludingNested() {
		return alternativeListIncludingNested;
	}

	public void setAlternativeListIncludingNested(ArrayList<NamedType> alternativeListIncludingNested) {
		this.alternativeListIncludingNested = alternativeListIncludingNested;
	}

	@Override
	public boolean isChoiceType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return null;
    }
    
	@Override
	public String getName() {
		return ("CHOICE");
	}
}
