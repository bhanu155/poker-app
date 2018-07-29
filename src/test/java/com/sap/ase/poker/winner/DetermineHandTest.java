package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.TWO;
import static com.sap.ase.poker.model.Card.Suit.CLUBS;
import static com.sap.ase.poker.model.CardFixtures.*;
import static com.sap.ase.poker.winner.Hand.Type.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.winner.IllegalHand.DuplicateCards;
import com.sap.ase.poker.winner.IllegalHand.IllegalNumberOfCards;

public class DetermineHandTest {

	@Test
	public void illegalNumberOfCards() throws Exception {
		try {
			new DetermineHand().execute();
			fail("0 cards should be illegal for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(0, e.numberOfCards);
		}
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_3, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7);
			fail("6 cards should be illegal for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(6, e.numberOfCards);
		}
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_3, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7, CLUBS_8, CLUBS_9);
			fail("8 cards should be illegal for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(8, e.numberOfCards);
		}
	}

	@Test
	public void duplicateCards() throws Exception {
		try {
			new DetermineHand().execute(CLUBS_2, new Card(TWO, CLUBS), CLUBS_3, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7);
			fail("duplicate cards should be illegal for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_2, e.duplicateCard);
		}
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_3, CLUBS_2, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7);
			fail("duplicate cards should be illegal for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_2, e.duplicateCard);
		}
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_7, CLUBS_3, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7);
			fail("duplicate cards should be illegal for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_7, e.duplicateCard);
		}
	}

	@Test
	public void highCard() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_KING, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9,
				SPADES_JACK);
		assertEquals(HIGH_CARD, result.type);
		assertHandCards(SPADES_KING, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, result);
	}

	@Test
	public void pair() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_4, DIAMONDS_9,
				SPADES_JACK);
		assertEquals(PAIR, result.type);
		assertHandCards(CLUBS_4, SPADES_4, SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, result);
	}

	@Test
	public void twoPairs() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_4, DIAMONDS_QUEEN,
				SPADES_JACK);
		assertEquals(TWO_PAIRS, result.type);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, CLUBS_4, SPADES_4, SPADES_JACK, result);
	}

	@Test
	public void twoPairs_outOfThree() throws Exception {
		Hand result = new DetermineHand().execute(SPADES_4, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_10,
				DIAMONDS_QUEEN, SPADES_JACK);
		assertEquals(TWO_PAIRS, result.type);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, DIAMONDS_10, SPADES_10, SPADES_JACK, result);
	}

	@Test
	public void threeOfAKind() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_4, HEARTS_4,
				SPADES_JACK);
		assertEquals(THREE_OF_A_KIND, result.type);
		assertHandCards(CLUBS_4, SPADES_4, HEARTS_4, SPADES_QUEEN, SPADES_JACK, result);
	}

	@Test
	public void fourOfAKind() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_2, CLUBS_4, DIAMONDS_4, DIAMONDS_10, SPADES_4, HEARTS_4,
				SPADES_JACK);
		assertEquals(FOUR_OF_A_KIND, result.type);
		assertHandCards(CLUBS_4, DIAMONDS_4, SPADES_4, HEARTS_4, SPADES_JACK, result);
	}

	@Test
	public void fullHouse() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_2, CLUBS_4, DIAMONDS_4, DIAMONDS_QUEEN, SPADES_4, SPADES_QUEEN,
				SPADES_JACK);
		assertEquals(FULL_HOUSE, result.type);
		assertHandCards(CLUBS_4, DIAMONDS_4, SPADES_4, DIAMONDS_QUEEN, SPADES_QUEEN, result);
	}

	@Test
	public void fullHouse_twoThreeOfAKind() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_QUEEN, SPADES_4, HEARTS_4,
				CLUBS_QUEEN);
		assertEquals(FULL_HOUSE, result.type);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, CLUBS_QUEEN, CLUBS_4, SPADES_4, result);
	}

	@Test
	public void straight() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_4, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
				SPADES_QUEEN, DIAMONDS_ACE);
		assertEquals(STRAIGHT, result.type);
		assertHandCards(SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, result);
	}

	@Test
	public void straightWithSixCards() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_4, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
				SPADES_QUEEN, DIAMONDS_KING);
		assertEquals(STRAIGHT, result.type);
		assertHandCards(DIAMONDS_KING, SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, result);
	}

	@Test
	public void straightWithPair() throws Exception {
		Hand result = new DetermineHand().execute(SPADES_KING, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
				SPADES_QUEEN, DIAMONDS_KING);
		assertEquals(STRAIGHT, result.type);
		assertHandCards(SPADES_KING, SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, result);
	}

	@Test
	public void falseStraight() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_4, CLUBS_2, DIAMONDS_8, DIAMONDS_ACE, SPADES_JACK, SPADES_QUEEN,
				DIAMONDS_KING);
		assertEquals(HIGH_CARD, result.type);
		assertHandCards(DIAMONDS_ACE, DIAMONDS_KING, SPADES_QUEEN, SPADES_JACK, DIAMONDS_8, result);
	}

	@Test
	public void smallStraight() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_3, CLUBS_2, DIAMONDS_4, DIAMONDS_ACE, SPADES_5, SPADES_QUEEN,
				DIAMONDS_KING);
		assertEquals(STRAIGHT, result.type);
		assertHandCards(SPADES_5, DIAMONDS_4, CLUBS_3, CLUBS_2, DIAMONDS_ACE, result);
	}

	@Test
	public void flush() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_4, DIAMONDS_9, DIAMONDS_4, DIAMONDS_8, DIAMONDS_10,
				DIAMONDS_QUEEN, SPADES_KING);
		assertEquals(FLUSH, result.type);
		assertHandCards(DIAMONDS_QUEEN, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, DIAMONDS_4, result);
	}

	@Test
	public void flushWithSixCards() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_4, DIAMONDS_9, DIAMONDS_4, DIAMONDS_8, DIAMONDS_10,
				DIAMONDS_QUEEN, DIAMONDS_KING);
		assertEquals(FLUSH, result.type);
		assertHandCards(DIAMONDS_KING, DIAMONDS_QUEEN, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, result);
	}

	@Test
	public void straightFlush() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_4, DIAMONDS_9, DIAMONDS_4, DIAMONDS_8, DIAMONDS_10,
				DIAMONDS_QUEEN, DIAMONDS_JACK);
		assertEquals(STRAIGHT_FLUSH, result.type);
		assertHandCards(DIAMONDS_QUEEN, DIAMONDS_JACK, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, result);
	}

	@Test
	public void smallStraightFlush() throws Exception {
		Hand result = new DetermineHand().execute(DIAMONDS_2, DIAMONDS_ACE, DIAMONDS_4, DIAMONDS_3, DIAMONDS_5,
				DIAMONDS_QUEEN, DIAMONDS_JACK);
		assertEquals(STRAIGHT_FLUSH, result.type);
		assertHandCards(DIAMONDS_5, DIAMONDS_4, DIAMONDS_3, DIAMONDS_2, DIAMONDS_ACE, result);
	}

	@Test
	public void straightFlushWithSixCards() throws Exception {
		Hand result = new DetermineHand().execute(DIAMONDS_2, DIAMONDS_ACE, DIAMONDS_4, DIAMONDS_3, DIAMONDS_5,
				DIAMONDS_QUEEN, DIAMONDS_6);
		assertEquals(STRAIGHT_FLUSH, result.type);
		assertHandCards(DIAMONDS_6, DIAMONDS_5, DIAMONDS_4, DIAMONDS_3, DIAMONDS_2, result);
	}

	@Test
	public void straightFlushWithHigherNonFlushStraight() throws Exception {
		Hand result = new DetermineHand().execute(DIAMONDS_2, DIAMONDS_ACE, DIAMONDS_4, DIAMONDS_3, DIAMONDS_5,
				DIAMONDS_QUEEN, SPADES_6);
		assertEquals(STRAIGHT_FLUSH, result.type);
		assertHandCards(DIAMONDS_5, DIAMONDS_4, DIAMONDS_3, DIAMONDS_2, DIAMONDS_ACE, result);
	}

	@Test
	public void straightFlushWithHigherNonStraightFlush() throws Exception {
		Hand result = new DetermineHand().execute(DIAMONDS_2, DIAMONDS_ACE, DIAMONDS_4, DIAMONDS_3, DIAMONDS_5,
				DIAMONDS_QUEEN, DIAMONDS_8);
		assertEquals(STRAIGHT_FLUSH, result.type);
		assertHandCards(DIAMONDS_5, DIAMONDS_4, DIAMONDS_3, DIAMONDS_2, DIAMONDS_ACE, result);
	}

	@Test
	public void royalFlush() throws Exception {
		Hand result = new DetermineHand().execute(CLUBS_4, DIAMONDS_9, DIAMONDS_ACE, DIAMONDS_KING, DIAMONDS_10,
				DIAMONDS_QUEEN, DIAMONDS_JACK);
		assertEquals(ROYAL_FLUSH, result.type);
		assertHandCards(DIAMONDS_ACE, DIAMONDS_KING, DIAMONDS_QUEEN, DIAMONDS_JACK, DIAMONDS_10, result);
	}

	private void assertHandCards(Card card1, Card card2, Card card3, Card card4, Card card5, Hand result) {
		assertEquals(5, result.cards.size());
		assertEquals(card1, result.cards.get(0));
		assertEquals(card2, result.cards.get(1));
		assertEquals(card3, result.cards.get(2));
		assertEquals(card4, result.cards.get(3));
		assertEquals(card5, result.cards.get(4));
	}
}
