package com.sap.ase.poker.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import com.sap.ase.poker.model.rounds.Flop;
import com.sap.ase.poker.model.rounds.PreFlop;
import com.sap.ase.poker.model.rounds.River;
import com.sap.ase.poker.model.rounds.Round;
import com.sap.ase.poker.model.rounds.Turn;

public class Table {
	private int numOfPlayersThatPerformedAction = 0;
	private TablePlayers players = new TablePlayers();
	private ArrayList<Card> communityCards = new ArrayList<>();
	private Deck deck = new Deck();
	private static final int DEFAULT_START_CASH = 100;
	private int pot = 0;
	private int currentMaxBet;
	
	private Queue<Round> rounds = new ArrayDeque<>();
	private Round currentRound;
	
	public Iterable<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) {
		players.add(new Player(name, DEFAULT_START_CASH));
	}

	public void startGame() {
		initTexasHoldemRounds();
		startNextRound();
		currentMaxBet = currentRound.getCurrentMaxBet();
		pot = currentRound.getPot();
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
			if (rounds.isEmpty()) {
				// TODO determine winner
				players.nextStartPlayer();
				startGame();
			} else {
				startNextRound();
			}
		}
	}
	
	private void initTexasHoldemRounds() {
		rounds.clear();
		rounds.add(new PreFlop(players, deck, communityCards));
		rounds.add(new Flop(players, deck, communityCards));
		rounds.add(new Turn(players, deck, communityCards));
		rounds.add(new River(players, deck, communityCards));
	}
	private void startNextRound() {
		currentRound = rounds.remove();
		currentRound.start();
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

	@SuppressWarnings("serial")
	public class IllegalOperationException extends RuntimeException{
	}
}
