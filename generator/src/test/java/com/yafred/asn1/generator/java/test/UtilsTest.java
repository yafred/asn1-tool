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
package com.yafred.asn1.generator.java.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yafred.asn1.generator.java.Utils;


public class UtilsTest {

	@Test
	public void test_1() {
		String asn1Var = "variable";
		String javaVar = "variable";
		
		assertEquals(javaVar, Utils.normalize(asn1Var));
	}
	
	@Test
	public void test_2() {
		String asn1Var = "a-variable";
		String javaVar = "aVariable";
		
		assertEquals(javaVar, Utils.normalize(asn1Var));
	}
	
	@Test
	public void test_3() {
		String asn1Var = "with-cap";
		String javaVar = "WithCap";
		
		assertEquals(javaVar, Utils.uNormalize(asn1Var));
	}
	
	@Test
	public void test_4() {
		String asn1Var = "my-module";
		String javaVar = "my_module";
		
		assertEquals(javaVar, Utils.normalizePackageName(asn1Var));
	}
}
