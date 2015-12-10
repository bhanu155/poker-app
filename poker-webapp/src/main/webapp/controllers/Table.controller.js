sap.ui.define([ "sap/ui/core/mvc/Controller" ], function(Controller) {
	"use strict";

	return Controller.extend("poker.controllers.Table", {
		
		joinTable : function() {
			var jsonModel = this.getView().getModel();

			var playerName = this.byId("nameInputField").getValue();

			jQuery.ajax({
				url : "api/table/1/players",
				method : "POST",
				data : JSON.stringify({
					"playerName" : playerName
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
		
		resetTable: function() {
			var jsonModel = this.getView().getModel();

			jQuery.ajax({
				url : "api/table/1/init",
				method : "POST",
				contentType : "application/json",
				success : function() {
					jsonModel.loadData("api/table/1");
				}
			});
		},
		
		placeBet : function() {
			var jsonModel = this.getView().getModel();

			var amount = this.byId("valueInputField").getValue();

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"amount" : amount
				}),
				contentType : "application/json",
				success : function() {
					jsonModel.loadData("api/table/1");
				}
			});
		}
	});
});
