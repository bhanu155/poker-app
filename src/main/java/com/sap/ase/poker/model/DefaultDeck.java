package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.Collections;

import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;

// XXX naming is not optimal - s.th. like French52Deck is more appropriate.
// However as right now we don't have a fully implemented extension, that
// demonstrates the game e.g. being played with a German card deck, we
// just call it "DefaultDeck" for the time being.
public class DefaultDeck implements Deck {

	private final ArrayList<Card> cards = new ArrayList<>();

	public DefaultDeck() {
		initCards();
	}

	@Override
	public Card dealCard() {
		if (cards.isEmpty()) {
			throw new OutOfCardsException();
		} else {
			return cards.remove(0);
		}
	}

	@Override
	public void shuffle() {
		cards.clear();
		initCards();
		Collections.shuffle(cards);
	}
	
	private void initCards() {
		for (Suit suit : Suit.values()) {
			for (Kind kind : Kind.values()) {
				cards.add(new Card(kind, suit));
			}
		}
	}
	
	@SuppressWarnings("serial")
	public class OutOfCardsException extends RuntimeException {
	}
}
