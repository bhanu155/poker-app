package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String name;
	private List<Card> cards = new ArrayList<Card>();
	private int bet;
	private int cash;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public void bet(int bet) {
		this.bet += bet;
		this.cash -= bet;
	}

	public int getBet() {
		return bet;
	}

	public int getCash() {
		return cash;
	}
	
	public void setCash(int cash) {
		this.cash = cash;
	}

	public void addCash(int pot) {
		cash += pot;
	}
}
