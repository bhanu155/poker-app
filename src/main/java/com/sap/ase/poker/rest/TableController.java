package com.sap.ase.poker.rest;

import com.sap.ase.poker.dto.BetRequestDto;
import com.sap.ase.poker.dto.GetTableResponseDto;
import com.sap.ase.poker.dto.JoinTableRequestDto;
import com.sap.ase.poker.dto.LobbyEntryDto;
import com.sap.ase.poker.security.JwtUserHttpServletRequestWrapper;
import com.sap.ase.poker.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(TableController.PATH)
public class TableController {

	public static final String PATH = "/api/v1";

	private TableService tableService;

	public TableController(TableService tableService) {
		this.tableService = tableService;
	}

//	@GetMapping
//	public List<LobbyEntryDto> lobby() {
//		return Collections.singletonList(new LobbyEntryDto("las-vegas", "0/10", "1/2"));
//	}

	@GetMapping
	public GetTableResponseDto getTable(JwtUserHttpServletRequestWrapper.PokerUserPrincipal principal) {
		String playerId = principal.getName();
		String playerName = principal.getDisplayName();
		GetTableResponseDto tableStatus = tableService.getTableStatus(playerId);
		return tableStatus;
	}

	@PostMapping("/players")
	public ResponseEntity<Void> joinTable(JwtUserHttpServletRequestWrapper.PokerUserPrincipal principal)
			throws IllegalAmount {
		tableService.addPlayer(principal.getName(), principal.getDisplayName());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/actions")
	public void placeBet(@RequestBody BetRequestDto betRequest) {
		try {
			switch (betRequest.getAction()) {
				case "call":
					tableService.call();
					break;
				case "check":
					tableService.check();
					break;
				case "fold":
					tableService.fold();
					break;
				case "raiseTo":
					tableService.raiseTo(betRequest.getAmount());
					break;
			}
		} catch (IllegalAmount e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}
}