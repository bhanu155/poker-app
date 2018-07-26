package com.sap.ase.poker.winner;

import com.sap.ase.poker.model.Card;

@SuppressWarnings("serial")
public class IllegalHand extends Exception {
	public IllegalHand(String message) {
		super(message);
	}

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
