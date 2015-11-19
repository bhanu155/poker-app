var Opa = sap.ui.test.Opa;
var Opa5 = sap.ui.test.Opa5;
QUnit.config.testTimeout = 60000;

var arrangements = new sap.ui.test.Opa5({
	iStartMyApp : function() {
		return this.iStartMyAppInAFrame("./index.html");
	}
});

var actions = new sap.ui.test.Opa5({});

var assertions = new sap.ui.test.Opa5({
	iShouldSeeAppAndPage : function() {
		return this.waitFor({
			id : "poker-app",
			check : function(oApp) {
				if (!oApp) {
					return false;
				} else {
					return true;
				}
			},
			success : function(oApp) {
				ok(false, "not yet implemented");
			},
			errorMessage : "not yet implemented"
		});
	}
});

Opa5.extendConfig({
	viewNamespace : "poker.views.",
	arrangements : arrangements,
	actions : actions,
	assertions : assertions
});

opaTest("Opa test for my app", function(Given, When, Then) {
	// Arrangements
	Given.iStartMyApp();

	//Actions
	//When.iPressOnTheButton();

	// Assertions
	Then.iShouldSeeAppAndPage();
	Then.iTeardownMyAppFrame();
});