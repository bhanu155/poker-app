var Opa = sap.ui.test.Opa;
var Opa5 = sap.ui.test.Opa5;
QUnit.config.testTimeout = 60000;

var arrangements = new sap.ui.test.Opa5({
	appStarted : function() {
		jQuery.ajax({
			url : "api/table/1",
			method : "DELETE",
			async : false
		});
		return this.iStartMyAppInAFrame("./index.html");
	}
});

var actions = new sap.ui.test.Opa5({
	playerJoins : function(playerName) {
		return this.waitFor({
			id : "nameInputField",
			viewName : "Table",
			success : function(nameInputField) {
				nameInputField.setValue(playerName);
			},
			errorMessage : "did not find the name input field"
		}).and.waitFor({
			id : "joinButton",
			viewName : "Table",
			success : function(joinButton) {
				joinButton.$().tap();
			},
			errorMessage : "did not find the join button"
		});
	},
	gameStarted : function() {
		return this.waitFor({
			id : "startButton",
			viewName : "Table",
			success : function(startButton) {
				setTimeout(function() {
					startButton.$().tap();
				}, 10);
			},
			errorMessage : "did not find the start button"
		});
	},
	betPlaced : function(value) {
		return this.waitFor({
			id : "valueInputField",
			viewName : "Table",
			success : function(valueInputField) {
				valueInputField.setValue(value);
			},
			errorMessage : "did not find the value input field"
		}).and.waitFor({
			id : "betButton",
			viewName : "Table",
			success : function(betButton) {
				setTimeout(function() {
					betButton.$().tap();
				}, 10);
			},
			errorMessage : "did not find the bet button"
		});
	}
});

var assertions = new sap.ui.test.Opa5({
	playerListShouldBeEmpty : function() {
		return this.waitFor({
			id : "players",
			viewName : "Table",
			success : function(players) {
				strictEqual(players.getItems().length, 0);
			},
			errorMessage : "did not find the players on the table"
		});
	},
	nameShouldAppearInThePlayersList : function(playerName) {
		return this.waitFor({
			controlType : "sap.m.ListItemBase",
			matchers : new sap.ui.test.matchers.Properties({
				title : playerName
			}),
			viewName : "Table",
			success : function(items) {
				ok(true);
			},
			errorMessage : "did not find the players on the table"
		});
	},
	playerShouldGetTwoCards : function() {
		return this.waitFor({
			controlType : "sap.m.StandardTile",
			viewName : "Table",
			success : function(items) {
				ok(true);
				strictEqual(items.length, 2);
			},
			errorMessage : "did not find the cards"
		});
	},
	betShouldBeDisplayed : function(value, playerName) {
		return this.waitFor({
			controlType : "sap.m.ListItemBase",
			matchers : new sap.ui.test.matchers.Properties({
				title : playerName,
				description : value
			}),
			viewName : "Table",
			success : function(items) {
				ok(true);
			},
			errorMessage : "did not find the players on the table"
		});
	},
	currentPlayerShouldBe : function(playerName) {
		return this.waitFor({
			id : "currentPlayer",
			viewName : "Table",
			matchers : function(currentPlayerText) {
				return currentPlayerText.getText() === playerName;
			},
			errorMessage : "did not find the current player info on the table"
		});
	},
	communityCardsShouldBeShown : function() {
		return this.waitFor({
			id : "communityCards",
			viewName : "Table",
			matchers : function(communityCards) {
				return communityCards.getTiles().length === 3;
			},
			success : function(communityCards) {
				ok(true);
			},
			errorMessage : "did not find the community cards"
		});
	}
});

Opa5.extendConfig({
	viewNamespace : "poker.views.",
	arrangements : arrangements,
	actions : actions,
	assertions : assertions
});

opaTest("Should see the list of players", function(Given, When, Then) {
	Given.appStarted();
	Then.playerListShouldBeEmpty();
});

opaTest("Should be able to join the table", function(Given, When, Then) {
	Given.appStarted();
	When.playerJoins("john");
	Then.nameShouldAppearInThePlayersList("john");
});

opaTest("Should be able to start the game", function(Given, When, Then) {
	Given.appStarted();
	When.playerJoins("john").and.playerJoins("cindy").and.gameStarted();
	Then.playerShouldGetTwoCards("john");
});

opaTest("Should be able to place a bet", function(Given, When, Then) {
	Given.appStarted();
	When.playerJoins("john").and.playerJoins("cindy").and.gameStarted().and
			.betPlaced(10);
	Then.betShouldBeDisplayed("10", "john").and.currentPlayerShouldBe("cindy");
});

opaTest("Should display community cards when bets are equal", function(Given, When, Then) {
	Given.appStarted();
	When.playerJoins("john").and.playerJoins("cindy").and.gameStarted().and.betPlaced(10).and.betPlaced(10);
	Then.communityCardsShouldBeShown();// .and.currentPlayerShouldBe("john");
});