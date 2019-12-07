/*******************************************************************************
 * Copyright (C) 2019 Fred D7e (https://github.com/yafred)
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
package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import g_025.Board1;
import g_025.Board2;
import g_025.Board3;
import g_025.Game;
import g_025.Game2;
import g_025.Row;




public class TestGeneratedCode_025 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {	
		/*
		 * Note: writing is cumbersome. Needs constructor or a constructing setValue().
		 */
		
		Row row1 = new Row();
		row1.setValue(new ArrayList<String>());
		row1.getValue().add("X");
		row1.getValue().add("0");
		row1.getValue().add("-");

		Row row2 = new Row();
		row2.setValue(new ArrayList<String>());
		row2.getValue().add("-");
		row2.getValue().add("-");
		row2.getValue().add("-");

		Row row3 = new Row();
		row3.setValue(new ArrayList<String>());
		row3.getValue().add("X");
		row3.getValue().add("-");
		row3.getValue().add("0");
		
		Board1 pdu = new Board1();
		pdu.setValue(new ArrayList<Row>());
		pdu.getValue().add(row1);
		pdu.getValue().add(row2);
		pdu.getValue().add(row3);
		
		String expectedHexa = "30 21 30 09 16 01 58 16 01 30 16 01 2d 30 09 16 01 2d 16 01 2d 16 01 2d 30 09 16 01 58 16 01 2d 16 01 30";
		
		testHelper.writePdu(pdu, Board1.class, expectedHexa);

		Board1 decodedPdu = (Board1) testHelper.readPdu(Board1.class, Board1.class, expectedHexa);
		
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int k=0; k<pdu.getValue().size(); k++) {
			assertEquals(pdu.getValue().get(k).getValue().size(), decodedPdu.getValue().get(k).getValue().size());
			for(int i=0; i<pdu.getValue().size(); i++) {
				assertEquals(pdu.getValue().get(k).getValue().get(i), decodedPdu.getValue().get(k).getValue().get(i));				
			}			
		}
	}

	
	@Test
	public void test_2() throws Exception {	
		/*
		 * Note: writing is cumbersome. Needs constructor or a constructing setValue().
		 */
		
		Board2.Row row1 = new Board2.Row();
		row1.setValue(new ArrayList<String>());
		row1.getValue().add("X");
		row1.getValue().add("0");
		row1.getValue().add("-");

		Board2.Row row2 = new Board2.Row();
		row2.setValue(new ArrayList<String>());
		row2.getValue().add("-");
		row2.getValue().add("-");
		row2.getValue().add("-");

		Board2.Row row3 = new Board2.Row();
		row3.setValue(new ArrayList<String>());
		row3.getValue().add("X");
		row3.getValue().add("-");
		row3.getValue().add("0");
		
		Board2 pdu = new Board2();
		pdu.setValue(new ArrayList<Board2.Row>());
		pdu.getValue().add(row1);
		pdu.getValue().add(row2);
		pdu.getValue().add(row3);
		
		String expectedHexa = "30 21 30 09 16 01 58 16 01 30 16 01 2d 30 09 16 01 2d 16 01 2d 16 01 2d 30 09 16 01 58 16 01 2d 16 01 30";
		
		testHelper.writePdu(pdu, Board2.class, expectedHexa);

		Board2 decodedPdu = (Board2) testHelper.readPdu(Board2.class, Board2.class, expectedHexa);
		
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int k=0; k<pdu.getValue().size(); k++) {
			assertEquals(pdu.getValue().get(k).getValue().size(), decodedPdu.getValue().get(k).getValue().size());
			for(int i=0; i<pdu.getValue().size(); i++) {
				assertEquals(pdu.getValue().get(k).getValue().get(i), decodedPdu.getValue().get(k).getValue().get(i));				
			}			
		}
	}


	@Test
	public void test_3() throws Exception {	
		/*
		 * Note: writing is cumbersome. Needs constructor or a constructing setValue().
		 */
		
		Game.Row row1 = new Game.Row();
		row1.setValue(new ArrayList<String>());
		row1.getValue().add("X");
		row1.getValue().add("0");
		row1.getValue().add("-");

		Game.Row row2 = new Game.Row();
		row2.setValue(new ArrayList<String>());
		row2.getValue().add("-");
		row2.getValue().add("-");
		row2.getValue().add("-");

		Game.Row row3 = new Game.Row();
		row3.setValue(new ArrayList<String>());
		row3.getValue().add("X");
		row3.getValue().add("-");
		row3.getValue().add("0");
		
		Game pdu = new Game();
		pdu.setBoard(new ArrayList<Game.Row>());
		pdu.getBoard().add(row1);
		pdu.getBoard().add(row2);
		pdu.getBoard().add(row3);
		
		String expectedHexa = "30 23 30 21 30 09 16 01 58 16 01 30 16 01 2d 30 09 16 01 2d 16 01 2d 16 01 2d 30 09 16 01 58 16 01 2d 16 01 30";
		
		testHelper.writePdu(pdu, Game.class, expectedHexa);

		Game decodedPdu = (Game) testHelper.readPdu(Game.class, Game.class, expectedHexa);
		
		assertNotNull(decodedPdu.getBoard());
		assertEquals(pdu.getBoard().size(), decodedPdu.getBoard().size());
		for(int k=0; k<pdu.getBoard().size(); k++) {
			assertEquals(pdu.getBoard().get(k).getValue().size(), decodedPdu.getBoard().get(k).getValue().size());
			for(int i=0; i<pdu.getBoard().size(); i++) {
				assertEquals(pdu.getBoard().get(k).getValue().get(i), decodedPdu.getBoard().get(k).getValue().get(i));				
			}			
		}
	}


	@Test
	public void test_4() throws Exception {	
		/*
		 * Note: writing is cumbersome. Needs constructor or a constructing setValue().
		 */
		
		Board3.Row row1 = new Board3.Row();
		row1.setValue(new ArrayList<String>());
		row1.getValue().add("X");
		row1.getValue().add("0");
		row1.getValue().add("-");

		Board3.Row row2 = new Board3.Row();
		row2.setValue(new ArrayList<String>());
		row2.getValue().add("-");
		row2.getValue().add("-");
		row2.getValue().add("-");

		Board3.Row row3 = new Board3.Row();
		row3.setValue(new ArrayList<String>());
		row3.getValue().add("X");
		row3.getValue().add("-");
		row3.getValue().add("0");
		
		Game2 pdu = new Game2();
		pdu.setBoard().setValue(new ArrayList<Board3.Row>());;
		pdu.getBoard().getValue().add(row1);
		pdu.getBoard().getValue().add(row2);
		pdu.getBoard().getValue().add(row3);
		
		String expectedHexa = "31 23 31 21 31 09 16 01 58 16 01 30 16 01 2d 31 09 16 01 2d 16 01 2d 16 01 2d 31 09 16 01 58 16 01 2d 16 01 30";
		
		testHelper.writePdu(pdu, Game2.class, expectedHexa);

		Game2 decodedPdu = (Game2) testHelper.readPdu(Game2.class, Game2.class, expectedHexa);
		
		assertNotNull(decodedPdu.getBoard());
		assertEquals(pdu.getBoard().getValue().size(), decodedPdu.getBoard().getValue().size());
		for(int k=0; k<pdu.getBoard().getValue().size(); k++) {
			assertEquals(pdu.getBoard().getValue().get(k).getValue().size(), decodedPdu.getBoard().getValue().get(k).getValue().size());
			for(int i=0; i<pdu.getBoard().getValue().size(); i++) {
				assertEquals(pdu.getBoard().getValue().get(k).getValue().get(i), decodedPdu.getBoard().getValue().get(k).getValue().get(i));				
			}			
		}
	}

		


}
