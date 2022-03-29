package com.sap.ase.poker.config;

import com.sap.ase.poker.model.deck.Card;
import com.sap.ase.poker.model.deck.Kind;
import com.sap.ase.poker.model.deck.Suit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sap.ase.poker.model.deck.Kind.*;
import static com.sap.ase.poker.model.deck.Suit.*;

@Configuration
public class DeckConfig {

    @Bean
    public List<Card> pokerCards() {
        List<Card> pokerCards = new ArrayList<>();
        List<Suit> suits = Arrays.asList(DIAMONDS, SPADES, CLUBS, HEARTS);
        List<Kind> kinds = Arrays.asList(TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE);

        for (Suit suit : suits) {
            for (Kind kind : kinds) {
                pokerCards.add(new Card(kind, suit));
            }
        }

        return pokerCards;
    }
}
