package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private ArrayList<Player> players = new ArrayList<>();
	private int numOfPlayersThatPerformedAction = 0;
	
	private Player currentPlayer = new InitialPlayer();
	private int currentIndex = 0;
	private ArrayList<Card> communityCards = new ArrayList<>();
	private Deck deck = new Deck();
	private static final int SMALL_BLIND = 1;
	private static final int BIG_BLIND = 2;
	private static final int DEFAULT_START_CASH = 100;
	private int pot = 0;
	
	public Iterable<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) {
		Player player = new Player();
		player.setName(name);
		player.setCash(DEFAULT_START_CASH);
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
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}
	
	public void call() {
		bet(1);
		onPlayerPerformedAction();
	}

	public void check() {
		onPlayerPerformedAction();
	}

	public void fold() {
		onPlayerPerformedAction();
		currentPlayer.addCash(pot);
	}

	private void forcedBet(int value) {
		bet(value);
		nextPlayer();		
	}
	
	private void bet(int value) {
		currentPlayer.bet(value);
		pot += value;
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

	private class InitialPlayer extends Player {

		@Override
		public String getName() {
			return "nobody";
		}

		@Override
		public void setName(String name) {
			throw new IllegalAccessError();
		}

		@Override
		public List<Card> getCards() {
			return new ArrayList<Card>();
		}

		@Override
		public void setCards(List<Card> cards) {
			throw new IllegalAccessError();
		}

		@Override
		public void bet(int bet) {
			throw new IllegalAccessError();
		}
	}
}
