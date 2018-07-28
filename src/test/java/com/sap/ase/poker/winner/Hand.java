package com.sap.ase.poker.winner;

import java.util.List;

import com.sap.ase.poker.model.Card;

// TODO can the card comparison be made easier using streams, and would this improve the array<->list back and forth conversion?
public class Hand implements Comparable<Hand> {
	protected final Type type;
	public final Card[] cards;
	
	public Hand(Type type, List<Card> fiveCards) {
		this.type = type;
		cards = fiveCards.toArray(new Card[0]);
	}

	@Override
	public final int compareTo(Hand otherHand) {
		return (type == otherHand.type) ? compareSameType(otherHand) : type.compareTo(otherHand.type);
	}

	private int compareSameType(Hand otherHand) {
		for (int i = 0; i < 5; i++) {
			if (cards[i].getKind() != otherHand.cards[i].getKind()) {
				return cards[i].compareTo(otherHand.cards[i]);
			}
		}
		return 0;
	}

	public static enum Type {
		// keep the order - enums compare by ordinal, so the order defines the "value"
		HIGH_CARD, PAIR, TWO_PAIRS, THREE_OF_A_KIND, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT, STRAIGHT_FLUSH, ROYAL_FLUSH;
	}
}
