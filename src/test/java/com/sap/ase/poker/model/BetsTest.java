package com.sap.ase.poker.model;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sap.ase.poker.model.Bets.IllegalAmount;
import com.sap.ase.poker.winner.FindWinners.Winners;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BetsTest {
	private Bets bets;
	private Player alice;
	private Player bob;
	private TablePlayers players;
	
	@BeforeEach
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
		assertTrue(bets.areEven());
	}
	
	@Test
	public void aliceCalls() throws Exception {
		assertThrows(IllegalAmount.class, () -> {
			bets.call();
		});
	}

	@Test
	public void aliceRaisesMinimum() throws Exception {
		bets.raiseTo(1);
		
		assertBetAndCash(alice, 1, 99);
		assertBetAndCash(bob, 0, 100);
		assertFalse(bets.areEven());
	}
	
	@Test
	public void maximumRaisesMaximum() throws Exception {
		bets.raiseTo(100);
		
		assertBetAndCash(alice, 100, 0);
		assertBetAndCash(bob, 0, 100);
		assertFalse(bets.areEven());
	}
	
	@Test
	public void aliceExceedsHerCash() throws Exception {
		assertThrows(IllegalAmount.class, () -> {
			bets.raiseTo(101);
		});
	}
	
	@Test
	public void aliceRaises_bobChecks() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		assertThrows(IllegalAmount.class, () -> {
			bets.check();
		});
	}

	@Test
	public void aliceRaises_bobCalls() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		bets.call();
		
		assertBetAndCash(alice, 1, 99);
		assertBetAndCash(bob, 1, 99);
		assertTrue(bets.areEven());
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
		assertThrows(IllegalAmount.class, () -> {
			bets.raiseTo(1);
		});
	}

	@Test
	public void aliceRaises_bobReRaises() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		bets.raiseTo(2);

		assertBetAndCash(alice, 1, 99);
		assertBetAndCash(bob, 2, 98);
		assertFalse(bets.areEven());
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
		assertTrue(bets.areEven());
	}
	
	@Test
	public void distributePot_foldImmediately() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		bets.fold();
		bets.distributePot(new Winners(asList(alice), alice));
		assertBetAndCash(alice, 0, 100);
		assertBetAndCash(bob, 0, 100);
	}

	@Test
	public void distributePot_afterReRaise() throws Exception {
		bets.raiseTo(1);
		players.nextPlayer();
		bets.raiseTo(2);
		players.nextPlayer();
		bets.fold();
		bets.distributePot(new Winners(asList(bob), alice));
		assertBetAndCash(alice, 0, 99);
		assertBetAndCash(bob, 0, 101);
	}

	@Test
	public void distributePot_splitPot() throws Exception {		
		bets.raiseTo(1);	
		players.nextPlayer();
		bets.call();
		bets.distributePot(new Winners(asList(alice, bob), alice));
		assertBetAndCash(alice, 0, 100);
		assertBetAndCash(bob, 0, 100);
	}
	
	@Test
	public void distributePot_splitPot_oddChip() throws Exception {
		Player cindy = createPlayer("cindy");
		players.add(cindy);
		Player dave = createPlayer("dave");
		players.add(dave);
		bets.raiseTo(1);	
		players.nextPlayer();
		bets.call();
		players.nextPlayer();
		bets.call();
		players.nextPlayer();
		bets.call();
		bets.distributePot(new Winners(asList(alice, bob, cindy), alice));
		assertBetAndCash(alice, 0, 101);
		assertBetAndCash(bob, 0, 100);
		assertBetAndCash(cindy, 0, 100);
		assertBetAndCash(dave, 0, 99);
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
