package com.sap.ase.poker.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.ase.poker.gameguide.models.CardDeck;

public class CardDeckTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void cardDeckShouldContainFiftyTwoCards() {
		CardDeck deck = new CardDeck();
		for (int counter = 0; counter < 52; counter++) {
			deck.dealCard();
		}
		exception.expect(IndexOutOfBoundsException.class);
		deck.dealCard();
	}

}
