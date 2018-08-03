package com.sap.ase.poker.model;

public interface Deck {

	Card dealCard();

	void shuffle();
}