package com.sap.ase.poker.winner;

import static java.util.Collections.frequency;

import java.util.List;
import java.util.Optional;

import com.sap.ase.poker.model.Card;

@SuppressWarnings("serial")
public class IllegalCards extends RuntimeException {
	public IllegalCards(String message) {
		super(message);
	}

	public static void assert7Cards(List<Card> cards) throws IllegalNumberOfCards {
		if (cards.size() != 7) {
			throw new IllegalNumberOfCards(cards.size());
		}
	}

	public static void assertNoDuplicates(List<Card> cards) throws DuplicateCards {
		Optional<Card> firstDuplicate = cards.stream().filter(i -> frequency(cards, i) > 1).findFirst();
		if (firstDuplicate.isPresent()) {
			throw new DuplicateCards(firstDuplicate.get());
		}
	}

	public static class IllegalNumberOfCards extends IllegalCards {
		public final int numberOfCards;

		public IllegalNumberOfCards(int numberOfCards) {
			super(numberOfCards + " is an illegal number of cards");
			this.numberOfCards = numberOfCards;
		}
	}

	public static class DuplicateCards extends IllegalCards {
		public final Card duplicateCard;

		public DuplicateCards(Card duplicateCard) {
			super(duplicateCard + " is duplicate");
			this.duplicateCard = duplicateCard;
		}
	}
}
