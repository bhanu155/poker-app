package com.sap.ase.poker;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("table")
@Produces(MediaType.APPLICATION_JSON)
public class TexasPokerService {

	@GET
	@Path("{id}")
	public TableResponse getTable(@PathParam("id") int id) {
		TableResponse response = new TableResponse();
		TableResponse.Player john = new TableResponse.Player();
		john.setName("John");
		response.setPlayers(new TableResponse.Player[] { john });
		return response;
	}
}