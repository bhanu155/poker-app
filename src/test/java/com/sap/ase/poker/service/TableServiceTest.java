package com.sap.ase.poker.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.Card;
import com.sap.ase.poker.model.deck.Deck;
import com.sap.ase.poker.model.deck.PokerCardsSupplier;
import com.sap.ase.poker.model.deck.RandomCardShuffler;
import com.sap.ase.poker.model.deck.ShuffledDeckSupplier;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TableServiceTest {

	TableService tableService;
	Supplier<Deck> deckSupplier;

	@BeforeEach
	void setUp() {
		deckSupplier = new ShuffledDeckSupplier(new PokerCardsSupplier(), new RandomCardShuffler());
		tableService = new TableService(deckSupplier);
	}

	@Test
	void getStateShouldReturnOpen() {
		GameState gameState = tableService.getState();
		assertThat(gameState).isEqualTo(GameState.OPEN);

	}

	@Test
	void getPlayersShouldReturnEmptyList() {
		List<Player> playerList = tableService.getPlayers();
		assertThat(playerList.size()).isEqualTo(0);
	}

	@Test
	void onePlayerShouldReturnSinglePlayerList() {
		tableService.addPlayer("SBR", "Raghav");
		assertThat(tableService.getPlayers().size()).isEqualTo(1);
	}

	@ParameterizedTest
	@MethodSource("provideParameters")
	void addPlayerShouldReturnAddedPlayersList(String id, String name) {
		tableService.addPlayer(id, name);
		Player player = tableService.getPlayers().get(tableService.getPlayers().size() - 1);
		assertThat(player.getName()).isEqualTo(name);
		assertThat(player.getCash()).isEqualTo(100);
		assertThat(player.isActive()).isFalse();
	}

	private static Stream<Arguments> provideParameters() {
		return Stream.of(Arguments.of("SBR", "Raghav"), Arguments.of("CB", "Chandra Bhanu"),
				Arguments.of("TGS", "Thimmaraju"));
	}

	@Test
	void startShouldReturnPreFlop() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");
		tableService.start();
		GameState gameState = tableService.getState();
		assertThat(gameState).isEqualTo(GameState.PRE_FLOP);
	}

	@Test
	void lessThanTwoPlayerShouldReturnOpenState() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.start();
		GameState gameState = tableService.getState();
		assertThat(gameState).isEqualTo(GameState.OPEN);
	}

	@Test
	void startGameShouldDrawCardsAndActivatePlayersAndSetFirstPlayerAsCurrentPlayer() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");
		tableService.start();
		Player player = tableService.getPlayers().get(tableService.getPlayers().size() - 1);
		assertThat(player.getHandCards()).hasSize(2);
		assertThat(player.isActive()).isTrue();
		Player currentPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currentPlayer).isNotNull();
		assertThat(currentPlayer.getId()).isEqualTo("SBR");

	}

	@Test
	void beforeStartCurrentPlayerShouldBeEmpty() {
		assertThat(tableService.getCurrentPlayer()).isEmpty();
	}

	@Test
	void getPlayerCardsShouldReturnTwoCards() {

		List<Card> playerCards = tableService.getPlayerCards("SBR");
		assertThat(playerCards).hasSize(0);

		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");

		playerCards = tableService.getPlayerCards("SBR");
		assertThat(playerCards).hasSize(0);

		tableService.start();
		playerCards = tableService.getPlayerCards("SBR");
		assertThat(playerCards).hasSize(2);
	}

	@Test
	void getCommunityCardsShouldReturnEmptyListWhenPreFlop() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");
		tableService.start();
		List<Card> communityCards = tableService.getCommunityCards();
		assertThat(communityCards).hasSize(0);
	}

	@Test
	void getCommunityCardsShouldReturnValidCards() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");
		tableService.start();
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);

		List<Card> communityCards = tableService.getCommunityCards();
		assertThat(communityCards).hasSize(3);
	}

	@Test
	void performCheckShouldUpdateCurrentPlayerToNext() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");
		tableService.start();

		tableService.performAction("check", 0);
		Player currPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currPlayer).isNotNull();
		assertThat(currPlayer.getId()).isEqualTo("CB");

		tableService.performAction("check", 0);
		currPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currPlayer).isNotNull();
		assertThat(currPlayer.getId()).isEqualTo("TGS");

		tableService.performAction("check", 0);
		tableService.performAction("check", 0);
		currPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currPlayer).isNotNull();
		assertThat(currPlayer.getId()).isEqualTo("CB");

	}

	@Test
	void allPlayersCheckedInPreFlopShouldDrawThreeCommunityCards() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");
		tableService.start();
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);

		List<Card> communityCards = tableService.getCommunityCards();
		assertThat(communityCards).hasSize(3);
	}

	@Test
	void performCheckAllPlayersShouldUpdateFlopStatus() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");
		tableService.start();
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);

		assertThat(tableService.getState()).isEqualTo(GameState.FLOP);
	}

}