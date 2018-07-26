package com.sap.ase.poker.winner;

import static com.sap.ase.poker.model.CardFixtures.CLUBS_10;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_8;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_9;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_ACE;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_JACK;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_KING;
import static com.sap.ase.poker.model.CardFixtures.CLUBS_QUEEN;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_10;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_2;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_3;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_4;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_5;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_7;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_8;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_9;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_ACE;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_JACK;
import static com.sap.ase.poker.model.CardFixtures.DIAMONDS_QUEEN;
import static com.sap.ase.poker.model.CardFixtures.HEARTS_10;
import static com.sap.ase.poker.model.CardFixtures.HEARTS_3;
import static com.sap.ase.poker.model.CardFixtures.HEARTS_ACE;
import static com.sap.ase.poker.model.CardFixtures.HEARTS_JACK;
import static com.sap.ase.poker.model.CardFixtures.SPADES_10;
import static com.sap.ase.poker.model.CardFixtures.SPADES_6;
import static com.sap.ase.poker.model.CardFixtures.SPADES_7;
import static com.sap.ase.poker.model.CardFixtures.SPADES_8;
import static com.sap.ase.poker.model.CardFixtures.SPADES_ACE;
import static com.sap.ase.poker.model.CardFixtures.SPADES_KING;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sap.ase.poker.model.Card;

public class PlayerHandTest {

	@Test
	public void royalFlush() {
		List<Card> royalFlush = Arrays.asList(CLUBS_ACE, CLUBS_KING, CLUBS_QUEEN, CLUBS_JACK, CLUBS_10, CLUBS_9,
				CLUBS_8);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(royalFlush);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.Definition.ROYALFLUSH));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void royalFlushWithPair() {
		List<Card> royalFlush = Arrays.asList(CLUBS_ACE, CLUBS_KING, CLUBS_QUEEN, CLUBS_JACK, CLUBS_10, DIAMONDS_ACE,
				SPADES_8);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(royalFlush);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.Definition.ROYALFLUSH));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void straightFlush() {
		List<Card> straightFlush = Arrays.asList(CLUBS_ACE, CLUBS_KING, DIAMONDS_QUEEN, DIAMONDS_JACK, DIAMONDS_10,
				DIAMONDS_9, DIAMONDS_8);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(straightFlush);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.Definition.STRAIGHTFLUSH));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void fourOfAKind() {
		List<Card> fourOfAKind = Arrays.asList(CLUBS_ACE, DIAMONDS_ACE, HEARTS_ACE, SPADES_ACE, DIAMONDS_10, HEARTS_10,
				SPADES_10);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(fourOfAKind);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.Definition.FOUROFAKIND));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(4));
		assertThat(pokerHand.getRedundantCards().size(), is(3));
	}

	@Test
	public void fullHouse() {
		List<Card> fullHouse = Arrays.asList(CLUBS_ACE, DIAMONDS_ACE, HEARTS_ACE, SPADES_8, DIAMONDS_10, HEARTS_10,
				SPADES_10);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(fullHouse).getPokerHandDefinition(),
				is(PokerHand.Definition.FULLHOUSE));
	}

	@Test
	public void flush() {
		List<Card> flush = Arrays.asList(CLUBS_ACE, DIAMONDS_ACE, DIAMONDS_9, DIAMONDS_2, DIAMONDS_5, HEARTS_JACK,
				DIAMONDS_7);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(flush).getPokerHandDefinition(), is(PokerHand.Definition.FLUSH));
	}

	@Test
	public void straight() {
		List<Card> straight = Arrays.asList(CLUBS_ACE, DIAMONDS_2, SPADES_KING, DIAMONDS_QUEEN, DIAMONDS_JACK,
				HEARTS_10, DIAMONDS_7);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(straight);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.Definition.STRAIGHT));

		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));

		assertThat(pokerHand.getCardsUsedForPokerHand().get(0).compareTo(CLUBS_ACE), is(0));
		assertThat(pokerHand.getRedundantCards().get(0).compareTo(DIAMONDS_7), is(0));
	}

	@Test
	public void straightWithLowestCards() {
		List<Card> straight = Arrays.asList(CLUBS_ACE, DIAMONDS_ACE, SPADES_6, DIAMONDS_5, DIAMONDS_4, HEARTS_3,
				DIAMONDS_2);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(straight);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.Definition.STRAIGHT));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void threeOfAKind() {
		List<Card> threeOfAKind = Arrays.asList(CLUBS_ACE, DIAMONDS_ACE, SPADES_ACE, DIAMONDS_QUEEN, DIAMONDS_JACK,
				HEARTS_10, DIAMONDS_7);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(threeOfAKind).getPokerHandDefinition(),
				is(PokerHand.Definition.THREEOFAKIND));
	}

	@Test
	public void twoPairs() {
		List<Card> twoPair = Arrays.asList(CLUBS_ACE, DIAMONDS_ACE, SPADES_7, DIAMONDS_QUEEN, DIAMONDS_JACK, HEARTS_10,
				DIAMONDS_7);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(twoPair).getPokerHandDefinition(), is(PokerHand.Definition.TWOPAIR));
	}

	@Test
	public void pair() {
		List<Card> pair = Arrays.asList(CLUBS_ACE, DIAMONDS_9, SPADES_7, DIAMONDS_QUEEN, DIAMONDS_JACK, HEARTS_10,
				DIAMONDS_7);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(pair).getPokerHandDefinition(), is(PokerHand.Definition.PAIR));
	}

	@Test
	public void highestCard() {
		List<Card> none = Arrays.asList(CLUBS_ACE, DIAMONDS_9, SPADES_7, DIAMONDS_QUEEN, DIAMONDS_JACK, HEARTS_10,
				DIAMONDS_3);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(none);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.Definition.NONE));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(0));
	}
}
