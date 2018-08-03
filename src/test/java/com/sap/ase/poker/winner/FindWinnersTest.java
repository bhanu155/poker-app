package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.CardFixtures.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Player;

public class FindWinnersTest {

	@Test
	public void twoHighCards() throws Exception {
		Player jane = new Player("Jane Foe", 100);
		jane.setCards(asList(CLUBS_ACE, HEARTS_KING));
		Player john = new Player("John Doe", 100);
		john.setCards(asList(HEARTS_KING, CLUBS_QUEEN));
	
		List<Player> players = asList(jane, john);
		List<Card> communityCards = asList(CLUBS_2, CLUBS_3, HEARTS_4, HEARTS_6, HEARTS_7);
		List<Player> winners = new FindWinners().apply(players, communityCards).list;
		assertEquals(1, winners.size());
		assertEquals(jane, winners.get(0));
	}
	
	@Test
	public void twoHighCards_sameResult() throws Exception {
		Player jane = new Player("Jane Foe", 100);
		jane.setCards(asList(CLUBS_4, HEARTS_3));
		Player john = new Player("John Doe", 100);
		john.setCards(asList(HEARTS_2, CLUBS_3));

		List<Player> players = asList(jane, john);
		List<Card> communityCards = asList(CLUBS_5, CLUBS_6, HEARTS_8, HEARTS_9, HEARTS_10);
		List<Player> winners = new FindWinners().apply(players, communityCards).list;
		assertEquals(2, winners.size());
		assertTrue(winners.contains(john));
		assertTrue(winners.contains(jane));
	}
	
	@Test
	public void oddChipsWinner_firstInSequence() {
		Player jane = new Player("Jane Foe", 100);
		jane.setCards(asList(CLUBS_2, HEARTS_3));
		Player john = new Player("John Doe", 100);
		john.setCards(asList(HEARTS_2, CLUBS_3));

		List<Player> players = asList(jane, john);
		List<Card> communityCards = asList(CLUBS_5, CLUBS_6, HEARTS_8, HEARTS_9, HEARTS_10);
		Player oddChipsWinner = new FindWinners().apply(players, communityCards).oddChipsWinner;
		
		assertEquals(jane, oddChipsWinner);
	}
}
