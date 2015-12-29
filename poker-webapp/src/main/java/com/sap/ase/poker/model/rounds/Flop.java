package com.sap.ase.poker.model.rounds;

import java.util.List;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Deck;

public class Flop  {

	private final Deck deck;
	private final List<Card> communityCards;
	
	public Flop(Deck deck, List<Card> communityCards) {
		this.deck = deck;
		this.communityCards = communityCards;
	}

	public void start() {
		showCommunityCards(3);
	}
	
	private void showCommunityCards(int count) {
		for (int i = 0; i < count; i++) {
			this.communityCards.add(deck.dealCard());
		}
	}
}
