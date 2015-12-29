package com.sap.ase.poker.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class TablePlayersTest {

	private static final Player ALICE = new Player("alice", 0);
	private static final Player BOB = new Player("bob", 0);
	private static final Player CHRIS = new Player("chris", 0);
	private TablePlayers players;

	@Before
	public void setup() {
		ALICE.setActive();
		BOB.setActive();
		CHRIS.setActive();
		
		players = new TablePlayers();
		players.add(ALICE);
		players.add(BOB);
	}

	@Test
	public void shouldReturnFirstPlayerInitially() throws Exception {
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void nextPlayerShouldMoveToNextPlayer() throws Exception {
		players.nextPlayer();
		assertThat(players.getCurrentPlayer(), is(BOB));
	}
	
	@Test
	public void nextPlayerWhenLastPlayer() throws Exception {
		players.nextPlayer();
		players.nextPlayer();
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void nextPlayerWith3Players() throws Exception {
		players.add(CHRIS);
		players.nextPlayer();
		players.nextPlayer();
		players.nextPlayer();
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void nextGameShouldMoveStartPlayer() throws Exception {
		players.nextGame();
		assertThat(players.getCurrentPlayer(), is(BOB));
	}
	
	@Test
	public void nextGameWhenLastPlayer() throws Exception {
		players.nextGame();
		players.nextGame();
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void nextGameWith3Players() throws Exception {
		players.add(CHRIS);
		players.nextGame();
		players.nextGame();
		players.nextGame();
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void nextGameShouldAlsoResetCurrentPlayer() throws Exception {
		players.nextPlayer();
		players.nextGame();
		assertThat(players.getCurrentPlayer(), is(BOB));
		players.nextPlayer();
		players.nextPlayer();
		players.nextGame();
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void all() throws Exception {
		Iterator<Player> it = players.iterator();
		assertThat(it.next(),is(ALICE));
		assertThat(it.next(),is(BOB));
	}
}
