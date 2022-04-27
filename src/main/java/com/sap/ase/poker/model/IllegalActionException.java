package com.sap.ase.poker.model;

/*
 * This class is internally used to identify illegal operations. Example:
 * raising when the player doesn't have sufficient cash. This is an illegal
 * usage from the client, not a server error.
 */
@SuppressWarnings("serial")
public class IllegalActionException extends RuntimeException {
    public IllegalActionException(String message) {
        super(message);
    }
}