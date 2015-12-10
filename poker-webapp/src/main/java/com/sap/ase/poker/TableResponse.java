package com.sap.ase.poker;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Player;

public class TableResponse {

	private Player[] players;
	private Card[] cards;

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public Card[] getCards() {
		return cards;
	}

	public void setCards(Card[] cards) {
		this.cards = cards;
	}
}
