package com.sap.ase.poker.model;

import java.util.Arrays;

public class PreFlop {

	private static final int SMALL_BLIND = 1;
	private static final int BIG_BLIND = 2;

	private final TablePlayers players;
	private final Deck deck;
	public int pot = 0;
	public int currentMaxBet = 0;

	public PreFlop(TablePlayers players, Deck deck) {
		this.players = players;
		this.deck = deck;
	}

	public void start() {
		for (Player p : players) {
			p.setCards(Arrays.asList(deck.dealCard(), deck.dealCard()));
			p.clearBet();
			p.setActive();
		}
		forcedBet(SMALL_BLIND);
		forcedBet(BIG_BLIND);
		currentMaxBet  = BIG_BLIND;
	}
	
	private void forcedBet(int amount) {
		players.getCurrentPlayer().bet(amount);
		pot += amount;
		players.nextPlayer();
	}
}
