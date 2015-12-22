package com.sap.ase.poker.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.ase.poker.model.Deck.OutOfCardsException;

public class DeckTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldReturnCardWithSuitAndKind() throws Exception {
		Deck texDeck = new Deck();
		Card c = texDeck.dealCard();
		assertNotNull(c.getKind());
		assertNotNull(c.getSuit());
	}

	@Test
	public void shouldReturn52DifferentCards() throws Exception {
		Deck texDeck = new Deck();
		Set<String> uniqueCards = new HashSet<String>();

		for (int i = 0; i < 52; i++) {
			Card c = texDeck.dealCard();
			System.out.println(c);
			uniqueCards.add(c.getSuit() + "_" + c.getKind());
		}
		
		assertThat(uniqueCards.size(), is(52));
		
		thrown.expect(OutOfCardsException.class);
		texDeck.dealCard();
	}
}
