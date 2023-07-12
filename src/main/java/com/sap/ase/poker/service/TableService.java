package com.sap.ase.poker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.model.IllegalActionException;
import com.sap.ase.poker.model.IllegalAmountException;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.Card;
import com.sap.ase.poker.model.deck.Deck;
import com.sap.ase.poker.model.deck.Kind;
import com.sap.ase.poker.model.deck.Suit;
import com.sap.ase.poker.model.rules.WinnerRules;
import com.sap.ase.poker.model.rules.Winners;

@Service
public class TableService {

	private final Supplier<Deck> deckSupplier;

	private GameState gameState;

	private List<Player> players;

	private Player winner;

	private int currentPlayerIdx;

	private List<Card> communityCards;

	private int currentBet;

	private int pot;

	Map<String, Integer> bets;

	private boolean isRaisePerformed;
	
	@Autowired
	private WinnerRules winnerRules;

	private static final int BONUS_CASH = 100;

	public TableService(Supplier<Deck> deckSupplier) {
		this.deckSupplier = deckSupplier;
		communityCards = new ArrayList<>();
		gameState = GameState.OPEN;
		players = new ArrayList<Player>();
		winner = null;
		currentPlayerIdx = -1;
		currentBet = 0;
		bets = new HashMap<String, Integer>();
	}

	public GameState getState() {
		return gameState;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Card> getPlayerCards(String playerId) {
		Player player = new Player(playerId, null, 0);
		int idx = players.indexOf(player);

		if (idx < 0)
			return new ArrayList<>();
		else
			return players.get(idx).getHandCards();
	}

	public List<Card> getCommunityCards() {
		return communityCards;
	}

	public Optional<Player> getCurrentPlayer() {
		if (currentPlayerIdx == -1) {
			return Optional.empty();
		}
		return Optional.of(players.get(currentPlayerIdx));
	}

	public Map<String, Integer> getBets() {
		return bets;
	}

	public int getPot() {
		return pot;
	}

	public Optional<Player> getWinner() {
		if(winner == null)
			return Optional.empty();
		return Optional.of(winner);
	}

	public List<Card> getWinnerHand() {
		// TODO: implement me
		return Arrays.asList(new Card(Kind.ACE, Suit.CLUBS), new Card(Kind.KING, Suit.CLUBS),
				new Card(Kind.QUEEN, Suit.CLUBS), new Card(Kind.JACK, Suit.CLUBS), new Card(Kind.TEN, Suit.CLUBS));
	}

	public void start() {
		if (players.size() >= 2) {
			gameState = GameState.PRE_FLOP;
			startPreFlopRound();
		}
	}

	private void startPreFlopRound() {
		for (Player player : players) {
			player.getHandCards().add(deckSupplier.get().draw());
			player.getHandCards().add(deckSupplier.get().draw());
			player.setActive();
		}
		currentPlayerIdx = 0;

	}

	public void addPlayer(String playerId, String playerName) {
		players.add(new Player(playerId, playerName, BONUS_CASH));
		System.out.printf("Player joined the table: %s%n", playerId);
	}

	public void performAction(String action, int amount) throws IllegalAmountException {
		Player currentPlayer = players.get(currentPlayerIdx);
		switch (action) {
		case "check":
			performCheckAction(currentPlayer);
			break;
		case "raise":
			performRaiseAction(currentPlayer, amount);
			break;
		case "fold":
			performFoldAction(currentPlayer);
			break;
		case "call":
			performCallAction(currentPlayer);
			break;
		}
		if (gameState != GameState.ENDED) {
			currentPlayer.setHasPlayed(true);
			moveToNextActivePlayer();
			drawCommunityCardsAndMoveToNextRound();
			if(gameState.equals(GameState.ENDED)) {
				determineWinners();
			}
		}

		System.out.printf("Action performed: %s, amount: %d%n", action, amount);
	}
	
	private void determineWinners() {
		List<Player> activePlayers = new ArrayList<>();
		for(Player player: players) {
			if(player.isActive())
				activePlayers.add(player);
		}
		
		Winners winners = winnerRules.findWinners(communityCards, activePlayers);
		if(!winners.getWinners().isEmpty()) {
			winner = winners.getWinners().get(0);
		}
	}

	private void performCallAction(Player currentPlayer) {
		if (!isRaisePerformed) {
			throw new IllegalActionException("Call Should not allow without Raise");
		}
		currentPlayer.bet(currentBet - currentPlayer.getBet());

	}

	private void performFoldAction(Player currentPlayer) {
		currentPlayer.setInactive();
		int activePlayerCount = 0;
		for (Player player : players) {
			if (player.isActive())
				activePlayerCount++;
		}
		if (activePlayerCount == 1) {
			moveToNextActivePlayer();
			winner = players.get(currentPlayerIdx);
			endGame();
		}

	}

	private void endGame() {
		gameState = GameState.ENDED;
	}

	private void performRaiseAction(Player currentPlayer, int amount) {
		if (isValidAmount(currentPlayer, amount)) {
//			currentPlayer.deductCash(amount);
			currentPlayer.bet(amount);
			isRaisePerformed = true;
			currentBet = amount;
		}

	}

	private boolean isValidAmount(Player currentPlayer, int amount) {
		if (amount <= currentBet) {
			throw new IllegalAmountException("Raise amount can not be less than or Equal to Current Bet");
		}

		if (currentPlayer != null) {
			if (amount > currentPlayer.getCash()) {
				throw new IllegalAmountException("Raise amount can not be greater than available cash");
			} else {
				for (Player player : players) {
					if (amount > player.getCash()) {
						throw new IllegalAmountException("Raise amount should not exceed other players available cash");
					}
				}
			}
		}
		return true;
	}

	private void drawCommunityCardsAndMoveToNextRound() {
		boolean isRoundEnded = true;

		for (Player player : players) {
			if (player.isActive() && (!player.isHasPlayed() || currentBet != player.getBet())) {
				isRoundEnded = false;
				break;
			}
		}

		if (isRoundEnded) {
			int cardCount = 0;
			switch (gameState) {
			case PRE_FLOP:
				cardCount = 3;
				break;
			case FLOP:
				cardCount = 1;
				break;
			case TURN:
				cardCount = 1;
				break;
			case RIVER:
				cardCount = 0;
				break;
			}

			// draw comm cards
			for (int i = 0; i < cardCount; i++) {
				communityCards.add(deckSupplier.get().draw());
			}

			collectPot();
			gameState = gameState.next();
		}
	}

	private void resetBets() {
		// TODO Auto-generated method stub

	}

	private void collectPot() {
		int roundPot = 0;

		for (Player player : players) {
			roundPot += player.getBet();
			if (bets.containsKey(player.getId())) {
				bets.put(player.getId(), player.getBet() + bets.get(player.getId()));
			} else {
				bets.put(player.getId(), player.getBet());
			}

			player.clearBet();
		}

		pot = roundPot;
		currentBet = 0;
	}

	private void performCheckAction(Player currentPlayer) {
		if (currentPlayer.getBet() != currentBet) {
			throw new IllegalActionException("Check should not allow if bet amount rised");
		}
	}

	private void moveToNextActivePlayer() {
		do {
			currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
		} while (!players.get(currentPlayerIdx).isActive());
	}

	public WinnerRules getWinnerRules() {
		return winnerRules;
	}

	public void setWinnerRules(WinnerRules winnerRules) {
		this.winnerRules = winnerRules;
	}
	
	

}
