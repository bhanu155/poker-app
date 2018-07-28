package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.CardFixtures.CLUBS_2;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_3;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_4;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_QUEEN;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_10;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_2;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_3;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_4;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_5;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_6;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_8;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_9;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_ACE;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_JACK;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_KING;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_QUEEN;
import static com.sap.ase.poker.model.CardFixtures.HEARTS_4;
import static com.sap.ase.poker.model.CardFixtures.SPADES_10;
import static com.sap.ase.poker.model.CardFixtures.SPADES_4;
import static com.sap.ase.poker.model.CardFixtures.SPADES_5;
import static com.sap.ase.poker.model.CardFixtures.SPADES_6;
import static com.sap.ase.poker.model.CardFixtures.SPADES_JACK;
import static com.sap.ase.poker.model.CardFixtures.SPADES_KING;
import static com.sap.ase.poker.model.CardFixtures.SPADES_QUEEN;
import static com.sap.ase.poker.winner.Hand.Type.FLUSH;
import static com.sap.ase.poker.winner.Hand.Type.FOUR_OF_A_KIND;
import static com.sap.ase.poker.winner.Hand.Type.FULL_HOUSE;
import static com.sap.ase.poker.winner.Hand.Type.HIGH_CARD;
import static com.sap.ase.poker.winner.Hand.Type.PAIR;
import static com.sap.ase.poker.winner.Hand.Type.ROYAL_FLUSH;
import static com.sap.ase.poker.winner.Hand.Type.STRAIGHT;
import static com.sap.ase.poker.winner.Hand.Type.STRAIGHT_FLUSH;
import static com.sap.ase.poker.winner.Hand.Type.THREE_OF_A_KIND;
import static com.sap.ase.poker.winner.Hand.Type.TWO_PAIRS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.winner.DetermineHand.Result;
import com.sap.ase.poker.winner.IllegalHand.DuplicateCards;
import com.sap.ase.poker.winner.IllegalHand.IllegalNumberOfCards;

public class DetermineHandTest {

	@Test
	public void illegalNumberOfCards() throws Exception {
		try {
			new DetermineHand().execute();
			fail("6 cards should be illegal for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(0, e.numberOfCards);
		}
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_4, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK);
			fail("6 cards should be illegal for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(6, e.numberOfCards);
		}
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_4, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
					SPADES_QUEEN, SPADES_KING);
			fail("8 cards are not evaluate for hand evaluation");
		} catch (IllegalNumberOfCards e) {
			assertEquals(8, e.numberOfCards);
		}
	}

	@Test
	public void duplicateCards() throws Exception {
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_2, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
					SPADES_QUEEN);
			fail("duplicate cards should be illegal for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_2, e.duplicateCard);
		}
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_4, DIAMONDS_10, CLUBS_4, DIAMONDS_9, SPADES_JACK,
					SPADES_QUEEN);
			fail("duplicate cards are not evaluate for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_4, e.duplicateCard);
		}
		try {
			new DetermineHand().execute(CLUBS_2, CLUBS_4, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
					CLUBS_4);
			fail("duplicate cards are not evaluate for hand evaluation");
		} catch (DuplicateCards e) {
			assertEquals(CLUBS_4, e.duplicateCard);
		}
	}

	@Test
	public void highCard() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_KING, DIAMONDS_10, DIAMONDS_8,
				DIAMONDS_9, SPADES_JACK);
		assertEquals(HIGH_CARD, result.handType);
		assertHandCards(SPADES_KING, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, result);
	}

	@Test
	public void pair() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_4,
				DIAMONDS_9, SPADES_JACK);
		assertEquals(PAIR, result.handType);
		assertHandCards(CLUBS_4, SPADES_4, SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, result);
	}

	@Test
	public void twoPairs() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_4,
				DIAMONDS_QUEEN, SPADES_JACK);
		assertEquals(TWO_PAIRS, result.handType);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, CLUBS_4, SPADES_4, SPADES_JACK, result);
	}

	@Test
	public void twoPairs_outOfThree() throws Exception {
		Result result = new DetermineHand().execute(SPADES_4, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_10,
				DIAMONDS_QUEEN, SPADES_JACK);
		assertEquals(TWO_PAIRS, result.handType);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, DIAMONDS_10, SPADES_10, SPADES_JACK, result);
	}

	@Test
	public void threeOfAKind() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_10, SPADES_4, HEARTS_4,
				SPADES_JACK);
		assertEquals(THREE_OF_A_KIND, result.handType);
		assertHandCards(CLUBS_4, SPADES_4, HEARTS_4, SPADES_QUEEN, SPADES_JACK, result);
	}

	@Test
	public void threeOfAKind_outOfTwo() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_2, CLUBS_4, SPADES_QUEEN, DIAMONDS_QUEEN, SPADES_4,
				HEARTS_4, CLUBS_QUEEN);
		assertEquals(THREE_OF_A_KIND, result.handType);
		assertHandCards(SPADES_QUEEN, DIAMONDS_QUEEN, CLUBS_QUEEN, CLUBS_4, SPADES_4, result);
	}

	@Test
	public void fourOfAKind() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_2, CLUBS_4, DIAMONDS_4, DIAMONDS_10, SPADES_4, HEARTS_4,
				SPADES_JACK);
		assertEquals(FOUR_OF_A_KIND, result.handType);
		assertHandCards(CLUBS_4, DIAMONDS_4, SPADES_4, HEARTS_4, SPADES_JACK, result);
	}

	@Test
	public void fullHouse() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_2, CLUBS_4, DIAMONDS_4, DIAMONDS_QUEEN, SPADES_4,
				SPADES_QUEEN, SPADES_JACK);
		assertEquals(FULL_HOUSE, result.handType);
		assertHandCards(CLUBS_4, DIAMONDS_4, SPADES_4, DIAMONDS_QUEEN, SPADES_QUEEN, result);
	}

	@Test
	public void straight() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_4, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
				SPADES_QUEEN, DIAMONDS_ACE);
		assertEquals(STRAIGHT, result.handType);
		assertHandCards(SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, result);
	}
	
	@Test
	public void straightWithSixCards() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_4, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
				SPADES_QUEEN, DIAMONDS_KING);
		assertEquals(STRAIGHT, result.handType);
		assertHandCards(DIAMONDS_KING, SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, result);
	}
	
	@Test
	public void straightWithPair() throws Exception {
		Result result = new DetermineHand().execute(SPADES_KING, DIAMONDS_10, DIAMONDS_8, DIAMONDS_9, SPADES_JACK,
				SPADES_QUEEN, DIAMONDS_KING);
		assertEquals(STRAIGHT, result.handType);
		assertHandCards(SPADES_KING, SPADES_QUEEN, SPADES_JACK, DIAMONDS_10, DIAMONDS_9, result);
	}
	
	@Test
	public void falseStraight() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_4, CLUBS_2, DIAMONDS_8, DIAMONDS_ACE, SPADES_JACK,
				SPADES_QUEEN, DIAMONDS_KING);
		assertEquals(HIGH_CARD, result.handType);
		assertHandCards(DIAMONDS_ACE, DIAMONDS_KING, SPADES_QUEEN, SPADES_JACK, DIAMONDS_8, result);
	}
	
	@Test
	public void smallStraight() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_3, CLUBS_2, DIAMONDS_4, DIAMONDS_ACE, SPADES_5,
				SPADES_QUEEN, DIAMONDS_KING);
		assertEquals(STRAIGHT, result.handType);
		assertHandCards(SPADES_5, DIAMONDS_4, CLUBS_3, CLUBS_2, DIAMONDS_ACE, result);
	}
	
	@Test
	public void flush() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_4, DIAMONDS_9, DIAMONDS_4, DIAMONDS_8, DIAMONDS_10,
				DIAMONDS_QUEEN, SPADES_KING);
		assertEquals(FLUSH, result.handType);
		assertHandCards(DIAMONDS_QUEEN, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, DIAMONDS_4, result);
	}

	@Test
	public void flushWithSixCards() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_4, DIAMONDS_9, DIAMONDS_4, DIAMONDS_8, DIAMONDS_10,
				DIAMONDS_QUEEN, DIAMONDS_KING);
		assertEquals(FLUSH, result.handType);
		assertHandCards(DIAMONDS_KING, DIAMONDS_QUEEN, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, result);
	}
	
	@Test
	public void straightFlush() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_4, DIAMONDS_9, DIAMONDS_4, DIAMONDS_8, DIAMONDS_10,
				DIAMONDS_QUEEN, DIAMONDS_JACK);
		assertEquals(STRAIGHT_FLUSH, result.handType);
		assertHandCards(DIAMONDS_QUEEN, DIAMONDS_JACK, DIAMONDS_10, DIAMONDS_9, DIAMONDS_8, result);
	}

	@Test
	public void smallStraightFlush() throws Exception {
		Result result = new DetermineHand().execute(DIAMONDS_2, DIAMONDS_ACE, DIAMONDS_4, DIAMONDS_3, DIAMONDS_5,
				DIAMONDS_QUEEN, DIAMONDS_JACK);
		assertEquals(STRAIGHT_FLUSH, result.handType);
		assertHandCards(DIAMONDS_5, DIAMONDS_4, DIAMONDS_3, DIAMONDS_2, DIAMONDS_ACE, result);
	}

	@Test
	public void straightFlushWithSixCards() throws Exception {
		Result result = new DetermineHand().execute(DIAMONDS_2, DIAMONDS_ACE, DIAMONDS_4, DIAMONDS_3, DIAMONDS_5,
				DIAMONDS_QUEEN, DIAMONDS_6);
		assertEquals(STRAIGHT_FLUSH, result.handType);
		assertHandCards(DIAMONDS_6, DIAMONDS_5, DIAMONDS_4, DIAMONDS_3, DIAMONDS_2, result);
	}

	@Test
	public void straightFlushWithHigherNonFlushStraight() throws Exception {
		Result result = new DetermineHand().execute(DIAMONDS_2, DIAMONDS_ACE, DIAMONDS_4, DIAMONDS_3, DIAMONDS_5,
				DIAMONDS_QUEEN, SPADES_6);
		assertEquals(STRAIGHT_FLUSH, result.handType);
		assertHandCards(DIAMONDS_5, DIAMONDS_4, DIAMONDS_3, DIAMONDS_2, DIAMONDS_ACE, result);
	}

	@Test
	public void royalFlush() throws Exception {
		Result result = new DetermineHand().execute(CLUBS_4, DIAMONDS_9, DIAMONDS_ACE, DIAMONDS_KING, DIAMONDS_10,
				DIAMONDS_QUEEN, DIAMONDS_JACK);
		assertEquals(ROYAL_FLUSH, result.handType);
		assertHandCards(DIAMONDS_ACE, DIAMONDS_KING, DIAMONDS_QUEEN, DIAMONDS_JACK, DIAMONDS_10, result);
	}

	private void assertHandCards(Card card1, Card card2, Card card3, Card card4, Card card5, Result result) {
		assertEquals(5, result.handOfFive.length);
		assertEquals(card1, result.handOfFive[0]);
		assertEquals(card2, result.handOfFive[1]);
		assertEquals(card3, result.handOfFive[2]);
		assertEquals(card4, result.handOfFive[3]);
		assertEquals(card5, result.handOfFive[4]);
	}
}
