package com.sap.ase.poker.model;

public class InvalidHandException extends RuntimeException {
    public InvalidHandException(String message) {
        super(message);
    }
}
