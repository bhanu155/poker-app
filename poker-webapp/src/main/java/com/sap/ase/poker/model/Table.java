package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private ArrayList<Player> players = new ArrayList<>();
	private Player currentPlayer = new InitialPlayer();
	private ArrayList<Card> communityCards = new ArrayList<>();

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) {
		Player player = new Player();
		player.setName(name);
		this.players.add(player);
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
		if (players.get(0).getBet() == players.get(1).getBet()) {
			showCommunityCards();
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	private void showCommunityCards() {
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
	
	
	private class InitialPlayer extends Player {

		@Override
		public String getName() {
			return "nobody";
		}

		@Override
		public void setName(String name) {
			throw new IllegalAccessError();
		}

		@Override
		public List<Card> getCards() {
			return new ArrayList<Card>();
		}

		@Override
		public void setCards(List<Card> cards) {
			throw new IllegalAccessError();
		}

		@Override
		public void bet(int bet) {
			throw new IllegalAccessError();
		}
	}
}