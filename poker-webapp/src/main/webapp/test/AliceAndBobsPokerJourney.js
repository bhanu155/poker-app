sap.ui.require([ "poker/test/operations" ], function(operations) {
	var Opa = sap.ui.test.Opa;
	var Opa5 = sap.ui.test.Opa5;

	Opa5.extendConfig({
		viewNamespace : "poker.views.",
		arrangements : operations.arrangements,
		actions : operations.actions,
		assertions : operations.assertions
	});

	opaTest("Start game", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob");
		Then.nameShouldAppearInPlayerList("Alice").
			and.nameShouldAppearInPlayerList("Bob").
			and.currentPlayerShouldBe("Alice").
			and.playerShouldGetTwoCards().
			and.iTeardownMyAppFrame();
	});

	opaTest("Alice bets", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob").
			and.placeBet(1);
		Then.betShouldBeDisplayed(1, "Alice").
			and.currentPlayerShouldBe("Bob").
			and.iTeardownMyAppFrame();
	});

	opaTest("Alice and Bob bet, show the flop", function(Given, When, Then) {
		Given.appStarted();
		When.startGameWithTwoPlayers("Alice", "Bob").
			and.placeBet(1).
			and.placeBet(1);
		Then.communityCardsShouldBeShown(3).
			and.iTeardownMyAppFrame();
	});
});
