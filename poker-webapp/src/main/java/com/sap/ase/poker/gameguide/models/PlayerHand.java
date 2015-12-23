package com.sap.ase.poker.gameguide.models;

import java.util.ArrayList;

public class PlayerHand {
	ArrayList<Card> cards;
	ArrayList<Card> cardsForBestHand;
	ArrayList<Card> missingCardsForBetterHand;
	Hand bestPossibleHand;

	public PlayerHand() {
		this.cards = new ArrayList<Card>();
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public ArrayList<Card> getCardsForBestHand() {
		return cardsForBestHand;
	}

	public void setCardsForBestHand(ArrayList<Card> cardsForBestHand) {
		this.cardsForBestHand = cardsForBestHand;
	}

	public ArrayList<Card> getMissingCardsForBetterHand() {
		return missingCardsForBetterHand;
	}

	public void setMissingCardsForBetterHand(ArrayList<Card> missingCardsForBetterHand) {
		this.missingCardsForBetterHand = missingCardsForBetterHand;
	}

	public Hand getBestPossibleHand() {
		return bestPossibleHand;
	}

	public void setBestPossibleHand(Hand bestPossibleHand) {
		this.bestPossibleHand = bestPossibleHand;
	}

}
