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
package com.yafred.asn1.model.type;

import java.util.ArrayList;

import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.TagClass;
import com.yafred.asn1.model.Type;


public class EnumeratedType extends Type {


    private ArrayList<NamedNumber> rootEnumeration = null;
    /*
     * If additionalEnumeration is non null, then type is extensible
     */
    private ArrayList<NamedNumber> additionalEnumeration = null;

    public EnumeratedType(ArrayList<NamedNumber> rootEnumeration, ArrayList<NamedNumber> additionalEnumeration) {
		this.rootEnumeration = rootEnumeration;
		this.additionalEnumeration = additionalEnumeration;
    }
    
    public ArrayList<NamedNumber> getRootEnumeration() {
		return rootEnumeration;
	}

	public void setRootEnumeration(ArrayList<NamedNumber> rootEnumeration) {
		this.rootEnumeration = rootEnumeration;
	}

	public ArrayList<NamedNumber> getAdditionalEnumeration() {
		return additionalEnumeration;
	}

	public void setAdditionalEnumeration(ArrayList<NamedNumber> additionalEnumeration) {
		this.additionalEnumeration = additionalEnumeration;
	}

	@Override
	public boolean isEnumeratedType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(10), TagClass.UNIVERSAL_TAG, null);
    }

	@Override
	public String getName() {
		return ("ENUMERATED");
	}
}
