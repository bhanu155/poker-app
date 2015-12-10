package com.sap.ase.poker;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.Table;

@Path("table")
@Produces(MediaType.APPLICATION_JSON)
public class TexasPokerService {
	Table table = new Table();

	@GET
	@Path("{id}")
	public TableResponse getTable(@PathParam("id") int id) {

		TableResponse response = new TableResponse();

		response.setPlayers(table.getPlayers().toArray(new Player[0]));
		
		response.setCards(table.getCurrentPlayer().getCards().toArray(new Card[0]));
		return response;
	}

	@POST
	@Path("{id}/startGame")
	public void startGame(@PathParam("id") int id) {
		table.startGame();
	}
	
	@DELETE
	@Path("{id}")
	public void deleteTable(@PathParam("id") int id) {
		table = new Table();
	}
	
	@POST
	@Path("{id}/players")
	public void joinTable(JoinPlayerRequest joinPlayerRequest, @PathParam("id") int id) {
		Player newPlayer = new Player();
		newPlayer.setName(joinPlayerRequest.getName());
		table.getPlayers().add(newPlayer);
	}
	
	@POST
	@Path("{id}/bets")
	public void placeBet(Bet bet, @PathParam("id") int id) {
		table.placeBet(bet.getValue());
	}
}