package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.ACE;
import com.sap.ase.poker.winner.Hand.Type;
import static com.sap.ase.poker.winner.IllegalHand.assert7Cards;
import static com.sap.ase.poker.winner.IllegalHand.assertNoDuplicates;
import static java.util.Arrays.asList;
import static java.util.Collections.reverseOrder;
import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;

public class DetermineHand {

	public Hand execute(Card... sevenCards) throws IllegalHand {
		List<Card> cards = asList(sevenCards);
		assert7Cards(cards);
		assertNoDuplicates(cards);
		sort(cards, reverseOrder());

		TreeMap<Kind, List<Card>> kindMap = new TreeMap<>(reverseOrder());
		TreeMap<Suit, List<Card>> suitMap = new TreeMap<>(reverseOrder());
		for (Card card : cards) {
			Kind kind = card.getKind();
			Suit suit = card.getSuit();

			List<Card> cardsByKind = kindMap.containsKey(kind) ? kindMap.get(kind) : new ArrayList<>();
			cardsByKind.add(card);
			kindMap.put(kind, cardsByKind);

			List<Card> cardsBySuit = suitMap.containsKey(suit) ? suitMap.get(suit) : new ArrayList<>();
			cardsBySuit.add(card);
			suitMap.put(suit, cardsBySuit);
		}
		List<List<Card>> fourOfAKinds = kindMap.values().stream().filter(list -> list.size() == 4).collect(toList());
		List<List<Card>> threeOfAKinds = kindMap.values().stream().filter(list -> list.size() == 3).collect(toList());
		List<List<Card>> pairs = kindMap.values().stream().filter(list -> list.size() == 2).collect(toList());

		List<Card> flush = suitMap.values().stream().filter(list -> list.size() >= 5).findFirst().orElse(null);
		if (flush != null) {
			List<Card> straightFlush = straight(flush);
			if (straightFlush != null) {
				if (straightFlush.get(0).getKind() == ACE) {
					return new Hand(Type.ROYAL_FLUSH, straightFlush);
				} else {
					return new Hand(Type.STRAIGHT_FLUSH, straightFlush);
				}
			} else {
				return new Hand(Type.FLUSH, flush.subList(0, 5));
			}
		}

		// Flatten the list of by-kind collections. If there are multiple cards of the
		// same kind, lets say we have a diamond 10 and a spades 10, we can just pick
		// the first, (=kindGroup.cards.get(0)), this is good enough for a straight
		List<Card> byKindFlat = kindMap.values().stream().map(list -> list.get(0)).collect(toList());
		List<Card> straight = straight(byKindFlat);
		if (straight != null) {
			return new Hand(Type.STRAIGHT, straight);
		}

		if (fourOfAKinds.size() == 1) {
			return new Hand(Type.FOUR_OF_A_KIND, firstFiveUniqueCards(asList(fourOfAKinds.get(0), cards)));
		} else if ((threeOfAKinds.size() >= 1) && (pairs.size() >= 1)) {
			return new Hand(Type.FULL_HOUSE, firstFiveUniqueCards(asList(threeOfAKinds.get(0), pairs.get(0))));
		} else if (threeOfAKinds.size() == 2) {
			return new Hand(Type.FULL_HOUSE, firstFiveUniqueCards(asList(threeOfAKinds.get(0), threeOfAKinds.get(1))));
		} else if (threeOfAKinds.size() == 1) {
			return new Hand(Type.THREE_OF_A_KIND, firstFiveUniqueCards(asList(threeOfAKinds.get(0), cards)));
		} else if (pairs.size() >= 2) {
			return new Hand(Type.TWO_PAIRS, firstFiveUniqueCards(asList(pairs.get(0), pairs.get(1), cards)));
		} else if (pairs.size() >= 1) {
			return new Hand(Type.PAIR, firstFiveUniqueCards(asList(pairs.get(0), cards)));
		} else {
			return new Hand(Type.HIGH_CARD, cards.subList(0, 5));
		}
	}

	private List<Card> firstFiveUniqueCards(List<List<Card>> cards) {
		return cards.stream().flatMap(List::stream).distinct().limit(5).collect(toList());
	}

	/*
	 * strategy: compare rank of current card with rank of card 4 indices before; if
	 * the difference in rank is 4, then we have found 5 consecutive cards.
	 * 
	 * precondition: sorted + no duplicate kinds, e.g. clubs 7 and spades 7 is wrong
	 *
	 * example: diamonds 7 at index 2 and clubs 3 at index 6 is a straight
	 */
	private List<Card> straight(List<Card> cards) {
		// if first card is ace, add to end of list (a straight can also start from 1)
		if (cards.get(0).getKind() == ACE) {
			cards.add(cards.get(0));
		}

		for (int i = 4; i < cards.size(); i++) {
			int rank = cards.get(i).getKind().rank;
			int previousRank = cards.get(i - 4).getKind().rank;
			// modulo to consider ace as "1" in a small straight
			if (rank % ACE.rank == previousRank - 4) {
				return cards.subList(i - 4, i + 1);
			}
		}
		return null;
	}
}
