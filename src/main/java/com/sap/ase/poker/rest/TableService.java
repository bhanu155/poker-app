package com.sap.ase.poker.rest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sap.ase.poker.model.Bets.IllegalAmount;
import com.sap.ase.poker.model.DefaultDeck;
import com.sap.ase.poker.model.Table;

@Path("table")
@Produces(MediaType.APPLICATION_JSON)
public class TableService {

	private static final String PLAYERNAME = "Player";
	private Table table = new Table(new DefaultDeck());

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
		} catch (IllegalAmount e) {
			//XXX see the other comment in Pre-Flop.start - this is the reason why we need this horrible catch block
			//A much more appropriate try/catch in the "startGame" case would be that there are not sufficient (active) players!
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}

	@POST
	@Path("{tableId}")
	public void createTable(@PathParam("tableId") int tableId) {
		table = new Table(new DefaultDeck());
	}

	@POST
	@Path("{tableId}/players")
	public void joinTable(JoinTableRequest joinRequest, @PathParam("tableId") int tableId) {
		table.addPlayer(joinRequest.getPlayerName());
	}

	//XXX not nice from an HTTP/REST perspective
	//The switch is ok here as won't need type/case extension lateron, what is not nice however
	//is how we abuse the protocol a litle bit, as there are different kinds of posts - one has an amount,
	//all the others don't have it (and don't need it).
	//A "cleaner" REST API would just always have the amount - so actually the server wouldn't care any longer
	//whether we "check" (amount=current max bet), "call" (amount=current max bet), raise (amount>current max bet)
	//or "fold" (this should even be a different endpoint then - probably a DELETE /table/<id>/player!).
	//This would simplify also the table and bets classes significantly.
	//The client then needs to do a bit more work, e.g. setting the right amount for check and call,
	//however the client anyway has to be improved right now, e.g. to hide call/check/raise buttons in the particular cases.
	//To decide this, the client needs to know about "current amount" and "delta to current max bet" _anyway_. 
	//
	//This idea is definitely debatable! Having a very domain-specific API like we have it right now, is also a nice thing.
	//On the other hand, being more compliant with HTTP/REST makes it easier to leverage REST-based concepts on the client side,
	//have a cleaner MVC approach, e.g. using a UI5 custom model, implementing a true RESTful JSON model that interacts with the server, ...
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
		} catch (IllegalAmount e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}
}