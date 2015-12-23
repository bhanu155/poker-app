package com.sap.ase.poker.gameguide.models;

import java.util.ArrayList;

public class PlayerHandAggregate implements Comparable<PlayerHandAggregate> {

	private Card.Kinds kind;
	private int countKinds;
	private ArrayList<Card.Suits> suits;

	public PlayerHandAggregate() {
		this.kind = null;
		this.countKinds = 0;
		this.suits = new ArrayList<Card.Suits>();
	}

	@Override
	public int compareTo(PlayerHandAggregate pHA) {
		return pHA.kind.rank - this.kind.rank;
	}

	public Card.Kinds getKind() {
		return kind;
	}

	public void setKind(Card.Kinds kind) {
		this.kind = kind;
	}

	public int getCountKinds() {
		return countKinds;
	}

	public void setCountKinds(int countKinds) {
		this.countKinds = countKinds;
	}

	public ArrayList<Card.Suits> getSuits() {
		return suits;
	}

	public void setSuits(ArrayList<Card.Suits> suits) {
		this.suits = suits;
	}

}
