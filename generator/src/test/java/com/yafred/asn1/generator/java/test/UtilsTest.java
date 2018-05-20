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
