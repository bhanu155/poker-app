package com.sap.ase.poker.rest;

import java.util.ArrayList;
import java.util.List;

public class GetTableResponse {

	private List<Player> players = new ArrayList<>();
	private List<Card> playerCards = new ArrayList<>();
	private String currentPlayer;
	private List<Card> communityCards = new ArrayList<>();

	public GetTableResponse(com.sap.ase.poker.model.Table table) {
		for (com.sap.ase.poker.model.Player player : table.getPlayers()) {
			players.add(new Player(player));
		}

		for (com.sap.ase.poker.model.Card card : table.getCurrentPlayer().getCards()) {
			playerCards.add(new Card(card));
		}

		for (com.sap.ase.poker.model.Card card : table.getCommunityCards()) {
			communityCards.add(new Card(card));
		}

		currentPlayer = table.getCurrentPlayer().getName();
	}
	
	public Player[] getPlayers() {
		return players.toArray(new Player[0]);
	}

	public Card[] getCards() {
		return playerCards.toArray(new Card[0]);
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public Card[] getCommunityCards() {
		return communityCards.toArray(new Card[0]);
	}

	public static class Player {

		private String name;
		private int bet;

		public Player(com.sap.ase.poker.model.Player player) {
			this.name = player.getName();
			this.bet = player.getBet();
		}

		public String getName() {
			return name;
		}

		public int getBet() {
			return bet;
		}
	}

	public static class Card {

		private String suit;
		private String kind;

		public Card(com.sap.ase.poker.model.Card card) {
			suit = card.getSuit();
			kind = card.getKind();
		}

		public String getSuit() {
			return suit;
		}

		public String getKind() {
			return kind;
		}
	}
}
