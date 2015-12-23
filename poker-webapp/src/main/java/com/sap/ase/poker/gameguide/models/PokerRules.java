package com.sap.ase.poker.gameguide.models;

import java.util.ArrayList;
import java.util.Collections;

public class PokerRules {

	private ArrayList<Hand> hands;

	public PokerRules() {
		hands = new ArrayList<Hand>();

		Hand rF = new Hand();
		rF.setName("Royal Flush");
		rF.setDescription("Five cards: 10, Jack, Queen, King, Ace of the same kind");
		rF.setRank(1);
		ArrayList<HandRule> hDKindRF = new ArrayList<HandRule>();
		ArrayList<Card.Kinds> specificKinds = new ArrayList<Card.Kinds>();
		specificKinds.add(Card.Kinds.TEN);
		specificKinds.add(Card.Kinds.JACK);
		specificKinds.add(Card.Kinds.QUEEN);
		specificKinds.add(Card.Kinds.KING);
		specificKinds.add(Card.Kinds.ACE);
		hDKindRF.add(new HandRule(HandRuleDefinition.SPECIFICKINDS, 5, specificKinds, null, null));
		rF.setHandRulesKind(hDKindRF);
		ArrayList<HandRule> hDSuitRF = new ArrayList<HandRule>();
		hDSuitRF.add(new HandRule(HandRuleDefinition.SAME, 5, null, null, null));
		rF.setHandRulesSuit(hDSuitRF);
		hands.add(rF);

		Hand sF = new Hand();
		sF.setName("Straight Flush");
		sF.setDescription("Five cards ranked sequentially in order in same suit");
		sF.setRank(2);
		ArrayList<HandRule> hDKindSF = new ArrayList<HandRule>();
		hDKindSF.add(new HandRule(HandRuleDefinition.SEQUENTIALLYRANKED, 5, null, null, null));
		sF.setHandRulesKind(hDKindSF);
		ArrayList<HandRule> hDSuitSF = new ArrayList<HandRule>();
		hDSuitSF.add(new HandRule(HandRuleDefinition.SAME, 5, null, null, null));
		sF.setHandRulesSuit(hDSuitSF);
		hands.add(sF);

		Hand fOK = new Hand();
		fOK.setName("Four of a kind");
		fOK.setDescription("Four cards of the same numerical rank");
		fOK.setRank(3);
		ArrayList<HandRule> hDKindFOK = new ArrayList<HandRule>();
		hDKindFOK.add(new HandRule(HandRuleDefinition.SAME, 4, null, null, null));
		fOK.setHandRulesKind(hDKindFOK);
		ArrayList<HandRule> hDSuitFOK = new ArrayList<HandRule>();
		hDSuitFOK.add(new HandRule(HandRuleDefinition.ANY, 4, null, null, null));
		fOK.setHandRulesSuit(hDSuitFOK);
		hands.add(fOK);

		Hand fH = new Hand();
		fH.setName("Full House");
		fH.setDescription(
				"Three cards of the same numerical rank combined with two other cards of the same numerical rank");
		fH.setRank(4);
		ArrayList<HandRule> hDKindFH = new ArrayList<HandRule>();
		hDKindFH.add(new HandRule(HandRuleDefinition.SAME, 2, null, null, null));
		hDKindFH.add(new HandRule(HandRuleDefinition.SAME, 3, null, null, null));
		fH.setHandRulesKind(hDKindFH);
		ArrayList<HandRule> hDSuitFH = new ArrayList<HandRule>();
		hDSuitFH.add(new HandRule(HandRuleDefinition.ANY, 2, null, null, null));
		hDSuitFH.add(new HandRule(HandRuleDefinition.ANY, 3, null, null, null));
		fH.setHandRulesSuit(hDSuitFH);
		hands.add(fH);

		Hand tP = new Hand();
		tP.setName("Two Pair");
		tP.setDescription(
				"Two cards of the same numerical rank combined with two other cards of the same numerical rank");
		tP.setRank(8);
		ArrayList<HandRule> hDKindTP = new ArrayList<HandRule>();
		hDKindTP.add(new HandRule(HandRuleDefinition.SAME, 2, null, null, null));
		hDKindTP.add(new HandRule(HandRuleDefinition.SAME, 2, null, null, null));
		tP.setHandRulesKind(hDKindTP);
		ArrayList<HandRule> hDSuitTP = new ArrayList<HandRule>();
		hDSuitTP.add(new HandRule(HandRuleDefinition.ANY, 2, null, null, null));
		hDSuitTP.add(new HandRule(HandRuleDefinition.ANY, 2, null, null, null));
		tP.setHandRulesSuit(hDSuitTP);
		hands.add(tP);
	}

	public PlayerHand getBestHandForPlayer(PlayerHand playerHand) {
		Collections.sort(playerHand.getCards());

		ArrayList<PlayerHandAggregate> pHAs = getAggregatePlayerHand(playerHand.getCards());

		for (PlayerHandAggregate pHA : pHAs) {
			System.out.println("Kind: " + pHA.getKind().value + " Count: " + pHA.getCountKinds());
			for (Card.Suits s : pHA.getSuits()) {
				System.out.println(" Suit: " + s);
			}
		}

		for (Hand hand : this.hands) {
			checkRulesFulfilled(pHAs, hand, playerHand);

			// if (checkRulesFulfilled(pHAs, hand, playerHand).size() != 0) {
			// // playerHand.setBestPossibleHand(hand);
			// // break;
			// }
		}

		return playerHand;
	}

	public ArrayList<PlayerHandAggregate> getAggregatePlayerHand(ArrayList<Card> cards) {

		PlayerHandAggregate pHA = new PlayerHandAggregate();
		ArrayList<PlayerHandAggregate> pHAs = new ArrayList<PlayerHandAggregate>();
		ArrayList<Card.Suits> suits = new ArrayList<Card.Suits>();

		Card.Kinds lastKind = null;
		int sameKindCount = 1;

		for (int i = 0; i < cards.size(); i++) {
			Card c = cards.get(i);
			if (lastKind == c.getKind()) {
				sameKindCount++;
			} else if (lastKind != c.getKind() && i != 0) {
				pHA.setCountKinds(sameKindCount);
				pHA.setSuits(suits);
				pHA.setKind(lastKind);
				pHAs.add(pHA);
				pHA = new PlayerHandAggregate();
				suits = new ArrayList<Card.Suits>();
				sameKindCount = 1;
			}
			if (i == cards.size() - 1) {
				suits.add(c.getSuit());
				pHA.setCountKinds(sameKindCount);
				pHA.setSuits(suits);
				pHA.setKind(c.getKind());
				pHAs.add(pHA);
				pHA = new PlayerHandAggregate();
				suits = new ArrayList<Card.Suits>();
			}
			lastKind = c.getKind();
			suits.add(c.getSuit());
		}
		return pHAs;
	}

	public ArrayList<Integer> checkRulesFulfilled(ArrayList<PlayerHandAggregate> pHAs, Hand hand,
			PlayerHand playerHand) {
		System.out.println("####### " + hand.getName());
		ArrayList<RuleMatcher> ruleMatchers = determineAggregatesWhichFitToRule(pHAs, hand);
		if (ruleMatchers == null) {
			return null;
		}
		Collections.sort(ruleMatchers);
		// sorted via priority and count of cards of the rule

		ArrayList<RuleMatcher> finalRuleMatchers = new ArrayList<RuleMatcher>();
		ArrayList<RuleMatcher> ruleMatchersToBeOptimized = new ArrayList<RuleMatcher>();

		for (int rMIteratorComp = 0; rMIteratorComp < ruleMatchers.size(); rMIteratorComp++) {
			// distribute RuleMatchers into two different Lists => final or
			// optimizeable
			RuleMatcher ruleMatcher = ruleMatchers.get(rMIteratorComp);
			Collections.sort(ruleMatcher.getPotentialAggregates());
			int potSize = ruleMatcher.getPotentialAggregates().size();

			if (potSize == 1 && finalRuleMatchers.contains(ruleMatcher)) {
				System.out.println("Rule cannot be fullfilled");
			} else if (potSize == 1 && !finalRuleMatchers.contains(ruleMatcher)) {
				ruleMatcher.setpHAUsedForHand(ruleMatcher.getPotentialAggregates().get(0));
				finalRuleMatchers.add(ruleMatcher);
			} else {
				ruleMatchersToBeOptimized.add(ruleMatcher);
			}
		}
		if (finalRuleMatchers.size() == ruleMatchers.size()) {
			// all rules are fulfilled
			return null;
		}

		this.optimateResult(finalRuleMatchers, ruleMatchersToBeOptimized);
		return null;

	}

	private ArrayList<RuleMatcher> determineAggregatesWhichFitToRule(ArrayList<PlayerHandAggregate> pHAs, Hand hand) {
		ArrayList<RuleMatcher> ruleMatchers = new ArrayList<RuleMatcher>();
		for (HandRule hRKind : hand.getHandRulesKind()) {
			// determine aggregates which fit to the rule
			// remark: highest card is at the beginning of the list
			RuleMatcher ruleMatcher = new RuleMatcher();
			ArrayList<PlayerHandAggregate> potentialAggregatesForHandRule = new ArrayList<PlayerHandAggregate>();
			ruleMatcher.setHandRule(hRKind);

			switch (hRKind.getHandRuleDefinition()) {
			case ANY:
				break;
			case SPECIFICKINDS:
				break;
			case SAME:
				for (int pHAIndex = 0; pHAIndex < pHAs.size(); pHAIndex++) {
					PlayerHandAggregate pHA = pHAs.get(pHAIndex);
					if (pHA.getCountKinds() >= hRKind.getConsideredCartsCount()) {
						potentialAggregatesForHandRule.add(pHA);
					}
				}
				break;
			case SEQUENTIALLYRANKED:
				break;
			default:
				break;
			}
			ruleMatcher.setPotentialAggregates(potentialAggregatesForHandRule);
			if (ruleMatcher.getPotentialAggregates().size() == 0) {
				System.out.println("Kein Aggregat für die Regel gefunden: " + hRKind.getHandRuleDefinition());
				return null;
			}
			ruleMatchers.add(ruleMatcher);
		}
		for (RuleMatcher rm : ruleMatchers) {
			System.out.println(rm.getHandRule().getHandRuleDefinition() + " | CardsCount: "
					+ rm.getHandRule().getConsideredCartsCount());
			for (PlayerHandAggregate pHA : rm.getPotentialAggregates()) {
				System.out.println("   " + pHA.getKind());
			}
		}
		return ruleMatchers;
	}

	private void optimateResult(ArrayList<RuleMatcher> finalRuleMatchers,
			ArrayList<RuleMatcher> ruleMatchersToBeOptimized) {

		// - braucht eine andere Regel diese Karte? Wenn ja, wie viele?
		// - eine regel hat eine priorität
		// - karten haben einen rank

		for (int i = 0; i < ruleMatchersToBeOptimized.size(); i++) {
			RuleMatcher ruleMatcherToOpt = ruleMatchersToBeOptimized.get(i);
			for (PlayerHandAggregate pHAPotential : ruleMatcherToOpt.getPotentialAggregates()) {
				int count = 0;
				for (int j = 0; j < finalRuleMatchers.size(); j++) {
					RuleMatcher ruleMatcherComp = finalRuleMatchers.get(j);
					if (ruleMatcherComp.getpHAUsedForHand().getKind() == pHAPotential.getKind()) {
						count = count + ruleMatcherComp.getpHAUsedForHand().getCountKinds();
					}
				}
				if (pHAPotential.getSuits().size() - count >= 0) {
					// es sind noch kinds übrig
				} else if (pHAPotential.getSuits().size() - count < 0
						&& ruleMatcherToOpt.getPotentialAggregates().size() == 2) {
					// wenn zur erfüllung einer regel zwei aggregate zur
					// verfügung stehen, aber eines davon bereits vollständig
					// für eine
					// andere regel verwendet wurde und keine karten zur
					// erfüllung der aktuelle regel verfügbar sind ==> dann
					// bleibt nur noch übrig, das andere aggregat zu nutzen
					ruleMatcherToOpt.setpHAUsedForHand(pHAPotential);
					finalRuleMatchers.add(ruleMatcherToOpt);
					System.out.println("zweites aggregat wurde zwangs-gesetzt");
				}
			}
		}

		for (int i = 0; i < ruleMatchersToBeOptimized.size(); i++) {
			RuleMatcher ruleMatcher = ruleMatchersToBeOptimized.get(i);
			for (int j = 0; j < ruleMatchersToBeOptimized.size(); j++) {
				RuleMatcher ruleMatcherComp = ruleMatchersToBeOptimized.get(j);
				// hat eine andere regel auch anspruch, wenn ja gewinnt die prio

				for (int pHAIndex = 0; pHAIndex < ruleMatcher.getPotentialAggregates().size(); pHAIndex++) {
					PlayerHandAggregate pHA = ruleMatcher.getPotentialAggregates().get(pHAIndex);
					for (int pHACompIndex = 0; pHACompIndex < ruleMatcherComp.getPotentialAggregates()
							.size(); pHACompIndex++) {
						PlayerHandAggregate pHAComp = ruleMatcherComp.getPotentialAggregates().get(pHACompIndex);

						if (i >= j) {
							// i hat vorang
						} else {
							// j hat vorang
						}

						HandRuleDefinition hRD = ruleMatcher.getHandRule().getHandRuleDefinition();
						HandRuleDefinition hRDComp = ruleMatcherComp.getHandRule().getHandRuleDefinition();

						if (hRD == hRDComp) {

						} else {
							if (pHA.getKind() == pHAComp.getKind()) {
								// beide wollen dasselbe aggregat
								if (hRD.priority == hRDComp.priority) {
									// beide regeln haben dieselbe prio
								} else if (hRD.priority > hRDComp.priority) {
									// regel von rulematcher ist höher
								} else {
									// regel von rulematcher ist niedriger
								}
							}
						}

						if (pHACompIndex == pHAIndex - 1) {
							pHACompIndex++;
						}
					}
				}
				if (j == i - 1)
					j++;
			}
		}
	}
}
