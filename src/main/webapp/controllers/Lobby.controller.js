sap.ui.define([ "sap/ui/core/mvc/Controller" ], function(Controller) {
	"use strict";

	//Disgusting code, but I WOULDN'T fix it, reason: the lobby is actually right now anyway an intermediate thing.
	//An actual lobby wouldn't have an input form for multiple players and a single hard coded table that gets
	//initialized and started directly, but rather after you logged in (registration/logon is yet another requirement, not even part of the lobby),
	//you would see a bunch of tables and could then join one!
	//When a table is "started", is at this point certainly something that needs to be discussed, e.g. automatically, if at least two players with enough cash are on the table.
	//So, instead of improving this code, rather leave it, and replace this class with a real lobby instead.
	return Controller.extend("poker.controllers.Lobby", {
		
		startGame : function() {
			post("players", {
				"playerName" : this.byId("player1").getValue()
			});
			post("players", {
				"playerName" : this.byId("player2").getValue()
			});
			post("startGame");
						
			this.fireEvent("startGame");
		}
	});
	
	function post(path, data) {
		jQuery.ajax({
			url : "api/table/1/" + path,
			data : JSON.stringify(data),
			method : "POST",
			async : false,
			contentType : "application/json"
		});		
	}
});
