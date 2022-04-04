package com.sap.ase.poker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OfflineUserDetailsService implements UserDetailsService {

	public static final String POKER_USER_AUTHORITY = "USER";
	
	private final Map<String, PokerUser> users = new HashMap<>();

	private PasswordEncoder passwordEncoder;

	@Autowired
	public OfflineUserDetailsService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;

		users.put("al-capone", new PokerUser("al-capone", "all-in", "Al Capone"));
		users.put("pat-garret", new PokerUser("pat-garret", "all-in", "Pat Garret"));
		users.put("wyatt-earp", new PokerUser("wyatt-earp", "all-in", "Wyatt Earp"));
		users.put("doc-holiday", new PokerUser("doc-holiday", "all-in", "Doc Holiday"));
		users.put("wild-bill", new PokerUser("wild-bill", "all-in", "Wild Bill"));
		users.put("calamity-jane", new PokerUser("calamity-jane", "all-in", "Calamity Jane"));
		users.put("kitty-leroy", new PokerUser("kitty-leroy", "all-in", "Kitty Leroy"));
		users.put("madame-moustache", new PokerUser("madame-moustache", "all-in", "Madame Moustache"));
		users.put("poker-alice", new PokerUser("poker-alice", "all-in", "Poker Alice"));

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return users.get(username);
	}

	public boolean isPasswordCorrect(String id, String password) {
		return passwordEncoder.matches(password, loadUserByUsername(id).getPassword());
	}

	public class PokerUser extends org.springframework.security.core.userdetails.User {



		private static final long serialVersionUID = -121874185585306360L;

		private String displayName;

		public PokerUser(String username, String password, String displayName) {
			super(username, passwordEncoder.encode(password), AuthorityUtils.createAuthorityList(POKER_USER_AUTHORITY));
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

	}

}
