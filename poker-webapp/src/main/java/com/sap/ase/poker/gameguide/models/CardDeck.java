package com.sap.ase.poker.gameguide.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardDeck {
	private List<Card> cardDeck;

	public CardDeck() {
		this.cardDeck = new ArrayList<Card>();
		this.createCardDeck();
		this.shuffleCardDeck();
	}

	private void shuffleCardDeck() {
		Collections.shuffle(this.cardDeck, new Random());
	}

	private void createCardDeck() {
		for (Card.Kinds kind : Card.Kinds.values()) {
			for (Card.Suits suit : Card.Suits.values()) {
				Card c = new Card();
				c.setKind(kind);
				c.setSuit(suit);
				this.cardDeck.add(c);
			}
		}
	}

	public Card dealCard() {
		Card c = this.cardDeck.get(0);
		this.cardDeck.remove(0);
		return c;
	}
}
