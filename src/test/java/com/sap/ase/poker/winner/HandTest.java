package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.CardFixtures.*;
import static com.sap.ase.poker.winner.Hand.Type.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.ase.poker.winner.Hand.Type;

public class HandTest {

	@Test
	public void typesCompleteAndOrderedAndCanParse() throws Exception {
		assertEquals(Type.valueOf("HIGH_CARD"), Type.values()[0]);
		assertEquals(Type.valueOf("PAIR"), Type.values()[1]);
		assertEquals(Type.valueOf("TWO_PAIRS"), Type.values()[2]);
		assertEquals(Type.valueOf("THREE_OF_A_KIND"), Type.values()[3]);
		assertEquals(Type.valueOf("STRAIGHT"), Type.values()[4]);
		assertEquals(Type.valueOf("FLUSH"), Type.values()[5]);
		assertEquals(Type.valueOf("FULL_HOUSE"), Type.values()[6]);
		assertEquals(Type.valueOf("FOUR_OF_A_KIND"), Type.values()[7]);
		assertEquals(Type.valueOf("STRAIGHT_FLUSH"), Type.values()[8]);
		assertEquals(Type.valueOf("ROYAL_FLUSH"), Type.values()[9]);
	}

	@Test
	public void betterHighCard_onFirstCard() throws Exception {
		Hand hand1 = new Hand(HIGH_CARD, asList(SPADES_6));
		Hand hand2 = new Hand(HIGH_CARD, asList(SPADES_7));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void betterHighCard_onSecondCard() throws Exception {
		Hand hand1 = new Hand(HIGH_CARD, asList(SPADES_6, CLUBS_2));
		Hand hand2 = new Hand(HIGH_CARD, asList(SPADES_7, DIAMONDS_6));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void sameHighCard() throws Exception {
		Hand hand1 = new Hand(HIGH_CARD, asList(SPADES_6));
		Hand hand2 = new Hand(HIGH_CARD, asList(HEARTS_6));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	@Test
	public void highCard_pair() throws Exception {
		Hand hand1 = new Hand(HIGH_CARD, asList(DIAMONDS_KING, CLUBS_2));
		Hand hand2 = new Hand(PAIR, asList(DIAMONDS_QUEEN, SPADES_QUEEN));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void betterPair() throws Exception {
		Hand hand1 = new Hand(PAIR, asList(DIAMONDS_QUEEN, SPADES_QUEEN, DIAMONDS_6));
		Hand hand2 = new Hand(PAIR, asList(DIAMONDS_KING, SPADES_KING, SPADES_6));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void betterPair_onKicker() throws Exception {
		Hand hand1 = new Hand(PAIR, asList(CLUBS_KING, HEARTS_KING, DIAMONDS_6, CLUBS_2));
		Hand hand2 = new Hand(PAIR, asList(DIAMONDS_KING, SPADES_KING, SPADES_7, SPADES_6));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void samePair() throws Exception {
		Hand hand1 = new Hand(PAIR, asList(CLUBS_KING, HEARTS_KING, CLUBS_7, DIAMONDS_6));
		Hand hand2 = new Hand(PAIR, asList(DIAMONDS_KING, SPADES_KING, SPADES_7, SPADES_6));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	@Test
	public void pair_twoPairs() throws Exception {
		Hand hand1 = new Hand(PAIR, asList(CLUBS_KING, HEARTS_KING, CLUBS_8, CLUBS_7, HEARTS_6));
		Hand hand2 = new Hand(TWO_PAIRS, asList(DIAMONDS_KING, SPADES_KING, SPADES_7, DIAMONDS_6, SPADES_6));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void betterTwoPairs_onFirstPair() throws Exception {
		Hand hand1 = new Hand(TWO_PAIRS, asList(CLUBS_KING, HEARTS_KING, CLUBS_6, HEARTS_6));
		Hand hand2 = new Hand(TWO_PAIRS, asList(DIAMONDS_ACE, SPADES_ACE, DIAMONDS_6, SPADES_6));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void betterTwoPairs_onSecondPair() throws Exception {
		Hand hand1 = new Hand(TWO_PAIRS, asList(CLUBS_KING, HEARTS_KING, CLUBS_6, HEARTS_6));
		Hand hand2 = new Hand(TWO_PAIRS, asList(DIAMONDS_KING, SPADES_KING, DIAMONDS_7, SPADES_7));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void betterTwoPairs_onKicker() throws Exception {
		Hand hand1 = new Hand(TWO_PAIRS, asList(CLUBS_KING, HEARTS_KING, CLUBS_6, HEARTS_6, CLUBS_5));
		Hand hand2 = new Hand(TWO_PAIRS, asList(DIAMONDS_KING, SPADES_KING, SPADES_7, DIAMONDS_6, SPADES_6));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void sameTwoPairs() throws Exception {
		Hand hand1 = new Hand(TWO_PAIRS, asList(CLUBS_KING, HEARTS_KING, CLUBS_7, CLUBS_6, HEARTS_6));
		Hand hand2 = new Hand(TWO_PAIRS, asList(DIAMONDS_KING, SPADES_KING, SPADES_7, DIAMONDS_6, SPADES_6));
		assertTrue(hand1.compareTo(hand2) == 0);
	}

	@Test
	public void twoPairs_threeOfAKind() throws Exception {
		Hand hand1 = new Hand(TWO_PAIRS, asList(CLUBS_ACE, HEARTS_ACE, CLUBS_KING, HEARTS_KING, CLUBS_7));
		Hand hand2 = new Hand(THREE_OF_A_KIND,
				asList(DIAMONDS_QUEEN, SPADES_QUEEN, DIAMONDS_QUEEN, SPADES_7, SPADES_6));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void threeOfAKind_straight() throws Exception {
		Hand hand1 = new Hand(THREE_OF_A_KIND,
				asList(HEARTS_ACE, DIAMONDS_QUEEN, SPADES_QUEEN, DIAMONDS_QUEEN, CLUBS_7));
		Hand hand2 = new Hand(STRAIGHT, asList(DIAMONDS_9, SPADES_8, SPADES_7, SPADES_6, DIAMONDS_5));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void betterStraight() throws Exception {
		Hand hand1 = new Hand(STRAIGHT, asList(DIAMONDS_5, SPADES_4, DIAMONDS_3, CLUBS_2, HEARTS_ACE));
		Hand hand2 = new Hand(STRAIGHT, asList(SPADES_6, SPADES_5, DIAMONDS_4, SPADES_3, SPADES_2));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void straight_flush() throws Exception {
		Hand hand1 = new Hand(STRAIGHT, asList(DIAMONDS_10, CLUBS_9, DIAMONDS_8, CLUBS_7, DIAMONDS_6));
		Hand hand2 = new Hand(FLUSH, asList(HEARTS_9, HEARTS_8, HEARTS_7, HEARTS_4, HEARTS_3));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void flush_fullHouse() throws Exception {
		Hand hand1 = new Hand(FLUSH, asList(HEARTS_9, HEARTS_8, HEARTS_7, HEARTS_4, HEARTS_3));
		Hand hand2 = new Hand(FULL_HOUSE, asList(CLUBS_4, DIAMONDS_4, SPADES_4, CLUBS_3, SPADES_3));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void fullHouse_fourOfAKind() throws Exception {
		Hand hand1 = new Hand(FULL_HOUSE, asList(CLUBS_9, DIAMONDS_9, SPADES_9, CLUBS_3, SPADES_3));
		Hand hand2 = new Hand(FOUR_OF_A_KIND, asList(HEARTS_8, SPADES_8, CLUBS_8, DIAMONDS_8, HEARTS_3));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void fourOfAKind_straightFlush() throws Exception {
		Hand hand1 = new Hand(FOUR_OF_A_KIND, asList(HEARTS_8, SPADES_8, CLUBS_8, DIAMONDS_8, HEARTS_3));
		Hand hand2 = new Hand(STRAIGHT_FLUSH, asList(CLUBS_KING, CLUBS_QUEEN, CLUBS_JACK, CLUBS_10, CLUBS_9));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

	@Test
	public void straightFlush_royalFlush() throws Exception {
		Hand hand1 = new Hand(STRAIGHT_FLUSH, asList(CLUBS_KING, CLUBS_QUEEN, CLUBS_JACK, CLUBS_10, CLUBS_9));
		Hand hand2 = new Hand(ROYAL_FLUSH, asList(SPADES_ACE, SPADES_KING, SPADES_QUEEN, SPADES_JACK, SPADES_10));
		assertTrue(hand1.compareTo(hand2) < 0);
	}

}
