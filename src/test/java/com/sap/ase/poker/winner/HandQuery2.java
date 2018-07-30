package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.ACE;
import static com.sap.ase.poker.model.Card.Kind.TWO;
import static com.sap.ase.poker.winner.Hand.Type.FLUSH;
import static com.sap.ase.poker.winner.Hand.Type.FOUR_OF_A_KIND;
import static com.sap.ase.poker.winner.Hand.Type.FULL_HOUSE;
import static com.sap.ase.poker.winner.Hand.Type.HIGH_CARD;
import static com.sap.ase.poker.winner.Hand.Type.ROYAL_FLUSH;
import static com.sap.ase.poker.winner.Hand.Type.STRAIGHT;
import static com.sap.ase.poker.winner.Hand.Type.STRAIGHT_FLUSH;
import static com.sap.ase.poker.winner.Hand.Type.THREE_OF_A_KIND;
import static com.sap.ase.poker.winner.Hand.Type.TWO_PAIRS;
import static com.sap.ase.poker.winner.IllegalHand.assert7Cards;
import static com.sap.ase.poker.winner.IllegalHand.assertNoDuplicates;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.sort;
import static java.util.Collections.reverseOrder;

import java.util.Collections;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.winner.Hand.Type;

public class HandQuery2 {

	private Card[] sortedCards;
	private final Card[][] bySuit = new Card[4][7];
	private final Card[][] byKind = new Card[13][7];
	private final int[] bySuitCount = new int[4];
	private final int[] byKindCount = new int[13];

	private int consecutiveCount = 0;
	private Card[] straight = new Card[5];
	private int pairsCount = 0;
	private final Card[][] pairs = new Card[3][2];
	private int threeOfAKindCount = 0;
	private final Card[][] threeOfAKind = new Card[2][3];
	private int fourOfAKindCount = 0;
	private final Card[][] fourOfAKind = new Card[1][4];
	private Card[] flush;
	private Card[] straightFlush;

	public Hand findBestHand(Card... sevenCards) throws IllegalHand {
		assert7Cards(sevenCards);
		assertNoDuplicates(sevenCards);
		sortedCards = copyOf(sevenCards, 7);
		sort(sortedCards, reverseOrder());
		findPossibleCombinations();
		return findBestCombination();
	}

	private void findPossibleCombinations() {
		for (Card card : sortedCards) {
			int suitIndex = card.suit.ordinal();
			bySuit[suitIndex][bySuitCount[suitIndex]++] = card;
			int kindIndex = card.kind.ordinal();
			byKind[kindIndex][byKindCount[kindIndex]++] = card;

			if ((consecutiveCount < 5) && ((consecutiveCount == 0) || (card.kind.rank == straight[consecutiveCount - 1].kind.rank - 1))) {
				straight[consecutiveCount++] = card;
			} else if ((consecutiveCount < 5) && (consecutiveCount >= 1) && (card.kind.rank < straight[consecutiveCount - 1].kind.rank - 1)) {
				consecutiveCount = 0;
				straight[consecutiveCount++] = card;
			}
			if ((consecutiveCount == 4) && (sortedCards[0].kind == ACE) && (card.kind == TWO)) {
				straight[consecutiveCount++] = sortedCards[0];
			}

			if (bySuitCount[suitIndex] == 5) {
				flush = copyOf(bySuit[suitIndex], 5);
			}
			if (bySuitCount[suitIndex] >= 5) {
				if ((straightFlush == null) && (bySuit[suitIndex][bySuitCount[suitIndex] - 5].kind.rank == card.kind.rank + 4)) {
					straightFlush = copyOfRange(bySuit[suitIndex], bySuitCount[suitIndex] - 5, bySuitCount[suitIndex]);
				} else if ((straightFlush == null) && ((bySuit[suitIndex][bySuitCount[suitIndex] - 4].kind.rank == card.kind.rank + 3)
						&& (bySuit[suitIndex][0].kind == ACE) && (card.kind == TWO))) {
					straightFlush = new Card[5];
					arraycopy(bySuit[suitIndex], bySuitCount[suitIndex] - 4, straightFlush, 0, 4);
					straightFlush[4] = bySuit[suitIndex][0];
				}
			}
		}
		
		// iterate backwards to preserve the kind-based reverse sort order
		for (int i = byKindCount.length - 1; i >= 0; i--) {
			if (byKindCount[i] == 4) {
				fourOfAKind[fourOfAKindCount++] = copyOf(byKind[i], 4);
			} else if (byKindCount[i] == 3) {
				threeOfAKind[threeOfAKindCount++] = copyOf(byKind[i], 3);
			} else if (byKindCount[i] == 2) {
				pairs[pairsCount++] = copyOf(byKind[i], 2);
			}
		}
	}

	private Hand findBestCombination() {
		if ((straightFlush != null) && (straightFlush[0].kind == ACE)) {
			return new Hand(ROYAL_FLUSH, straightFlush);
		} else if (straightFlush != null) {
			return new Hand(STRAIGHT_FLUSH, straightFlush);
		} else if (fourOfAKindCount == 1) {
			return new Hand(FOUR_OF_A_KIND, firstFiveUniqueCards(fourOfAKind[0]));
		} else if (threeOfAKindCount == 2) {
			Card[] cards = new Card[5];
			arraycopy(threeOfAKind[0], 0, cards, 0, 3);
			arraycopy(threeOfAKind[1], 0, cards, 3, 2);
			return new Hand(FULL_HOUSE, cards);
		} else if ((threeOfAKindCount == 1) && (pairsCount >= 1)) {
			Card[] cards = new Card[5];
			arraycopy(threeOfAKind[0], 0, cards, 0, 3);
			arraycopy(pairs[0], 0, cards, 3, 2);
			return new Hand(FULL_HOUSE, cards);
		} else if (flush != null) {
			return new Hand(FLUSH, flush);
		} else if (straight[4] != null) {
			return new Hand(STRAIGHT, straight);
		} else if (threeOfAKindCount == 1) {
			return new Hand(THREE_OF_A_KIND, firstFiveUniqueCards(threeOfAKind[0]));
		} else if (pairsCount >= 2) {
			Card[] cards = new Card[4];
			arraycopy(pairs[0], 0, cards, 0, 2);
			arraycopy(pairs[1], 0, cards, 2, 2);
			return new Hand(TWO_PAIRS, firstFiveUniqueCards(cards));
		} else if (pairsCount == 1) {
			return new Hand(Type.PAIR, firstFiveUniqueCards(pairs[0]));
		} else {
			return new Hand(HIGH_CARD, firstFiveUniqueCards(new Card[0]));
		}
	}

	private Card[] firstFiveUniqueCards(Card[] leadingCards) {
		Card[] cards = new Card[5];
		arraycopy(leadingCards, 0, cards, 0, leadingCards.length);
		int i = leadingCards.length;
		for (Card card : sortedCards) {
			boolean exists = false;
			for (Card filterCard : leadingCards) {
				if (card.equals(filterCard)) {
					exists = true;
				}
			}
			if ((!exists) && (i < 5)) {
				cards[i++] = card;
			}
		}
		return cards;
	}
}
