package com.sap.ase.poker.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.ase.poker.dto.CardDto;
import com.sap.ase.poker.dto.GetTableResponseDto;
import com.sap.ase.poker.dto.PlayerDto;
import com.sap.ase.poker.model.Player;
import com.sap.ase.poker.model.deck.Card;
import com.sap.ase.poker.model.deck.Kind;
import com.sap.ase.poker.model.deck.Suit;
import com.sap.ase.poker.service.TableService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TableControllerTest {

    private static final String PATH = "/api/v1/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TableService tableService;

    @Test
    void getTable_returnsGetTableResponseDtoWithTableStatus() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("alice");

        GetTableResponseDto tableStatus = createGetTableResponseDto();
        Mockito.when(tableService.getTableStatus(mockPrincipal.getName())).thenReturn(tableStatus);

        MockHttpServletResponse response = mockMvc.perform(get(PATH).principal(mockPrincipal))
                .andExpect(status().isOk()).andReturn().getResponse();

        GetTableResponseDto result = objectMapper.readValue(response.getContentAsString(), GetTableResponseDto.class);

        assertThat(result.getPlayers()).hasSize(2);
    }

    private GetTableResponseDto createGetTableResponseDto() {
        GetTableResponseDto tableStatus = new GetTableResponseDto("alice");
        List<CardDto> communityCardDtos = Arrays.asList(
                new CardDto(new Card(Kind.ACE, Suit.DIAMONDS)),
                new CardDto(new Card(Kind.KING, Suit.DIAMONDS)),
                new CardDto(new Card(Kind.NINE, Suit.CLUBS)));
        List<CardDto> playerCardDtos = Arrays.asList(
                new CardDto(new Card(Kind.ACE, Suit.CLUBS)),
                new CardDto(new Card(Kind.ACE, Suit.HEARTS))
        );
        List<PlayerDto> playerDtos = Arrays.asList(
                new PlayerDto(new Player("alice", "Alice", 100)),
                new PlayerDto(new Player("bob", "Bob", 100)));
        tableStatus.setCommunityCards(communityCardDtos);
        tableStatus.setCurrentPlayer(new PlayerDto(new Player("alice", "Alice", 100)));
        tableStatus.setPlayerCards(playerCardDtos);
        tableStatus.setPlayers(playerDtos);
        return tableStatus;
    }
}
