package com.sap.ase.poker.model.rules;

public class InvalidAmountOfCardsException extends RuntimeException {
    public InvalidAmountOfCardsException(String message) {
        super(message);
    }
}
