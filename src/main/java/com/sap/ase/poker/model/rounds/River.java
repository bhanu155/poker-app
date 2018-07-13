package com.sap.ase.poker.model.rounds;

import java.util.List;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Deck;
import com.sap.ase.poker.model.TablePlayers;

public class River extends Round {

	public River(TablePlayers players, Deck deck, List<Card> communityCards) {
		super(players, deck, communityCards);
	}

	public void start() {
		dealCommunityCards(1);
	}
}
