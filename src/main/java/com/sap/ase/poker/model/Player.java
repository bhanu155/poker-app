package com.sap.ase.poker.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Player {

	private final String name;
	private int cash;

	private int bet = 0;
	private boolean isActive = false;

	public Player(String name, int cash) {
		this.name = name;
		this.cash = cash;
	}

	public String getName() {
		return name;
	}

	public void bet(int bet) {
		this.bet += bet;
		this.cash -= bet;
	}

	public int getBet() {
		return bet;
	}

	public void clearBet() {
		bet = 0;
	}

	public int getCash() {
		return cash;
	}

	public void addCash(int amount) {
		cash += amount;
	}

	public void setActive() {
		this.isActive = true;
	}

	public void setInactive() {
		this.isActive = false;
	}

	public boolean isActive() {
		return isActive;
	}
}
