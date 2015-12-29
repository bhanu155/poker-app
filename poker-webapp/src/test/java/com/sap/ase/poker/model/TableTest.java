package com.sap.ase.poker.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.Table;
import com.sap.ase.poker.model.Table.IllegalOperationException;

public class TableTest {
	private Table table;
	private static final String ALICE = "alice";
	private static final String BOB = "bob";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		table = new Table();
		table.addPlayer(ALICE);
		table.addPlayer(BOB);
		table.startGame();
	}

	@Test(expected = IllegalOperationException.class)
	public void illegalCheck() throws Exception {
		table.check();
	}

	@Test
	public void illegalCall() throws Exception {
		table.call();
		thrown.expect(IllegalOperationException.class);
		table.call();
	}

	@Test
	public void call() throws Exception {
		table.call();
		assertBetAndCash(ALICE, 2, 98);
	}

	@Test
	public void raise() throws Exception {
		table.raiseTo(3);
		assertBetAndCash(ALICE, 3, 97);
	}

	@Test(expected = IllegalOperationException.class)
	public void illegalRaiseExceedsCash() throws Exception {
		table.raiseTo(101);
	}

	@Test
	public void legalRaiseMaximum() throws Exception {
		table.raiseTo(100);
	}

	@Test(expected = IllegalOperationException.class)
	public void illegalRaiseLessOrEqualMaxBet() throws Exception {
		table.raiseTo(2);
	}

	@Test
	public void legalRaiseMinimum() throws Exception {
		table.raiseTo(3);
	}

	@Test
	public void reRaising() throws Exception {
		table.raiseTo(3);
		table.raiseTo(4);
		table.call();
		assertThat(table.getCommunityCards().size(), is(3));
	}

	@Test
	public void fold() throws Exception {
		table.fold();
		assertBetAndCash(BOB, 1, 100);
		assertBetAndCash(ALICE, 2, 97);
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
		assertThat(table.getCommunityCards().size(), is(4));
		
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
