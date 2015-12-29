package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Table {
	private int numOfPlayersThatPerformedAction = 0;
	private TablePlayers players = new TablePlayers();
	private ArrayList<Card> communityCards = new ArrayList<>();
	private Deck deck = new Deck();
	private static final int DEFAULT_START_CASH = 100;
	private int pot = 0;
	private int round;
	private int currentMaxBet;
	
	private PreFlop preFlop;
	private Flop flop;
	private Turn turn;
	private River river;

	public Iterable<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) {
		this.players.add(new Player(name, DEFAULT_START_CASH));
	}

	public void startGame() {
		preFlop = new PreFlop(players, deck);
		flop = new Flop(deck, communityCards);
		turn = new Turn(deck, communityCards);
		river = new River(deck, communityCards);
		
		preFlop.start();
		round = 0;
		currentMaxBet = preFlop.currentMaxBet;
		pot = preFlop.pot;
	}

	public Player getCurrentPlayer() {
		return players.getCurrentPlayer();
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}

	public void call() {

		final int delta = currentMaxBet - getCurrentPlayer().getBet();
		if (delta == 0) {
			throw new IllegalOperationException();
		}
		betDelta(delta);

		onPlayerPerformedAction();
	}

	public void check() {
		if (getCurrentPlayer().getBet() != currentMaxBet) {
			throw new IllegalOperationException();
		}
		onPlayerPerformedAction();
	}

	public void fold() {
		getCurrentPlayer().setInactive();
		onPlayerPerformedAction();
	}

	public void raiseTo(int amount) {
		int delta = amount - getCurrentPlayer().getBet();
		if (getCurrentPlayer().getCash() < delta || amount <= currentMaxBet) {
			throw new IllegalOperationException();
		}
		currentMaxBet = amount;
		betDelta(delta);
		onPlayerPerformedAction();
	}

	private void betDelta(int delta) {
		getCurrentPlayer().bet(delta);
		pot += delta;
	}

	private void onPlayerPerformedAction() {
		numOfPlayersThatPerformedAction++;
		players.nextPlayer();

		if (onlyOneActivePlayer()) {
			getCurrentPlayer().addCash(pot);
			players.nextStartPlayer();
			startGame();
		} else if (shouldNextRoundStart()) {
			numOfPlayersThatPerformedAction = 0;
			if (round == 0) {
				flop.start();
				round++;
			} else if (round == 1) {
				turn.start();
				round++;
			} else if (round == 2) {
				river.start();
				round++;
			} else if (round == 3) {
				// TODO determine winner
				players.nextStartPlayer();
				startGame();
			}
		}
	}

	private boolean onlyOneActivePlayer() {
		return players.activePlayersSize() == 1;
	}

	private boolean shouldNextRoundStart() {
		return areAllBetsEven() && didAllPlayersPerformAnAction();
	}

	private boolean areAllBetsEven() {
		Set<Integer> uniqueBets = new HashSet<Integer>();
		for (Player p : players) {
			if (p.isActive()) {
				uniqueBets.add(p.getBet());
			}
		}
		return uniqueBets.size() == 1;
	}

	private boolean didAllPlayersPerformAnAction() {
		return numOfPlayersThatPerformedAction >= players.activePlayersSize();
	}

	private void showCommunityCards(int count) {
		for (int i = 0; i < count; i++) {
			this.communityCards.add(deck.dealCard());
		}
	}
	
	@SuppressWarnings("serial")
	public class IllegalOperationException extends RuntimeException{
	}
}
