package com.sap.ase.poker.gameguide.models;

import java.util.ArrayList;

public class HandRule {
	HandRuleDefinition handRuledefinition;
	int consideredCartsCount;
	ArrayList<Card.Kinds> specificKinds;
	ArrayList<Card.Suits> specificSuits;
	ArrayList<Card> specificCards;

	public HandRule(HandRuleDefinition handRuledefinition, int consideredCartsCount,
			ArrayList<Card.Kinds> specificKinds, ArrayList<Card.Suits> specificSuits, ArrayList<Card> specificCards) {
		this.handRuledefinition = handRuledefinition;
		this.consideredCartsCount = consideredCartsCount;
		this.specificKinds = specificKinds;
		this.specificSuits = specificSuits;
		this.specificCards = specificCards;
	}

	public HandRuleDefinition getHandRuleDefinition() {
		return handRuledefinition;
	}

	public int getConsideredCartsCount() {
		return consideredCartsCount;
	}

	public ArrayList<Card.Kinds> getSpecificKinds() {
		return specificKinds;
	}

	public ArrayList<Card.Suits> getSpecificSuits() {
		return specificSuits;
	}

	public ArrayList<Card> getSpecificCards() {
		return specificCards;
	}

}
