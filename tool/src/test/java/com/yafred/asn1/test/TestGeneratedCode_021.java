package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import org.junit.Test;

import g_021.Players;
import g_021.Races;
import g_021.Traffic;
import g_021.Traffic_2;




public class TestGeneratedCode_021 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Players.Item player1 = new Players.Item();
		player1.setName("Freja");
		player1.setAge(Integer.valueOf(5));
		Players.Item player2 = new Players.Item();
		player2.setName("Astrid");
		player2.setAge(Integer.valueOf(6));
		
		ArrayList<Players.Item> players = new ArrayList<Players.Item>();
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
		Players.Item player1 = new Players.Item();
		player1.setName("Freja");
		player1.setAge(Integer.valueOf(5));
		Players.Item player2 = new Players.Item();
		player2.setName("Astrid");
		player2.setAge(Integer.valueOf(6));
		
		ArrayList<Players.Item> players = new ArrayList<Players.Item>();
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
		race1.setDifficulty(Races.Item.Difficulty.high);
		
		Races.Item race2 = new Races.Item();
		race2.setName("Baby");
		race2.setDistance(Integer.valueOf(40));
		race2.setDifficulty(Races.Item.Difficulty.low);
		
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
		Traffic_2.Item item1 = new Traffic_2.Item();
		item1.setRequest().setId(Integer.valueOf(1));
		item1.setRequest().setText("hello");
		
		Traffic_2.Item item2 = new Traffic_2.Item();
		item2.setRequest().setId(Integer.valueOf(2));
		item2.setRequest().setText("world");

		Traffic_2.Item item3 = new Traffic_2.Item();
		item3.setResponse().setId(Integer.valueOf(1));
		item3.setResponse().setErrorMessage("ok");
		
		ArrayList<Traffic_2.Item> items = new ArrayList<Traffic_2.Item>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		
		Traffic_2 pdu = new Traffic_2();
		pdu.setValue(items);

		String expectedHexa = "31 2d a0 0e a0 0c 30 0a 02 01 01 16 05 68 65 6c 6c 6f a0 0e a0 0c 30 0a 02 01 02 16 05 77 6f 72" +
		" 6c 64 a0 0b a1 09 30 07 02 01 01 16 02 6f 6b";

		testHelper.writePdu(pdu, Traffic_2.class, expectedHexa);
		
		Traffic_2 decodedPdu = (Traffic_2) testHelper.readPdu(Traffic_2.class, Traffic_2.class, expectedHexa);
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
