sap.ui.define([ "sap/ui/core/mvc/Controller" ], function(Controller) {
	"use strict";

	return Controller.extend("poker.controllers.Table", {
		
		joinTable : function() {
			var jsonModel = this.getView().getModel();

			var name = this.byId("nameInputField").getValue();

			jQuery.ajax({
				url : "api/table/1/players",
				method : "POST",
				data : JSON.stringify({
					"name" : name
				}),
				contentType : "application/json",
				success : function() {
					jsonModel.loadData("api/table/1");
				}
			});
		},

		startGame : function() {
			var jsonModel = this.getView().getModel();

			jQuery.ajax({
				url : "api/table/1/startGame",
				method : "POST",
				contentType : "application/json",
				success : function() {
					jsonModel.loadData("api/table/1");
				}
			});
		},
		
		placeBet : function() {
			var jsonModel = this.getView().getModel();

			var value = this.byId("valueInputField").getValue();

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"value" : value
				}),
				contentType : "application/json",
				success : function() {
					jsonModel.loadData("api/table/1");
				}
			});
		}
	});
});
