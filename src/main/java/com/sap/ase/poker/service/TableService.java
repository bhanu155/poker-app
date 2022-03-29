package com.sap.ase.poker.service;

import com.sap.ase.poker.dto.GetTableResponseDto;
import com.sap.ase.poker.rest.IllegalAmount;
import org.springframework.stereotype.Service;

@Service
public class TableService {

    public void addPlayer(String playerName) {
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

        return new GetTableResponseDto("");
    }
}
