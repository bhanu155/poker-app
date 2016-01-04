package com.sap.ase.poker.winner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Card.Kind;
import com.sap.ase.poker.model.Card.Suit;

public class PlayerHandAggregateTest {
	@Test
	public void shouldReturnAggregateForRoyalFlush() {
		List<Card> royalFlush = Arrays.asList(TestCard.clubsAce, TestCard.clubsKing, TestCard.clubsQueen,
				TestCard.clubsJack, TestCard.clubs10, TestCard.clubs9, TestCard.clubs8);
		PlayerHandAggregate pHA = new PlayerHandAggregate(royalFlush);

		assertThat(pHA.getKindAggregates().size(), is(7));
		assertThat(pHA.getSuitWhichCanBeFoundFiveTimesOrMoreInPlayerCards(pHA.getSuitAggregates()), is(Suit.CLUBS));
		assertThat(pHA.areFiveCardsOfSameSuit(pHA.getSuitAggregates()), is(true));
		for (Map.Entry<Suit, List<Kind>> entry : pHA.getSuitAggregates().entrySet()) {
			if (entry.getKey() == Suit.CLUBS) {
				assertThat(entry.getValue().size(), is(7));
			} else {
				assertThat(entry.getValue().size(), is(0));
			}
		}
		assertThat(pHA.getLongestSequence().getHighestKind(), is(Kind.ACE));
		assertThat(pHA.getLongestSequence().getLength(), is(7));
		assertThat(pHA.getLongestSequence().getSuitOfSequenceWithSameSuit(), is(Suit.CLUBS));
		assertThat(pHA.getLongestSequence().getHighestKindOfSequenceWithSameSuit(), is(Kind.ACE));
		assertThat(pHA.getLongestSequence().isSameSuit(), is(true));
	}
}
