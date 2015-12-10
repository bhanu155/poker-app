package com.sap.ase.poker;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sap.ase.poker.model.Bet;
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

		List<TableResponse.Player> players = new ArrayList<TableResponse.Player>();
		for (Player player : table.getPlayers()) {
			TableResponse.Player p = new TableResponse.Player();
			p.setName(player.getName());
			p.setBet(player.getBet());
			players.add(p);
		}
		response.setPlayers(players.toArray(new TableResponse.Player[0]));

		List<TableResponse.Card> cards = new ArrayList<TableResponse.Card>();
		for (Card card : table.getCurrentPlayer().getCards()) {
			TableResponse.Card c = new TableResponse.Card();
			c.setSuit(card.getSuit());
			c.setKind(card.getKind());
			cards.add(c);
		}
		response.setCards(cards.toArray(new TableResponse.Card[0]));

		List<TableResponse.Card> communityCards = new ArrayList<TableResponse.Card>();
		for (Card card : table.getCommunityCards()) {
			TableResponse.Card c = new TableResponse.Card();
			c.setSuit(card.getSuit());
			c.setKind(card.getKind());
			communityCards.add(c);
		}
		response.setCommunityCards(communityCards.toArray(new TableResponse.Card[0]));

		response.setCurrentPlayer(table.getCurrentPlayer().getName());

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
	public void joinTable(TableRequests.JoinTable joinRequest, @PathParam("id") int id) {
		Player newPlayer = new Player();
		newPlayer.setName(joinRequest.getName());
		table.getPlayers().add(newPlayer);
	}

	@POST
	@Path("{id}/bets")
	public void placeBet(Bet bet, @PathParam("id") int id) {
		table.placeBet(bet.getValue());
		if (table.getPlayers().get(0).getBet() == table.getPlayers().get(1).getBet()) {
			table.showCommunityCards();
		}
	}
}