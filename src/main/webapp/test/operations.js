sap.ui.define([], function() {

	var fillInput = function(viewName, id, value) {
		return {
			id : id,
			viewName : viewName,
			success : function(inputField) {
				inputField.setValue(value);
			},
			errorMessage : "did not find the input field with id " + id + " in view " + viewName
		};
	};

	var tapButton = function(viewName, id) {
		return {
			id : id,
			viewName : viewName,
			success : function(button) {
				button.$().tap();
			},
			errorMessage : "did not find the button with id " + id + " in view " + viewName
		};
	};

	var elementPresent = function(viewName, id, options) {
		options = options || {};
		options.success = options.success || function(button) {
			ok(true);
		};

		return {
			id : id,
			viewName : viewName,
			matchers : options.matchers,
			success : options.success,
			errorMessage : "did not find the element with id " + id + " in view " + viewName
		};
	};

	var controlTypePresent = function(viewName, controlType, options) {
		options = options || {};
		options.success = options.success || function(button) {
			ok(true);
		};

		return {
			controlType : controlType,
			viewName : viewName,
			matchers : options.matchers,
			success : options.success,
			errorMessage : "did not find a matching element of control type " + controlType + " in view " + viewName
		};
	};

	return {
		arrangements : new sap.ui.test.Opa5({
			appStarted : function() {
				jQuery.ajax({
					url : "api/table/1/init",
					method : "POST",
					async : false
				});
				return this.iStartMyAppInAFrame("./index.html");
			}
		}),

		actions : new sap.ui.test.Opa5({
			startGameWithTwoPlayers : function(player1, player2) {
				return this.waitFor(fillInput("Lobby", "player1", player1)).and.waitFor(fillInput("Lobby", "player2",
						player2)).and.waitFor(tapButton("Lobby", "start"));
			},
			raiseTo : function(amount) {
				return this.waitFor(fillInput("Table", "amount", amount)).and.waitFor(tapButton("Table", "raise"));
			},
			check : function(){
				return this.waitFor(tapButton("Table", "check"));
			},
			call : function(){
				return this.waitFor(tapButton("Table", "call"));
			},
			fold : function(){
				return this.waitFor(tapButton("Table", "fold"));
			}	
			
		}),

		assertions : new sap.ui.test.Opa5({
			playerShouldHaveBet : function(playerName, bet) {
				return this.waitFor(controlTypePresent("Table", "sap.m.ListItemBase", {
					matchers : new sap.ui.test.matchers.Properties({
						title : playerName,
						info : "Bet: " + bet
					})
				}));
			},
			playerShouldHaveCash : function(playerName, cash) {
				return this.waitFor(controlTypePresent("Table", "sap.m.ListItemBase", {
					matchers : new sap.ui.test.matchers.Properties({
						title : playerName,
						description : "Cash: " + cash
					})
				}));
			},
			currentPlayerShouldBe : function(playerName) {
				return this.waitFor(controlTypePresent("Table", "sap.m.ListItemBase", {
					matchers : function(item) {
						return item.hasStyleClass("currentPlayer");
					},
					success : function(items) {
						strictEqual(items.length, 1);
						strictEqual(items[0].getTitle(), playerName);
					}
				}));
			},
			playerShouldGetTwoCards : function() {
				return this.waitFor(controlTypePresent("Table", "poker.controls.Card", {
					success : function(items) {
						strictEqual(items.length, 2);
					}
				}));
			},
			betShouldBeDisplayed : function(amount, playerName) {
				return this.waitFor(controlTypePresent("Table", "sap.m.ListItemBase", {
					matchers : new sap.ui.test.matchers.Properties({
						title : playerName,
						counter : amount
					})
				}));
			},
			communityCardsShouldBeShown : function(numberOfCards) {
				var communityCardsId;
				return this.waitFor(elementPresent("Table", "communityCards", {
					success : function(communityCards) {
						communityCardsId = communityCards.getId();
					}
				})).and.waitFor(controlTypePresent("Table", "poker.controls.Card", {
					matchers : function(item) {
						return item.getParent().getId() === communityCardsId;
					},
					success : function(items) {
						strictEqual(items.length, numberOfCards);
					}
				}));
			}
		})
	};
});
