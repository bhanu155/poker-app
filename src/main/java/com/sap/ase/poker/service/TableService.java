package com.sap.ase.poker.service;

import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.model.IllegalAmountException;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.Card;
import com.sap.ase.poker.model.deck.Deck;
import com.sap.ase.poker.model.deck.Kind;
import com.sap.ase.poker.model.deck.Suit;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
public class TableService {

    private final Supplier<Deck> deckSupplier;
    
    private GameState gameState; 
    
    private List<Player> players;
    
    private Player currentPlayer;
    
    private static final int BONUS_CASH = 100;

    public TableService(Supplier<Deck> deckSupplier) {
        this.deckSupplier = deckSupplier;
        gameState = GameState.OPEN;
        players = new ArrayList<Player>();
    }

    public GameState getState() {
        return gameState;
    }

    public List<Player> getPlayers() {
    	return players;
    }

    public List<Card> getPlayerCards(String playerId) {
        // TODO: implement me
        return Arrays.asList(new Card(Kind.JACK, Suit.CLUBS),
                new Card(Kind.TEN, Suit.CLUBS)
        );
    }

    public List<Card> getCommunityCards() {
        // TODO: implement me
        return Arrays.asList(
                new Card(Kind.ACE, Suit.CLUBS),
                new Card(Kind.KING, Suit.CLUBS),
                new Card(Kind.QUEEN, Suit.CLUBS),
                new Card(Kind.FOUR, Suit.HEARTS),
                new Card(Kind.SEVEN, Suit.SPADES)
        );
    }

    public Optional<Player> getCurrentPlayer() {
        return Optional.of(currentPlayer);
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
        return Arrays.asList(
                new Card(Kind.ACE, Suit.CLUBS),
                new Card(Kind.KING, Suit.CLUBS),
                new Card(Kind.QUEEN, Suit.CLUBS),
                new Card(Kind.JACK, Suit.CLUBS),
                new Card(Kind.TEN, Suit.CLUBS)
        );
    }

    public void start() {
    	if(players.size()>=2) {
    		gameState = GameState.PRE_FLOP;
    		startPreFlopRound();
    	}    	
    }
    private void startPreFlopRound() {
    	for(Player player : players) {
    		player.getHandCards().add(deckSupplier.get().draw());
    		player.getHandCards().add(deckSupplier.get().draw());
    		player.setActive();
    	}
    	currentPlayer = players.get(0);
    	
    }

    public void addPlayer(String playerId, String playerName) {
       players.add(new Player(playerId, playerName, BONUS_CASH));
       System.out.printf("Player joined the table: %s%n", playerId);
    }

    public void performAction(String action, int amount) throws IllegalAmountException {
        // TODO: implement me
        System.out.printf("Action performed: %s, amount: %d%n", action, amount);
    }

}
