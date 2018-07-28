package com.sap.ase.poker.winner;

import java.util.List;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;

public class KindGroup implements Comparable<KindGroup> {
	public final Kind kind;
	public final List<Card> cards;

	public KindGroup(Kind kind, List<Card> cards) {
		this.kind = kind;
		this.cards = cards;
	}

	@Override
	public int compareTo(KindGroup otherKindGroup) {
		return this.kind.compareTo(otherKindGroup.kind);
	}
}
