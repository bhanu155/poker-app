package com.sap.ase.poker.rest;

public class LobbyEntry {

	private final String id;
	private final String players;
	private final String stakes;
	
	public LobbyEntry(String id, String players, String stakes) {
		this.id = id;
		this.players = players;
		this.stakes = stakes;
	}

	public String getId() {
		return id;
	}

	public String getPlayers() {
		return players;
	}

	public String getStakes() {
		return stakes;
	}

}
