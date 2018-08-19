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

	//XXX exposing the "IllegalAmount" exception feels wrong in this case.
	//In general, it is often a client error if an illegal amount is bet (i.e. bet more than the player has cash).
	//So "IllegalAmount" being a checked exception in general feels ok.
	//However, in this particular case, it more feels like a server error -> runtime exception.
	//Reason: why would we even start a game when one of the players doesn't have enough money?
	//This player should have been set inactive or even removed entirely from the players list!
	//So we'd better try/catch, wrap the checked exception into a runtime exception, and don't expose the checked exception to the caller.
	public void start() throws IllegalAmount {
		dealCardsToEachPlayer();		
		bets.raiseTo(SMALL_BLIND);
		players.nextPlayer();
		bets.raiseTo(BIG_BLIND);
		players.nextPlayer();
	}
}
