package com.yafred.asn1.generator.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import com.yafred.asn1.generator.SampleGenerator;

public class SampleTest  {

    @Test
    public void test1() {
    	String ouputPath = System.getProperty("buildDirectory");
    	
    	assertNotNull(ouputPath);
    	
    	try {
			new SampleGenerator(ouputPath + File.separator + "generated-test-sources", "sample").generateCode();
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(true, false);
		}
    }

 

}
