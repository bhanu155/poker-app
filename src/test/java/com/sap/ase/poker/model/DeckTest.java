package com.sap.ase.poker.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import com.sap.ase.poker.model.DefaultDeck.OutOfCardsException;
import org.junit.jupiter.api.Test;

public class DeckTest {

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
		
		assertThrows(OutOfCardsException.class, () -> {
			deck.dealCard();
		});
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
