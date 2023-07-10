package com.sap.ase.poker.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sap.ase.poker.model.GameState;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.Deck;

@SpringBootTest
class TableServiceTest {
	
	@Autowired
	TableService tableService;
	
	@MockBean
	Supplier<Deck> deckSupplier;
	
	@BeforeEach
	void setUp(){
		
	}
	
	@Test
	void getStateOfTheGame(){
		GameState gameState = tableService.getState();
		assertThat(gameState).isEqualTo(GameState.OPEN);
		
	}
	
	@Test
	void isEmptyPlayersList(){
		 List<Player> playerList = tableService.getPlayers();
		 assertThat(playerList.size()).isEqualTo(0);
	}
	
	@Test
	void onePlayerShouldReturnFromPlayerList() {
		tableService.addPlayer("SBR", "Raghav");
		assertThat(tableService.getPlayers().size()).isEqualTo(1);
	}
	
	@ParameterizedTest
	@MethodSource("provideParameters")
	void addPlayerToDeck(String id,String name) {
		tableService.addPlayer(id, name);
		Player player = tableService.getPlayers().get(tableService.getPlayers().size()-1);
		assertThat(player.getName()).isEqualTo(name);
		assertThat(player.getCash()).isEqualTo(100);
		assertThat(player.isActive()).isFalse();
	}
	
	private static Stream<Arguments> provideParameters() {
	    return Stream.of(
	            Arguments.of("SBR", "Raghav"),
	            Arguments.of("CB", "Chandra Bhanu"),
	            Arguments.of("TGS", "Thimmaraju")
	    );
	}

	 
	
	
}