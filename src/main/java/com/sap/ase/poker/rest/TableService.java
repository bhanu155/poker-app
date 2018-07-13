package com.sap.ase.poker.rest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sap.ase.poker.model.Table;
import com.sap.ase.poker.model.Table.IllegalOperationException;

@Path("table")
@Produces(MediaType.APPLICATION_JSON)
public class TableService {

	private static final String PLAYERNAME = "Player";
	private Table table = new Table();

	@GET
	@Path("{tableId}")
	public GetTableResponse getTable(@PathParam("tableId") int tableId, @HeaderParam(PLAYERNAME) String playerName) {
		return new GetTableResponse(table, playerName);
	}

	@POST
	@Path("{tableId}/startGame")
	public void startGame(@PathParam("tableId") int tableId) {
		try {
			table.startGame();
		} catch (IllegalOperationException e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}

	@POST
	@Path("{tableId}/init")
	public void deleteTable(@PathParam("tableId") int tableId) {
		table = new Table();
	}

	@POST
	@Path("{tableId}/players")
	public void joinTable(JoinTableRequest joinRequest, @PathParam("tableId") int tableId) {
		table.addPlayer(joinRequest.getPlayerName());
	}

	@POST
	@Path("{tableId}/bets")
	public void placeBet(BetRequest betRequest, @PathParam("tableId") int tableId) {
		try {
			switch (betRequest.getAction()) {
			case "call":
				table.call();
				break;
			case "check":
				table.check();
				break;
			case "fold":
				table.fold();
				break;
			case "raiseTo":
				table.raiseTo(betRequest.getAmount());
				break;
			}
		} catch (IllegalOperationException e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}
}