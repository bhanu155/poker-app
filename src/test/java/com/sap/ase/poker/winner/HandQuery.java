//package com.sap.ase.poker.winner;
//
//import static com.sap.ase.poker.model.Card.Kind.ACE;
//import static com.sap.ase.poker.winner.Hand.Type.*;
//import static com.sap.ase.poker.winner.IllegalHand.assert7Cards;
//import static com.sap.ase.poker.winner.IllegalHand.assertNoDuplicates;
//import static java.util.Arrays.asList;
//import static java.util.Collections.reverseOrder;
//import static java.util.Collections.sort;
//import static java.util.stream.Collectors.toList;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TreeMap;
//
//import com.sap.ase.poker.model.Card;
//import com.sap.ase.poker.model.Card.Kind;
//import com.sap.ase.poker.model.Card.Suit;
//
//public class HandQuery {
//
//	private List<Card> sortedCards;
//	private TreeMap<Kind, List<Card>> byKind;
//	private TreeMap<Suit, List<Card>> bySuit;
//
//	public Hand findBestHand(Card... sevenCards) throws IllegalHand {
//		prepareCards(sevenCards);
//		fillLookupStructures();
//		return findBestHand();
//	}
//
//	private void prepareCards(Card... sevenCards) throws IllegalHand {
//		sortedCards = asList(sevenCards);
//		assert7Cards(sortedCards);
//		assertNoDuplicates(sortedCards);
//		sort(sortedCards, reverseOrder());
//	}
//
//	private void fillLookupStructures() {
//		byKind = new TreeMap<>(reverseOrder());
//		bySuit = new TreeMap<>(reverseOrder());
//
//		for (Card card : sortedCards) {
//			List<Card> cardsByKind = byKind.containsKey(card.kind) ? byKind.get(card.kind) : new ArrayList<>();
//			cardsByKind.add(card);
//			byKind.put(card.kind, cardsByKind);
//
//			List<Card> cardsBySuit = bySuit.containsKey(card.suit) ? bySuit.get(card.suit) : new ArrayList<>();
//			cardsBySuit.add(card);
//			bySuit.put(card.suit, cardsBySuit);
//		}
//	}
//
//	private Hand findBestHand() {
//		List<Card> flush = bySuit.values().stream().filter(list -> list.size() >= 5).findFirst().orElse(null);
//		if (flush != null) {
//			List<Card> strFlush = straight(flush);
//			if (strFlush != null) {
//				return startsWithAce(strFlush) ? new Hand(ROYAL_FLUSH, strFlush) : new Hand(STRAIGHT_FLUSH, strFlush);
//			} else {
//				return new Hand(FLUSH, flush.subList(0, 5));
//			}
//		}
//
//		// Flatten the list of by-kind collections. If there are multiple cards of the
//		// same kind, lets say we have a diamond 10 and a spades 10, we can just pick
//		// the first, (=kindGroup.cards.get(0)), this is good enough for a straight
//		List<Card> byKindFlat = byKind.values().stream().map(list -> list.get(0)).collect(toList());
//		List<Card> straight = straight(byKindFlat);
//		if (straight != null) {
//			return new Hand(STRAIGHT, straight);
//		}
//
//		List<List<Card>> fourOfAKinds = byKind.values().stream().filter(list -> list.size() == 4).collect(toList());
//		List<List<Card>> threeOfAKinds = byKind.values().stream().filter(list -> list.size() == 3).collect(toList());
//		List<List<Card>> pairs = byKind.values().stream().filter(list -> list.size() == 2).collect(toList());
//
//		if (fourOfAKinds.size() == 1) {
//			return new Hand(FOUR_OF_A_KIND, firstFiveUniqueCards(asList(fourOfAKinds.get(0), sortedCards)));
//		} else if ((threeOfAKinds.size() >= 1) && (pairs.size() >= 1)) {
//			return new Hand(FULL_HOUSE, firstFiveUniqueCards(asList(threeOfAKinds.get(0), pairs.get(0))));
//		} else if (threeOfAKinds.size() == 2) {
//			return new Hand(FULL_HOUSE, firstFiveUniqueCards(asList(threeOfAKinds.get(0), threeOfAKinds.get(1))));
//		} else if (threeOfAKinds.size() == 1) {
//			return new Hand(THREE_OF_A_KIND, firstFiveUniqueCards(asList(threeOfAKinds.get(0), sortedCards)));
//		} else if (pairs.size() >= 2) {
//			return new Hand(TWO_PAIRS, firstFiveUniqueCards(asList(pairs.get(0), pairs.get(1), sortedCards)));
//		} else if (pairs.size() >= 1) {
//			return new Hand(PAIR, firstFiveUniqueCards(asList(pairs.get(0), sortedCards)));
//		} else {
//			return new Hand(HIGH_CARD, sortedCards.subList(0, 5));
//		}
//	}
//	
//	private List<Card> firstFiveUniqueCards(List<List<Card>> cards) {
//		return cards.stream().flatMap(List::stream).distinct().limit(5).collect(toList());
//	}
//
//	private boolean startsWithAce(List<Card> cards) {
//		return cards.get(0).kind == ACE;
//	}
//
//	/*
//	 * strategy: compare rank of current card with rank of card 4 indices before; if
//	 * the difference in rank is 4, then we have found 5 consecutive cards.
//	 * 
//	 * precondition: sorted + no duplicate kinds, e.g. clubs 7 and spades 7 is wrong
//	 *
//	 * example: diamonds 7 at index 2 and clubs 3 at index 6 is a straight
//	 */
//	private List<Card> straight(List<Card> cards) {
//		// if first card is ace, add to end of list (a straight can also start from 1)
//		if (cards.get(0).kind == ACE) {
//			cards.add(cards.get(0));
//		}
//
//		for (int i = 4; i < cards.size(); i++) {
//			int rank = cards.get(i).kind.rank;
//			int previousRank = cards.get(i - 4).kind.rank;
//			// modulo to consider ace as "1" in a small straight
//			if (rank % ACE.rank == previousRank - 4) {
//				return cards.subList(i - 4, i + 1);
//			}
//		}
//		return null;
//	}
//}
