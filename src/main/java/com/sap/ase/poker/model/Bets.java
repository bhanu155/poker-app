package com.sap.ase.poker.model;

import java.util.HashSet;
import java.util.Set;

import com.sap.ase.poker.model.Table.IllegalOperationException;

public class Bets {

	private final TablePlayers players;
	private int pot = 0;
	private int currentMaxBet = 0;

	public Bets(TablePlayers players) {
		this.players = players;
	}

	public void call() throws IllegalOperationException {
		int delta = currentMaxBet - getCurrentPlayerBet();
		if (delta == 0) {
			throw new IllegalOperationException("Calling is not possible - player has max bet already - can only check or fold");
		}
		bet(delta);
	}

	public void check() throws IllegalOperationException {
		if (getCurrentPlayerBet() != currentMaxBet) {
			throw new IllegalOperationException("Checking not possible - player needs to bet at least " + currentMaxBet);
		}
	}

	public void fold() {
		players.getCurrentPlayer().setInactive();
	}

	public void raiseTo(int amount) throws IllegalOperationException {
		int delta = amount - getCurrentPlayerBet();
		if (players.getCurrentPlayer().getCash() < delta || amount <= currentMaxBet) {
			throw new IllegalOperationException("Betting " + amount + " is insufficient or player doesn't have enough cash");
		}
		bet(delta);
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
}
