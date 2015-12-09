package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String name;
	private List<Card> cards = new ArrayList<Card>();

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
}
