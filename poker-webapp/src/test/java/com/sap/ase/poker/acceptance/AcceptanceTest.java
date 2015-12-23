package com.sap.ase.poker.acceptance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

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
	public void testFold() throws Exception {
		table.fold();
		assertThat(table.getCurrentPlayer().getCash(), is(101));
	}

	@Test
	public void testRaise() throws Exception {
		table.raiseTo(3);
		table.call();
		assertBetAndCash(ALICE, 3, 97);
		assertBetAndCash(BOB, 3, 97);
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
