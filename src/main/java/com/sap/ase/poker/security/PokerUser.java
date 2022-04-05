package com.sap.ase.poker.security;


public class PokerUser {

    private final String name;
    private final String displayName;
    private final String password;

    public PokerUser(String name, String displayName, String password) {
        this.name = name;
        this.displayName = displayName;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return this.password;
    }
}