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

import java.util.ArrayList;


public class ModuleDefinition {
    /**
     * Name of the module.
     */
    private ModuleIdentifier moduleIdentifier = null;

	/**
     * Default tagging context.
     */
    private TagDefault tagDefault = TagDefault.EXPLICIT_TAGS;

    /**
     * Default extensibility context.
     */
    private boolean extensibilityImplied = false;

    /**
     * Flag to determine if all assignments found in this module are available to other modules.
     */
    private boolean exportAll = false;

    /**
     * List of symbols (TextNode) exported by this module.
     * If the list is
     */
    private ArrayList<String> symbolsExported = null;

    /**
     * List of symbols imported by this module
     * @see SymbolsFromModule
     */
    private ArrayList<SymbolsFromModule> symbolsImported = null;

    /**
     * List of assignment made in this module
     * @see Assignment
     */
    private ArrayList<Assignment> assignmentList = null;

    /**
     * Creates a module definition.
     * @param moduleIdentifier Identififier of the module in the ASN.1 object tree.
     * @param tagDefault Default tagging of this module.
     * @param extensibilityImplied Default extensibility of this module.
     * @param symbolsExported List of symbols made usable by other modules (null means all exported, size 0 means none exported).
     * @param symbolsImported List of symbols defined in other modules.
     * @param assignmentList List of assignmentList made in this module.
     */
    public ModuleDefinition(ModuleIdentifier moduleIdentifier,
        TagDefault tagDefault,
        boolean extensibilityImplied, ArrayList<String> symbolsExported,
        ArrayList<SymbolsFromModule> symbolsImported, ArrayList<Assignment> assignmentList) {
        this.moduleIdentifier = moduleIdentifier;
 
        this.tagDefault = tagDefault;
        this.extensibilityImplied = extensibilityImplied;
        this.symbolsExported = symbolsExported;

        if (symbolsExported == null) {
            exportAll = true;
        }

        this.symbolsImported = symbolsImported;
        this.assignmentList = assignmentList;
    }

    public ModuleIdentifier getModuleIdentifier() {
		return moduleIdentifier;
	}

	public void setModuleIdentifier(ModuleIdentifier moduleIdentifier) {
		this.moduleIdentifier = moduleIdentifier;
	}

	public TagDefault getTagDefault() {
		return tagDefault;
	}

	public void setTagDefault(TagDefault tagDefault) {
		this.tagDefault = tagDefault;
	}

	public boolean isExtensibilityImplied() {
		return extensibilityImplied;
	}

	public void setExtensibilityImplied(boolean extensibilityImplied) {
		this.extensibilityImplied = extensibilityImplied;
	}

	public boolean isExportAll() {
		return exportAll;
	}

	public void setExportAll(boolean exportAll) {
		this.exportAll = exportAll;
	}

	public ArrayList<String> getSymbolsExported() {
		return symbolsExported;
	}

	public void setSymbolsExported(ArrayList<String> symbolsExported) {
		this.symbolsExported = symbolsExported;
	}

	public ArrayList<SymbolsFromModule> getSymbolsImported() {
		return symbolsImported;
	}

	public void setSymbolsImported(ArrayList<SymbolsFromModule> symbolsImported) {
		this.symbolsImported = symbolsImported;
	}

	public ArrayList<Assignment> getAssignmentList() {
		return assignmentList;
	}

	public void setAssignmentList(ArrayList<Assignment> assignmentList) {
		this.assignmentList = assignmentList;
	}
}
