package com.sap.ase.poker.model.rounds;

import java.util.List;

import com.sap.ase.poker.model.Bets;
import com.sap.ase.poker.model.Bets.IllegalAmount;
import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Deck;
import com.sap.ase.poker.model.TablePlayers;

public class PreFlop extends Round {

	private static final int SMALL_BLIND = 1;
	private static final int BIG_BLIND = 2;
	private final Bets bets;

	public PreFlop(TablePlayers players, Deck deck, List<Card> communityCards, Bets bets) {
		super(players, deck, communityCards);
		this.bets = bets;
	}

	public void start() throws IllegalAmount {
		dealCardsToEachPlayer();
		bets.raiseTo(SMALL_BLIND);
		players.nextPlayer();
		bets.raiseTo(BIG_BLIND);
		players.nextPlayer();
	}
}
