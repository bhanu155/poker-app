package com.sap.ase.poker.rest;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.ase.poker.model.Bets.IllegalAmount;
import com.sap.ase.poker.model.DefaultDeck;
import com.sap.ase.poker.model.Table;
import com.sap.ase.poker.security.JwtUserHttpServletRequestWrapper.PokerUserPrincipal;

@RestController
@RequestMapping("/api/tables")
public class TableService {

	private Table table = new Table(new DefaultDeck());

	@GetMapping
	public List<LobbyEntry> lobby() {
		return Arrays.asList(new LobbyEntry("las-vegas", "0/10", "1/2"));
	}

	@GetMapping("/{tableId}")
	public GetTableResponse getTable(Principal principal, @PathParam("tableId") String tableId) {
		String playerId = principal.getName();
		return new GetTableResponse(table, playerId);
	}

	@PostMapping("/{tableId}")
	public void createTable(@PathParam("tableId") String tableId) {
		table = new Table(new DefaultDeck());
	}

	@PostMapping("/{tableId}/players")
	public ResponseEntity<Void> joinTable(@RequestBody JoinTableRequest joinRequest, @PathParam("tableId") String tableId)
			throws IllegalAmount {
		// FIXME the server should not throw in case of IllegalAmount exception:
		// if a player doesn't have sufficient money, he/she should just become
		// "inactive" instead
		table.addPlayer(joinRequest.getPlayerName());
		return ResponseEntity.noContent().build();
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
	@PostMapping("/{tableId}/bets")
	public void placeBet(@RequestBody BetRequest betRequest, @PathParam("tableId") String tableId) {
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