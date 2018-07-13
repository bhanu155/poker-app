package com.sap.ase.poker.winner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;

public class PlayerHandAggregate {

	private List<KindAggregate> kindAggregates;
	private LongestSequence longestSequence;
	private Map<Suit, List<Kind>> suitAggregates;

	public PlayerHandAggregate(List<Card> cards) {
		this.suitAggregates = new HashMap<Suit, List<Kind>>();
		suitAggregates.put(Suit.HEARTS, new ArrayList<Kind>());
		suitAggregates.put(Suit.CLUBS, new ArrayList<Kind>());
		suitAggregates.put(Suit.DIAMONDS, new ArrayList<Kind>());
		suitAggregates.put(Suit.SPADES, new ArrayList<Kind>());
		this.generateAggregates(cards);
	}

	public class LongestSequence implements Comparable<LongestSequence> {
		private final int length;
		private final Kind highestKind;
		private boolean isSameSuit;
		private Kind highestKindOfSequenceWithSameSuit;
		private Suit suitOfSequenceWithSameSuit;

		public LongestSequence(int countOfSeqRankedcards, Kind kind) {
			this.length = countOfSeqRankedcards;
			this.highestKind = kind;
		}

		public void setSequenceCharacteristics(Map<Suit, List<Kind>> suitAggregates) {
			// checks whether the sequence consists out of the same suit
			Suit suit = getSuitWhichCanBeFoundFiveTimesOrMoreInPlayerCards(suitAggregates);
			if (suit == null) {
				this.isSameSuit = false;
				return;
			}
			int counterCardsOfSameSuitInSequence = 1;
			for (int i = 1; i < suitAggregates.get(suit).size(); i++) {
				Kind oneKindBefore = suitAggregates.get(suit).get(i - 1);
				Kind currentKind = suitAggregates.get(suit).get(i);

				if (oneKindBefore.rank - currentKind.rank == 1) {
					counterCardsOfSameSuitInSequence++;
				} else {
					counterCardsOfSameSuitInSequence = 1;
				}
				if (i > suitAggregates.get(suit).size() - 4 && counterCardsOfSameSuitInSequence < i - 3) {
					System.out.println("there are no five cards in sequence of same suit");
					/*
					 * e.g. you have 7 cards: after the fourth card you must
					 * need a counter of 2 to fulfill a sequence of five cards
					 * with same suit
					 */
					this.isSameSuit = false;
					return;
				}
				if (counterCardsOfSameSuitInSequence == 5) {
					this.highestKindOfSequenceWithSameSuit = suitAggregates.get(suit)
							.get(i + 1 - counterCardsOfSameSuitInSequence);
					this.isSameSuit = true;
					this.suitOfSequenceWithSameSuit = suit;
				}
			}
		}

		@Override
		public int compareTo(LongestSequence h) {
			return h.length - this.length;
		}

		public int getLength() {
			return length;
		}

		public Kind getHighestKind() {
			return highestKind;
		}

		public Kind getHighestKindOfSequenceWithSameSuit() {
			return highestKindOfSequenceWithSameSuit;
		}

		public boolean isSameSuit() {
			return isSameSuit;
		}

		public void setSameSuit(boolean isSameSuit) {
			this.isSameSuit = isSameSuit;
		}

		public Suit getSuitOfSequenceWithSameSuit() {
			return suitOfSequenceWithSameSuit;
		}

	}

	private void generateAggregates(List<Card> cards) {
		KindAggregate kA = new KindAggregate();
		ArrayList<KindAggregate> kAs = new ArrayList<KindAggregate>();
		ArrayList<Suit> suits = new ArrayList<Suit>();
		ArrayList<LongestSequence> sequences = new ArrayList<LongestSequence>();
		Collections.sort(cards);
		Kind lastKind = null;
		Kind highestKindOfSeqRank = cards.get(0).getKind();
		int sequentialRankedcardsCounter = 1;
		for (int i = 0; i < cards.size(); i++) {
			Card c = cards.get(i);
			this.suitAggregates.get(c.getSuit()).add(c.getKind());
			if (lastKind != c.getKind() && i != 0) {
				if (lastKind.rank - c.getKind().rank == 1) {
					sequentialRankedcardsCounter++;
				} else if (sequentialRankedcardsCounter >= 2) {
					sequences.add(new LongestSequence(sequentialRankedcardsCounter, highestKindOfSeqRank));
					sequentialRankedcardsCounter = 1;
					highestKindOfSeqRank = c.getKind();
				} else {
					highestKindOfSeqRank = c.getKind();
				}
				kA.setSuits(suits);
				kA.setKind(lastKind);
				kAs.add(kA);
				kA = new KindAggregate();
				suits = new ArrayList<Suit>();
			}
			if (i == cards.size() - 1) {
				if (lastKind.rank - c.getKind().rank == 1) {
					sequences.add(new LongestSequence(sequentialRankedcardsCounter, highestKindOfSeqRank));
				}
				suits.add(c.getSuit());
				kA.setSuits(suits);
				kA.setKind(c.getKind());
				kAs.add(kA);
				kA = new KindAggregate();
				suits = new ArrayList<Suit>();
			}
			lastKind = c.getKind();
			suits.add(c.getSuit());
		}
		if (sequences.size() != 0) {
			Collections.sort(sequences);
			this.longestSequence = sequences.get(0);
			if (this.longestSequence.getLength() >= 5) {
				this.longestSequence.setSequenceCharacteristics(this.suitAggregates);
			}
		} else {
			this.longestSequence = new LongestSequence(0, null);
		}
		this.kindAggregates = kAs;

	}

	public Suit getSuitWhichCanBeFoundFiveTimesOrMoreInPlayerCards(Map<Suit, List<Kind>> suitAggregates) {
		Suit suit = null;
		for (Map.Entry<Suit, List<Kind>> entry : suitAggregates.entrySet()) {
			if (entry.getValue().size() >= 5) {
				suit = entry.getKey();
				break;
			}
		}
		return suit;
	}

	public boolean areFiveCardsOfSameSuit(Map<Suit, List<Kind>> suitAggregates) {
		if (getSuitWhichCanBeFoundFiveTimesOrMoreInPlayerCards(suitAggregates) != null) {
			return true;
		}
		return false;
	}

	public List<KindAggregate> getKindAggregates() {
		return kindAggregates;
	}

	public LongestSequence getLongestSequence() {
		return longestSequence;
	}

	public Map<Suit, List<Kind>> getSuitAggregates() {
		return suitAggregates;
	}

}
