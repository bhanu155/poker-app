package com.sap.ase.poker.gameguide.models;

public class Card implements Comparable<Card> {

	public enum Suits {
		DIAMONDS, HEARTS, SPADES, CLUBS;
	}

	public enum Kinds {
		TWO(1, "2"), THREE(2, "3"), FOUR(3, "4"), FIFE(4, "5"), SIX(5, "6"), SEVEN(6, "7"), EIGHT(7, "8"), NINE(8,
				"9"), TEN(9, "10"), JACK(10, "Jack"), QUEEN(11, "Queen"), KING(12, "King"), ACE(13, "Ace");

		public int rank;
		public String value;

		private Kinds(int rank, String value) {
			this.rank = rank;
			this.value = value;
		}
	}

	private Card.Kinds kind;
	private Card.Suits suit;

	public Card() {
		super();
	}

	public Card(Kinds kind, Suits suit) {
		super();
		this.kind = kind;
		this.suit = suit;
	}

	public Card.Kinds getKind() {
		return kind;
	}

	public Card.Suits getSuit() {
		return suit;
	}

	public void setKind(Card.Kinds kind) {
		this.kind = kind;
	}

	public void setSuit(Card.Suits suit) {
		this.suit = suit;
	}

	@Override
	public int compareTo(Card c) {
		return c.getKind().rank - this.kind.rank;
	}
}
