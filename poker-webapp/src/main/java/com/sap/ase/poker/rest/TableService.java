package com.sap.ase.poker.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.Table;

@Path("table")
@Produces(MediaType.APPLICATION_JSON)
public class TableService {
	
	private Table table = new Table();

	@GET
	@Path("{tableId}")
	public GetTableResponse getTable(@PathParam("tableId") int tableId) {
		return new GetTableResponse(table);
	}

	@POST
	@Path("{tableId}/startGame")
	public void startGame(@PathParam("tableId") int tableId) {
		table.startGame();
	}

	@POST
	@Path("{tableId}/init")
	public void deleteTable(@PathParam("tableId") int tableId) {
		table = new Table();
	}

	@POST
	@Path("{tableId}/players")
	public void joinTable(JoinTableRequest joinRequest, @PathParam("tableId") int tableId) {
		Player newPlayer = new Player();
		newPlayer.setName(joinRequest.getPlayerName());
		table.getPlayers().add(newPlayer);
	}

	@POST
	@Path("{tableId}/bets")
	public void placeBet(BetRequest betRequest, @PathParam("tableId") int tableId) {
		table.placeBet(betRequest.getAmount());
	}
}