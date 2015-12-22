package com.sap.ase.poker.acceptance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.sap.ase.poker.model.Table;

public class AcceptanceTest {

	@Test
	public void startGame() throws Exception {
		Table table = new Table();
		table.addPlayer("alice");
		table.addPlayer("bob");
		table.startGame();

		assertThat(table.getCurrentPlayer().getName(), is("alice"));
		assertThat(table.getPlayers().get(0).getBet(), is(1));
		assertThat(table.getPlayers().get(1).getBet(), is(2));
		assertThat(table.getCommunityCards().size(), is(0));
			
		table.call();
		assertThat(table.getCommunityCards().size(), is(0));
		assertThat(table.getCurrentPlayer().getName(), is("bob"));
		
		table.check();
		assertThat(table.getCommunityCards().size(), is(3));
		assertThat(table.getCurrentPlayer().getName(), is("alice"));
	}
}
