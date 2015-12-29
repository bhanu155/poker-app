package com.sap.ase.poker.model;

import java.util.HashSet;
import java.util.Set;

public class Bets {

	private final TablePlayers players;
	private int pot = 0;
	private int currentMaxBet = 0;

	public Bets(TablePlayers players) {
		this.players = players;
	}
	
	public void call() {
		int delta = currentMaxBet - getCurrentPlayerBet();
		if (delta == 0) {
			throw new IllegalOperationException();
		}
		bet(delta);
	}

	public void check() {
		if (getCurrentPlayerBet() != currentMaxBet) {
			throw new IllegalOperationException();
		}
	}

	public void fold() {
		players.getCurrentPlayer().setInactive();
	}

	public void raiseTo(int amount) {
		int delta = amount - getCurrentPlayerBet();
		if (players.getCurrentPlayer().getCash() < delta || amount <= currentMaxBet) {
			throw new IllegalOperationException();
		}
		bet(delta);
	}
	
	private void bet(int amount) {
		Player currentPlayer = players.getCurrentPlayer(); 
		currentPlayer.bet(amount);
		currentMaxBet = Math.max(currentMaxBet, currentPlayer.getBet());
		pot += amount;
	}

	public int getPot() {
		return pot;
	}
	
	private int getCurrentPlayerBet() {
		return players.getCurrentPlayer().getBet();
	}

	public int getCurrentMaxBet() {
		return currentMaxBet;
	}
	
	public boolean areAllBetsEven() {
		Set<Integer> uniqueBets = new HashSet<>();
		for (Player p : players) {
			if (p.isActive()) {
				uniqueBets.add(p.getBet());
			}
		}
		return uniqueBets.size() == 1;
	}
	
	@SuppressWarnings("serial")
	public class IllegalOperationException extends RuntimeException{
	}
}
