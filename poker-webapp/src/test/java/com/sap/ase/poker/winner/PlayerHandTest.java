package com.sap.ase.poker.winner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sap.ase.poker.model.Card;

public class PlayerHandTest {

	@Test
	public void shouldReturnRoyalFlush() {
		List<Card> royalFlush = Arrays.asList(TestCard.clubsAce, TestCard.clubsKing, TestCard.clubsQueen,
				TestCard.clubsJack, TestCard.clubs10, TestCard.clubs9, TestCard.clubs8);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(royalFlush);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.ROYALFLUSH));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void shouldReturnRoyalFlushAlsoWhenThereAreTwoPairs() {
		List<Card> royalFlush = Arrays.asList(TestCard.clubsAce, TestCard.clubsKing, TestCard.clubsQueen,
				TestCard.clubsJack, TestCard.clubs10, TestCard.diamondsAce, TestCard.diamondsKing);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(royalFlush);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.ROYALFLUSH));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void shouldReturnRoyalFlushAlsoWhenThereAreThreeOfAKind() {
		List<Card> royalFlush = Arrays.asList(TestCard.clubsAce, TestCard.clubsKing, TestCard.clubsQueen,
				TestCard.clubsJack, TestCard.clubs10, TestCard.diamondsAce, TestCard.heartsAce);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(royalFlush);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.ROYALFLUSH));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void shouldReturnRoyalFlushAlsoWhenThereAreThreeKings() {
		List<Card> royalFlush = Arrays.asList(TestCard.clubsAce, TestCard.clubsKing, TestCard.clubsQueen,
				TestCard.clubsJack, TestCard.clubs10, TestCard.diamondsKing, TestCard.heartsKing);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(royalFlush);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.ROYALFLUSH));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void shouldReturnStraightFlush() {
		List<Card> straightFlush = Arrays.asList(TestCard.clubsAce, TestCard.clubsKing, TestCard.diamondsQueen,
				TestCard.diamondsJack, TestCard.diamonds10, TestCard.diamonds9, TestCard.diamonds8);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(straightFlush);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.STRAIGHTFLUSH));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void shouldReturnFourOfAKind() {
		List<Card> fourOfAKind = Arrays.asList(TestCard.clubsAce, TestCard.diamondsAce, TestCard.heartsAce,
				TestCard.spadesAce, TestCard.diamonds10, TestCard.hearts10, TestCard.spades10);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(fourOfAKind);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.FOUROFAKIND));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(4));
		assertThat(pokerHand.getRedundantCards().size(), is(3));
	}

	@Test
	public void shouldReturnFullHouse() {
		List<Card> fullHouse = Arrays.asList(TestCard.clubsAce, TestCard.diamondsAce, TestCard.heartsAce,
				TestCard.spades8, TestCard.diamonds10, TestCard.hearts10, TestCard.spades10);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(fullHouse).getPokerHandDefinition(),
				is(PokerHand.PokerHandDefinition.FULLHOUSE));
	}

	@Test
	public void shouldReturnFlush() {
		List<Card> flush = Arrays.asList(TestCard.clubsAce, TestCard.diamondsAce, TestCard.diamonds9,
				TestCard.diamonds2, TestCard.diamonds5, TestCard.heartsJack, TestCard.diamonds7);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(flush).getPokerHandDefinition(),
				is(PokerHand.PokerHandDefinition.FLUSH));
	}

	@Test
	public void shouldReturnStraight() {
		List<Card> straight = Arrays.asList(TestCard.clubsAce, TestCard.diamonds2, TestCard.spadesKing,
				TestCard.diamondsQueen, TestCard.diamondsJack, TestCard.hearts10, TestCard.diamonds7);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(straight);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.STRAIGHT));

		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));

		assertThat(pokerHand.getCardsUsedForPokerHand().get(0).compareTo(TestCard.clubsAce), is(0));
		assertThat(pokerHand.getRedundantCards().get(0).compareTo(TestCard.diamonds7), is(0));
	}

	@Test
	public void shouldReturnStraightAlsoWithLowestCards() {
		List<Card> straight = Arrays.asList(TestCard.clubsAce, TestCard.diamondsAce, TestCard.spades6,
				TestCard.diamonds5, TestCard.diamonds4, TestCard.hearts3, TestCard.diamonds2);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(straight);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.STRAIGHT));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(5));
		assertThat(pokerHand.getRedundantCards().size(), is(2));
	}

	@Test
	public void shouldReturnThreeOfAKind() {
		List<Card> threeOfAKind = Arrays.asList(TestCard.clubsAce, TestCard.diamondsAce, TestCard.spadesAce,
				TestCard.diamondsQueen, TestCard.diamondsJack, TestCard.hearts10, TestCard.diamonds7);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(threeOfAKind).getPokerHandDefinition(),
				is(PokerHand.PokerHandDefinition.THREEOFAKIND));
	}

	@Test
	public void shouldReturnTwoPair() {
		List<Card> twoPair = Arrays.asList(TestCard.clubsAce, TestCard.diamondsAce, TestCard.spades7,
				TestCard.diamondsQueen, TestCard.diamondsJack, TestCard.hearts10, TestCard.diamonds7);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(twoPair).getPokerHandDefinition(),
				is(PokerHand.PokerHandDefinition.TWOPAIR));
	}

	@Test
	public void shouldReturnPair() {
		List<Card> pair = Arrays.asList(TestCard.clubsAce, TestCard.diamonds9, TestCard.spades7, TestCard.diamondsQueen,
				TestCard.diamondsJack, TestCard.hearts10, TestCard.diamonds7);
		PlayerHand pHA = new PlayerHand();
		assertThat(pHA.determineBestHandForCards(pair).getPokerHandDefinition(),
				is(PokerHand.PokerHandDefinition.PAIR));
	}

	@Test
	public void shouldReturnNone() {
		List<Card> none = Arrays.asList(TestCard.clubsAce, TestCard.diamonds9, TestCard.spades7, TestCard.diamondsQueen,
				TestCard.diamondsJack, TestCard.hearts10, TestCard.diamonds3);
		PlayerHand pHA = new PlayerHand();
		PokerHand pokerHand = pHA.determineBestHandForCards(none);
		assertThat(pokerHand.getPokerHandDefinition(), is(PokerHand.PokerHandDefinition.NONE));
		assertThat(pokerHand.getCardsUsedForPokerHand().size(), is(0));
		// assertThat(pokerHand.getRedundantCards().size(), is(0));
	}

}
