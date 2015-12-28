package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TablePlayers implements Iterable<Player>{

	private List<Player> players = new ArrayList<Player>();
	private int currentIndex = 0;
	private int startIndex = 0;
	
	public void add(Player player) {
		players.add(player);
	}

	public Player getCurrentPlayer() {
		return players.size() == 0 ? new NullPlayer() : players.get(currentIndex);
	}

	public void nextPlayer() {
		currentIndex = (currentIndex + 1) % players.size();		
	}

	public void nextGame() {
		startIndex = (startIndex + 1) % players.size();		
		currentIndex = startIndex;
	}

	@Override
	public Iterator<Player> iterator() {
		return players.iterator();
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
