package com.sap.ase.poker;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

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
		ArrayList<TableResponse.Player> players = new ArrayList<TableResponse.Player>();
		for (Player player : table.getPlayers()) {
			TableResponse.Player p = new TableResponse.Player();
			p.setName(player.getName());
			players.add(p);
		}
		
		ArrayList<TableResponse.Card> cards = new ArrayList<TableResponse.Card>();
		if (table.getPlayers().size() > 0) {
			for (Card card : table.getPlayers().get(0).getCards()) {
				TableResponse.Card c = new TableResponse.Card();
				c.setKind(card.getKind());
				c.setSuit(card.getSuit());
				cards.add(c);
			}			
		}
		
		response.setPlayers(players.toArray(new TableResponse.Player[0]));
		response.setCards(cards.toArray(new TableResponse.Card[0]));
		return response;
	}

	@POST
	@Path("{id}/startGame")
	public void startGame(@PathParam("id") int id) {
		table.startGame();
	}
	
	@POST
	@Path("{id}/players")
	public void joinTable(JoinPlayerRequest joinPlayerRequest, @PathParam("id") int id) {
		Player newPlayer = new Player();
		newPlayer.setName(joinPlayerRequest.getName());
		table.getPlayers().add(newPlayer);
	}
}