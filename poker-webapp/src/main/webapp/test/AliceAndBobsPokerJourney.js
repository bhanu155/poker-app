var Opa = sap.ui.test.Opa;
var Opa5 = sap.ui.test.Opa5;
QUnit.config.testTimeout = 60000;

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

var arrangements = new sap.ui.test.Opa5({
	appStarted : function() {
		jQuery.ajax({
			url : "api/table/1/init",
			method : "POST",
			async : false
		});
		return this.iStartMyAppInAFrame("./index.html");
	}
});

var actions = new sap.ui.test.Opa5({
	startGameWithTwoPlayers : function(player1, player2) {
		return this.waitFor(fillInput("Lobby", "player1", player1)).
				and.waitFor(fillInput("Lobby", "player2", player2)).
				and.waitFor(tapButton("Lobby", "start"));
	},
	placeBet : function(amount) {
		return this.waitFor(fillInput("Table", "amount", amount)).
				and.waitFor(tapButton("Table", "bet"));
	}
});

var assertions = new sap.ui.test.Opa5({
	nameShouldAppearInPlayerList : function(playerName) {
		return this.waitFor(controlTypePresent("Table", "sap.m.ListItemBase", {
			matchers : new sap.ui.test.matchers.Properties({
				title : playerName
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
		return this.waitFor(controlTypePresent("Table", "sap.m.ObjectListItem", {
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
		return this.waitFor(controlTypePresent("Table", "poker.controls.Card", {
			success : function(items) {
				strictEqual(items.length, numberOfCards);
			}
		}));
	}
});

Opa5.extendConfig({
	viewNamespace : "poker.views.",
	arrangements : arrangements,
	actions : actions,
	assertions : assertions
});

opaTest("Start game", function(Given, When, Then) {
	Given.appStarted();
	When.startGameWithTwoPlayers("Alice", "Bob");
	Then.nameShouldAppearInPlayerList("Alice").and.nameShouldAppearInPlayerList("Bob").and
			.currentPlayerShouldBe("Alice").and.playerShouldGetTwoCards().and.iTeardownMyAppFrame();
});

opaTest("Alice bets", function(Given, When, Then) {
	Given.appStarted();
	When.startGameWithTwoPlayers("Alice", "Bob").and.placeBet(1);
	Then.betShouldBeDisplayed(1, "Alice").and.currentPlayerShouldBe("Bob").and.iTeardownMyAppFrame();
});

opaTest("Alice and Bob bet, show the flop", function(Given, When, Then) {
	Given.appStarted();
	When.startGameWithTwoPlayers("Alice", "Bob").and.placeBet(1).and.placeBet(1);
	Then.communityCardsShouldBeShown(3).and.iTeardownMyAppFrame();
});