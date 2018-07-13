package com.sap.ase.poker.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.ase.poker.model.Bets.IllegalOperationException;

public class BetsTest {
	private Bets bets;
	private Player alice;
	private Player bob;
	private TablePlayers players;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		players = new TablePlayers();
		alice = createPlayer("alice");
		players.add(alice);
		bob = createPlayer("bob");
		players.add(bob);
		
		bets = new Bets(players);
	}

	@Test
	public void aliceChecks() throws Exception {
		bets.check();
		
		assertBetAndCash(alice, 0, 100);
		assertBetAndCash(bob, 0, 100);
		assertTrue(bets.areAllBetsEven());
	}
	
	@Test
	public void aliceCalls() throws Exception {
		thrown.expect(IllegalOperationException.class);
		bets.call();
	}

	@Test
	public void aliceRaisesMinimum() throws Exception {
		bets.raiseTo(1);
		
		assertBetAndCash(alice, 1, 99);
		assertBetAndCash(bob, 0, 100);
		assertFalse(bets.areAllBetsEven());
	}
	
	@Test
	public void maximumRaisesMaximum() throws Exception {
		bets.raiseTo(100);
		
		assertBetAndCash(alice, 100, 0);
		assertBetAndCash(bob, 0, 100);
		assertFalse(bets.areAllBetsEven());
	}
	
	@Test
	public void aliceExceedsHerCash() throws Exception {
		thrown.expect(IllegalOperationException.class);
		bets.raiseTo(101);
	}
	
	@Test
	public void aliceRaises_bobChecks() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		thrown.expect(IllegalOperationException.class);
		bets.check();
	}

	@Test
	public void aliceRaises_bobCalls() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		bets.call();
		
		assertBetAndCash(alice, 1, 99);
		assertBetAndCash(bob, 1, 99);
		assertTrue(bets.areAllBetsEven());
	}
	
	@Test
	public void aliceFolds() throws Exception {
		bets.fold();
		assertFalse(alice.isActive());
		assertTrue(bob.isActive());
	}
	
	@Test
	public void aliceRaises_bobFolds() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		bets.fold();
		assertTrue(alice.isActive());
		assertFalse(bob.isActive());
	}
	
	@Test
	public void aliceRaises_bobTriesToRaiseLess() throws Exception {
		bets.raiseTo(2);
		players.nextPlayer();
		thrown.expect(IllegalOperationException.class);
		bets.raiseTo(1);
	}

	@Test
	public void aliceRaises_bobReRaises() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		bets.raiseTo(2);

		assertBetAndCash(alice, 1, 99);
		assertBetAndCash(bob, 2, 98);
		assertFalse(bets.areAllBetsEven());
	}
	
	@Test
	public void aliceRaises_bobReRaises_aliceCalls() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		bets.raiseTo(2);
		players.nextPlayer();
		bets.call();

		assertBetAndCash(alice, 2, 98);
		assertBetAndCash(bob, 2, 98);
		assertTrue(bets.areAllBetsEven());
	}
	
	private void assertBetAndCash(Player player, int bet, int cash) {
		assertThat(player.getBet(), is(bet));
		assertThat(player.getCash(), is(cash));
	}
	
	private Player createPlayer(String name) {
		Player player = new Player(name, 100);
		player.setActive();
		return player;
	}
}
