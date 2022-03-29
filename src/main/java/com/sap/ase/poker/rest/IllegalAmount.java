package com.sap.ase.poker.rest;

/*
 * This class is internally used to identify illegal operations. Example:
 * raising when the player doesn't have sufficient cash. This is an illegal
 * usage from the client, not a server error.
 */
@SuppressWarnings("serial")
public class IllegalAmount extends Exception {
    public IllegalAmount(String message) {
        super(message);
    }
}