package com.sap.ase.poker.winner;

import static java.util.stream.Stream.concat;

import java.util.List;

import com.sap.ase.poker.model.Card;

// TODO should we even expose the sub classes? they are exclusively used by winner/pot determination
// TODO can the card comparison be made easier using streams, and would this improve the array<->list back and forth conversion?
public abstract class Hand implements Comparable<Hand> {
	protected final Type type;
	public final Card[] allCards;

	public Hand(Type type, Card[] allCards) {
		this.type = type;
		this.allCards = allCards;
	}

	@Override
	public final int compareTo(Hand otherHand) {
		for (int i = 0; i < 5; i++) {
			if (allCards[i].getKind() != otherHand.allCards[i].getKind()) {
				return allCards[i].compareTo(otherHand.allCards[i]);
			}
		}
		return 0;
	}

	public static class HighCard extends Hand {

		// TODO could we use a sorted set here?
		public HighCard(List<Card> cards) {
			super(Type.HIGH_CARD, cards.toArray(new Card[0]));
		}
	}

	public static class Pair extends Hand {
		// TODO could we use a sorted set here?
		public Pair(List<Card> pairCards, List<Card> next3Cards) {
			super(Type.PAIR, concat(pairCards.stream(), next3Cards.stream()).toArray(Card[]::new));
		}
	}

	public static class TwoPairs extends Hand {

		public TwoPairs(List<Card> highPairCards, List<Card> lowPairCards, List<Card> nextCard) {
			super(Type.TWO_PAIRS, concat(highPairCards.stream(), concat(lowPairCards.stream(), nextCard.stream()))
					.toArray(Card[]::new));
		}
	}

	public static class ThreeOfAKind extends Hand {

		public ThreeOfAKind(List<Card> threeOfAKindCards, List<Card> next2Cards) {
			super(Type.THREE_OF_A_KIND, concat(threeOfAKindCards.stream(), next2Cards.stream()).toArray(Card[]::new));
		}
	}

	public static class Straight extends Hand {
		public Straight(List<Card> straightCards) {
			super(Type.STRAIGHT, straightCards.toArray(new Card[0]));
		}
	}

	public static class Flush extends Hand {
		public Flush(List<Card> flushCards) {
			super(Type.FLUSH, flushCards.toArray(new Card[0]));
		}
	}

	public static class FullHouse extends Hand {
		public FullHouse(List<Card> threeOfAKindCards, List<Card> pairCards) {
			super(Type.FULL_HOUSE, concat(threeOfAKindCards.stream(), pairCards.stream()).toArray(Card[]::new));
		}
	}

	public static class FourOfAKind extends Hand {
		public FourOfAKind(List<Card> fourOfAKindCards, List<Card> nextCard) {
			super(Type.FOUR_OF_A_KIND, concat(fourOfAKindCards.stream(), nextCard.stream()).toArray(Card[]::new));
		}
	}

	public static class StraightFlush extends Hand {
		public StraightFlush(List<Card> straightFlushCards) {
			super(Type.STRAIGHT_FLUSH, straightFlushCards.toArray(new Card[0]));
		}
	}

	public static class RoyalFlush extends Hand {
		public RoyalFlush(List<Card> royalFlushCards) {
			super(Type.ROYAL_FLUSH, royalFlushCards.toArray(new Card[0]));
		}
	}

	public static enum Type {
		// keep the order - enums compare by ordinal, so the order defines the "value"
		// TODO do we really need the enum at all?
		HIGH_CARD, PAIR, TWO_PAIRS, THREE_OF_A_KIND, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT, STRAIGHT_FLUSH, ROYAL_FLUSH;
	}
}
