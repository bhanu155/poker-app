package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.CardFixtures.CLUBS_2;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_KING;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_6;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_KING;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_QUEEN;
import static com.sap.ase.poker.model.CardFixtures.HEARTS_KING;
import static com.sap.ase.poker.model.CardFixtures.SPADES_6;
import static com.sap.ase.poker.model.CardFixtures.SPADES_7;
import static com.sap.ase.poker.model.CardFixtures.SPADES_KING;
import static com.sap.ase.poker.model.CardFixtures.SPADES_QUEEN;
import static com.sap.ase.poker.winner.Hand.Type.HIGH_CARD;
import static com.sap.ase.poker.winner.Hand.Type.PAIR;
import static java.util.Arrays.asList;
import static java.util.Arrays.sort;
import static java.util.Collections.reverseOrder;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.ase.poker.model.Card;

public class HandTest {

	@Test
	public void highCard_firstCardHigher() throws Exception {
		Hand hand1 = createHighCardHand(SPADES_6);
		Hand hand2 = createHighCardHand(SPADES_7);

		Hand[] hands = new Hand[] { hand1, hand2 };
		sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}

	@Test
	public void highCard_secondCardHigher() throws Exception {
		Hand hand1 = createHighCardHand(SPADES_6, CLUBS_2);
		Hand hand2 = createHighCardHand(DIAMONDS_6, SPADES_7);

		Hand[] hands = new Hand[] { hand1, hand2 };
		sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}

	@Test
	public void highCard_pair() throws Exception {
		Hand hand1 = createHighCardHand(DIAMONDS_KING, CLUBS_2);
		Hand hand2 = new Hand(PAIR, asList(DIAMONDS_QUEEN, SPADES_QUEEN, DIAMONDS_6));

		Hand[] hands = new Hand[] { hand1, hand2 };
		sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}

	@Test
	public void pair_betterPair() throws Exception {
		Hand hand1 = new Hand(PAIR, asList(DIAMONDS_QUEEN, SPADES_QUEEN, DIAMONDS_6));
		Hand hand2 = new Hand(PAIR, asList(DIAMONDS_KING, SPADES_KING, SPADES_6));

		Hand[] hands = new Hand[] { hand1, hand2 };
		sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}

	@Test
	public void pair_samePair() throws Exception {
		Hand hand1 = new Hand(PAIR, asList(CLUBS_KING, HEARTS_KING, DIAMONDS_6, CLUBS_2));
		Hand hand2 = new Hand(PAIR, asList(DIAMONDS_KING, SPADES_KING, SPADES_6, SPADES_7));

		Hand[] hands = new Hand[] { hand1, hand2 };
		sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}

	private Hand createHighCardHand(Card... cards) {
		return new Hand(HIGH_CARD, asList(cards));
	}
}
