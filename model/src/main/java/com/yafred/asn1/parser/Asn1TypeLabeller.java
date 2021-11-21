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
package com.yafred.asn1.parser;

import java.util.ArrayList;

import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeWithComponents;
import com.yafred.asn1.model.type.ListOfType;
import com.yafred.asn1.model.type.NamedType;

public class Asn1TypeLabeller {

    ArrayList<String> breadCrumbs;
	StringBuffer breadCrumbsBuffer;
    boolean verbose = false;

    public Asn1TypeLabeller(boolean verbose) {
        this.verbose = verbose;
        this.breadCrumbs = new ArrayList<String>();
		this.breadCrumbsBuffer = new StringBuffer();
    }

    public void visit(Specification specification) {
		for(ModuleDefinition moduleDefinition : specification.getModuleDefinitionList()) {
            breadCrumbs.add(moduleDefinition.getModuleIdentifier().getModuleReference());
			visit(moduleDefinition);
            breadCrumbs.remove(breadCrumbs.size()-1);
		}
	}

    private void visit(ModuleDefinition moduleDefinition) {
		for(Assignment assignment : moduleDefinition.getAssignmentList()) {
			if(assignment.isTypeAssignment()) {
				visit((TypeAssignment)assignment);
			}
		}
    }

    public void visit(TypeAssignment typeAssignment) {
		if(typeAssignment.getType() != null) {
            breadCrumbs.add(typeAssignment.getReference());
			visitType(typeAssignment.getType());
            breadCrumbs.remove(breadCrumbs.size()-1);
		}
	}
	
    private void visitType(Type type) {
		if(type.isListOfType()) {
			visitListOfType((ListOfType)type);			
		}    
        else if(type.isTypeWithComponents()) {
            visit((TypeWithComponents)type);
        }
        else {
            type.setLabel(getBreadCrumbsAsString());
            if(verbose) {
                System.out.println(getBreadCrumbsAsString());
            }
        }
    }

	private void visitListOfType(ListOfType listOfType) {
        String name = "item";
    	if(listOfType.getElement().getName() != null) {
            name = listOfType.getElement().getName();
    	}
        breadCrumbs.add(name);
		visitType(listOfType.getElement().getType());
        breadCrumbs.remove(breadCrumbs.size()-1);
	}

    private void visit(TypeWithComponents typeWithComponents) {
    	if(typeWithComponents.getRootComponentList() != null && typeWithComponents.getRootComponentList().size() != 0) {
	    	for(Component component : typeWithComponents.getRootComponentList()) {
                visit(component);
	    	}
    	}
    	if(typeWithComponents.getExtensionComponentList() != null) {
	    	for(Component component : typeWithComponents.getExtensionComponentList()) {
                visit(component);
	    	}
    	}
    	if(typeWithComponents.getAdditionalComponentList() != null) {
	    	for(Component component : typeWithComponents.getAdditionalComponentList()) {
                visit(component);
	    	}
    	}
    }

    private void visit(Component component) {
		if(component.isNamedType()) {
			NamedType namedType = (NamedType)component;
            breadCrumbs.add(namedType.getName());
			visitType(namedType.getType());
            breadCrumbs.remove(breadCrumbs.size()-1);
		}
	}

    public String getBreadCrumbsAsString() {
		breadCrumbsBuffer.delete(0, breadCrumbsBuffer.length());

		boolean isFirst = true;
		for(String breadCrumb : breadCrumbs) {
			if(!isFirst) {
				breadCrumbsBuffer.append(".");
			}
			isFirst = false;
			breadCrumbsBuffer.append(breadCrumb);
		}

		return breadCrumbsBuffer.toString();
	}

}
