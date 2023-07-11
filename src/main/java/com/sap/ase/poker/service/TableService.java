package com.sap.ase.poker.service;

import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.model.IllegalAmountException;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.Card;
import com.sap.ase.poker.model.deck.Deck;
import com.sap.ase.poker.model.deck.Kind;
import com.sap.ase.poker.model.deck.Suit;

import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
public class TableService {

	private final Supplier<Deck> deckSupplier;

	private GameState gameState;

	private List<Player> players;

	private int currentPlayerIdx;

	private List<Card> communityCards;
	
	private int currentBet;

	private static final int BONUS_CASH = 100;

	public TableService(Supplier<Deck> deckSupplier) {
		this.deckSupplier = deckSupplier;
		communityCards = new ArrayList<>();
		gameState = GameState.OPEN;
		players = new ArrayList<Player>();
		currentPlayerIdx = -1;
		currentBet = 0;
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
		// TODO: implement me
		return new HashMap<String, Integer>() {
			{
				put("al-capone", 100);
				put("alice", 50);
			}
		};
	}

	public int getPot() {
		// TODO: implement me
		return 150;
	}

	public Optional<Player> getWinner() {
		// TODO: implement me
		return Optional.of(new Player("al-capone", "Al capone", 500));
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
		switch (action) {
		case "check":
			performCheckAction();
			break;
		case "raise":
			performRaiseAction(amount);
			break;
		}
		moveToNextPlayer();
		drawCommunityCardsAndMoveToNextRound(3);

		System.out.printf("Action performed: %s, amount: %d%n", action, amount);
	}

	private void performRaiseAction(int amount) {
		Player currentPlayer = getCurrentPlayer().orElse(null);
		if(isValidAmount(currentPlayer, amount)) {
			currentPlayer.deductCash(amount);
		}
		
	}

	private boolean isValidAmount(Player currentPlayer, int amount) {
		if(amount <= currentBet) {
			throw new IllegalAmountException("Raise amount can not be less than or Equal to Current Bet");
		}
		
		if(currentPlayer != null) {
			if(amount > currentPlayer.getCash()) {
				throw new IllegalAmountException("Raise amount can not be greater than available cash");
			}else {
				for(Player player : players) {
					if(amount > player.getCash()) {
						throw new IllegalAmountException("Raise amount should not exceed other players available cash");
					}
				}
			}			
		}
		return true;
	}

	private void drawCommunityCardsAndMoveToNextRound(int cardCount) {
		boolean hasAllPlayed = true;

		for (Player player : players) {
			if (!player.isHasPlayed()) {
				hasAllPlayed = false;
				break;
			}
		}

		if (hasAllPlayed) {
			// draw comm cards
			for (int i = 0; i < cardCount; i++) {
				communityCards.add(deckSupplier.get().draw());
			}

			// update status
			gameState = gameState.next();
		}
	}

	private void performCheckAction() {
		players.get(currentPlayerIdx).setHasPlayed(true);
	}

	private void moveToNextPlayer() {
		currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
	}

}
