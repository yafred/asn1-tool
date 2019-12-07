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
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import org.junit.Test;

import g_021.Players;
import g_021.Races;
import g_021.Traffic;
import g_021.Traffic2;




public class TestGeneratedCode_021 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Players.Player player1 = new Players.Player();
		player1.setName("Freja");
		player1.setAge(Integer.valueOf(5));
		Players.Player player2 = new Players.Player();
		player2.setName("Astrid");
		player2.setAge(Integer.valueOf(6));
		
		ArrayList<Players.Player> players = new ArrayList<Players.Player>();
		players.add(player1);
		players.add(player2);

		Players pdu = new Players();
		pdu.setValue(players);
	
		String expectedHexa = "30 19 30 0a 16 05 46 72 65 6a 61 02 01 05 30 0b 16 06 41 73 74 72 69 64 02 01 06";
		testHelper.writePdu(pdu, Players.class, expectedHexa);
		
		Players decodedPdu = (Players) testHelper.readPdu(Players.class, Players.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			assertEquals(pdu.getValue().get(i).getName(), decodedPdu.getValue().get(i).getName());
			assertEquals(pdu.getValue().get(i).getAge(), decodedPdu.getValue().get(i).getAge());
		}
	}

	@Test
	public void test_2() throws Exception {
		Players.Player player1 = new Players.Player();
		player1.setName("Freja");
		player1.setAge(Integer.valueOf(5));
		Players.Player player2 = new Players.Player();
		player2.setName("Astrid");
		player2.setAge(Integer.valueOf(6));
		
		ArrayList<Players.Player> players = new ArrayList<Players.Player>();
		players.add(player1);
		players.add(player2);

		Players pdu = new Players();
		pdu.setValue(players);
	
		String expectedHexa = "30 80 30 80 16 05 46 72 65 6a 61 02 01 05 00 00 30 80 16 06 41 73 74 72 69 64 02 01 06 00 00 00 00";
		//testHelper.writePdu(pdu, Players.class, expectedHexa);
		
		Players decodedPdu = (Players) testHelper.readPdu(Players.class, Players.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			assertEquals(pdu.getValue().get(i).getName(), decodedPdu.getValue().get(i).getName());
			assertEquals(pdu.getValue().get(i).getAge(), decodedPdu.getValue().get(i).getAge());
		}
	}
	
	@Test
	public void test_3() throws Exception {
		Races.Item race1 = new Races.Item();
		race1.setName("Gold");
		race1.setDistance(Integer.valueOf(220));
		race1.setDifficulty(Races.Item.Difficulty.HIGH);
		
		Races.Item race2 = new Races.Item();
		race2.setName("Baby");
		race2.setDistance(Integer.valueOf(40));
		race2.setDifficulty(Races.Item.Difficulty.LOW);
		
		ArrayList<Races.Item> races = new ArrayList<Races.Item>();
		races.add(race1);
		races.add(race2);
		
		Races pdu = new Races();
		pdu.setValue(races);
		
		String expectedHexa = "30 29 31 13 a0 06 1a 04 47 6f 6c 64 a1 04 02 02 00 dc a2 03 0a 01 02 31 12 a0 06 1a 04 42 61 62" + 
				" 79 a1 03 02 01 28 a2 03 0a 01 00";
		testHelper.writePdu(pdu, Races.class, expectedHexa);

		Races decodedPdu = (Races) testHelper.readPdu(Races.class, Races.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			assertEquals(pdu.getValue().get(i).getName(), decodedPdu.getValue().get(i).getName());
			assertEquals(pdu.getValue().get(i).getDistance(), decodedPdu.getValue().get(i).getDistance());
			assertEquals(pdu.getValue().get(i).getDifficulty(), decodedPdu.getValue().get(i).getDifficulty());
		}

	}

	@Test
	public void test_4() throws Exception {
		Traffic.Item item1 = new Traffic.Item();
		item1.setRequest().setId(Integer.valueOf(1));
		item1.setRequest().setText("hello");
		
		Traffic.Item item2 = new Traffic.Item();
		item2.setRequest().setId(Integer.valueOf(2));
		item2.setRequest().setText("world");

		Traffic.Item item3 = new Traffic.Item();
		item3.setResponse().setId(Integer.valueOf(1));
		item3.setResponse().setErrorMessage("ok");
		
		ArrayList<Traffic.Item> items = new ArrayList<Traffic.Item>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		
		Traffic pdu = new Traffic();
		pdu.setValue(items);

		String expectedHexa = "31 2d a0 0e a0 0c 30 0a 02 01 01 16 05 68 65 6c 6c 6f a0 0e a0 0c 30 0a 02 01 02 16 05 77 6f 72" +
		" 6c 64 a0 0b a1 09 30 07 02 01 01 16 02 6f 6b";

		testHelper.writePdu(pdu, Traffic.class, expectedHexa);
		
		Traffic decodedPdu = (Traffic) testHelper.readPdu(Traffic.class, Traffic.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			if(pdu.getValue().get(i).getRequest() == null) {
				assertNull(decodedPdu.getValue().get(i).getRequest());
				assertEquals(pdu.getValue().get(i).getResponse().getId(), decodedPdu.getValue().get(i).getResponse().getId());
				assertEquals(pdu.getValue().get(i).getResponse().getErrorMessage(), decodedPdu.getValue().get(i).getResponse().getErrorMessage());
			}
			if(pdu.getValue().get(i).getResponse() == null) {
				assertNull(decodedPdu.getValue().get(i).getResponse());
				assertEquals(pdu.getValue().get(i).getRequest().getId(), decodedPdu.getValue().get(i).getRequest().getId());
				assertEquals(pdu.getValue().get(i).getRequest().getText(), decodedPdu.getValue().get(i).getRequest().getText());
			}		
		}
	
	}
	
	
	@Test
	public void test_5() throws Exception {
		Traffic2.Item item1 = new Traffic2.Item();
		item1.setRequest().setId(Integer.valueOf(1));
		item1.setRequest().setText("hello");
		
		Traffic2.Item item2 = new Traffic2.Item();
		item2.setRequest().setId(Integer.valueOf(2));
		item2.setRequest().setText("world");

		Traffic2.Item item3 = new Traffic2.Item();
		item3.setResponse().setId(Integer.valueOf(1));
		item3.setResponse().setErrorMessage("ok");
		
		ArrayList<Traffic2.Item> items = new ArrayList<Traffic2.Item>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		
		Traffic2 pdu = new Traffic2();
		pdu.setValue(items);

		String expectedHexa = "31 2d a0 0e a0 0c 30 0a 02 01 01 16 05 68 65 6c 6c 6f a0 0e a0 0c 30 0a 02 01 02 16 05 77 6f 72" +
		" 6c 64 a0 0b a1 09 30 07 02 01 01 16 02 6f 6b";

		testHelper.writePdu(pdu, Traffic2.class, expectedHexa);
		
		Traffic2 decodedPdu = (Traffic2) testHelper.readPdu(Traffic2.class, Traffic2.class, expectedHexa);
		assertNotNull(decodedPdu.getValue());
		assertEquals(pdu.getValue().size(), decodedPdu.getValue().size());
		for(int i=0; i<pdu.getValue().size(); i++) {
			if(pdu.getValue().get(i).getRequest() == null) {
				assertNull(decodedPdu.getValue().get(i).getRequest());
				assertEquals(pdu.getValue().get(i).getResponse().getId(), decodedPdu.getValue().get(i).getResponse().getId());
				assertEquals(pdu.getValue().get(i).getResponse().getErrorMessage(), decodedPdu.getValue().get(i).getResponse().getErrorMessage());
			}
			if(pdu.getValue().get(i).getResponse() == null) {
				assertNull(decodedPdu.getValue().get(i).getResponse());
				assertEquals(pdu.getValue().get(i).getRequest().getId(), decodedPdu.getValue().get(i).getRequest().getId());
				assertEquals(pdu.getValue().get(i).getRequest().getText(), decodedPdu.getValue().get(i).getRequest().getText());
			}		
		}
	
	}

}
