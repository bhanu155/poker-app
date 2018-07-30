package com.sap.ase.poker.winner;

import static java.util.Collections.frequency;

import java.util.List;
import java.util.Optional;

import com.sap.ase.poker.model.Card;

//TODO naming is confusing - here, a hand has 7 cards, in hand class it has 5...
@SuppressWarnings("serial")
public class IllegalHand extends Exception {
	public IllegalHand(String message) {
		super(message);
	}

	public static void assert7Cards(Card ... cards) throws IllegalNumberOfCards {
		if (cards.length != 7) {
			throw new IllegalNumberOfCards(cards.length);
		}
	}

	public static void assertNoDuplicates(Card ... cards) throws DuplicateCards {
		for (int i = 0; i < cards.length; i++) {
			for (int j = 0; j < cards.length; j++) {
				if ((i != j) && (cards[i].equals(cards[j]))) {
					throw new DuplicateCards(cards[i]);
				}
			}
			
		}
	}

//	public static void assert7Cards(List<Card> cards) throws IllegalNumberOfCards {
//		if (cards.size() != 7) {
//			throw new IllegalNumberOfCards(cards.size());
//		}
//	}
//
//	public static void assertNoDuplicates(List<Card> cards) throws DuplicateCards {
//		Optional<Card> firstDuplicate = cards.stream().filter(i -> frequency(cards, i) > 1).findFirst();
//		if (firstDuplicate.isPresent()) {
//			throw new DuplicateCards(firstDuplicate.get());
//		}
//	}
	
	public static class IllegalNumberOfCards extends IllegalHand {
		public final int numberOfCards;

		public IllegalNumberOfCards(int numberOfCards) {
			super(numberOfCards + " is an illegal number of cards");
			this.numberOfCards = numberOfCards;
		}
	}

	public static class DuplicateCards extends IllegalHand {
		public final Card duplicateCard;

		public DuplicateCards(Card duplicateCard) {
			super(duplicateCard + " is duplicate");
			this.duplicateCard = duplicateCard;
		}
	}
}
