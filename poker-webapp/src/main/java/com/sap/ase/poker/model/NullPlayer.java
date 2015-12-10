package com.sap.ase.poker.model;

import java.util.ArrayList;
import java.util.List;

public class NullPlayer extends Player {

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
