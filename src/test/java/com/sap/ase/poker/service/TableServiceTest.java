package com.sap.ase.poker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.model.IllegalAmountException;
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
	void setup() {
//		Deck deck = Mockito.mock(Deck.class);
//		PokerCardsSupplier pokerCardsSupplier = Mockito.mock(PokerCardsSupplier.class);
//		RandomCardShuffler randomCardShuffler = Mockito.mock(RandomCardShuffler.class);
//
//		ShuffledDeckSupplier deckSupplier = Mockito.mock(ShuffledDeckSupplier.class);
//
//		tableService = new TableService(deckSupplier);

		deckSupplier = new ShuffledDeckSupplier(new PokerCardsSupplier(), new RandomCardShuffler());
		tableService = new TableService(deckSupplier);
	}

	private void addPlayers() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("CB", "Chandra Bhanu");
		tableService.addPlayer("TGS", "Thimmaraju");
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
		addPlayers();
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
		addPlayers();
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

		addPlayers();

		playerCards = tableService.getPlayerCards("SBR");
		assertThat(playerCards).hasSize(0);

		tableService.start();
		playerCards = tableService.getPlayerCards("SBR");
		assertThat(playerCards).hasSize(2);
	}

	@Test
	void getCommunityCardsShouldReturnEmptyListWhenPreFlop() {
		addPlayers();
		tableService.start();
		List<Card> communityCards = tableService.getCommunityCards();
		assertThat(communityCards).hasSize(0);
	}

	@Test
	void getCommunityCardsShouldReturnValidCards() {
		addPlayers();
		tableService.start();
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);

		List<Card> communityCards = tableService.getCommunityCards();
		assertThat(communityCards).hasSize(3);
	}

	@Test
	void performCheckShouldUpdateCurrentPlayerToNext() {
		addPlayers();
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
		addPlayers();
		tableService.start();
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);

		List<Card> communityCards = tableService.getCommunityCards();
		assertThat(communityCards).hasSize(3);
	}

	@Test
	void performCheckAllPlayersShouldUpdateFlopStatus() {
		addPlayers();
		tableService.start();
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);
		tableService.performAction("check", 0);

		assertThat(tableService.getState()).isEqualTo(GameState.FLOP);
	}
	@Test
	void raiseAmountShouldGreaterThanCurrentBid() {
		addPlayers();
		tableService.start();
			
		Exception exception = assertThrows(IllegalAmountException.class, () -> {
	        tableService.performAction("raise", 0);
	    });
		String expectedMessage = "Raise amount can not be less than or Equal to Current Bet";
	    String actualMessage = exception.getMessage();

	    assertThat(actualMessage).isEqualTo(expectedMessage);

	}
	@Test
	void raiseAmountShouldLessThanAvailableCash() {
		addPlayers();
		tableService.start();
			
		Exception exception = assertThrows(IllegalAmountException.class, () -> {
	        tableService.performAction("raise", 200);
	    });
		String expectedMessage = "Raise amount can not be greater than available cash";
	    String actualMessage = exception.getMessage();

	    assertThat(actualMessage).isEqualTo(expectedMessage);

	}
	@Test
	void performRaiseShouldDeductPlayerCash() {
		addPlayers();
		tableService.start();
		
		Player currentPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currentPlayer).isNotNull();
		int currentCashamount = currentPlayer.getCash();
		tableService.performAction("raise", 10);
		assertThat(currentPlayer.getCash()).isEqualTo(currentCashamount - 10);
		
		currentPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currentPlayer).isNotNull();
		currentCashamount = currentPlayer.getCash();
		tableService.performAction("raise", 20);
		assertThat(currentPlayer.getCash()).isEqualTo(currentCashamount - 20);
	}
	@Test
	void raiseAmountShouldNotExceedOtherPlayersAvailableCash() {
		addPlayers();
		tableService.start();
		tableService.performAction("raise", 10);
		
		Exception exception = assertThrows(IllegalAmountException.class, () -> {
	        tableService.performAction("raise", 95);
	    });
		String expectedMessage = "Raise amount should not exceed other players available cash";
	    String actualMessage = exception.getMessage();

	    assertThat(actualMessage).isEqualTo(expectedMessage);
	}

}