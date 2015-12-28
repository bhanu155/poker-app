package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sap.ase.poker.Exceptions.IllegalOperationException;

public class Table {
	private int numOfPlayersOnTable = 0;
	private int numOfPlayersThatPerformedAction = 0;
	private TablePlayers players = new TablePlayers();
	private ArrayList<Card> communityCards = new ArrayList<>();
	private Deck deck = new Deck();
	private static final int SMALL_BLIND = 1;
	private static final int BIG_BLIND = 2;
	private static final int DEFAULT_START_CASH = 100;
	private int pot = 0;
	private int round;
	private int currentMaxBet = BIG_BLIND;

	public Iterable<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) {
		numOfPlayersOnTable++;
		this.players.add(new Player(name, DEFAULT_START_CASH));
	}

	public void startGame() {

		for (Player p : players) {
			List<Card> cards = new ArrayList<>();
			cards.add(deck.dealCard());
			cards.add(deck.dealCard());
			p.setCards(cards);
			p.clearBet();
		}
		round = 0;
		forcedBet(SMALL_BLIND);
		forcedBet(BIG_BLIND);
		currentMaxBet = BIG_BLIND;
	}

	public Player getCurrentPlayer() {
		return players.getCurrentPlayer();
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}

	public void call() {
		
		final int delta = currentMaxBet - getCurrentPlayer().getBet();
		if(delta==0){
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
		onPlayerPerformedAction();
		getCurrentPlayer().addCash(pot);
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

	private void forcedBet(int amount) {
		betDelta(amount);
		players.nextPlayer();
	}

	private void betDelta(int delta) {
		getCurrentPlayer().bet(delta);
		pot += delta;
	}

	private void onPlayerPerformedAction() {
		numOfPlayersThatPerformedAction++;
		players.nextPlayer();

		if (isRoundFinished()) {
			numOfPlayersThatPerformedAction = 0;
			if (round == 0) {
				showCommunityCards(3);
				round++;
			} else if (round == 2 || round == 1) {
				showCommunityCards(1);
				round++;
			} else if (round == 3) {
				// TODO determine winner and start next round;
				// TODO nextGame should be moved to startGame
				players.nextGame();
				startGame();
			}
		}
	}

	private boolean isRoundFinished() {
		return areAllBetsEven() && didAllPlayersPerformAnAction();
	}

	private boolean areAllBetsEven() {
		Set<Integer> uniqueBets = new HashSet<Integer>();
		for (Player p : players) {
			uniqueBets.add(p.getBet());
		}
		return uniqueBets.size() == 1;
	}

	private boolean didAllPlayersPerformAnAction() {
		return numOfPlayersThatPerformedAction == numOfPlayersOnTable;
	}

	private void showCommunityCards(int count) {
		for (int i = 0; i < count; i++) {
			this.communityCards.add(deck.dealCard());
		}
	}
}
