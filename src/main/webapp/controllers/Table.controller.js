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

			//XXX if we had a somewhat "cleaner" REST interface from the server, we could try to implement a
			//true JSON-based custom UI5 model, so we don't need all these ajax calls in the controller...
			//See also the corresponding comment in the server code (TableService.placeBet)
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
		
		//XXX see the comment below - what do we need the playerName for??
		setPlayer : function(name) {
			this.playerName = name;
		}
	});
	
	//XXX WTF? Why do we need the playerName in order to _load_ the current table information?
	//And what the hell is the playerName? The name of the current player or what?
	//I'm afraid if the code is just that non-intuitive and self-explanatory for whatever reason, we'd better leave a comment here that explains why this is needed...
	function loadData(model, playerName) {
		model.loadData("api/table/1", undefined, undefined, undefined, undefined, undefined, { "Player" : playerName })
	}
});
