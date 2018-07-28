package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.ACE;
import static java.util.Collections.frequency;
import static java.util.Collections.reverseOrder;
import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;
import com.sap.ase.poker.winner.Hand.Flush;
import com.sap.ase.poker.winner.Hand.FourOfAKind;
import com.sap.ase.poker.winner.Hand.FullHouse;
import com.sap.ase.poker.winner.Hand.HighCard;
import com.sap.ase.poker.winner.Hand.Pair;
import com.sap.ase.poker.winner.Hand.RoyalFlush;
import com.sap.ase.poker.winner.Hand.Straight;
import com.sap.ase.poker.winner.Hand.StraightFlush;
import com.sap.ase.poker.winner.Hand.ThreeOfAKind;
import com.sap.ase.poker.winner.Hand.TwoPairs;
import com.sap.ase.poker.winner.Hand.Type;
import com.sap.ase.poker.winner.IllegalHand.DuplicateCards;
import com.sap.ase.poker.winner.IllegalHand.IllegalNumberOfCards;

public class DetermineHand {

	public Result execute(Card... sevenCards) throws IllegalHand {
		List<Card> sortedCards = Arrays.asList(sevenCards);
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

		List<Card> handOfFive = new ArrayList<>();
		List<Card> remainingCards = new ArrayList<>(sortedCards);

		for (List<Card> cardsOfSameSuit : suitMap.values()) {
			if (cardsOfSameSuit.size() >= 5) {
				List<Card> straightFlush = straight(cardsOfSameSuit);
				if (straightFlush != null) {
					if (straightFlush.get(0).getKind() == ACE) {
						return new Result(new RoyalFlush(straightFlush));
					} else {
						return new Result(new StraightFlush(straightFlush));
					}
				} else {
					return new Result(new Flush(cardsOfSameSuit.subList(0, 5)));
				}
			}
		}

		// Flatten the list of by-kind collections. If there are multiple cards of the
		// same kind, lets say we have a diamond 10 and a spades 10, we can just pick
		// the first, (=kindGroup.cards.get(0)), this is good enough for a straight
		List<Card> byKindFlat = byKind.stream().map(kindGroup -> kindGroup.cards.get(0)).collect(Collectors.toList());
		List<Card> straight = straight(byKindFlat);
		if (straight != null) {
			return new Result(new Straight(straight));
		}

		if (fourOfAKinds.size() == 1) {
			moveCards(fourOfAKinds.get(0).cards, remainingCards, handOfFive);
			handOfFive.addAll(remainingCards.subList(0, 1)); // TODO can delegate this to class?
			return new Result(new FourOfAKind(fourOfAKinds.get(0).cards, remainingCards.subList(0, 1)));
		} else if ((threeOfAKinds.size() >= 1) && (pairs.size() >= 1)) {
			moveCards(threeOfAKinds.get(0).cards, remainingCards, handOfFive);
			moveCards(pairs.get(0).cards, remainingCards, handOfFive);  // TODO can delegate this to class?
			return new Result(new FullHouse(threeOfAKinds.get(0).cards, pairs.get(0).cards));
		} else if (threeOfAKinds.size() >= 1) {
			moveCards(threeOfAKinds.get(0).cards, remainingCards, handOfFive); // TODO can delegate this to class?
			return new Result(new ThreeOfAKind(threeOfAKinds.get(0).cards, remainingCards.subList(0, 2)));
		} else if (pairs.size() >= 2) {
			moveCards(pairs.get(0).cards, remainingCards, handOfFive);
			moveCards(pairs.get(1).cards, remainingCards, handOfFive); // TODO can delegate this to class?
			return new Result(new TwoPairs(pairs.get(0).cards, pairs.get(1).cards, remainingCards.subList(0, 1)));
		} else if (pairs.size() >= 1) {
			moveCards(pairs.get(0).cards, remainingCards, handOfFive); // TODO can delegate this to class?
			return new Result(new Pair(pairs.get(0).cards, remainingCards.subList(0, 3)),
					handOfFive.toArray(new Card[0]));
		} else {
			return new Result(new HighCard(sortedCards.subList(0, 5)), sortedCards.subList(0, 5).toArray(new Card[0]));
		}
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
		public final Type handType;
		public final Card[] handOfFive;

		public Result(Type handType, Card... handOfFive) {
			this.handType = handType;
			this.handOfFive = handOfFive;
		}

		public Result(Hand hand, Card... handOfFive) {
			this.handType = hand.type;
			this.handOfFive = hand.getCards();
		}
	}
}
