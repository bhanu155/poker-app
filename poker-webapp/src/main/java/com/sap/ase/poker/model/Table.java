package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private ArrayList<Player> players = new ArrayList<>();
	private int numOfPlayersThatPlacedBet = 0;
	
	private Player currentPlayer = new InitialPlayer();
	private int currentIndex = 0;
	private ArrayList<Card> communityCards = new ArrayList<>();
	private Deck deck = new Deck();
	private static final int SMALL_BLIND = 1;
	private static final int BIG_BLIND = 2;

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) {
		Player player = new Player();
		player.setName(name);
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
		placeBet(SMALL_BLIND);
		placeBet(BIG_BLIND);
	}

	private void placeBet(int value) {
		currentPlayer.bet(value);
		currentIndex = (currentIndex + 1) % players.size();		
		currentPlayer = players.get(currentIndex);
		
		if (players.get(0).getBet() == players.get(1).getBet()) {
			if (numOfPlayersThatPlacedBet == players.size()) {
				showCommunityCards();				
			}
		}
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	private void showCommunityCards() {
		this.communityCards.add(deck.dealCard());
		this.communityCards.add(deck.dealCard());
		this.communityCards.add(deck.dealCard());
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
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

	public void call() {
		numOfPlayersThatPlacedBet++;
		placeBet(1);		
	}

	public void check() {
		numOfPlayersThatPlacedBet++;
		placeBet(0);
	}
}
