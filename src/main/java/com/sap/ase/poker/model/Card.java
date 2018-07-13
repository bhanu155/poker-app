package com.sap.ase.poker.model;

public class Card implements Comparable<Card> {

	public enum Suit {
		DIAMONDS("diamonds"), HEARTS("hearts"), SPADES("spades"), CLUBS("clubs");

		public final String value;

		private Suit(String value) {
			this.value = value;
		}
	}

	public enum Kind {
		TWO(1, "2"), THREE(2, "3"), FOUR(3, "4"), FIVE(4, "5"), SIX(5, "6"), SEVEN(6, "7"), EIGHT(7, "8"), NINE(8,
				"9"), TEN(9, "10"), JACK(10, "Jack"), QUEEN(11, "Queen"), KING(12, "King"), ACE(13, "Ace");

		public final int rank;
		public final String value;

		private Kind(int rank, String value) {
			this.rank = rank;
			this.value = value;
		}
	}

	private Card.Kind kind;
	private Card.Suit suit;

	public Card(Kind kind, Suit suit) {
		this.kind = kind;
		this.suit = suit;
	}

	public Kind getKind() {
		return kind;
	}

	public Suit getSuit() {
		return suit;
	}

	@Override
	public int compareTo(Card c) {
		return c.getKind().rank - this.kind.rank;
	}
}
