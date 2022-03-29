package com.sap.ase.poker.model.deck;

public enum Suit {
    DIAMONDS("diamonds"),
    HEARTS("hearts"),
    SPADES("spades"),
    CLUBS("clubs");

    public final String value;

    private Suit(String value) {
        this.value = value;
    }
}
