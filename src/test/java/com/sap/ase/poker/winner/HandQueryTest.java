package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.Card.Kind.TWO;
import static com.sap.ase.poker.model.Card.Suit.CLUBS;
import static com.sap.ase.poker.model.CardFixtures.*;
import static com.sap.ase.poker.winner.Hand.Type.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.winner.IllegalHand.DuplicateCards;
import com.sap.ase.poker.winner.IllegalHand.IllegalNumberOfCards;

@RunWith(Parameterized.class)
public class HandQueryTest {

	@Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1000][0];
    }
	
	@Test
	public void illegalNumberOfCards() throws Exception {
		try {
			findBestHand();
			fail("0 cards should be illegal for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(0, e.numberOfCards);
		}
		try {
			findBestHand(CLUBS_2, CLUBS_3, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7);
			fail("6 cards should be illegal for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(6, e.numberOfCards);
		}
		try {
			findBestHand(CLUBS_2, CLUBS_3, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7, CLUBS_8, CLUBS_9);
			fail("8 cards should be illegal for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(8, e.numberOfCards);
		}
	}

	@Test
	public void duplicateCards() throws Exception {
		try {
			findBestHand(CLUBS_2, new Card(TWO, CLUBS), CLUBS_3, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7);
			fail("duplicate cards should be illegal for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_2, e.duplicateCard);
		}
		try {
			findBestHand(CLUBS_2, CLUBS_3, CLUBS_2, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7);
			fail("duplicate cards should be illegal for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_2, e.duplicateCard);
		}
		try {
			findBestHand(CLUBS_2, CLUBS_7, CLUBS_3, CLUBS_4, CLUBS_5, CLUBS_6, CLUBS_7);
			fail("duplicate cards should be illegal for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_7, e.duplicateCard);
		}
	}

	@Test
	public void highCard() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_4, SPADES_10, CLUBS_9, CLUBS_6, HEARTS_8, SPADES_JACK);
		assertEquals(HIGH_CARD, hand.type);
		assertHandCards(SPADES_JACK, SPADES_10, CLUBS_9, HEARTS_8, CLUBS_6, hand);
	}

	@Test
	public void pair() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_4, SPADES_10, HEARTS_8, SPADES_4, DIAMONDS_9, SPADES_3);
		assertEquals(PAIR, hand.type);
		assertHandCards(CLUBS_4, SPADES_4, SPADES_10, DIAMONDS_9, HEARTS_8, hand);
	}

	@Test
	public void twoPairs() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_4, DIAMONDS_QUEEN, SPADES_JACK);
		assertEquals(TWO_PAIRS, hand.type);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, CLUBS_4, SPADES_4, SPADES_JACK, hand);
	}

	@Test
	public void twoPairs_outOfThree() throws Exception {
		Hand hand = findBestHand(SPADES_4, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_10, DIAMONDS_QUEEN, SPADES_JACK);
		assertEquals(TWO_PAIRS, hand.type);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, DIAMONDS_10, SPADES_10, SPADES_JACK, hand);
	}

	@Test
	public void threeOfAKind() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_4, HEARTS_4, SPADES_JACK);
		assertEquals(THREE_OF_A_KIND, hand.type);
		assertHandCards(CLUBS_4, SPADES_4, HEARTS_4, SPADES_QUEEN, SPADES_JACK, hand);
	}

	@Test
	public void fourOfAKind() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_4, DIAMONDS_4, DIAMONDS_10, SPADES_4, HEARTS_4, SPADES_JACK);
		assertEquals(FOUR_OF_A_KIND, hand.type);
		assertHandCards(CLUBS_4, DIAMONDS_4, SPADES_4, HEARTS_4, SPADES_JACK, hand);
	}

	@Test
	public void fullHouse() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_4, HEARTS_4, CLUBS_QUEEN, SPADES_4, SPADES_QUEEN, SPADES_3);
		assertEquals(FULL_HOUSE, hand.type);
		assertHandCards(CLUBS_4, HEARTS_4, SPADES_4, CLUBS_QUEEN, SPADES_QUEEN, hand);
	}

	@Test
	public void fullHouse_twoThreeOfAKind() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_QUEEN, SPADES_4, HEARTS_4, CLUBS_QUEEN);
		assertEquals(FULL_HOUSE, hand.type);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, CLUBS_QUEEN, CLUBS_4, SPADES_4, hand);
	}

	@Test
	public void straight() throws Exception {
		Hand hand = findBestHand(CLUBS_4, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK, SPADES_QUEEN, DIAMONDS_ACE);
		assertEquals(STRAIGHT, hand.type);
		assertHandCards(SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, hand);
	}

	@Test
	public void straightWithSixCards() throws Exception {
		Hand hand = findBestHand(CLUBS_4, HEARTS_10, HEARTS_8, HEARTS_9, SPADES_JACK, SPADES_QUEEN, HEARTS_KING);
		assertEquals(STRAIGHT, hand.type);
		assertHandCards(HEARTS_KING, SPADES_QUEEN, SPADES_JACK, HEARTS_10, HEARTS_9, hand);
	}

	@Test
	public void straightWithPair() throws Exception {
		Hand hand = findBestHand(SPADES_KING, CLUBS_10, CLUBS_8, CLUBS_9, SPADES_JACK, SPADES_QUEEN, CLUBS_KING);
		assertEquals(STRAIGHT, hand.type);
		assertHandCards(SPADES_KING, SPADES_QUEEN, SPADES_JACK, CLUBS_10, CLUBS_9, hand);
	}

	@Test
	public void falseStraight() throws Exception {
		Hand hand = findBestHand(CLUBS_4, CLUBS_2, DIAMONDS_8, DIAMONDS_ACE, SPADES_JACK, SPADES_QUEEN, DIAMONDS_KING);
		assertEquals(HIGH_CARD, hand.type);
		assertHandCards(DIAMONDS_ACE, DIAMONDS_KING, SPADES_QUEEN, SPADES_JACK, DIAMONDS_8, hand);
	}

	@Test
	public void smallStraight() throws Exception {
		Hand hand = findBestHand(CLUBS_3, CLUBS_2, DIAMONDS_4, DIAMONDS_ACE, SPADES_5, SPADES_QUEEN, DIAMONDS_KING);
		assertEquals(STRAIGHT, hand.type);
		assertHandCards(SPADES_5, DIAMONDS_4, CLUBS_3, CLUBS_2, DIAMONDS_ACE, hand);
	}

	@Test
	public void flush() throws Exception {
		Hand hand = findBestHand(CLUBS_4, DIAMONDS_9, DIAMONDS_4, DIAMONDS_8, DIAMONDS_10, DIAMONDS_QUEEN, SPADES_KING);
		assertEquals(FLUSH, hand.type);
		assertHandCards(DIAMONDS_QUEEN, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, DIAMONDS_4, hand);
	}

	@Test
	public void flushWithSixCards() throws Exception {
		Hand hand = findBestHand(SPADES_4, CLUBS_9, CLUBS_4, CLUBS_8, CLUBS_10, CLUBS_QUEEN, CLUBS_KING);
		assertEquals(FLUSH, hand.type);
		assertHandCards(CLUBS_KING, CLUBS_QUEEN, CLUBS_10, CLUBS_9, CLUBS_8, hand);
	}

	@Test
	public void straightFlush() throws Exception {
		Hand hand = findBestHand(SPADES_4, CLUBS_9, CLUBS_4, CLUBS_8, CLUBS_10, CLUBS_QUEEN, CLUBS_JACK);
		assertEquals(STRAIGHT_FLUSH, hand.type);
		assertHandCards(CLUBS_QUEEN, CLUBS_JACK, CLUBS_10, CLUBS_9, CLUBS_8, hand);
	}

	@Test
	public void smallStraightFlush() throws Exception {
		Hand hand = findBestHand(CLUBS_QUEEN, CLUBS_2, CLUBS_ACE, CLUBS_4, CLUBS_3, CLUBS_5, CLUBS_JACK);
		assertEquals(STRAIGHT_FLUSH, hand.type);
		assertHandCards(CLUBS_5, CLUBS_4, CLUBS_3, CLUBS_2, CLUBS_ACE, hand);
	}

	@Test
	public void straightFlushWithSixCards() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_ACE, CLUBS_4, CLUBS_3, CLUBS_5, CLUBS_QUEEN, CLUBS_6);
		assertEquals(STRAIGHT_FLUSH, hand.type);
		assertHandCards(CLUBS_6, CLUBS_5, CLUBS_4, CLUBS_3, CLUBS_2, hand);
	}

	@Test
	public void straightFlushWithHigherNonFlushStraight() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_ACE, CLUBS_4, CLUBS_3, CLUBS_5, CLUBS_QUEEN, SPADES_6);
		assertEquals(STRAIGHT_FLUSH, hand.type);
		assertHandCards(CLUBS_5, CLUBS_4, CLUBS_3, CLUBS_2, CLUBS_ACE, hand);
	}

	@Test
	public void straightFlushWithHigherNonStraightFlush() throws Exception {
		Hand hand = findBestHand(CLUBS_2, CLUBS_ACE, CLUBS_4, CLUBS_3, CLUBS_5, CLUBS_QUEEN, CLUBS_8);
		assertEquals(STRAIGHT_FLUSH, hand.type);
		assertHandCards(CLUBS_5, CLUBS_4, CLUBS_3, CLUBS_2, CLUBS_ACE, hand);
	}

	@Test
	public void royalFlush() throws Exception {
		Hand hand = findBestHand(SPADES_4, CLUBS_9, CLUBS_ACE, CLUBS_KING, CLUBS_10, CLUBS_QUEEN, CLUBS_JACK);
		assertEquals(ROYAL_FLUSH, hand.type);
		assertHandCards(CLUBS_ACE, CLUBS_KING, CLUBS_QUEEN, CLUBS_JACK, CLUBS_10, hand);
	}

	private Hand findBestHand(Card... cards) throws IllegalHand {
		return new HandQuery2().findBestHand(cards);
	}

	private void assertHandCards(Card card1, Card card2, Card card3, Card card4, Card card5, Hand hand) {
		assertEquals(5, hand.cards.length);
		assertEquals(card1, hand.cards[0]);
		assertEquals(card2, hand.cards[1]);
		assertEquals(card3, hand.cards[2]);
		assertEquals(card4, hand.cards[3]);
		assertEquals(card5, hand.cards[4]);
	}

//	private void assertHandCards(Card card1, Card card2, Card card3, Card card4, Card card5, Hand hand) {
//		assertEquals(5, hand.cards.size());
//		assertEquals(card1, hand.cards.get(0));
//		assertEquals(card2, hand.cards.get(1));
//		assertEquals(card3, hand.cards.get(2));
//		assertEquals(card4, hand.cards.get(3));
//		assertEquals(card5, hand.cards.get(4));
//	}
}
