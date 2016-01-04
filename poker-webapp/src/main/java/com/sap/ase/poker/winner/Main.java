package com.sap.ase.poker.winner;

import java.util.Arrays;
import java.util.List;

import com.sap.ase.poker.model.Card;

public class Main {
	static Card hearts10 = new Card(Card.Kind.TEN, Card.Suit.HEARTS);
	static Card clubs10 = new Card(Card.Kind.TEN, Card.Suit.CLUBS);
	static Card diamonds10 = new Card(Card.Kind.TEN, Card.Suit.DIAMONDS);
	static Card diamondsKing = new Card(Card.Kind.KING, Card.Suit.DIAMONDS);
	static Card clubsKing = new Card(Card.Kind.KING, Card.Suit.CLUBS);
	static Card hearts4 = new Card(Card.Kind.FOUR, Card.Suit.HEARTS);
	static Card diamonds4 = new Card(Card.Kind.FOUR, Card.Suit.DIAMONDS);

	public static void main(String[] args) {
		List<Card> fullHouse = Arrays.asList(hearts10, clubs10, diamonds10, diamondsKing, clubsKing, hearts4,
				diamonds4);

		Card pc12 = new Card(Card.Kind.ACE, Card.Suit.DIAMONDS);
		Card pc11 = new Card(Card.Kind.KING, Card.Suit.DIAMONDS);
		Card pc10 = new Card(Card.Kind.QUEEN, Card.Suit.DIAMONDS);
		Card pc9 = new Card(Card.Kind.JACK, Card.Suit.DIAMONDS);
		Card pc8 = new Card(Card.Kind.TEN, Card.Suit.DIAMONDS);
		Card pc13 = new Card(Card.Kind.NINE, Card.Suit.DIAMONDS);
		Card pc14 = new Card(Card.Kind.FIVE, Card.Suit.DIAMONDS);

		List<Card> royalFlush = Arrays.asList(pc8, pc9, pc10, pc11, pc12, pc13, pc14);

		Card pc20 = new Card(Card.Kind.ACE, Card.Suit.DIAMONDS);
		Card pc17 = new Card(Card.Kind.QUEEN, Card.Suit.CLUBS);
		Card pc16 = new Card(Card.Kind.JACK, Card.Suit.DIAMONDS);
		Card pc15 = new Card(Card.Kind.TEN, Card.Suit.DIAMONDS);
		Card pc19 = new Card(Card.Kind.NINE, Card.Suit.DIAMONDS);
		Card pc18 = new Card(Card.Kind.EIGHT, Card.Suit.DIAMONDS);
		Card pc21 = new Card(Card.Kind.SEVEN, Card.Suit.DIAMONDS);

		List<Card> straightFlush = Arrays.asList(pc15, pc16, pc17, pc18, pc19, pc20, pc21);

		Card pc29 = new Card(Card.Kind.TEN, Card.Suit.DIAMONDS);
		Card pc30 = new Card(Card.Kind.TEN, Card.Suit.DIAMONDS);
		Card pc31 = new Card(Card.Kind.TEN, Card.Suit.DIAMONDS);
		Card pc32 = new Card(Card.Kind.EIGHT, Card.Suit.DIAMONDS);
		Card pc33 = new Card(Card.Kind.NINE, Card.Suit.DIAMONDS);
		Card pc34 = new Card(Card.Kind.TEN, Card.Suit.DIAMONDS);
		Card pc35 = new Card(Card.Kind.SEVEN, Card.Suit.DIAMONDS);

		List<Card> forOfAKind = Arrays.asList(pc29, pc30, pc31, pc32, pc33, pc34, pc35);

		Card pc22 = new Card(Card.Kind.ACE, Card.Suit.HEARTS);
		Card pc23 = new Card(Card.Kind.TWO, Card.Suit.DIAMONDS);
		Card pc24 = new Card(Card.Kind.KING, Card.Suit.DIAMONDS);
		Card pc25 = new Card(Card.Kind.THREE, Card.Suit.DIAMONDS);
		Card pc26 = new Card(Card.Kind.NINE, Card.Suit.DIAMONDS);
		Card pc27 = new Card(Card.Kind.FOUR, Card.Suit.DIAMONDS);
		Card pc28 = new Card(Card.Kind.SEVEN, Card.Suit.CLUBS);

		List<Card> flush = Arrays.asList(pc22, pc23, pc24, pc25, pc26, pc27, pc28);

		Card pc36 = new Card(Card.Kind.ACE, Card.Suit.HEARTS);
		Card pc37 = new Card(Card.Kind.KING, Card.Suit.DIAMONDS);
		Card pc38 = new Card(Card.Kind.QUEEN, Card.Suit.DIAMONDS);
		Card pc39 = new Card(Card.Kind.JACK, Card.Suit.DIAMONDS);
		Card pc40 = new Card(Card.Kind.TEN, Card.Suit.DIAMONDS);
		Card pc41 = new Card(Card.Kind.FOUR, Card.Suit.DIAMONDS);
		Card pc42 = new Card(Card.Kind.SEVEN, Card.Suit.CLUBS);

		List<Card> straight = Arrays.asList(pc36, pc37, pc38, pc39, pc40, pc41, pc42);

		PlayerHand pHRoyalFlush = new PlayerHand();
		pHRoyalFlush.determineBestHandForCards(royalFlush);

		System.out.println("\n\n###########\n");

		PlayerHand pHStraightFlush = new PlayerHand();
		pHStraightFlush.determineBestHandForCards(straightFlush);

		System.out.println("\n\n###########\n");

		PlayerHand pHFourOfAKind = new PlayerHand();
		pHFourOfAKind.determineBestHandForCards(forOfAKind);

		System.out.println("\n\n###########\n");

		PlayerHand pHFullHouse = new PlayerHand();
		pHFullHouse.determineBestHandForCards(fullHouse);

		System.out.println("\n\n###########\n");

		PlayerHand pHFlush = new PlayerHand();
		pHFlush.determineBestHandForCards(flush);

		System.out.println("\n\n###########\n");

		PlayerHand pHStraight = new PlayerHand();
		pHStraight.determineBestHandForCards(straight);
	}
}
