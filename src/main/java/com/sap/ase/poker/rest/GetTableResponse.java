package com.sap.ase.poker.rest;

import java.util.ArrayList;
import java.util.List;

public class GetTableResponse {

	private List<Player> players = new ArrayList<>();
	private String currentPlayer = "nobody";

	public GetTableResponse(String uiPlayerName) {
	}

	public Player[] getPlayers() {
		return players.toArray(new Player[0]);
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public static class Player {

		private final String name;
		private final int bet;
		private final int cash;

		public Player(com.sap.ase.poker.model.Player player) {
			this.name = player.getName();
			this.bet = player.getBet();
			this.cash = player.getCash();
		}

		public String getName() {
			return name;
		}

		public int getBet() {
			return bet;
		}

		public int getCash() {
			return cash;
		}
	}
}
