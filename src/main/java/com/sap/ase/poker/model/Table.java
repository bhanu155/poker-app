package com.sap.ase.poker.model;

import static java.util.Arrays.asList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import com.sap.ase.poker.model.Bets.IllegalAmount;
import com.sap.ase.poker.model.rounds.Flop;
import com.sap.ase.poker.model.rounds.PreFlop;
import com.sap.ase.poker.model.rounds.River;
import com.sap.ase.poker.model.rounds.Round;
import com.sap.ase.poker.model.rounds.Turn;
import com.sap.ase.poker.winner.FindWinners;
import com.sap.ase.poker.winner.FindWinners.Winners;

public class Table {
	private static final int DEFAULT_START_CASH = 100;

	private int numOfPlayersThatPerformedAction = 0;
	private TablePlayers players = new TablePlayers();
	private ArrayList<Card> communityCards = new ArrayList<>();
	private final Deck deck;
	private Bets bets;

	private Queue<Round> rounds = new ArrayDeque<>();
	private Round currentRound;

	public Table(Deck deck) {
		this.deck = deck;
	}

	public Iterable<Player> getPlayers() {
		return players;
	}

	public void addPlayer(String name) throws IllegalAmount {
		Player player = new Player(name, DEFAULT_START_CASH);		
		players.add(player);
		if (players.size() == 2) {
			startGame();
		}
	}

	//FIXME can we start a game with insufficient players? That would feel wrong...
	//"IllegalNumberOfPlayers" would also be a much more accurate client error than the current
	//"IllegalAmount" exception - see the other comments in PreFlop.start and TableService.startGame
	private void startGame() throws IllegalAmount {
		deck.shuffle();
		communityCards = new ArrayList<>();
		bets = new Bets(players);
		initTexasHoldemRounds();
		startNextRound();
	}

	public Player getCurrentPlayer() {
		return players.getCurrentPlayer();
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}

	public void call() throws IllegalAmount {
		bets.call();
		onPlayerPerformedAction();
	}

	public void check() throws IllegalAmount {
		bets.check();
		onPlayerPerformedAction();
	}

	public void fold() throws IllegalAmount {
		bets.fold();
		onPlayerPerformedAction();
	}

	public void raiseTo(int amount) throws IllegalAmount {
		bets.raiseTo(amount);
		onPlayerPerformedAction();
	}

	private void onPlayerPerformedAction() throws IllegalAmount {
		numOfPlayersThatPerformedAction++;
		//FIXME is that a bug? Check what happens if the next round starts:
		//Do we then always have the right start player?
		//Say, we play with Alice, Bob, Cindy. Cindy was start player in current round.
		//According to Texas Hold'em poker rules, next round, the first active player
		//next to the dealer in clockwise order should be the start player.
		//Is that true if Cindy raised, Alice called, Bob called?
		//I suppose currently Cindy would be start player in the next round, and that's wrong.
		//
		//FIXME also, check if the cards are dealt "correctly" i.e. in the right order
		//(although technically it is a random deck, so it won't make the game unfair,
		//however typically in Texas hold'em the cards are dealt first to the small blind player
		players.nextPlayer();

		if (isOnlyOneActivePlayerLeft()) {
			bets.distributePot(new Winners(asList(getCurrentPlayer()), getCurrentPlayer()));
			players.nextStartPlayer();
			startGame();
		} else if (shouldStartNextRound()) {
			numOfPlayersThatPerformedAction = 0;
			if (rounds.isEmpty()) {
				//FIXME is that a bug? Check what happens if a folded player has the best hand
				Winners winners = new FindWinners().apply(players.asList(), this.communityCards);
				bets.distributePot(winners);
				players.nextStartPlayer();
				startGame();
			} else {
				startNextRound();
			}
		}
	}
	
//	private void onPlayerPerformedAction1() throws IllegalAmount {
//		nextPlayer();
//		if (isOnlyOneActivePlayerLeft()) {
//			distributePotToOnlyLeftPlayer();
//			startNextGame();
//		} else if (isRoundFinished()) {
//			if (isLastRound()) {
//				distributePotToWinner();
//				startNextGame();
//			} else {
//				startNextRound();
//			}
//		}
//	}

	private void initTexasHoldemRounds() {
		rounds.clear();
		rounds.add(new PreFlop(players, deck, communityCards, bets));
		rounds.add(new Flop(players, deck, communityCards));
		rounds.add(new Turn(players, deck, communityCards));
		rounds.add(new River(players, deck, communityCards));
	}

	private void startNextRound() throws IllegalAmount {
		currentRound = rounds.remove();
		currentRound.start();
	}

	private boolean isOnlyOneActivePlayerLeft() {
		return players.activePlayersSize() == 1;
	}

	private boolean shouldStartNextRound() {
		return bets.areEven() && didAllPlayersPerformAnAction();
	}

	private boolean didAllPlayersPerformAnAction() {
		return numOfPlayersThatPerformedAction >= players.activePlayersSize();
	}
}
