package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.ACE;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.FLUSH;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.FOUR_OF_A_KIND;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.FULL_HOUSE;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.HIGH_CARD;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.PAIR;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.STRAIGHT;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.STRAIGHT_FLUSH;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.ROYAL_FLUSH;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.THREE_OF_A_KIND;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.TWO_PAIRS;
import static java.util.Collections.frequency;
import static java.util.Collections.reverseOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;
import com.sap.ase.poker.winner.IllegalHand.DuplicateCards;
import com.sap.ase.poker.winner.IllegalHand.IllegalNumberOfCards;

public class DetermineBestHand {

	public Result execute(Card... sevenCards) throws IllegalHand {
		List<Card> sortedCards = Arrays.asList(sevenCards);
		assert7Cards(sortedCards);
		assertNoDuplicates(sortedCards);

		Collections.sort(sortedCards, reverseOrder());

		// TODO can we avoid or improve that copying between kindMap and byKind?
		Map<Kind, List<Card>> kindMap = new HashMap<>();
		Map<Suit, List<Card>> suitMap = new HashMap<>();
		for (Card card : sortedCards) {
			Kind kind = card.getKind();
			Suit suit = card.getSuit();

			List<Card> cardsByKind = kindMap.containsKey(kind) ? kindMap.get(kind) : new ArrayList<>();
			cardsByKind.add(card);
			kindMap.put(kind, cardsByKind);

			List<Card> cardsBySuit = suitMap.containsKey(suit) ? suitMap.get(suit) : new ArrayList<>();
			cardsBySuit.add(card);
			suitMap.put(suit, cardsBySuit);
		}
		List<KindGroup> byKind = new ArrayList<>();
		for (Entry<Kind, List<Card>> entry : kindMap.entrySet()) {
			byKind.add(new KindGroup(entry.getKey(), entry.getValue()));
		}
		Collections.sort(byKind, reverseOrder());

		List<KindGroup> singleCards = new ArrayList<>();
		List<KindGroup> pairs = new ArrayList<>();
		List<KindGroup> threeOfAKinds = new ArrayList<>();
		List<KindGroup> fourOfAKinds = new ArrayList<>();
		for (KindGroup kindGroup : byKind) {
			List<Card> cardsByKind = kindGroup.cards;
			if (cardsByKind.size() == 1) {
				singleCards.add(kindGroup);
			} else if (cardsByKind.size() == 2) {
				pairs.add(kindGroup);
			} else if (cardsByKind.size() == 3) {
				threeOfAKinds.add(kindGroup);
			} else if (cardsByKind.size() == 4) {
				fourOfAKinds.add(kindGroup);
			}
		}

		List<Card> handOfFive = new ArrayList<>();
		List<Card> remainingCards = new ArrayList<>(sortedCards);

		if (fourOfAKinds.size() == 1) {
			moveCards(fourOfAKinds.get(0).cards, remainingCards, handOfFive);
			handOfFive.addAll(remainingCards.subList(0, 1));
			return new Result(FOUR_OF_A_KIND, handOfFive.toArray(new Card[0]));
		}

		if ((threeOfAKinds.size() >= 1) && (pairs.size() >= 1)) {
			moveCards(threeOfAKinds.get(0).cards, remainingCards, handOfFive);
			moveCards(pairs.get(0).cards, remainingCards, handOfFive);
			return new Result(FULL_HOUSE, handOfFive.toArray(new Card[0]));
		}

		for (List<Card> cardsOfSameSuit : suitMap.values()) {
			if (cardsOfSameSuit.size() >= 5) {
				List<Card> straightFlush = straight(cardsOfSameSuit);
				if (straightFlush != null) {
					if (straightFlush.get(0).getKind() == ACE) {
						return new Result(ROYAL_FLUSH, straightFlush.toArray(new Card[0]));
					} else {
						return new Result(STRAIGHT_FLUSH, straightFlush.toArray(new Card[0]));
					}
				} else {
					moveCards(cardsOfSameSuit.subList(0, 5), remainingCards, handOfFive);
					return new Result(FLUSH, handOfFive.toArray(new Card[0]));
				}
			}
		}

		// Flatten the list of KindGroup, if there are multiple cards of the same kind,
		// lets say we have a diamond 10 and a spades 10, we can just pick the first,
		// (=kindGroup.cards.get(0)), this is good enough for a straight
		List<Card> byKindFlat = byKind.stream().map(kindGroup -> kindGroup.cards.get(0)).collect(Collectors.toList());
		List<Card> straight = straight(byKindFlat);
		if (straight != null) {
			return new Result(STRAIGHT, straight.toArray(new Card[0]));
		}

		if (threeOfAKinds.size() >= 1) {
			moveCards(threeOfAKinds.get(0).cards, remainingCards, handOfFive);
			handOfFive.addAll(remainingCards.subList(0, 2));
			return new Result(THREE_OF_A_KIND, handOfFive.toArray(new Card[0]));
		}

		if (pairs.size() >= 2) {
			moveCards(pairs.get(0).cards, remainingCards, handOfFive);
			moveCards(pairs.get(1).cards, remainingCards, handOfFive);
			handOfFive.addAll(remainingCards.subList(0, 1));
			return new Result(TWO_PAIRS, handOfFive.toArray(new Card[0]));
		}

		if (pairs.size() >= 1) {
			moveCards(pairs.get(0).cards, remainingCards, handOfFive);
			handOfFive.addAll(remainingCards.subList(0, 3));
			return new Result(PAIR, handOfFive.toArray(new Card[0]));
		}

		return new Result(HIGH_CARD, sortedCards.subList(0, 5).toArray(new Card[0]));
	}

	private List<Card> straight(List<Card> cards) {
		if (cards.get(0).getKind() == ACE) {
			// if first card is ace, add to end of list (a straight can also start from 1)
			cards.add(cards.get(0));
		}
		int consecutiveCount = 1;
		for (int i = 1; i < cards.size(); i++) {
			Kind kind = cards.get(i).getKind();
			Kind previousKind = cards.get(i - 1).getKind();
			// modulo 13 to consider ace (rank 13) as "1" (rank 0) in a small straight
			if (kind.rank % 13 == previousKind.rank - 1) {
				consecutiveCount++;
			} else {
				consecutiveCount = 1;
			}
			if (consecutiveCount == 5) {
				return cards.subList(i - 4, i + 1);
			}
		}
		return null;
	}

	private void moveCards(List<Card> cardsToMove, List<Card> fromRemainingCards, List<Card> toHandOfFive) {
		toHandOfFive.addAll(cardsToMove);
		fromRemainingCards.removeAll(cardsToMove);
	}

	private void assert7Cards(Collection<Card> cards) throws IllegalNumberOfCards {
		if (cards.size() != 7) {
			throw new IllegalNumberOfCards(cards.size());
		}
	}

	private void assertNoDuplicates(Collection<Card> cards) throws DuplicateCards {
		Optional<Card> firstDuplicate = cards.stream().filter(i -> frequency(cards, i) > 1).findFirst();
		if (firstDuplicate.isPresent()) {
			throw new DuplicateCards(firstDuplicate.get());
		}
	}

	public class Result {
		public final HandType handType;
		public final Card[] handOfFive;

		public Result(HandType handType, Card... handOfFive) {
			this.handType = handType;
			this.handOfFive = handOfFive;
		}
	}

	private class KindGroup implements Comparable<KindGroup> {
		private final Kind kind;
		private final List<Card> cards;

		public KindGroup(Kind kind, List<Card> cards) {
			this.kind = kind;
			this.cards = cards;
		}

		@Override
		public int compareTo(KindGroup kindAggregate) {
			return this.kind.compareTo(kindAggregate.kind);
		}
	}

	public enum HandType {
		HIGH_CARD, PAIR, TWO_PAIRS, THREE_OF_A_KIND, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT, STRAIGHT_FLUSH, ROYAL_FLUSH
	}
}
