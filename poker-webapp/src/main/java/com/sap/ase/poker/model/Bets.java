package com.sap.ase.poker.model;

public class Bets {

	private final TablePlayers players;
	private int pot = 0;
	private int currentMaxBet = 0;

	public Bets(TablePlayers players) {
		this.players = players;
	}
	
	public void bet(int amount) {
		Player currentPlayer = players.getCurrentPlayer(); 
		currentPlayer.bet(amount);
		currentMaxBet = Math.max(currentMaxBet, currentPlayer.getBet());
		pot += amount;
	}

	public int getPot() {
		return pot;
	}
	
	public int getCurrentMaxBet() {
		return currentMaxBet;
	}
}
