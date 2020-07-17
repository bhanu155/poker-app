package com.sap.ase.poker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Iterator;

public class TablePlayersTest {

	private static final Player ALICE = new Player("alice", 0);
	private static final Player BOB = new Player("bob", 0);
	private static final Player CHRIS = new Player("chris", 0);
	private TablePlayers players;

	@BeforeEach
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
		players.nextStartPlayer();
		assertThat(players.getCurrentPlayer(), is(BOB));
	}
	
	@Test
	public void nextGameWhenLastPlayer() throws Exception {
		players.nextStartPlayer();
		players.nextStartPlayer();
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void nextGameWith3Players() throws Exception {
		players.add(CHRIS);
		players.nextStartPlayer();
		players.nextStartPlayer();
		players.nextStartPlayer();
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void nextGameShouldAlsoResetCurrentPlayer() throws Exception {
		players.nextPlayer();
		players.nextStartPlayer();
		assertThat(players.getCurrentPlayer(), is(BOB));
		players.nextPlayer();
		players.nextPlayer();
		players.nextStartPlayer();
		assertThat(players.getCurrentPlayer(), is(ALICE));
	}
	
	@Test
	public void all() throws Exception {
		Iterator<Player> it = players.toList().iterator();
		assertThat(it.next(), is(ALICE));
		assertThat(it.next(), is(BOB));
	}
}
