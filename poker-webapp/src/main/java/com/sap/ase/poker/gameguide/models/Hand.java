package com.sap.ase.poker.gameguide.models;

import java.util.ArrayList;

public class Hand {
	private String name;
	private String description;
	private int rank;
	private ArrayList<HandRule> handRulesKind;
	private ArrayList<HandRule> handRulesSuit;

	public ArrayList<HandRule> getHandRulesKind() {
		return handRulesKind;
	}

	public void setHandRulesKind(ArrayList<HandRule> handRulesKind) {
		this.handRulesKind = handRulesKind;
	}

	public ArrayList<HandRule> getHandRulesSuit() {
		return handRulesSuit;
	}

	public void setHandRulesSuit(ArrayList<HandRule> handRulesSuit) {
		this.handRulesSuit = handRulesSuit;
	}

	public Hand() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}
