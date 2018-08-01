package com.sap.ase.poker.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import com.sap.ase.poker.model.Bets.IllegalAmount;
import com.sap.ase.poker.model.rounds.Flop;
import com.sap.ase.poker.model.rounds.PreFlop;
import com.sap.ase.poker.model.rounds.River;
import com.sap.ase.poker.model.rounds.Round;
import com.sap.ase.poker.model.rounds.Turn;

public class Table {
	private static final int DEFAULT_START_CASH = 100;

	private int numOfPlayersThatPerformedAction = 0;
	private TablePlayers players = new TablePlayers();
	private ArrayList<Card> communityCards = new ArrayList<>();
	private Deck deck = new Deck();
	private Bets bets;
	
	private Queue<Round> rounds = new ArrayDeque<>();
	private Round currentRound;
	
	public Iterable<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) {
		players.add(new Player(name, DEFAULT_START_CASH));
	}

	public void startGame() throws IllegalAmount {
		deck = new Deck();
		communityCards = new ArrayList<>();
		bets = new Bets(players);
		initTexasHoldemRounds();
		startNextRound();
	}

	public Player getCurrentPlayer() {
		return players.getCurrentPlayer();
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}

	public void call() throws IllegalAmount {
		bets.call();
		onPlayerPerformedAction();
	}

	public void check() throws IllegalAmount {
		bets.check();
		onPlayerPerformedAction();
	}

	public void fold() throws IllegalAmount {
		bets.fold();
		onPlayerPerformedAction();
	}

	public void raiseTo(int amount) throws IllegalAmount {
		bets.raiseTo(amount);
		onPlayerPerformedAction();
	}

	private void onPlayerPerformedAction() throws IllegalAmount {
		numOfPlayersThatPerformedAction++;
		players.nextPlayer();

		if (isOnlyOneActivePlayerLeft()) {
			getCurrentPlayer().addCash(bets.getPot());
			players.nextStartPlayer();
			startGame();
		} else if (shouldStartNextRound()) {
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
		rounds.add(new PreFlop(players, deck, communityCards, bets));
		rounds.add(new Flop(players, deck, communityCards));
		rounds.add(new Turn(players, deck, communityCards));
		rounds.add(new River(players, deck, communityCards));
	}
	
	private void startNextRound() throws IllegalAmount {
		currentRound = rounds.remove();
		currentRound.start();
	}

	private boolean isOnlyOneActivePlayerLeft() {
		return players.activePlayersSize() == 1;
	}

	private boolean shouldStartNextRound() {
		return bets.areEven() && didAllPlayersPerformAnAction();
	}

	private boolean didAllPlayersPerformAnAction() {
		return numOfPlayersThatPerformedAction >= players.activePlayersSize();
	}
}
