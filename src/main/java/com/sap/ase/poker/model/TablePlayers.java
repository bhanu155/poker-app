package com.sap.ase.poker.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TablePlayers {

	private List<Player> players = new ArrayList<>();
	private int currentIndex = 0;
	private int startIndex = 0;

	public void add(Player player) {
		if (!playerExists(player)) {
			players.add(player);
		}
	}

	private boolean playerExists(Player player) {
		return players.stream().anyMatch(p->p.getName().equals(player.getName()));
	}

	public Player getCurrentPlayer() {
		return players.size() == 0 ? new NullPlayer() : players.get(currentIndex);
	}

	public int activePlayersSize() {
		int count = 0;
		for (Player p : players) {
			if (p.isActive()) {
				count++;
			}
		}
		return count;
	}

	public int size() {
		return players.size();
	}

	public void nextPlayer() {
		currentIndex = (currentIndex + 1) % players.size();
		if (!players.get(currentIndex).isActive()) {
			nextPlayer();
		}
	}

	public void nextStartPlayer() {
		startIndex = (startIndex + 1) % players.size();
		currentIndex = startIndex;
	}

	public List<Player> toList() {
		return unmodifiableList(players);
	}

	private class NullPlayer extends Player {

		public NullPlayer() {
			super("nobody", -1);
		}

		@Override
		public void setCards(Collection<Card> cards) {
			throw new NullPlayerException();
		}

		@Override
		public void bet(int bet) {
			throw new NullPlayerException();
		}

		@Override
		public void addCash(int pot) {
			throw new NullPlayerException();
		}
	}

	@SuppressWarnings("serial")
	private class NullPlayerException extends IllegalStateException {

		public NullPlayerException() {
			super("tried to play the game without a player");
		}
	}
}
