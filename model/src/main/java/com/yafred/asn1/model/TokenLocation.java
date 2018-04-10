package com.yafred.asn1.model;

/**
 * Location of a symbol or a production in the input.
 * Not actually an ASN.1 production.
 */
public class TokenLocation {
	private int line;
	private int column;
	
	public TokenLocation(int line, int column) {
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	public String toString() {
		return "[line " + Integer.toString(line) + ":" + Integer.toString(column) + "]";
	}
}
