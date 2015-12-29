package com.sap.ase.poker.model.rounds;

import java.util.Arrays;
import java.util.List;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Deck;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.TablePlayers;

public abstract class Round {

	protected final TablePlayers players;
	private final Deck deck;
	private final List<Card> communityCards;
	
	public int pot = 0;
	private int currentMaxBet = 0;

	public Round(TablePlayers players, Deck deck, List<Card> communityCards) {
		this.players = players;
		this.deck = deck;
		this.communityCards = communityCards;
	}

	public abstract void start();
		
	protected void dealCommunityCards(int count) {
		for (int i = 0; i < count; i++) {
			this.communityCards.add(deck.dealCard());
		}
	}
	
	protected void dealCardsToEachPlayer() {
		for (Player p : players) {
			p.setCards(Arrays.asList(deck.dealCard(), deck.dealCard()));
			p.clearBet();
			p.setActive();
		}
	}
	
	public void bet(int amount) {
		Player currentPlayer = players.getCurrentPlayer(); 
		currentPlayer.bet(amount);
		currentMaxBet = Math.max(currentMaxBet, currentPlayer.getBet());
		pot += amount;
	}
	
	public int getPot() {
		return pot;
	}
	
	public int getCurrentMaxBet() {
		return currentMaxBet;
	}
}