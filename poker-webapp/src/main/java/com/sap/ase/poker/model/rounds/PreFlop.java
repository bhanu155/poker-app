package com.sap.ase.poker.model.rounds;

import java.util.List;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Deck;
import com.sap.ase.poker.model.TablePlayers;

public class PreFlop extends Round {

	private static final int SMALL_BLIND = 1;
	private static final int BIG_BLIND = 2;

	public PreFlop(TablePlayers players, Deck deck, List<Card> communityCards) {
		super(players, deck, communityCards);
	}

	public void start() {
		dealCardsToEachPlayer();
		bet(SMALL_BLIND);
		bet(BIG_BLIND);
	}
}
