package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Player {

	private final String name;
	private List<Card> cards = new ArrayList<Card>();
	private int bet;
	private int cash;
	private boolean isActive;
	
	public Player(String name, int cash) {
		this.name = name;
		this.cash = cash;
		this.isActive = true;
	}
	
	public String getName() {
		return name;		
	}

	public Iterable<Card> getCards() {
		return cards;
	}

	public void setCards(Collection<Card> cards) {
		this.cards = new ArrayList<Card>(cards);
	}

	public void bet(int bet) {
		this.bet += bet;
		this.cash -= bet;
	}

	public int getBet() {
		return bet;
	}
	
	public void clearBet(){
		bet =0;
	}

	public int getCash() {
		return cash;
	}
	
	public void addCash(int amount) {
		cash += amount;
	}
	
	public void setIsActive(boolean active){
		this.isActive = active; 
	}

	public boolean isActive() {
		return isActive;
	}
	
}
