package com.sap.ase.poker.winner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;
import com.sap.ase.poker.winner.PokerHand.Definition;

public class PlayerHand {

	public PokerHand determineBestHandForCards(List<Card> playerCards) {
		PlayerHandAggregate pHA = new PlayerHandAggregate(playerCards);
		if (pHA.getLongestSequence() != null && pHA.getLongestSequence().getLength() >= 5) {
			if (pHA.getLongestSequence().isSameSuit()) {
				if (pHA.getLongestSequence().getHighestKindOfSequenceWithSameSuit() == Kind.ACE) {
					return createPokerHandOutOfSuitAggregates(PokerHand.Definition.ROYALFLUSH, pHA);
				} else {
					return createPokerHandOutOfSuitAggregates(PokerHand.Definition.STRAIGHTFLUSH, pHA);
				}
			} else {
				List<Card> cardsUsedForPokerHand = new ArrayList<Card>();
				List<Card> redundantCards = new ArrayList<Card>();
				Kind highestKind = pHA.getLongestSequence().getHighestKind();
				int indexOfHighestKind = -1;
				for (int i = 0; i < pHA.getKindAggregates().size(); i++) {
					KindAggregate kA = pHA.getKindAggregates().get(i);
					Kind kind = kA.getKind();
					if (kind == highestKind) {
						indexOfHighestKind = i;
					}
					if (indexOfHighestKind != -1 && i >= indexOfHighestKind && cardsUsedForPokerHand.size() < 5) {
						List<Card> dummy = createCardsOutOfKindAggregate(kA);
						if (dummy.size() > 1) {
							cardsUsedForPokerHand.addAll(dummy.subList(0, 1));
							redundantCards.addAll(dummy.subList(1, dummy.size()));
						} else {
							cardsUsedForPokerHand.addAll(dummy);
						}
					} else {
						redundantCards.addAll(createCardsOutOfKindAggregate(kA));
					}
				}
				return new PokerHand(PokerHand.Definition.STRAIGHT, cardsUsedForPokerHand, redundantCards);
			}
		} else {
			// FourOfAKind - 3
			// FullHouse - 4
			// ThreeOfAKind -7
			// TwoPair - 8
			// Pair - 9
			// Flush
			Suit s = pHA.getSuitWhichCanBeFoundFiveTimesOrMoreInPlayerCards(pHA.getSuitAggregates());
			if (s != null) {
				return createPokerHandOutOfSuitAggregates(PokerHand.Definition.FLUSH, pHA);
			}

			int indexThreeOfAKind = -1;
			int indexPairOne = -1;
			int indexPairTwo = -1;
			List<Card> cardsUsedForPokerHand = new ArrayList<Card>();
			List<Card> redundantCards = new ArrayList<Card>();
			Definition pHD = null;

			boolean pokerHandFound = false;
			for (int i = 0; i < pHA.getKindAggregates().size(); i++) {
				KindAggregate kA = pHA.getKindAggregates().get(i);
				int suitsCount = kA.getSuits().size();

				if (!pokerHandFound) {
					if (suitsCount == 4) {
						pHD = Definition.FOUROFAKIND;
						cardsUsedForPokerHand.addAll(createCardsOutOfKindAggregate(kA));
						pokerHandFound = true;
					} else if ((suitsCount >= 2 && indexThreeOfAKind != -1)
							|| (suitsCount == 3 && indexPairOne != -1)) {
						pHD = Definition.FULLHOUSE;
						cardsUsedForPokerHand
								.addAll(createCardsOutOfKindAggregate(pHA.getKindAggregates().get(indexThreeOfAKind)));
						List<Card> dummy = createCardsOutOfKindAggregate(kA);
						if (dummy.size() > 2) {
							cardsUsedForPokerHand.addAll(dummy.subList(0, 2));
							redundantCards.add(dummy.get(2));
						} else {
							cardsUsedForPokerHand.addAll(dummy);
						}
						pokerHandFound = true;
					} else if (suitsCount == 3 && indexThreeOfAKind == -1) {
						indexThreeOfAKind = i;
					} else if (suitsCount >= 2 && indexPairOne == -1) {
						indexPairOne = i;
					} else if (suitsCount >= 2 && indexPairTwo == -1) {
						indexPairTwo = i;
					} else {
						for (Suit suit : kA.getSuits()) {
							redundantCards.add(new Card(kA.getKind(), suit));
						}
					}
				} else {
					for (Suit suit : kA.getSuits()) {
						redundantCards.add(new Card(kA.getKind(), suit));
					}
				}
			}
			if (pokerHandFound) {
				return new PokerHand(pHD, cardsUsedForPokerHand, redundantCards);
			} else if (indexThreeOfAKind != -1) {
				pHD = Definition.THREEOFAKIND;
			} else if (indexPairOne != -1 && indexPairTwo != -1) {
				pHD = Definition.TWOPAIR;
			} else if (indexPairOne != -1) {
				pHD = Definition.PAIR;
			} else {
				return new PokerHand(Definition.NONE, cardsUsedForPokerHand, redundantCards);
			}

			for (int i = 0; i < pHA.getKindAggregates().size(); i++) {
				KindAggregate kA = pHA.getKindAggregates().get(i);
				if (i == indexThreeOfAKind || i == indexPairOne || i == indexPairTwo) {
					cardsUsedForPokerHand.addAll(createCardsOutOfKindAggregate(kA));
				} else {
					redundantCards.addAll(createCardsOutOfKindAggregate(kA));
				}
			}
			return new PokerHand(pHD, cardsUsedForPokerHand, redundantCards);
		}
	}

	private List<Card> createCardsOutOfKindAggregate(KindAggregate kA) {
		List<Card> cards = new ArrayList<Card>();
		for (Suit suit : kA.getSuits()) {
			cards.add(new Card(kA.getKind(), suit));
		}
		return cards;
	}

	private PokerHand createPokerHandOutOfSuitAggregates(PokerHand.Definition pHD, PlayerHandAggregate pHA) {
		List<Card> cardsUsedForPokerHand = new ArrayList<Card>();
		List<Card> redundantCards = new ArrayList<Card>();
		Suit suitReference;

		if (pHA.getLongestSequence().isSameSuit()) {
			// RoyalFlush, StraightFlush
			suitReference = pHA.getLongestSequence().getSuitOfSequenceWithSameSuit();
		} else {
			// Flush
			suitReference = pHA.getSuitWhichCanBeFoundFiveTimesOrMoreInPlayerCards(pHA.getSuitAggregates());
		}
		for (Map.Entry<Suit, List<Kind>> entry : pHA.getSuitAggregates().entrySet()) {
			for (Kind kind : entry.getValue()) {
				if (entry.getKey() == suitReference && cardsUsedForPokerHand.size() < 5) {
					cardsUsedForPokerHand.add(new Card(kind, entry.getKey()));
				} else {
					redundantCards.add(new Card(kind, entry.getKey()));
				}
			}
		}
		return new PokerHand(pHD, cardsUsedForPokerHand, redundantCards);
	}
}
