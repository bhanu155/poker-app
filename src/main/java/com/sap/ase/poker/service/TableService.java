package com.sap.ase.poker.service;

import com.sap.ase.poker.dto.CardDto;
import com.sap.ase.poker.dto.GetTableResponseDto;
import com.sap.ase.poker.dto.PlayerDto;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.Card;
import com.sap.ase.poker.model.deck.Kind;
import com.sap.ase.poker.model.deck.Suit;
import com.sap.ase.poker.rest.IllegalAmount;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableService {

    public void addPlayer(String playerId) {
//        TODO: implement me
    }

    public void call() {
//        TODO: implement me
    }

    public void check() {
//        TODO: implement me
    }

    public void fold() {
//        TODO: implement me
    }

    public void raiseTo(int amount) throws IllegalAmount {
//        TODO: implement me
    }

    public GetTableResponseDto getTableStatus(String playerId) {

        GetTableResponseDto responseDto = new GetTableResponseDto("");


        List<PlayerDto> playerDtos = Arrays.asList(
                new PlayerDto(new Player("al", "Al", 100)),
                new PlayerDto(new Player("alice", "Alice", 100))
        );
        PlayerDto currentPlayerDto = new PlayerDto(new Player("al", "Al", 100));
        List<CardDto> playerCardDtos = Arrays.asList(
                new CardDto(new Card(Kind.ACE, Suit.CLUBS)),
                new CardDto(new Card(Kind.ACE, Suit.DIAMONDS))
        );
        List<CardDto> communityCardDtos = Arrays.asList(
                new CardDto(new Card(Kind.ACE, Suit.SPADES)),
                new CardDto(new Card(Kind.ACE, Suit.HEARTS)),
                new CardDto(new Card(Kind.SEVEN, Suit.HEARTS))
        );
        Map<String, Integer> bets = new HashMap<>();

        bets.put("al", 100);
        bets.put("alice", 50);

        responseDto.setPlayers(playerDtos);
        responseDto.setCurrentPlayer(currentPlayerDto);
        responseDto.setPot(0);
        responseDto.setPlayerCards(playerCardDtos);
        responseDto.setCommunityCards(communityCardDtos);
        responseDto.setBets(bets);
        return responseDto;
    }
}
