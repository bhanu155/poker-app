package com.sap.ase.poker.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(TableService.PATH)
public class TableService {

	public static final String PATH = "/api/tables";


	@GetMapping
	public List<LobbyEntry> lobby() {
		return Arrays.asList(new LobbyEntry("las-vegas", "0/10", "1/2"));
	}

	@GetMapping("/{tableId}")
	public GetTableResponse getTable(Principal principal, @PathParam("tableId") String tableId) {
		return null;
	}

	@PostMapping("/{tableId}/players")
	public ResponseEntity<Void> joinTable(@RequestBody JoinTableRequest joinRequest, @PathParam("tableId") String tableId)
			throws IllegalAmount {
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{tableId}/bets")
	public void placeBet(@RequestBody BetRequest betRequest, @PathParam("tableId") String tableId) {

	}

	public static class IllegalAmount extends Exception {
		public IllegalAmount(String message) {
			super(message);
		}
	}
}