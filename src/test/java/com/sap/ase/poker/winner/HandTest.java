package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.KING;
import static com.sap.ase.poker.model.Card.Kind.QUEEN;
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
import static java.util.Arrays.asList;
import static java.util.Collections.reverseOrder;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.winner.Hand.HighCard;
import com.sap.ase.poker.winner.Hand.Pair;

public class HandTest {

	@Test
	public void highCard_firstCardHigher() throws Exception {

		HighCard hand1 = createHighCardHand(SPADES_6);
		HighCard hand2 = createHighCardHand(SPADES_7);

		Hand[] hands = new Hand[] { hand1, hand2 };
		Arrays.sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}

	@Test
	public void highCard_secondCardHigher() throws Exception {
		HighCard hand1 = createHighCardHand(SPADES_6, CLUBS_2);
		HighCard hand2 = createHighCardHand(DIAMONDS_6, SPADES_7);

		Hand[] hands = new Hand[] { hand1, hand2 };
		Arrays.sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}
	
	@Test
	public void highCard_pair() throws Exception {
		HighCard hand1 = createHighCardHand(SPADES_6, CLUBS_2);
		Pair hand2 = createPairHand(new KindGroup(KING, asList(DIAMONDS_KING, SPADES_KING)));

		Hand[] hands = new Hand[] { hand1, hand2 };
		Arrays.sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}
	
	@Test
	public void pair_betterPair() throws Exception {
		Pair hand1 = createPairHand(new KindGroup(QUEEN, Arrays.asList(DIAMONDS_QUEEN, SPADES_QUEEN)));
		Pair hand2 = createPairHand(new KindGroup(KING, Arrays.asList(DIAMONDS_KING, SPADES_KING)));

		Hand[] hands = new Hand[] { hand1, hand2 };
		Arrays.sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}
	
	@Test
	public void pair_samePair() throws Exception {
		Pair hand1 = createPairHand(new KindGroup(KING, asList(CLUBS_KING, HEARTS_KING)), DIAMONDS_6, CLUBS_2);
		Pair hand2 = createPairHand(new KindGroup(KING, asList(DIAMONDS_KING, SPADES_KING)), SPADES_6, SPADES_7);

		Hand[] hands = new Hand[] { hand1, hand2 };
		Arrays.sort(hands, reverseOrder());
		assertEquals(hand2, hands[0]);
	}
	
	private HighCard createHighCardHand(Card ...cards) {
		return new HighCard(asList(cards));
	}
	
	private Pair createPairHand(KindGroup pair, Card ... otherCards) {
		return new Pair(pair, asList(otherCards));
	}
}
