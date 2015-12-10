package com.sap.ase.poker.model;

import java.util.ArrayList;

public class Table {
	private ArrayList<Player> players = new ArrayList<>();
	private Player currentPlayer = new NullPlayer();
	private ArrayList<Card> communityCards = new ArrayList<>();

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public void startGame() {
		ArrayList<Card> cards = new ArrayList<>();

		Card c1 = new Card();
		c1.setSuit("hearts");
		c1.setKind("ace");
		cards.add(c1);

		Card c2 = new Card();
		c2.setSuit("spades");
		c2.setKind("ten");
		cards.add(c2);

		players.get(0).setCards(cards);
		players.get(1).setCards(cards);
		currentPlayer = players.get(0);
	}

	public void placeBet(int value) {
		currentPlayer.bet(value);
		currentPlayer = players.get(1);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void showCommunityCards() {
		Card c1 = new Card();
		c1.setSuit("hearts");
		c1.setKind("seven");
		this.communityCards.add(c1);

		Card c2 = new Card();
		c2.setSuit("spades");
		c2.setKind("nine");
		this.communityCards.add(c2);

		Card c3 = new Card();
		c3.setSuit("spades");
		c3.setKind("ten");
		this.communityCards.add(c3);

	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}
}
