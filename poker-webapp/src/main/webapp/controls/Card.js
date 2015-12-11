sap.ui.define([
	"sap/ui/core/Control"
], function (Control) {
	"use strict";
	
	var toUnicode = function(suit, kind){
		
		//See here for unicodes https://en.wikipedia.org/wiki/Playing_cards_in_Unicode
		//insert in your view:
		//<poker:Card class="sapUiSmallMarginBeginEnd" suit="Spade" kind="10"/>
		
		var unicode = "&#x1F0"
		if(suit === "Spade"){
			unicode = unicode + "A";
		}else if(suit === "Heart"){
			unicode = unicode + "B";
		}else if(suit === "Diamond"){
			unicode = unicode + "C";
		}else if(suit === "Club"){
			unicode = unicode + "D";
		}		
		if(kind >= 2 && kind <= 9){
			unicode = unicode + kind;
		}else{
			if(kind === "10"){
				unicode = unicode + "A";
			}else if(kind === "Jack"){
				unicode = unicode + "B";
			}else if(kind === "Queen"){
				unicode = unicode + "D";
			}else if(kind === "King"){
				unicode = unicode + "E";
			}else if(kind === "Ace"){
				unicode = unicode + "1";
			}
		}
		
		if(suit === "default" || kind === "default"){
			unicode = unicode + "F1";
		}
		
		return unicode;
		
	};
	
	var getFontColor = function(suit){
		if(suit === "Heart" || suit === "Diamond"){
			return "red";
		}
		return "black";
	}

	return Control.extend("poker.controls.Card", {
		metadata : {
			properties : {
				suit: 	{type : "string", defaultValue : "default"},
				kind: 	{type : "string", defaultValue : "default"}
			},
			aggregations : {
				
			},
			events : {
				
			}
		},
		init : function () {
			
		},
		renderer : function (oRM, oControl) {
			oRM.write("<div");
			oRM.writeControlData(oControl);
			oRM.writeClasses();
			if(getFontColor(oControl.getSuit()) === "red"){
				oRM.write("><div style=\"font-size:200px;color:red;\">");
			}else{
				oRM.write("><div style=\"font-size:200px;color:black;\">");
			}			
			oRM.write(toUnicode(oControl.getSuit(), oControl.getKind()));			
			oRM.write("</div></div></br></br>");
		}
	});
});