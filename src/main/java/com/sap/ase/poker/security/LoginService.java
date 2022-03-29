package com.sap.ase.poker.security;

import com.sap.ase.poker.config.OfflineUserDetailsService.PokerUser;
import com.sap.ase.poker.security.JwtUserHttpServletRequestWrapper.PokerUserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = LoginService.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginService {

	public static final String PATH = "/api/login";
	private final JwtTools jwtTools = new JwtTools(JwtTools.SECRET);

	@PostMapping
	public void login(UsernamePasswordAuthenticationToken authentication, HttpServletResponse response) {
		PokerUser pokerUser = (PokerUser) authentication.getPrincipal();
		String id = pokerUser.getUsername();
		String displayName = pokerUser.getDisplayName();

		String jwt = jwtTools.create(id, displayName);
		int cookieAgeSeconds = 100000;
		Cookie cookie = new Cookie("jwt", jwt);
		cookie.setPath("/");
		cookie.setMaxAge(cookieAgeSeconds);
		cookie.setComment("jwt token for poker app");
		cookie.setHttpOnly(false);
		response.addCookie(cookie);
	}

	@DeleteMapping
	public void logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("jwt", "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		cookie.setComment("jwt token for poker app");
		cookie.setHttpOnly(false);
		response.addCookie(cookie);
	}

	@GetMapping("user")
	public UserResponse getLoggedInUser(PokerUserPrincipal principal) {
		return new UserResponse(principal.getName(), principal.getDisplayName());
	}

	public static class UserResponse {
		private final String id;
		private final String name;

		public UserResponse(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}
	}

}
