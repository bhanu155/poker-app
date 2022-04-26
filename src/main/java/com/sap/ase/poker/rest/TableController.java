package com.sap.ase.poker.rest;

import com.sap.ase.poker.dto.BetRequestDto;
import com.sap.ase.poker.dto.GetTableResponseDto;
import com.sap.ase.poker.model.IllegalAmountException;
import com.sap.ase.poker.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(TableController.PATH)
public class TableController {

	public static final String PATH = "/api/v1";

	private final TableService tableService;

	private final Map<String, String> playerIdToName = new HashMap<>();

	public TableController(TableService tableService) {
		this.tableService = tableService;
		playerIdToName.put("al-capone", "Al Capone");
		playerIdToName.put("pat-garret", "Pat Garret");
		playerIdToName.put("wyatt-earp", "Wyatt Earp");
		playerIdToName.put("doc-holiday", "Doc Holiday");
		playerIdToName.put("wild-bill", "Wild Bill");
		playerIdToName.put("stu-ungar", "Stu Ungar");
		playerIdToName.put("kitty-leroy", "Kitty Leroy");
		playerIdToName.put("poker-alice", "Poker Alice");
		playerIdToName.put("madame-moustache", "Madame Moustache");
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
		String playerName = playerIdToName.getOrDefault(playerId, "Unknown");
		tableService.addPlayer(playerId, playerName);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/actions")
	public void placeBet(@RequestBody BetRequestDto betRequest) {
		try {
			tableService.performAction(betRequest.getAction(), betRequest.getAmount());
		} catch (IllegalAmountException e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}
}