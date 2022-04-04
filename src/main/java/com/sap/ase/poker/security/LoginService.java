package com.sap.ase.poker.security;

import com.sap.ase.poker.config.OfflineUserDetailsService;
import com.sap.ase.poker.config.OfflineUserDetailsService.PokerUser;
import com.sap.ase.poker.dto.LoginDto;
import com.sap.ase.poker.security.JwtUserHttpServletRequestWrapper.PokerUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(path = LoginService.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginService {

	public static final String PATH = "/login";
	private final JwtTools jwtTools = new JwtTools(JwtTools.SECRET);

	@Autowired
	OfflineUserDetailsService offlineUserDetailsService;

	@PostMapping
	public void login(@RequestBody LoginDto loginDto, HttpServletResponse response) throws IOException {
		String id = loginDto.getUsername();

		PokerUser userDetails = (PokerUser) offlineUserDetailsService.loadUserByUsername(id);

		if (offlineUserDetailsService.isPasswordCorrect(id, loginDto.getPassword())) {
			String displayName = userDetails.getDisplayName();

			String jwt = jwtTools.create(id, displayName);
			int cookieAgeSeconds = 100000;
			Cookie cookie = new Cookie("jwt", jwt);
			cookie.setPath("/");
			cookie.setMaxAge(cookieAgeSeconds);
			cookie.setComment("jwt token for poker app");
			cookie.setHttpOnly(false);
			response.addCookie(cookie);

		} else {
			response.sendError(HttpStatus.UNAUTHORIZED.value());
		}
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
