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
		Then.playerShouldHaveBet("Alice", 1).
			and.playerShouldHaveBet("Bob", 2).
			and.currentPlayerShouldBe("Alice").
			and.playerShouldGetTwoCards().
			and.iTeardownMyAppFrame();
	});

	opaTest("Alice calls", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob").
			and.call();
		Then.betShouldBeDisplayed(2, "Alice").
			and.currentPlayerShouldBe("Bob").
			and.iTeardownMyAppFrame();
	});

	opaTest("Alice calls, Bob checks, show the flop", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob").
			and.call().
			and.check();
		Then.communityCardsShouldBeShown(3).
			and.iTeardownMyAppFrame();
	});
});
