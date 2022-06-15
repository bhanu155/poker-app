package com.sap.ase.poker.rest;

import com.sap.ase.poker.data.PlayerNamesRepository;
import com.sap.ase.poker.dto.BetRequestDto;
import com.sap.ase.poker.dto.GetTableResponseDto;
import com.sap.ase.poker.model.IllegalAmountException;
import com.sap.ase.poker.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(TableController.PATH)
public class TableController {

	public static final String PATH = "/api/v1";

	private final TableService tableService;

	private final PlayerNamesRepository playerNamesRepository;

	public TableController(TableService tableService, PlayerNamesRepository playerNamesRepository) {
		this.tableService = tableService;
		this.playerNamesRepository = playerNamesRepository;
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
		tableStatus.setState(tableService.getState().getValue());
		tableStatus.setWinner(tableService.getWinner());
		tableStatus.setWinnerHand(tableService.getWinnerHand());
		return tableStatus;
	}

	@PostMapping("/players")
	public ResponseEntity<Void> joinTable(Principal principal) {
		String playerId = principal.getName();
		String playerName = playerNamesRepository.getNameForId(playerId);
		tableService.addPlayer(playerId, playerName);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/actions")
	public void placeBet(@RequestBody BetRequestDto betRequest) {
		try {
			int amount = betRequest.getArgs().length == 0 ? 0 : betRequest.getArgs()[0];
			tableService.performAction(betRequest.getType(), amount);
		} catch (IllegalAmountException e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}

	@PostMapping("/start")
	public ResponseEntity<Void> start() {
		tableService.start();
		return ResponseEntity.noContent().build();

	}
}