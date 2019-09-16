package com.sap.ase.poker.rest;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.sap.ase.poker.model.Bets.IllegalAmount;
import com.sap.ase.poker.model.DefaultDeck;
import com.sap.ase.poker.model.Table;

@Path("tables")
@Produces(MediaType.APPLICATION_JSON)
public class TableService {

	private Table table = new Table(new DefaultDeck());

	@GET
	public List<LobbyEntry> lobby() {
		return Arrays.asList(new LobbyEntry("las-vegas", "0/10", "1/2"));
	}

	@GET
	@Path("{tableId}")
	public GetTableResponse getTable(@Context SecurityContext securityContext, @PathParam("tableId") String tableId) {
		String playerId = securityContext.getUserPrincipal().getName();
		return new GetTableResponse(table, playerId);
	}

	@POST
	@Path("{tableId}")
	public void createTable(@PathParam("tableId") String tableId) {
		table = new Table(new DefaultDeck());
	}

	@POST
	@Path("{tableId}/players")
	public void joinTable(JoinTableRequest joinRequest, @PathParam("tableId") String tableId) throws IllegalAmount {
		// FIXME the server should not throw in case of IllegalAmount exception:
		// if a player doesn't have sufficient money, he/she should just become
		// "inactive" instead
		table.addPlayer(joinRequest.getPlayerName());
	}

	// XXX not nice from an HTTP/REST perspective
	// The switch is ok here as won't need type/case extension lateron, what is not
	// nice however is how we abuse the protocol a litle bit, as there are different
	// kinds of posts - one has an amount, all the others don't have it (and don't
	// need it).
	//
	// A "cleaner" REST API would just always have the amount - so actually the
	// server wouldn't care any longer whether we "check" (amount=current max bet),
	// "call" (amount=current max bet), raise (amount>current max bet) or "fold"
	// (this should even be a different endpoint then - probably a
	// DELETE /table/<id>/player!).
	//
	// This would simplify also the table and bets classes significantly.
	// The client then needs to do a bit more work, e.g. setting the right amount
	// for check and call, however the client anyway has to be improved right now,
	// e.g. to hide call/check/raise buttons in the particular cases.
	// To decide this, the client needs to know about "current amount" and "delta to
	// current max bet" _anyway_.
	//
	// This idea is definitely debatable! Having a very domain-specific API like we
	// have it right now, is also a nice thing.
	// On the other hand, being more compliant with HTTP/REST makes it easier to
	// leverage REST-based concepts on the client side,
	// have a cleaner MVC approach, e.g. using a UI5 custom model, implementing a
	// true RESTful JSON model that interacts with the server, ...
	@POST
	@Path("{tableId}/bets")
	public void placeBet(BetRequest betRequest, @PathParam("tableId") String tableId) {
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