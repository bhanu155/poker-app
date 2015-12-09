package com.sap.ase.poker.model;

import java.util.ArrayList;

public class Table {
	private ArrayList<Player> players = new ArrayList<>();

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
	}
}
