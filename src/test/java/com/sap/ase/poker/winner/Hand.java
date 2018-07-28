package com.sap.ase.poker.winner;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.sap.ase.poker.model.Card;

public abstract class Hand implements Comparable<Hand> {
	protected final int rank;

	public Hand(int rank) {
		this.rank = rank;
	}

	@Override
	public int compareTo(Hand otherHand) {
		if (rank == otherHand.rank) {
			return compareSameHandType(otherHand);
		} else {
			return rank - otherHand.rank;
		}
	}

	protected abstract int compareSameHandType(Hand otherHand);

	public abstract Card[] getCards();

	private static int compareCards(Card[] cards1, Card[] cards2) {
		for (int i = 0; i < 5; i++) {
			if (cards1[i].getKind() != cards2[i].getKind()) {
				return cards1[i].compareTo(cards2[i]);
			}
		}
		return 0;
	}

	public static class HighCard extends Hand {

		private final Card[] cards;

		// TODO could we use a sorted set here?
		public HighCard(List<Card> cards) {
			super(0);
			this.cards = cards.toArray(new Card[0]);
		}

		@Override
		protected int compareSameHandType(Hand otherHand) {
			return compareCards(cards, otherHand.getCards());
		}

		@Override
		public Card[] getCards() {
			return cards;
		}
	}

	public static class Pair extends Hand {

		private final KindGroup pair;
		private final Card[] next3Cards;

		// TODO could we use a sorted set here?
		public Pair(KindGroup pair, List<Card> next3Cards) {
			super(1);
			this.pair = pair;
			this.next3Cards = next3Cards.toArray(new Card[0]);
		}

		@Override
		protected int compareSameHandType(Hand otherHand) {
			Pair otherPairHand = (Pair) otherHand;
			int pairCompareResult = pair.compareTo(otherPairHand.pair);
			return (pairCompareResult == 0) ? compareCards(next3Cards, otherPairHand.next3Cards) : pairCompareResult;
		}

		@Override
		public Card[] getCards() {
			ArrayList<Card> cards = new ArrayList<>(pair.cards);
			cards.addAll(asList(next3Cards));
			return cards.toArray(new Card[0]);
		}
	}
}
