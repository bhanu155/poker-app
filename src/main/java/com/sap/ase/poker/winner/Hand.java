package com.sap.ase.poker.winner;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.sap.ase.poker.model.Card;

/**
 * Models a poker hand.
 * 
 * This class is pretty much private to class FindBestHand - it makes assumptions about
 * the structure of the arguments passed along, particularly grouping and sort order.
 */
public class Hand implements Comparable<Hand> {
	public final Type type;
	public final List<Card> cards;

	/**
	 * IMPORTANT: cards must be GROUPED AND SORTED IN DESCENDING ORDER, so that hands 
	 * are comparable. Grouped means that three-of-a-kind and pairs (IN THAT ORDER) 
	 * come first. Special rule: in a small straight, the ace comes last.<br/>
	 * 
	 * Example 1 (high card): [hearts 8, hearts 7, spades 5, spades 4, hearts 3]<br/>
	 * Example 2 (pair): [hearts 4, spades 4, spades 9, spades 7, hearts 6]<br/>
	 * Example 3 (full house): [hearts 4, spades 4, diamonds 4, hearts 6, spades 6]<br/>
	 * Example 4 (straight): [hearts 5, spades 4, diamonds 3, hearts 2, spades ace]<br/>
	 *
	 * @param type The type of hand, e.g. full house - this is the main criteria for comparison
	 * @param fiveCards The five cards - will be used to compare hands of the same type 
	 */
	public Hand(Type type, List<Card> fiveCards) {
		this.type = type;
		cards = unmodifiableList(fiveCards);
	}

	@Override
	public final int compareTo(Hand otherHand) {
		return (type == otherHand.type) ? compareSameType(otherHand) : type.compareTo(otherHand.type);
	}

	private int compareSameType(Hand otherHand) {
		for (int i = 0; i < otherHand.cards.size(); i++) {
			if (cards.get(i).kind != otherHand.cards.get(i).kind) {
				return cards.get(i).compareTo(otherHand.cards.get(i));
			}
		}
		return 0;
	}

	public enum Type {
		// keep the order - enums compare by ordinal, so the order defines the "value"
		HIGH_CARD, PAIR, TWO_PAIRS, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH;
	}
}
