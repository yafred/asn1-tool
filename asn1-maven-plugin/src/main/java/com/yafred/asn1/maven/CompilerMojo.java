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
package com.yafred.asn1.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Parses ASN.1 files {@code *.asn} and transforms them into Java
 * source files.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class CompilerMojo extends AbstractMojo {

    /**
     * The directory where the ANTLR grammar files ({@code *.asn}) are located.
     */
    @Parameter(defaultValue = "${basedir}/src/main/asn")
    private File sourceDirectory;

    /**
     * Specify output directory where the Java files are generated.
     */
	@Parameter(defaultValue = "${project.build.directory}/generated-sources/asn")
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException {
        if (!sourceDirectory.isDirectory()) {
            getLog().info("No ASN.1 files in " + sourceDirectory.getAbsolutePath());
            return;
        }

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
    }
}
