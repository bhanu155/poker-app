package com.sap.ase.poker.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.ase.poker.model.DefaultDeck.OutOfCardsException;

public class DeckTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldReturnCardWithSuitAndKind() throws Exception {
		Deck deck = new DefaultDeck();
		Card c = deck.dealCard();
		assertNotNull(c.kind);
		assertNotNull(c.suit);
	}

	@Test
	public void shouldReturn52DifferentCards() throws Exception {
		Deck deck = new DefaultDeck();
		Set<String> uniqueCards = new HashSet<String>();

		for (int i = 0; i < 52; i++) {
			Card c = deck.dealCard();
			uniqueCards.add(c.suit + "_" + c.kind);
		}
		
		assertThat(uniqueCards.size(), is(52));
		
		thrown.expect(OutOfCardsException.class);
		deck.dealCard();
	}
	
	@Test
	public void afterShufflingCanDeal52CardsAgain() throws Exception {
		Deck deck = new DefaultDeck();
		deck.dealCard();
		deck.shuffle();
		Set<String> uniqueCards = new HashSet<String>();
		for (int i = 0; i < 52; i++) {
			Card c = deck.dealCard();
			uniqueCards.add(c.suit + "_" + c.kind);
		}
		assertThat(uniqueCards.size(), is(52));
	}
}
