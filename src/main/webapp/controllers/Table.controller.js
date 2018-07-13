sap.ui.define([ "sap/ui/core/mvc/Controller" ], function(Controller) {
	"use strict";

	return Controller.extend("poker.controllers.Table", {

		onInit : function() {
			var view = this.getView();
			var jsonModel = view.getViewData().model;
			var playersList = view.byId("players");
			view.setModel(jsonModel);

			view.getModel().attachRequestCompleted(function(oEvent) {
				var data = oEvent.getSource().getData();
				playersList.getItems().forEach(function(item) {
					if (item.getTitle() === data.currentPlayer) {
						item.addStyleClass("currentPlayer");
					} else {
						item.removeStyleClass("currentPlayer");
					}
				});
			});
		},

		call : function() {
			var model = this.getView().getModel();
			var playerName = this.playerName;

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"action" : "call"
				}),
				contentType : "application/json",
				success : function() {
					loadData(model, playerName);
				}
			});
		},
		raiseTo : function() {

			var amount = this.byId("amount").getValue();
			var model = this.getView().getModel();
			var playerName = this.playerName;

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"action" : "raiseTo",
					"amount" : amount
				}),
				contentType : "application/json",
				success : function() {
					loadData(model, playerName);
				}
			});
		},

		check : function() {
			var model = this.getView().getModel();
			var playerName = this.playerName;

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"action" : "check"
				}),
				contentType : "application/json",
				success : function() {
					loadData(model, playerName);
				}
			});
		},

		fold : function() {
			var model = this.getView().getModel();
			var playerName = this.playerName;
			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"action" : "fold"
				}),
				contentType : "application/json",
				success : function() {
					loadData(model, playerName);
				}
			});
		},

		refreshData : function() {
			loadData(this.getView().getModel(), this.playerName);
		},
		
		setPlayer : function(name) {
			this.playerName = name;
		}
	});
	
	function loadData(model, playerName) {
		model.loadData("api/table/1", undefined, undefined, undefined, undefined, undefined, { "Player" : playerName })
	}
});
