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

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"action" : "call"
				}),
				contentType : "application/json",
				success : function() {
					model.loadData("api/table/1");
				}
			});
		},
		raiseTo : function() {

			var amount = this.byId("amount").getValue();
			var model = this.getView().getModel();

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"action" : "raiseTo",
					"amount" : amount
				}),
				contentType : "application/json",
				success : function() {
					model.loadData("api/table/1");
				}
			});
		},

		check : function() {
			var model = this.getView().getModel();

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"action" : "check"
				}),
				contentType : "application/json",
				success : function() {
					model.loadData("api/table/1");
				}
			});
		},

		fold : function() {
			var model = this.getView().getModel();

			jQuery.ajax({
				url : "api/table/1/bets",
				method : "POST",
				data : JSON.stringify({
					"action" : "fold"
				}),
				contentType : "application/json",
				success : function() {
					model.loadData("api/table/1");
				}
			});
		},

		refreshData : function() {
			this.getView().getModel().loadData("api/table/1");
		}
	});
});
