package com.sap.ase.poker.acceptance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.Table;

public class AcceptanceTest {
	private Table table;
	private static final String ALICE = "alice";
	private static final String BOB = "bob";

	@Before
	public void setup() {
		table = new Table();
		table.addPlayer(ALICE);
		table.addPlayer(BOB);
		table.startGame();
	}

	@Test
	public void gameNotStarted_shouldGetBasicInfo() throws Exception {
		table = new Table();
		Player currentPlayer = table.getCurrentPlayer();
		currentPlayer.getBet();
		currentPlayer.getCash();
		currentPlayer.getName();
		table.getCommunityCards();
		table.getPlayers();
	}

	@Test
	public void startGame() throws Exception {

		assertThat(table.getCurrentPlayer().getName(), is(ALICE));

		assertBetAndCash(ALICE, 1, 99);
		assertBetAndCash(BOB, 2, 98);

		assertThat(table.getCommunityCards().size(), is(0));

		table.call();
		assertThat(table.getCommunityCards().size(), is(0));
		assertThat(table.getCurrentPlayer().getName(), is(BOB));

		table.check();
		assertThat(table.getCommunityCards().size(), is(3));
		assertThat(table.getCurrentPlayer().getName(), is(ALICE));
	}

	@Test
	public void checkThroughTillEnd() throws Exception {
		table.call();
		table.check();
		assertThat(table.getCommunityCards().size(), is(3));
		table.check();
		table.check();
		assertThat(table.getCommunityCards().size(), is(4));
		table.check();
		table.check();
		assertThat(table.getCommunityCards().size(), is(5));
		table.check();
		table.check();

		assertThat(table.getCurrentPlayer().getName(), is(BOB));
		assertThat(findPlayer(BOB).getBet(), is(1));
		assertThat(findPlayer(ALICE).getBet(), is(2));
	}

	@Test
	public void raiseAndCallTillEnd() throws Exception {
		table.raiseTo(3);
		table.call();
		assertThat(table.getCommunityCards().size(), is(3));
		table.raiseTo(4);
		table.call();
		assertThat(table.getCommunityCards().size(), is(4));
		table.raiseTo(5);
		table.call();
		assertThat(table.getCommunityCards().size(), is(5));
		table.raiseTo(6);
		table.call();

		assertThat(table.getCurrentPlayer().getName(), is(BOB));
		assertThat(findPlayer(BOB).getBet(), is(1));
		assertThat(findPlayer(ALICE).getBet(), is(2));
	}

	@Test
	public void with3Players_foldFold() throws Exception {
		table = new Table();
		table.addPlayer(ALICE);
		table.addPlayer(BOB);
		table.addPlayer("chris");
		table.startGame();

		assertThat(table.getCurrentPlayer().getName(), is("chris"));
		table.fold();
		assertBetAndCash(ALICE, 1, 99);
		assertBetAndCash(BOB, 2, 98);
		assertBetAndCash("chris", 0, 100);

		assertThat(table.getCurrentPlayer().getName(), is(ALICE));
		table.fold();
		assertBetAndCash(ALICE, 0, 99);
		assertBetAndCash(BOB, 1, 100);
		assertBetAndCash("chris", 2, 98);
	}

	@Test
	public void with3Players_foldCallFold() throws Exception {
		table = new Table();
		table.addPlayer(ALICE);
		table.addPlayer(BOB);
		table.addPlayer("chris");
		table.startGame();

		table.fold();
		table.call();
		table.fold();
		assertBetAndCash(ALICE, 0, 102);
		assertBetAndCash(BOB, 1, 97);
		assertBetAndCash("chris", 2, 98);
	}

	@Test
	public void with3Players_chrisFoldsImmediately_bobFoldsLater() throws Exception {
		table = new Table();
		table.addPlayer(ALICE);
		table.addPlayer(BOB);
		table.addPlayer("chris");
		table.startGame();

		table.fold();
		table.call();
		table.check();
		assertBetAndCash(ALICE, 2, 98);
		assertBetAndCash(BOB, 2, 98);
		assertBetAndCash("chris", 0, 100);
		assertThat(table.getCommunityCards().size(), is(3));

		table.raiseTo(3);
		table.call();
		assertThat(table.getCommunityCards().size(), is(4));

		table.raiseTo(4);
		table.fold();

		assertBetAndCash(ALICE, 0, 103);
		assertBetAndCash(BOB, 1, 96);
		assertBetAndCash("chris", 2, 98);
	}

	private void assertBetAndCash(String playerName, int bet, int cash) {
		Player player = findPlayer(playerName);
		assertThat(player.getBet(), is(bet));
		assertThat(player.getCash(), is(cash));
	}

	private Player findPlayer(String playerName) {
		for (Player p : table.getPlayers()) {
			if (p.getName().equals(playerName)) {
				return p;
			}
		}
		throw new RuntimeException("player " + playerName + " does not exist");
	}
}
