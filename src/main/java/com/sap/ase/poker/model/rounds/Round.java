package com.sap.ase.poker.model.rounds;

import static java.util.Arrays.asList;

import java.util.List;

import com.sap.ase.poker.model.Bets.IllegalAmount;
import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Deck;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.TablePlayers;

//XXX instead of having a Round super class and all the derivatives, which, frankly speaking,
//are really really simple: would we be better of if we just had a "Rounds" class instead?
//Currently, some things currently are handled by the table object, yet logically are rather
//responsibility of the "Round", e.g. to decide whether the current round is finished - this
//can then also be handled by the new class.
//A new "Rounds" class would probably have some kind of "switch" for the different rounds
//(i.e. pre-flop, flop, turn, river) - however this is one of the "good" switches as we won't ever
//need to extend this, so having a non-oo solution here is fine, if not even the better solution!
public abstract class Round {

	protected final TablePlayers players;
	protected final Deck deck;
	protected final List<Card> communityCards;
	
	public Round(TablePlayers players, Deck deck, List<Card> communityCards) {
		this.players = players;
		this.deck = deck;
		this.communityCards = communityCards;
	}

	public abstract void start() throws IllegalAmount;
		
	protected void dealCommunityCards(int count) {
		//FIXME what about the burn card? This is a Texas Hold'em rule, so that requirement should also be specified by a test.
		for (int i = 0; i < count; i++) {
			this.communityCards.add(deck.dealCard());
		}
	}
	
	//XXX these cards are called "hole cards" according to Texas Hold'em poker terminology
	//XXX move this to PreFlop or even Table, since clearing the bet and setting the active/status doesn't really fit here conceptually
	protected void dealCardsToEachPlayer() {
		//FIXME According to Texas Hold'em poker rules, one card is dealt each time in clockwise order until each player has two cards
		//Right now, we are dealing two cards to each player in one go - so we are not compliant with Texas Hold'em poker rules, this requirement should also be specified by a test
		//FIXME Do we actually always start with the "right" player?
		//This is also a Texas Hold'em rule, so this requirement also needs to be specified by a test.
		//FIXME Could it be that we are dealing cards to inactive players?
		//What e.g. if a player is out of money? That player should become and remain inactive, or even be removed from the player list. 
		for (Player p : players.toList()) {
			p.setCards(asList(deck.dealCard(), deck.dealCard()));
			p.clearBet();
			p.setActive();
		}
	}
}