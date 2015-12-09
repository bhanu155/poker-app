var Opa = sap.ui.test.Opa;
var Opa5 = sap.ui.test.Opa5;
QUnit.config.testTimeout = 60000;

var arrangements = new sap.ui.test.Opa5({
	iStartMyApp : function() {
		return this.iStartMyAppInAFrame("./index.html");
	}
});

var actions = new sap.ui.test.Opa5({
	iJoinTheGame : function(playerName) {
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
	iStartTheGame : function() {
		return this.waitFor({
			id : "startButton",
			viewName : "Table",
			success : function(startButton) {
				startButton.$().tap();
			},
			errorMessage : "did not find the start button"
		});
	}
});

var assertions = new sap.ui.test.Opa5({
	iShouldSeeAnEmptyTable : function() {
		return this.waitFor({
			id : "players",
			viewName : "Table",
			success : function(players) {
				strictEqual(players.getItems().length, 0);
			},
			errorMessage : "did not find the players on the table"
		});
	},
	iShouldAppearInThePlayersList : function(playerName) {
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
	iShouldGetTwoCards : function() {
		return this.waitFor({
			controlType : "sap.m.StandardTile",
			viewName : "Table",
			success : function(items) {
				strictEqual(items.length, 2);
			},
			errorMessage : "did not find the cards"
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
	Given.iStartMyApp();
	Then.iShouldSeeAnEmptyTable();
	Then.iTeardownMyAppFrame();
});

opaTest("Should be able to join the table", function(Given, When, Then) {
	Given.iStartMyApp();
	When.iJoinTheGame("john");
	Then.iShouldAppearInThePlayersList("john");
});

opaTest("Should be able to start the game", function(Given, When, Then) {
	Given.iStartMyApp();
	When.iJoinTheGame("cindy").and.iStartTheGame();
	Then.iShouldGetTwoCards();
});