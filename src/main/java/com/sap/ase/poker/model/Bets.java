package com.sap.ase.poker.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bets {

	private final TablePlayers players;
	private int pot = 0;
	private int currentMaxBet = 0;

	public Bets(TablePlayers players) {
		this.players = players;
	}

	public void call() throws IllegalAmount {
		int delta = currentMaxBet - getCurrentPlayerBet();
		//TODO if player doesn't have enough cash, should be all-in -> side-pot
		if (delta == 0) {
			throw new IllegalAmount("Calling is not possible - player has max bet already - can only check or fold");
		}
		bet(delta);
	}

	public void check() throws IllegalAmount {
		if (getCurrentPlayerBet() != currentMaxBet) {
			throw new IllegalAmount("Checking not possible - player needs to bet at least " + currentMaxBet);
		}
	}

	public void fold() {
		players.getCurrentPlayer().setInactive();
	}

	public void raiseTo(int amount) throws IllegalAmount {
		int delta = amount - getCurrentPlayerBet();
		if (players.getCurrentPlayer().getCash() < delta || amount <= currentMaxBet) {
			throw new IllegalAmount("Betting " + amount + " is insufficient or player doesn't have enough cash");
		}
		bet(delta);
	}

	public boolean areEven() {
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

	public void distributePot(List<Player> winners, Player oddChipsWinner) {
		int oddChips = pot % winners.size();
		for (Player player : players) {
			player.clearBet();
			if (winners.contains(player)) {
				player.addCash(pot / winners.size());
			}
		}
		oddChipsWinner.addCash(oddChips);
	}
	
	/*
	 * This class is internally used to identify illegal operations. Example:
	 * raising when the player doesn't have sufficient cash. This is an illegal
	 * usage from the client, not a server error.
	 */
	@SuppressWarnings("serial")
	public static class IllegalAmount extends Exception {
		public IllegalAmount(String message) {
			super(message);
		}
	}
}
