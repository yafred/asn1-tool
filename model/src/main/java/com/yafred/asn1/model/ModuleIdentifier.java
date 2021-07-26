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

public class ModuleIdentifier {
	/**
	 * Name of the module.
	 */
	private String moduleReference;
	
	/**
	 * Location of the token in input stream
	 */
	private TokenLocation moduleReferenceTokenLocation;

	/**
	 * List of DefinitiveObjIdComponents
	 * A least 2 items (called 'arc') if not null.
	 */
	private ArrayList<DefinitiveObjectIdComponent> definitiveObjIdComponents;
	

	public String getModuleReference() {
		return moduleReference;
	}

	public void setModuleReference(String moduleReference) {
		this.moduleReference = moduleReference;
	}

	public TokenLocation getModuleReferenceTokenLocation() {
		return moduleReferenceTokenLocation;
	}

	public void setModuleReferenceTokenLocation(TokenLocation moduleReferenceTokenLocation) {
		this.moduleReferenceTokenLocation = moduleReferenceTokenLocation;
	}

	public ArrayList<DefinitiveObjectIdComponent> getDefinitiveObjIdComponents() {
		return definitiveObjIdComponents;
	}

	public void setDefinitiveObjIdComponents(ArrayList<DefinitiveObjectIdComponent> definitiveObjIdComponents) {
		this.definitiveObjIdComponents = definitiveObjIdComponents;
	}
}
