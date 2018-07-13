package com.sap.ase.poker.winner;

import java.util.ArrayList;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;

public class KindAggregate implements Comparable<KindAggregate> {

	private Kind kind;
	private ArrayList<Suit> suits;

	public KindAggregate() {
		this.kind = null;
		this.suits = new ArrayList<Suit>();
	}

	@Override
	public int compareTo(KindAggregate cA) {
		return cA.kind.rank - this.kind.rank;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public ArrayList<Card.Suit> getSuits() {
		return suits;
	}

	public void setSuits(ArrayList<Suit> suits) {
		this.suits = suits;
	}
}
