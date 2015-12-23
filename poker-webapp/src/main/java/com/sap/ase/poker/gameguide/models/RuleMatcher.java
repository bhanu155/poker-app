package com.sap.ase.poker.gameguide.models;

import java.util.ArrayList;

public class RuleMatcher implements Comparable<RuleMatcher> {
	private ArrayList<PlayerHandAggregate> potentialAggregates;
	private PlayerHandAggregate pHAUsedForHand;
	private HandRule handRule;

	public RuleMatcher() {
		this.potentialAggregates = new ArrayList<PlayerHandAggregate>();
		pHAUsedForHand = new PlayerHandAggregate();
	}

	@Override
	public int compareTo(RuleMatcher rM) {
		if (rM.getHandRule().getHandRuleDefinition().priority - this.handRule.getHandRuleDefinition().priority == 0) {
			return rM.getHandRule().getConsideredCartsCount() - this.getHandRule().getConsideredCartsCount();
		}
		return rM.getHandRule().getHandRuleDefinition().priority - this.handRule.getHandRuleDefinition().priority;
	}

	public PlayerHandAggregate getpHAUsedForHand() {
		return pHAUsedForHand;
	}

	public void setpHAUsedForHand(PlayerHandAggregate pHAUsedForHand) {
		this.pHAUsedForHand = pHAUsedForHand;
	}

	public ArrayList<PlayerHandAggregate> getPotentialAggregates() {
		return potentialAggregates;
	}

	public void setPotentialAggregates(ArrayList<PlayerHandAggregate> potentialAggregates) {
		this.potentialAggregates = potentialAggregates;
	}

	public HandRule getHandRule() {
		return handRule;
	}

	public void setHandRule(HandRule handRule) {
		this.handRule = handRule;
	}

}
