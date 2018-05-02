package com.yafred.asn1.generator.java;

public class Options {
	private String outputPath;
	private String packagePrefix = "";
	private boolean overwriteAllowed = false;
	private boolean beautify = false;
	
	public String getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	public String getPackagePrefix() {
		return packagePrefix;
	}
	public void setPackagePrefix(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}
	public boolean isOverwriteAllowed() {
		return overwriteAllowed;
	}
	public void setOverwriteAllowed(boolean overwriteAllowed) {
		this.overwriteAllowed = overwriteAllowed;
	}
	public boolean isBeautify() {
		return beautify;
	}
	public void setBeautify(boolean beautify) {
		this.beautify = beautify;
	}
}
