package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.ACE;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.FLUSH;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.FOUR_OF_A_KIND;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.FULL_HOUSE;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.HIGH_CARD;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.PAIR;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.ROYAL_FLUSH;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.STRAIGHT;
import static com.sap.ase.poker.winner.DetermineBestHand.HandType.STRAIGHT_FLUSH;
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

		Map<Kind, KindGroup> kindMap = new HashMap<>();
		Map<Suit, List<Card>> suitMap = new HashMap<>();
		for (Card card : sortedCards) {
			Kind kind = card.getKind();
			Suit suit = card.getSuit();

			KindGroup kindGroup = kindMap.containsKey(kind) ? kindMap.get(kind)
					: new KindGroup(kind, new ArrayList<>());
			kindGroup.cards.add(card);
			kindMap.put(kind, kindGroup);

			List<Card> cardsBySuit = suitMap.containsKey(suit) ? suitMap.get(suit) : new ArrayList<>();
			cardsBySuit.add(card);
			suitMap.put(suit, cardsBySuit);
		}
		List<KindGroup> byKind = new ArrayList<KindGroup>(kindMap.values());
		Collections.sort(byKind, reverseOrder());

		List<KindGroup> fourOfAKinds = byKind.stream().filter(kindGroup -> kindGroup.cards.size() == 4)
				.collect(Collectors.toList());
		List<KindGroup> threeOfAKinds = byKind.stream().filter(kindGroup -> kindGroup.cards.size() == 3)
				.collect(Collectors.toList());
		List<KindGroup> pairs = byKind.stream().filter(kindGroup -> kindGroup.cards.size() == 2)
				.collect(Collectors.toList());

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

		// Flatten the list of by-kind collections. If there are multiple cards of the
		// same kind, lets say we have a diamond 10 and a spades 10, we can just pick
		// the first, (=kindGroup.cards.get(0)), this is good enough for a straight
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

	/*
	 * strategy: compare rank of current card with rank of card 4 indices before; if
	 * the difference in rank is also 4, then we have found 5 consecutive cards.
	 * 
	 * example: diamonds 7 at index 2 and clubs 3 at index 6
	 * 
	 * precondition: each rank must be unique, e.g. clubs 7 and spades 7 is wrong
	 */
	private List<Card> straight(List<Card> cards) {
		// if first card is ace, add to end of list (a straight can also start from 1)
		if (cards.get(0).getKind() == ACE) {
			cards.add(cards.get(0));
		}

		for (int i = 4; i < cards.size(); i++) {
			Kind kind = cards.get(i).getKind();
			Kind previousKind = cards.get(i - 4).getKind();
			// modulo 13 to consider ace (rank 13) as "1" (rank 0) in a small straight
			if (kind.rank % 13 == previousKind.rank - 4) {
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

	public enum HandType {
		HIGH_CARD, PAIR, TWO_PAIRS, THREE_OF_A_KIND, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT, STRAIGHT_FLUSH, ROYAL_FLUSH
	}
}
