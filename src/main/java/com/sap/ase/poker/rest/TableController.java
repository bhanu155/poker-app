package com.sap.ase.poker.rest;

import com.sap.ase.poker.dto.BetRequestDto;
import com.sap.ase.poker.dto.GetTableResponseDto;
import com.sap.ase.poker.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(TableController.PATH)
public class TableController {

	public static final String PATH = "/api/v1";

	private TableService tableService;

	public TableController(TableService tableService) {
		this.tableService = tableService;
	}

	@GetMapping
	public GetTableResponseDto getTable(Principal principal) {
		String playerId = principal.getName();
		GetTableResponseDto tableStatus = new GetTableResponseDto();

		tableStatus.setPlayers(tableService.getPlayers());
		tableStatus.setCurrentPlayer(tableService.getCurrentPlayer());
		tableStatus.setPot(tableService.getPot());
		tableStatus.setPlayerCards(tableService.getPlayerCards(playerId));
		tableStatus.setCommunityCards(tableService.getCommunityCards());
		tableStatus.setBets(tableService.getBets());
		tableStatus.setState(tableService.getState());
		tableStatus.setWinner(tableService.getWinner());
		tableStatus.setWinnerHand(tableService.getWinnerHand());
		return tableStatus;
	}

	@PostMapping("/players")
	public ResponseEntity<Void> joinTable(Principal principal) {
		tableService.addPlayer(principal.getName());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/actions")
	public void placeBet(@RequestBody BetRequestDto betRequest) {
		try {
			tableService.performAction(betRequest.getAction(), betRequest.getAmount());
		} catch (IllegalAmount e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}
}