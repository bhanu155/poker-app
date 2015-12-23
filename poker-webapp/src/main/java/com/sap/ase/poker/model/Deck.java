package com.sap.ase.poker.model;

import static java.util.Collections.shuffle;

import java.util.ArrayList;

import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;

public class Deck {

	private final ArrayList<Card> cards = new ArrayList<>();
	
	public Deck(){
		for (Suit suit : Suit.values()) {
			for (Kind kind : Kind.values()) {
				cards.add(new Card(suit, kind));							
			}
		}
		shuffle(cards);
	}
	
	public Card dealCard() {
 		if (cards.isEmpty()) {
 			throw new OutOfCardsException();
 		} else {
 			return cards.remove(0); 			
 		}
	}
	
	@SuppressWarnings("serial")
	public class OutOfCardsException extends RuntimeException {
	}
}
