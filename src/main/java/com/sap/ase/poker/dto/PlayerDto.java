package com.sap.ase.poker.dto;

public class PlayerDto {

    private String name;
    private int bet;
    private int cash;

    public PlayerDto() {
    }

    public PlayerDto(com.sap.ase.poker.model.Player player) {
        this.name = player.getName();
        this.bet = player.getBet();
        this.cash = player.getCash();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
}
