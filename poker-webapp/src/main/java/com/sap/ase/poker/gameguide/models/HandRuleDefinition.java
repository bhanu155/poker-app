package com.sap.ase.poker.gameguide.models;

public enum HandRuleDefinition {
	ANY(3), SPECIFICKINDS(1), SPECIFICSUITS(1), SAME(2), SEQUENTIALLYRANKED(1), DIFFERENT(2);

	public int priority;

	private HandRuleDefinition(int priority) {
		this.priority = priority;
	}
}
