package com.sap.ase.poker.model;

public class Card {
	private final Suit suit;
	private final Kind kind;

	public Card(Suit suit, Kind kind) {
		this.suit = suit;
		this.kind = kind;				
	}
	
	public Suit getSuit() {
		return suit;
	}

	public Kind getKind() {
		return kind;
	}
	
	@Override
	public String toString() {
		return "Card [suit=" + suit + ", kind=" + kind + "]";
	}

	public static enum Suit {
		SPADES, HEARTS, DIAMONDS, CLUBS
	}
	
	public static enum Kind {
		ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
	}
}
