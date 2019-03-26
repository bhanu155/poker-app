sap.ui.define([ "sap/ui/core/mvc/Controller" ], function(Controller) {
	"use strict";
	const tableId = '1';
	
	return Controller.extend("poker.controllers.Table", {

		getCookie : function(name) {
			var value = "; " + document.cookie;
			var parts = value.split("; " + name + "=");
			if (parts.length == 2) return parts.pop().split(";").shift();
		},
		
		onInit : function() {
			setInterval(() => {
				jQuery.ajax({
					url : `api/table/${tableId}`,
					method : "GET",
					success : (data) => {
						const currentPlayerIsMe = (this.getCookie("UserName") == data.currentPlayer);
						this.getView().byId("toolbar").setEnabled(currentPlayerIsMe); 
						this.getView().getModel().setData(data);
						
						playersList.getItems().forEach(function(item) {
							if (item.getTitle() === data.currentPlayer) {								
								item.addStyleClass("currentPlayer");
							} else {
								item.removeStyleClass("currentPlayer");
							}
						});
					}
				});				
			}, 1000);
			
			var view = this.getView();
			var jsonModel = view.getViewData().model;
			var playersList = view.byId("players");
			view.setModel(jsonModel);

			view.getModel().attachRequestCompleted(function(oEvent) {
				var data = oEvent.getSource().getData();
			});
		},

		call : function() {
			bet({ "action" : "call" });
		},
		
		raiseTo : function() {
			var amount = this.byId("amount").getValue();
			bet({ "action" : "raiseTo", "amount" : amount });
		},

		check : function() {
			bet({ "action" : "check" })
		},

		fold : function() {
			var model = this.getView().getModel();
			var playerName = this.playerName;
			bet({ "action" : "fold" })
		},
		
		bet : function(betDetails) {
			jQuery.ajax({
				url : `api/table/${tableId}/bets`,
				method : "POST",
				data : JSON.stringify(betDetails),
				contentType : "application/json",
				success : () => {
					this.getView().byId("toolbar").setEnabled(false);
				}
			});			
		}
	});
});
