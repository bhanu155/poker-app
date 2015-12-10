package com.sap.ase.poker;

public class TableResponse {

	private Player[] players;
	private Card[] cards;
	private String currentPlayer;
	private Card[] communityCards;

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

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Card[] getCommunityCards() {
		return communityCards;
	}

	public void setCommunityCards(Card[] communityCards) {
		this.communityCards = communityCards;
	}

	public static class Player {

		private String name;
		private int bet;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getBet() {
			return bet;
		}

		public void setBet(int bet) {
			this.bet = bet;
		}
	}

	public static class Card {

		private String suit;
		private String kind;

		public String getSuit() {
			return suit;
		}

		public void setSuit(String suit) {
			this.suit = suit;
		}

		public String getKind() {
			return kind;
		}

		public void setKind(String kind) {
			this.kind = kind;
		}
	}
}
