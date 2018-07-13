sap.ui.define([ "sap/ui/core/Control" ], function(Control) {
	"use strict";

	// Unicodes: https://en.wikipedia.org/wiki/Playing_cards_in_Unicode

	function suitToUnicodeChar(suit) {
		if (suit === "spades") {
			return "A";
		} else if (suit === "hearts") {
			return "B";
		} else if (suit === "diamonds") {
			return "C";
		} else if (suit === "clubs") {
			return "D";
		} else {
			throw new Error("unknown suit: " + suit);
		}
	}

	function kindToUnicodeChar(kind) {
		if (kind === "ace") {
			return "1";
		} else if (parseInt(kind) >= 2 && parseInt(kind) <= 9) {
			return kind;
		} else if (kind === "10") {
			return "A";
		} else if (kind === "jack") {
			return "B";
		} else if (kind === "queen") {
			return "D";
		} else if (kind === "king") {
			return "E";
		} else {
			throw new Error("unknown kind: " + kind);
		}
	}
	
	var toUnicode = function(suit, kind) {
		return "&#x1F0" + suitToUnicodeChar(suit) + kindToUnicodeChar(kind);
	};

	var getFontColor = function(suit) {
		return (suit === "hearts" || suit === "diamonds") ? "red" : "black";
	}

	return Control.extend("poker.controls.Card", {
		metadata : {
			properties : {
				suit : {
					type : "string"
				},
				kind : {
					type : "string"
				}
			}
		},
		renderer : function(oRM, oControl) {
			oRM.write("<div");
			oRM.writeControlData(oControl);
			oRM.writeClasses();
			if (getFontColor(oControl.getSuit()) === "red") {
				oRM.write("><div style=\"font-size:5.5em;color:red;\">");
			} else {
				oRM.write("><div style=\"font-size:5.5em;color:black;\">");
			}
			oRM.write(toUnicode(oControl.getSuit(), oControl.getKind()));
			oRM.write("</div></div>");
		}
	});
});