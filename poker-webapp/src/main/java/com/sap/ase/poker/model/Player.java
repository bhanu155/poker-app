package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.ase.poker.Bet;

public class Player {

	private String name;
	private List<Card> cards = new ArrayList<Card>();
	private int bet;

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
	}
}
