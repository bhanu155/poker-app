package com.sap.ase.poker.dto;

import java.util.ArrayList;
import java.util.List;

public class GetTableResponseDto {

	private List<PlayerDto> playerDtos = new ArrayList<>();
	private List<CardDto> playerCardDtos = new ArrayList<>();
	private String currentPlayer = "nobody";
	private List<CardDto> communityCardDtos = new ArrayList<>();

	public GetTableResponseDto() {
	}

	public GetTableResponseDto(String uiPlayerName) {
	}

	public List<PlayerDto> getPlayerDtos() {
		return playerDtos;
	}

	public void setPlayerDtos(List<PlayerDto> playerDtos) {
		this.playerDtos = playerDtos;
	}

	public List<CardDto> getPlayerCardDtos() {
		return playerCardDtos;
	}

	public void setPlayerCardDtos(List<CardDto> playerCardDtos) {
		this.playerCardDtos = playerCardDtos;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public List<CardDto> getCommunityCardDtos() {
		return communityCardDtos;
	}

	public void setCommunityCardDtos(List<CardDto> communityCardDtos) {
		this.communityCardDtos = communityCardDtos;
	}
}
