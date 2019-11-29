package com.sap.ase.poker.security;

import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.ase.poker.security.JwtUserHttpServletRequestWrapper.PokerUserPrincipal;

@RestController
@RequestMapping(path = LoginService.PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginService {

	public static final String PATH = "/api/login";
	private final JwtTools jwtTools = new JwtTools(JwtTools.SECRET);
	private final DisplayNameLookup users = new DisplayNameLookup();

	@PostMapping
	public void login(Principal principal, HttpServletResponse response) throws NotAuthorizedException {
		String id = principal.getName();
		String displayName = users.getDisplayNameUserById(id);

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
