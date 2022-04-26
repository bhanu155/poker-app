package com.sap.ase.poker.service;

import com.sap.ase.poker.dto.CardDto;
import com.sap.ase.poker.dto.PlayerDto;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.*;
import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.rest.IllegalAmountException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class TableService {

    private final Supplier<Deck> deckSupplier;

    public TableService(Supplier<Deck> deckSupplier) {
        this.deckSupplier = deckSupplier;
    }

    public GameState getState() {
        // TODO: implement me
        return GameState.ENDED;
    }

    public List<PlayerDto> getPlayers() {
        // TODO: implement me
        return Arrays.asList(
                new PlayerDto(new Player("al-capone", "Al Capone", 100)),
                new PlayerDto(new Player("alice", "Alice", 100))
        );
    }

    public List<CardDto> getPlayerCards(String playerId) {
        // TODO: implement me
        return Arrays.asList(
                new CardDto(new Card(Kind.JACK, Suit.CLUBS)),
                new CardDto(new Card(Kind.TEN, Suit.CLUBS))
        );
    }

    public List<CardDto> getCommunityCards() {
        // TODO: implement me
        return Arrays.asList(
                new CardDto(new Card(Kind.ACE, Suit.CLUBS)),
                new CardDto(new Card(Kind.KING, Suit.CLUBS)),
                new CardDto(new Card(Kind.QUEEN, Suit.CLUBS)),
                new CardDto(new Card(Kind.FOUR, Suit.HEARTS)),
                new CardDto(new Card(Kind.SEVEN, Suit.SPADES))
        );
    }

    public PlayerDto getCurrentPlayer() {
        // TODO: implement me
        return new PlayerDto(new Player("al-capone", "Al", 100));
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

    public PlayerDto getWinner() {
        // TODO: implement me
        return new PlayerDto(new Player("al-capone", "Al capone", 500));
    }

    public List<CardDto> getWinnerHand() {
        // TODO: implement me
        return Arrays.asList(
                new CardDto(new Card(Kind.ACE, Suit.CLUBS)),
                new CardDto(new Card(Kind.KING, Suit.CLUBS)),
                new CardDto(new Card(Kind.QUEEN, Suit.CLUBS)),
                new CardDto(new Card(Kind.JACK, Suit.CLUBS)),
                new CardDto(new Card(Kind.TEN, Suit.CLUBS))
        );
    }

    public void start() {
        // TODO: implement me
    }

    public void addPlayer(String playerId, String playerName) {
        // TODO: implement me
        System.out.printf("Player joined the table: %s%n", playerId);
    }

    public void performAction(String action, int amount) throws IllegalAmountException {
        // TODO: implement me
        System.out.printf("Action performed: %s, amount: %d%n", action, amount);
    }

}
