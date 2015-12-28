package com.sap.ase.poker.gameguide.models;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		PokerRules pR = new PokerRules();
		CardDeck deck = new CardDeck();
		PlayerHand playerHand = new PlayerHand();

		Card a = deck.dealCard();
		Card b = deck.dealCard();
		Card c = deck.dealCard();
		Card d = deck.dealCard();
		Card e = deck.dealCard();
		Card f = deck.dealCard();
		Card g = deck.dealCard();

		Card h = new Card();
		h.setKind(Card.Kinds.TEN);
		h.setSuit(Card.Suits.CLUBS);

		Card i = new Card();
		i.setKind(Card.Kinds.TEN);
		i.setSuit(Card.Suits.CLUBS);

		Card j = new Card();
		j.setKind(Card.Kinds.TEN);
		j.setSuit(Card.Suits.DIAMONDS);

		Card k = new Card();
		k.setKind(Card.Kinds.TEN);
		k.setSuit(Card.Suits.HEARTS);

		Card l = new Card();
		l.setKind(Card.Kinds.KING);
		l.setSuit(Card.Suits.DIAMONDS);

		Card m = new Card();
		m.setKind(Card.Kinds.KING);
		m.setSuit(Card.Suits.CLUBS);

		Card n = new Card();
		n.setKind(Card.Kinds.KING);
		n.setSuit(Card.Suits.HEARTS);

		Card o = new Card();
		o.setKind(Card.Kinds.KING);
		o.setSuit(Card.Suits.HEARTS);

		Card q = new Card();
		q.setKind(Card.Kinds.ACE);
		q.setSuit(Card.Suits.HEARTS);

		Card r = new Card();
		r.setKind(Card.Kinds.ACE);
		r.setSuit(Card.Suits.HEARTS);

		Card s = new Card();
		s.setKind(Card.Kinds.ACE);
		s.setSuit(Card.Suits.HEARTS);

		Card t = new Card();
		t.setKind(Card.Kinds.FOUR);
		t.setSuit(Card.Suits.HEARTS);

		Card u = new Card();
		u.setKind(Card.Kinds.FOUR);
		u.setSuit(Card.Suits.HEARTS);

		// ArrayList<Card> myCards = new ArrayList<Card>(Arrays.asList(a, b, c,
		// d, e, f, g));
		ArrayList<Card> myCards = new ArrayList<Card>(Arrays.asList(h, i, j, k, l, m, n, o, q, r, s, t, u));

		Card pc1 = new Card();
		pc1.setKind(Card.Kinds.TEN);
		pc1.setSuit(Card.Suits.HEARTS);

		Card pc2 = new Card();
		pc2.setKind(Card.Kinds.TEN);
		pc2.setSuit(Card.Suits.CLUBS);

		Card pc3 = new Card();
		pc3.setKind(Card.Kinds.TEN);
		pc3.setSuit(Card.Suits.DIAMONDS);

		Card pc4 = new Card();
		pc4.setKind(Card.Kinds.KING);
		pc4.setSuit(Card.Suits.DIAMONDS);

		Card pc5 = new Card();
		pc5.setKind(Card.Kinds.KING);
		pc5.setSuit(Card.Suits.CLUBS);

		Card pc6 = new Card();
		pc6.setKind(Card.Kinds.FOUR);
		pc6.setSuit(Card.Suits.HEARTS);

		Card pc7 = new Card();
		pc7.setKind(Card.Kinds.FOUR);
		pc7.setSuit(Card.Suits.DIAMONDS);

		ArrayList<Card> fullHouse = new ArrayList<Card>(Arrays.asList(pc1, pc2, pc3, pc4, pc5));
		ArrayList<Card> fullHouseAllCards = new ArrayList<Card>(Arrays.asList(pc1, pc2, pc3, pc4, pc5, pc6, pc7));

		// playerHand.setCards(fullHouse);
		playerHand.setCards(fullHouseAllCards);
		pR.getBestHandForPlayer(playerHand);

	}
}
