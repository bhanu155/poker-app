package com.sap.ase.poker.model;

import static com.sap.ase.poker.model.CardFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.sap.ase.poker.model.Bets.IllegalAmount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TableTest {
	private Table table;
	private static final String ALICE = "alice";
	private static final String BOB = "bob";
	private static final String CHRIS = "chris";

	@BeforeEach
	public void setup() throws IllegalAmount {
		table = new Table(new DefaultDeck());
		table.addPlayer(ALICE);
		table.addPlayer(BOB);
	}

	@Test
	public void bobTriesToJoinTwice() throws Exception {
		table.addPlayer(BOB);
		assertThat(table.getPlayers().size(), is(2));
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
	public void with3Players_3rdPlayerDoesntParticipate_sinceGameAlreadyStarted() throws Exception {
		table.addPlayer(CHRIS);

		assertThat(table.getCurrentPlayer().getName(), is(ALICE));
		table.call();

		assertThat(table.getCurrentPlayer().getName(), is(BOB));
		table.check();

		assertThat(table.getCurrentPlayer().getName(), is(ALICE));
	}
	
	@Test
	public void with3Players_foldFold() throws Exception {
		table.addPlayer(CHRIS);
		table.fold();

		assertBetAndCash(ALICE, 0, 99);
		assertBetAndCash(BOB, 1, 100);
		assertBetAndCash(CHRIS, 2, 98);

		table.fold();		
		assertThat(table.getCurrentPlayer().getName(), is(BOB));

		table.fold();
		assertBetAndCash(ALICE, 2, 97);
		assertBetAndCash(BOB, 0, 100);
		assertBetAndCash(CHRIS, 1, 100);
	}

	@Test
	public void with3Players_aliceFoldsImmediately_bobFoldsLater() throws Exception {
		table.addPlayer(CHRIS);
		table.fold();
		assertBetAndCash(ALICE, 0, 99);
		assertBetAndCash(BOB, 1, 100);
		assertBetAndCash(CHRIS, 2, 98);	
		assertThat(table.getCurrentPlayer().getName(), is(ALICE));

		table.fold();
		table.call();
		table.check();
		assertBetAndCash(ALICE, 0, 99);
		assertBetAndCash(BOB, 2, 99);
		assertBetAndCash(CHRIS, 2, 98);
		assertThat(table.getCommunityCards().size(), is(3));
		assertThat(table.getCurrentPlayer().getName(), is(BOB));

		table.raiseTo(3);
		table.fold();
		assertBetAndCash(ALICE, 2, 97);
		assertBetAndCash(BOB, 0, 103);
		assertBetAndCash(CHRIS, 1, 97);
	}

	@Test
	public void checkThrough() throws Exception {
		/*
		 * XXX we're making assumptions on implementation detail.
		 * 
		 * We assume the 2x2 pre-flop cards are dealt like (alice, alice, bob, bob). If
		 * we'd change this to (alice, bob, alice, bob), the tests will break.
		 * 
		 * Also, this is not quite intuitive and makes the tests hard to understand,
		 * so if we can drive the tests a bit simpler we'd be better off. 
		 * 
		 * Alternative ideas:
		 * A) if we have different poker flavors, like e.g. classic poker (5 hand cards only) 
		 * or Omaha hold'em, then we will need a different implementation of "FindWinners".
		 * This will force us to make FindWinners an interface and have different implementations
		 * for the different poker games. We can use that interface to have a mock implementation
		 * to simulate different winners
		 * Downside: we don't test then the integration between Table and the real FindWinners implementation
		 * 
		 * B) like above, we could also mock one level deeper, i.e. mock the FindBestHand instead.
		 * by this we would be testing the integration between "Table" and "FindWinners".
		 * We would not test the integration between "FindWinners" and "FindBestHand" - this is no problem,
		 * as this integration is already tested through "FindWinnersTest"
		 * 
		 */
		table = new Table(new FixedCardsDeck(SPADES_ACE, CLUBS_ACE, CLUBS_2, CLUBS_3, HEARTS_5, HEARTS_6, HEARTS_7,
				SPADES_9, SPADES_10, HEARTS_JACK, DIAMONDS_JACK, SPADES_JACK, CLUBS_JACK));
		table.addPlayer(ALICE);
		table.addPlayer(BOB);

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
