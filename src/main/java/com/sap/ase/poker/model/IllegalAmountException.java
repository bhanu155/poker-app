package com.sap.ase.poker.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/*
 * This class is internally used to identify illegal operations. Example:
 * raising when the player doesn't have sufficient cash. This is an illegal
 * usage from the client, not a server error.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalAmountException extends RuntimeException {
    public IllegalAmountException(String message) {
        super(message);
    }
}