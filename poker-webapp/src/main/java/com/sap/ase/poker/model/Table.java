package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.Collection;

public class Table {
	private ArrayList<Player> players = new ArrayList<>();
	private int numOfPlayersThatPerformedAction = 0;
	
	private Player currentPlayer = new NullPlayer();
	private int currentIndex = 0;
	private ArrayList<Card> communityCards = new ArrayList<>();
	private Deck deck = new Deck();
	private static final int SMALL_BLIND = 1;
	private static final int BIG_BLIND = 2;
	private static final int DEFAULT_START_CASH = 100;
	private int pot = 0;
	private int currentMaxBet = BIG_BLIND;
	
	public Iterable<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) {
		Player player = new Player(name,DEFAULT_START_CASH);
		
		this.players.add(player);
	}

	public void startGame() {
		ArrayList<Card> cards = new ArrayList<>();
		cards.add(deck.dealCard());
		cards.add(deck.dealCard());
		players.get(0).setCards(cards);

		cards = new ArrayList<>();
		cards.add(deck.dealCard());
		cards.add(deck.dealCard());
		players.get(1).setCards(cards);
		
		currentPlayer = players.get(0);
		forcedBet(SMALL_BLIND);
		forcedBet(BIG_BLIND);
		currentMaxBet = BIG_BLIND;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}
	
	public void call() {
		betDelta(currentMaxBet - currentPlayer.getBet());
		onPlayerPerformedAction();
	}

	public void check() {
		onPlayerPerformedAction();
	}

	public void fold() {
		onPlayerPerformedAction();
		currentPlayer.addCash(pot);
	}

	public void raiseTo(int amount) {
		currentMaxBet = amount;
		betDelta(amount - currentPlayer.getBet());
		onPlayerPerformedAction();
	}
	
	private void forcedBet(int amount) {
		betDelta(amount);
		nextPlayer();		
	}
	
	private void betDelta(int delta) {
		currentPlayer.bet(delta);
		pot += delta;
	}

	private void onPlayerPerformedAction() {
		numOfPlayersThatPerformedAction++;
		nextPlayer();		
		if (isRoundFinished()) {
			showCommunityCards();				
		}
	}

	private void nextPlayer() {
		currentIndex = (currentIndex + 1) % players.size();		
		currentPlayer = players.get(currentIndex);
	}
	
	private boolean isRoundFinished() {
		return areAllBetsEven() && didAllPlayersPerformAnAction();
	}

	private boolean areAllBetsEven() {
		return players.get(0).getBet() == players.get(1).getBet();
	}

	private boolean didAllPlayersPerformAnAction() {
		return numOfPlayersThatPerformedAction == players.size();
	}

	private void showCommunityCards() {
		this.communityCards.add(deck.dealCard());
		this.communityCards.add(deck.dealCard());
		this.communityCards.add(deck.dealCard());
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
