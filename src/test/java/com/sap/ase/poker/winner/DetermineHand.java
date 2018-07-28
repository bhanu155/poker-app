package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.ACE;
import static com.sap.ase.poker.winner.Hand.Type.*;
import static com.sap.ase.poker.winner.IllegalHand.assert7Cards;
import static com.sap.ase.poker.winner.IllegalHand.assertNoDuplicates;
import static java.util.Arrays.asList;
import static java.util.Collections.reverseOrder;
import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;

public class DetermineHand {

	public Hand execute(Card... sevenCards) throws IllegalHand {
		List<Card> sortedCards = asList(sevenCards);
		assert7Cards(sortedCards);
		assertNoDuplicates(sortedCards);

		sort(sortedCards, reverseOrder());

		Map<Kind, KindGroup> kindMap = new HashMap<>();
		Map<Suit, List<Card>> suitMap = new HashMap<>();
		for (Card card : sortedCards) {
			Kind kind = card.getKind();
			Suit suit = card.getSuit();

			KindGroup kindGroup = kindMap.containsKey(kind) ? kindMap.get(kind) : new KindGroup(kind);
			kindGroup.cards.add(card);
			kindMap.put(kind, kindGroup);

			List<Card> cardsBySuit = suitMap.containsKey(suit) ? suitMap.get(suit) : new ArrayList<>();
			cardsBySuit.add(card);
			suitMap.put(suit, cardsBySuit);
		}
		List<KindGroup> byKind = new ArrayList<KindGroup>(kindMap.values());
		sort(byKind, reverseOrder());

		List<KindGroup> fourOfAKinds = byKind.stream().filter(kindGrp -> kindGrp.cards.size() == 4).collect(toList());
		List<KindGroup> threeOfAKinds = byKind.stream().filter(kindGrp -> kindGrp.cards.size() == 3).collect(toList());
		List<KindGroup> pairs = byKind.stream().filter(kindGrp -> kindGrp.cards.size() == 2).collect(toList());

		for (List<Card> cardsOfSameSuit : suitMap.values()) {
			if (cardsOfSameSuit.size() >= 5) {
				List<Card> straightFlush = straight(cardsOfSameSuit);
				if (straightFlush != null) {
					if (straightFlush.get(0).getKind() == ACE) {
						return new Hand(ROYAL_FLUSH, straightFlush);
					} else {
						return new Hand(STRAIGHT_FLUSH, straightFlush);
					}
				} else {
					return new Hand(FLUSH, cardsOfSameSuit.subList(0, 5));
				}
			}
		}

		// Flatten the list of by-kind collections. If there are multiple cards of the
		// same kind, lets say we have a diamond 10 and a spades 10, we can just pick
		// the first, (=kindGroup.cards.get(0)), this is good enough for a straight
		List<Card> byKindFlat = byKind.stream().map(kindGroup -> kindGroup.cards.get(0)).collect(toList());
		List<Card> straight = straight(byKindFlat);
		if (straight != null) {
			return new Hand(STRAIGHT, straight);
		}

		if (fourOfAKinds.size() == 1) {
			return new Hand(FOUR_OF_A_KIND, reduceToFiveCards(fourOfAKinds.get(0).cards, sortedCards));
		} else if ((threeOfAKinds.size() >= 1) && (pairs.size() >= 1)) {
			return new Hand(FULL_HOUSE,
					concat(threeOfAKinds.get(0).cards.stream(), pairs.get(0).cards.stream()).collect(toList()));
		} else if (threeOfAKinds.size() >= 1) {
			return new Hand(THREE_OF_A_KIND, reduceToFiveCards(threeOfAKinds.get(0).cards, sortedCards));
		} else if (pairs.size() >= 2) {
			return new Hand(TWO_PAIRS, reduceToFiveCards(
					concat(pairs.get(0).cards.stream(), pairs.get(1).cards.stream()).collect(toList()), sortedCards));
		} else if (pairs.size() >= 1) {
			return new Hand(PAIR, reduceToFiveCards(pairs.get(0).cards, sortedCards));
		} else {
			return new Hand(HIGH_CARD, sortedCards.subList(0, 5));
		}
	}

	private List<Card> reduceToFiveCards(List<Card> leadingCards, List<Card> sevenCards) {
		Stream<Card> filteredCards = sevenCards.stream().filter(card -> !leadingCards.contains(card));
		return concat(leadingCards.stream(), filteredCards).limit(5).collect(toList());
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
}
