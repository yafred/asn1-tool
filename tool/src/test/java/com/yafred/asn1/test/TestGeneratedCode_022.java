package com.yafred.asn1.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import org.junit.Test;

import g_022.Team;





public class TestGeneratedCode_022 {
	TestHelper testHelper = new TestHelper();

	@Test
	public void test_1() throws Exception {
		Team pdu = new Team();
		pdu.setCoach().setName("Joe");
		pdu.setCoach().setYears(Integer.valueOf(20));
		
		Team.Player player1 = new Team.Player();
		player1.setName("Tim");
		player1.setYears(Integer.valueOf(2));
		Team.Player player2 = new Team.Player();
		player2.setName("Tom");
		player2.setYears(Integer.valueOf(5));
		
		ArrayList<Team.Player> players = new ArrayList<Team.Player>();
		players.add(player1);
		players.add(player2);
		
		pdu.setPlayers(players);

	
		String expectedHexa = "31 20 a0 08 80 03 4a 6f 65 81 01 14 a1 14 30 08 80 03 54 69 6d 81 01 02 30 08 80 03 54 6f 6d 81 01 05";
		testHelper.writePdu(pdu, Team.class, expectedHexa);
		
		Team decodedPdu = (Team) testHelper.readPdu(Team.class, Team.class, expectedHexa);
		assertNotNull(decodedPdu.getCoach());
		assertEquals(pdu.getCoach().getName(), decodedPdu.getCoach().getName());
		assertEquals(pdu.getCoach().getYears(), decodedPdu.getCoach().getYears());
		assertNotNull(decodedPdu.getPlayers());
		assertEquals(pdu.getPlayers().size(), decodedPdu.getPlayers().size());
		for(int i=0; i<pdu.getPlayers().size(); i++) {
			assertEquals(pdu.getPlayers().get(i).getName(), decodedPdu.getPlayers().get(i).getName());
			assertEquals(pdu.getPlayers().get(i).getYears(), decodedPdu.getPlayers().get(i).getYears());
		}
	}

}
