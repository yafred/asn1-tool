package com.yafred.asn1.generator.java;

public class Options {
	private String outputDir;
	private boolean overwriteAllowed = false;

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public boolean isOverwriteAllowed() {
		return overwriteAllowed;
	}

	public void setOverwriteAllowed(boolean overwriteAllowed) {
		this.overwriteAllowed = overwriteAllowed;
	}
}
