package com.sap.ase.poker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.model.IllegalActionException;
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

	@Test
	void performFoldShouldInactivatePlayer() {
		addPlayers();
		tableService.start();
		Player currentPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currentPlayer).isNotNull();

		tableService.performAction("fold", 0);
		assertThat(currentPlayer.isActive()).isFalse();
	}

	@Test
	void performFoldShouldDeductPlayerBetFromCash() {
		addPlayers();
		tableService.start();

		tableService.performAction("raise", 10);
		tableService.performAction("raise", 20);
		tableService.performAction("raise", 30);

		Player currentPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currentPlayer).isNotNull();
		tableService.performAction("fold", 0); // Raghav folded with last bet 10
		assertThat(currentPlayer.getCash()).isEqualTo(100 - currentPlayer.getBet());
	}

	@Test
	void onlySingleActivePlayerAfterFoldShouldEndGame() {
		addPlayers();
		tableService.start();

		tableService.performAction("raise", 10);
		tableService.performAction("raise", 20);
		tableService.performAction("raise", 30);

		tableService.performAction("fold", 0); // Raghav folded with last bet 10
		tableService.performAction("fold", 0); // Bhanu folded with last bet 20

		Player currentPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currentPlayer).isNotNull();
		Player winner = tableService.getCurrentPlayer().orElse(null);
		assertThat(winner).isNotNull();

		assertThat(winner).isEqualTo(currentPlayer);
		assertThat(tableService.getState()).isEqualTo(GameState.ENDED);
	}

	@Test
	void performCallShouldNotAllowWithRise() {
		addPlayers();
		tableService.start();

		Exception exception = assertThrows(IllegalActionException.class, () -> {
			tableService.performAction("call", 0);
		});
		String expectedMessage = "Call Should not allow without Raise";
		String actualMessage = exception.getMessage();

		assertThat(actualMessage).isEqualTo(expectedMessage);
	}

	@Test
	void performCallShouldAllowWithRiseAndDeductFromCurrentBetAmount() {
		addPlayers();
		tableService.start();

		tableService.performAction("raise", 20);
		Player currentPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currentPlayer).isNotNull();
		System.out.println(currentPlayer.getCash());
		tableService.performAction("call", 0);
		assertThat(currentPlayer.getBet()).isEqualTo(20);
		assertThat(currentPlayer.getCash()).isEqualTo(100 - currentPlayer.getBet());

	}

	@Test
	void performCallShouldMatchRerise() {
		addPlayers();
		tableService.start();

		tableService.performAction("raise", 10);
		tableService.performAction("raise", 20);
		tableService.performAction("raise", 30);

		Player currentPlayer = tableService.getCurrentPlayer().orElse(null);
		assertThat(currentPlayer).isNotNull();
		tableService.performAction("call", 0);

		assertThat(currentPlayer.getBet()).isEqualTo(30);
		assertThat(currentPlayer.getCash()).isEqualTo(100 - currentPlayer.getBet());
	}

	@Test
	void performCheckShouldNotAllowIfAnyBetRaised() {
		addPlayers();
		tableService.start();
		tableService.performAction("raise", 10);

		Exception exception = assertThrows(IllegalActionException.class, () -> {
			tableService.performAction("check", 0);
		});
		String expectedMessage = "Check should not allow if bet amount rised";
		String actualMessage = exception.getMessage();
		assertThat(actualMessage).isEqualTo(expectedMessage);

	}

	@Test
	void BetAmountIsNotEqualShouldNotBeEndOfRound() {
		addPlayers();
		tableService.start();

		GameState gameState = tableService.getState();
		System.out.println(gameState.name());
		tableService.performAction("check", 0);
		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);

		System.out.println(tableService.getState().name());
		assertThat(tableService.getState()).isEqualTo(gameState);
	}

	@Test
	void BetAmountIsEqualAndAllPlayersPlayedShouldBeEndOfRound() {
		addPlayers();
		tableService.start();

		GameState gameState = tableService.getState();
		System.out.println(gameState.name());
		tableService.performAction("check", 0);
		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);

		System.out.println(tableService.getState().name());
		assertThat(tableService.getState()).isNotEqualTo(gameState);
	}

	@Test
	void flopToTurnShouldDrawFourCommunityCards() {
		addPlayers();
		tableService.start();

		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);

		tableService.performAction("raise", 20);
		tableService.performAction("fold", 0);
		tableService.performAction("call", 0);

		assertThat(tableService.getCommunityCards()).hasSize(4);

	}

	@Test
	void turnToReverShouldDrawFourCommunityCards() {
		addPlayers();
		tableService.start();

		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);

		tableService.performAction("raise", 20);
		tableService.performAction("fold", 0);
		tableService.performAction("call", 0);

		tableService.performAction("raise", 20);
		tableService.performAction("call", 0);

		assertThat(tableService.getCommunityCards()).hasSize(5);

	}

	@Test
	void endRoundShoudReturnTotalPot() {
		addPlayers();
		tableService.start();
		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);

		assertThat(tableService.getPot()).isEqualTo(30);
	}

	@Test
	void getPotShouldReturnTotalPotValue() {
		addPlayers();
		tableService.start();
		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);

		assertThat(tableService.getPot()).isEqualTo(30);
	}

	@Test
	void getBetsShouldReturnListOfAllPlayerBets() {
		addPlayers();
		tableService.start();
		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);

		assertThat(tableService.getBets()).hasSize(3);
		assertThat(tableService.getBets().get("SBR")).isEqualTo(10);
		assertThat(tableService.getBets().get("CB")).isEqualTo(10);
		assertThat(tableService.getBets().get("TGS")).isEqualTo(10);

	}

	@Test
	void getWinnerShouldReturnEmptyDuringGame() {
		addPlayers();
		tableService.start();// 0

		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);// 3

		tableService.performAction("raise", 20);
		tableService.performAction("fold", 0);
		tableService.performAction("call", 0);// 1

		tableService.performAction("raise", 20);
		tableService.performAction("call", 0);// 1

		tableService.performAction("raise", 20);
		tableService.performAction("fold", 0);// End

		Player winner = tableService.getWinner().orElse(null);
		assertThat(winner).isNotNull();
	}

	@Test
	void determineTheWinnerAtEndOfGame() {
		tableService.addPlayer("SBR", "Raghav");
		tableService.addPlayer("TGS", "Thimmaraju");

		tableService.start();// 0

		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);

		tableService.performAction("raise", 20);
		tableService.performAction("call", 0);

		tableService.performAction("raise", 20);
		tableService.performAction("call", 0);// 1

		tableService.performAction("raise", 20);
		tableService.performAction("call", 0);// End

		assertThat(tableService.getState()).isEqualTo(GameState.ENDED);
		Player winner = tableService.getWinner().orElse(null);
		assertThat(winner).isNotNull();

		assertThat(winner.getCash()).isEqualByComparingTo(170);
	}

	@Test
	void getWinnerHandShouldReturnWinningHand() {
		addPlayers();
		tableService.start();// 0

		assertThat(tableService.getWinnerHand()).isEmpty();

		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);// 3

		tableService.performAction("raise", 20);
		tableService.performAction("fold", 0);
		tableService.performAction("call", 0);// 1

		tableService.performAction("raise", 20);
		tableService.performAction("call", 0);// 1

		tableService.performAction("raise", 20);
		tableService.performAction("call", 0);// End

		assertThat(tableService.getWinnerHand()).hasSize(5);

	}

	@Test
	void getWinnerHandShouldEmptyWhenAllFoldedBeforeGameEnded() {
		addPlayers();
		tableService.start();// 0

		tableService.performAction("raise", 10);
		tableService.performAction("fold", 0);
		tableService.performAction("fold", 0);

		assertThat(tableService.getWinnerHand()).isEmpty();

	}

	@Test
	void multipleActionTest() {
		addPlayers();
		tableService.start();// 0

		tableService.performAction("raise", 10);
		tableService.performAction("call", 0);
		tableService.performAction("call", 0);// 3

		tableService.performAction("check", 0);

		assertThat(tableService.getCommunityCards()).hasSize(3);

		tableService.performAction("check", 0);
		tableService.performAction("check", 0);

		assertThat(tableService.getCommunityCards()).hasSize(4);

		tableService.performAction("raise", 10);
		tableService.performAction("fold", 0);
		tableService.performAction("raise", 20);
		tableService.performAction("call", 0);

		assertThat(tableService.getCommunityCards()).hasSize(5);

	}

	@Test
	void duplicatePlayerListTest() {
		tableService.addPlayer("CB", "Chandra");
		tableService.addPlayer("CB", "Bhanu");

		assertThat(tableService.getPlayers()).hasSize(1);

	}

}