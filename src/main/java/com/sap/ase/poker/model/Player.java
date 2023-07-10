package com.sap.ase.poker.model;

import com.sap.ase.poker.model.deck.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {

	private String id;
	private final String name;
	private int cash;

	private int bet = 0;
	private boolean isActive = false;
	private List<Card> handCards = new ArrayList<>();

	private boolean hasPlayed = false;

	public Player(String id, String name, int cash) {
		this.id = id;
		this.name = name;
		this.cash = cash;
	}

	public String getName() {
		return name;
	}

	public void bet(int bet) {
		this.bet += bet;
		deductCash(bet);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void deductCash(int amount) {
		cash -= amount;
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

	public List<Card> getHandCards() {
		return this.handCards;
	}

	public void setHandCards(List<Card> handCards) {
		this.handCards = handCards;
	}

	public boolean isHasPlayed() {
		return hasPlayed;
	}

	public void setHasPlayed(boolean hasPlayed) {
		this.hasPlayed = hasPlayed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(id, other.id);
	}

}
