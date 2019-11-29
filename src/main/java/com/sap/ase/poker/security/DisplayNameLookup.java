package com.sap.ase.poker.security;

import java.util.HashMap;
import java.util.Map;

public class DisplayNameLookup {

	private final Map<String, String> users = new HashMap<>();

	public DisplayNameLookup() {
		users.put("al-capone", "Al Capone");
		users.put("pat-garret", "Pat Garret");
		users.put("wyatt-earp", "Wyatt Earp");
		users.put("doc-holiday", "Doc Holiday");
		users.put("wild-bill", "Wild Bill");
		users.put("calamity-jane", "Calamity Jane");
		users.put("kitty-leroy", "Kitty Leroy");
		users.put("madame-moustache", "Madame Moustache");
		users.put("poker-alice", "Poker Alice");

	}

	public String getDisplayNameUserById(String id) {
		return users.get(id);
	}

}
