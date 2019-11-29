sap.ui.require([ "poker/test/operations" ], function(operations) {
	var Opa = sap.ui.test.Opa;
	var Opa5 = sap.ui.test.Opa5;

	Opa5.extendConfig({
		viewNamespace : "poker.views.",
		arrangements : operations.arrangements,
		actions : operations.actions,
		assertions : operations.assertions
	});

	opaTest("Start game, small and big blind should be set", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob");
		Then.playerShouldHaveCash("Alice", 99).and.playerShouldHaveBet("Alice", 1).
			and.playerShouldHaveCash("Bob", 98).and.playerShouldHaveBet("Bob", 2).
			and.currentPlayerShouldBe("Alice").
			and.playerShouldGetTwoCards().
			and.iTeardownMyAppFrame();
	});

	opaTest("Alice calls, next player should be bob", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob").
			and.call();
		Then.playerShouldHaveBet("Alice", 2).
			and.currentPlayerShouldBe("Bob").
			and.iTeardownMyAppFrame();
	});

	opaTest("Alice calls, Bob checks, show the flop", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob").
			and.call().
			and.check();
		Then.communityCardsShouldBeShown(3).
			and.currentPlayerShouldBe("Alice").
			and.iTeardownMyAppFrame();
	});
	
	opaTest("Alice folds, Bob should win the small blind from Alice", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob").
			and.fold();
		Then.playerShouldHaveCash("Bob", 100).
			and.playerShouldHaveCash("Alice", 97).
			and.iTeardownMyAppFrame();
	});
	
	opaTest("Alice raises", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob").
			and.raiseTo(3)
		Then.playerShouldHaveCash("Alice", 97).and.playerShouldHaveBet("Alice", 3).
			and.iTeardownMyAppFrame();
	});
});
