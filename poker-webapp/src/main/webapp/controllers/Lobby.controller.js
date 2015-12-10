sap.ui.define([ "sap/ui/core/mvc/Controller" ], function(Controller) {
	"use strict";

	return Controller.extend("poker.controllers.Lobby", {
		
		startGame : function() {

			post("init");
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
