package com.sap.ase.security;

import java.util.HashMap;
import java.util.Map;

public class Users {

	private final Map<String, User> users = new HashMap<>();

	public Users() {
		users.put("al-capone", new User("Al Capone", "welcomeAl"));
		users.put("pat-garret", new User("Pat Garret", "welcomePat"));
		users.put("wyatt-earp", new User("Wyatt Earp", "welcomeWyatt"));
		users.put("doc-holiday", new User("Doc Holiday", "welcomeDoc"));
		users.put("wild-bill", new User("Wild Bill", "welcomeBill"));
		users.put("calamity-jane", new User("Calamity Jane", "welcomeJane"));
		users.put("kitty-leroy", new User("Kitty Leroy", "welcomeKitty"));
		users.put("madame-moustache", new User("Madame Moustache", "welcomeMadame"));
		users.put("poker-alice", new User("Poker Alice", "welcomeAlice"));
	}
	
	public User getById(String id) {
		return users.get(id);
	}

	public class User {
		
		public final String name;
		public final String password;

		public User(String name, String password) {
			this.name = name;
			this.password = password;
		}
		
	}

}
