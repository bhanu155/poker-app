package com.sap.ase.poker.winner;

import java.util.List;

import com.sap.ase.poker.model.Card;

public class PokerHand {
	private PokerHandDefinition pokerHandDefinition;
	private List<Card> cardsUsedForPokerHand;
	private List<Card> redundantCards;

	public PokerHand(PokerHandDefinition pokerHandDefinition, List<Card> cardsUsedForPokerHand,
			List<Card> redundantCards) {
		this.cardsUsedForPokerHand = cardsUsedForPokerHand;
		this.redundantCards = redundantCards;
		this.pokerHandDefinition = pokerHandDefinition;
	}

	public enum PokerHandDefinition {
		ROYALFLUSH("Royal Flush", "Five cards: 10, Jack, Queen, King, Ace of the same kind", 1), STRAIGHTFLUSH(
				"Straight Flush", "Five cards ranked sequentially in order in same suit",
				2), FOUROFAKIND("Four of a kind", "Four cards of the same numerical rank", 3), FULLHOUSE("Full House",
						"Three cards of the same numerical rank combined with two other cards of the same numerical rank",
						4), FLUSH("Flush", "Five Cards of the same suit", 5), STRAIGHT("Straight",
								"Five cards ranked sequentially in order regardless of suit",
								6), THREEOFAKIND("Three of a Kind", "Three cards of the same numerical rank",
										7), TWOPAIR("Two Pair",
												"Two cards of the same numerical rank combined with two other cards of the same numerical rank",
												8), PAIR("Pair", "Two cards of the same numerical rank",
														9), NONE("NONE", "Sorry, you got no poker hand", 99);

		public final String name;
		public final String description;
		public final int rank;

		private PokerHandDefinition(String name, String description, int rank) {
			this.name = name;
			this.description = description;
			this.rank = rank;
		}
	}

	public PokerHandDefinition getPokerHandDefinition() {
		return pokerHandDefinition;
	}

	public List<Card> getCardsUsedForPokerHand() {
		return cardsUsedForPokerHand;
	}

	public List<Card> getRedundantCards() {
		return redundantCards;
	}

	public void setPokerHandDefinition(PokerHandDefinition pokerHandDefinition) {
		this.pokerHandDefinition = pokerHandDefinition;
	}

	public void setCardsUsedForPokerHand(List<Card> cardsUsedForPokerHand) {
		this.cardsUsedForPokerHand = cardsUsedForPokerHand;
	}

	public void setRedundantCards(List<Card> redundantCards) {
		this.redundantCards = redundantCards;
	}

}
