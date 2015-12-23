package com.sap.ase.poker.acceptance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.Table;

public class AcceptanceTest {

	@Test
	public void startGame() throws Exception {
		Table table = new Table();
		table.addPlayer("alice");
		table.addPlayer("bob");
		table.startGame();

		assertThat(table.getCurrentPlayer().getName(), is("alice"));
		Iterator<Player> players = table.getPlayers().iterator();
		
		assertBetAndCash(players.next(), 1, 99);
		assertBetAndCash(players.next(), 2, 98);
		
		assertThat(table.getCommunityCards().size(), is(0));
			
		table.call();
		assertThat(table.getCommunityCards().size(), is(0));
		assertThat(table.getCurrentPlayer().getName(), is("bob"));
		
		table.check();
		assertThat(table.getCommunityCards().size(), is(3));
		assertThat(table.getCurrentPlayer().getName(), is("alice"));
		
	}

	@Test
	public void testFold() throws Exception {
		Table table = new Table();
		table.addPlayer("alice");
		table.addPlayer("bob");
		table.startGame();
		
		table.fold();
		assertThat(table.getCurrentPlayer().getCash(), is(101));

	}
	private void assertBetAndCash(Player player2, int bet, int cash) {
		assertThat(player2.getBet(), is(bet));
		assertThat(player2.getCash(), is(cash));
	}
}
