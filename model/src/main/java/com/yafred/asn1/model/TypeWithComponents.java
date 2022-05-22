/*******************************************************************************
 * Copyright (C) 2022 Fred D7e (https://github.com/yafred)
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


abstract public class TypeWithComponents extends Type {
    private ArrayList<Component> rootComponentList = null;
    private ArrayList<Component> extensionComponentList = null;
    private ArrayList<Component> additionalComponentList = null;
    
    private boolean automaticTaggingSelected = false;

    public TypeWithComponents(ArrayList<Component> rootComponentList,
    		ArrayList<Component> extensionComponentList,
    		ArrayList<Component> additionalComponentList) {
    	this.rootComponentList = rootComponentList;
    	this.extensionComponentList = extensionComponentList;
    	this.additionalComponentList = additionalComponentList;
    }

    public ArrayList<Component> getRootComponentList() {
		return rootComponentList;
	}

	public void setRootComponentList(ArrayList<Component> rootComponentList) {
		this.rootComponentList = rootComponentList;
	}

	public ArrayList<Component> getExtensionComponentList() {
		return extensionComponentList;
	}

	public void setExtensionComponentList(ArrayList<Component> extensionComponentList) {
		this.extensionComponentList = extensionComponentList;
	}

	public ArrayList<Component> getAdditionalComponentList() {
		return additionalComponentList;
	}

	public void setAdditionalComponentList(ArrayList<Component> additionalComponentList) {
		this.additionalComponentList = additionalComponentList;
	}

	public boolean isAutomaticTaggingSelected() {
		return automaticTaggingSelected;
	}

	public void setAutomaticTaggingSelected(boolean automaticTaggingSelected) {
		this.automaticTaggingSelected = automaticTaggingSelected;
	}
   
    @Override
	public boolean isTypeWithComponents() {
    	return true;
    }
}
