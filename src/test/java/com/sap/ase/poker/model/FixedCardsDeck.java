package com.sap.ase.poker.model;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public class FixedCardsDeck implements Deck {

	private List<Card> cards;
	
	public FixedCardsDeck(Card... cards) {
		this.cards = new ArrayList<>(asList(cards));
	}

	@Override
	public Card dealCard() {
		return cards.remove(0);
	}

	@Override
	public void shuffle() {
	}
}
