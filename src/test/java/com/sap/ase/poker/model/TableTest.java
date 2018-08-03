package com.sap.ase.poker.model;

import static com.sap.ase.poker.model.CardFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.sap.ase.poker.model.Bets.IllegalAmount;

public class TableTest {
	private Table table;
	private static final String ALICE = "alice";
	private static final String BOB = "bob";

	@Before
	public void setup() throws IllegalAmount {
		table = new Table(new DefaultDeck());
		table.addPlayer(ALICE);
		table.addPlayer(BOB);
		table.startGame();
	}

	@Test
	public void bobCalls_aliceChecks() throws Exception {
		table.call();
		table.check();
		assertThat(table.getCommunityCards().size(), is(3));
	}

	@Test
	public void bobFolds() throws Exception {
		table.fold();
		assertBetAndCash(BOB, 1, 100);
		assertBetAndCash(ALICE, 2, 97);
	}

	@Test
	public void checkThrough() throws Exception {
		/*
		 * XXX we're making assumptions on implementation detail.
		 * 
		 * We assume the 2x2 pre-flop cards are dealt like (alice, alice, bob, bob). If
		 * we'd change this to (alice, bob, alice, bob), the tests will break. It is
		 * probably ok to make this assumption, as there shouldn't be a hard requirement
		 * that forces us to make such a refactoring in future.
		 * 
		 * However this is not quite intuitive and makes the tests hard to understand,
		 * so if we can drive the tests a bit simpler we'd be better off. One
		 * possibility for the future: if we have different poker flavors, like e.g.
		 * classic poker (5 hand cards only) or Omaha hold'em, then we will need a
		 * different implementation of "FindWinners". So this will force us to make
		 * FindWinners an interface and have different implementations for the different
		 * poker games.
		 * 
		 * With such an interface we can simply have a mock/spy implementation of
		 * FindWinners, instead of influencing the winner through the deck.
		 */
		table = new Table(new FixedCardsDeck(SPADES_ACE, CLUBS_ACE, CLUBS_2, CLUBS_3, HEARTS_5, HEARTS_6, HEARTS_7,
				SPADES_9, SPADES_10, HEARTS_JACK, DIAMONDS_JACK, SPADES_JACK, CLUBS_JACK));
		table.addPlayer(ALICE);
		table.addPlayer(BOB);
		table.startGame();

		// pre-flop
		table.call();
		table.check();

		// flop
		table.check();
		table.check();

		// turn
		table.check();
		table.check();

		// river -> should trigger showdown
		table.check();
		table.check();

		// showdown started next round already, so small+big blind were set
		assertBetAndCash(ALICE, 2, 100);
		assertBetAndCash(BOB, 1, 97);
	}

	@Test
	public void with3Players_foldFold() throws Exception {
		table = new Table(new DefaultDeck());
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
		table = new Table(new DefaultDeck());
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
		table = new Table(new DefaultDeck());
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
