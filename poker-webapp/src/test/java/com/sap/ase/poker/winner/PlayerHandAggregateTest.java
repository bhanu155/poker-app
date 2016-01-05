package com.sap.ase.poker.winner;

import static com.sap.ase.poker.winner.CardFixtures.CLUBS_10;
import static com.sap.ase.poker.winner.CardFixtures.CLUBS_8;
import static com.sap.ase.poker.winner.CardFixtures.CLUBS_9;
import static com.sap.ase.poker.winner.CardFixtures.CLUBS_ACE;
import static com.sap.ase.poker.winner.CardFixtures.CLUBS_JACK;
import static com.sap.ase.poker.winner.CardFixtures.CLUBS_KING;
import static com.sap.ase.poker.winner.CardFixtures.CLUBS_QUEEN;
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
		List<Card> royalFlush = Arrays.asList(CLUBS_ACE, CLUBS_KING, CLUBS_QUEEN, CLUBS_JACK, CLUBS_10, CLUBS_9,
				CLUBS_8);
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
