package com.sap.ase.security;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.sap.ase.security.FakeSecurityContext.User;
import com.sap.ase.security.JwtUserHttpServletRequestWrapper.PokerUserPrincipal;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginService {

	private final JwtTools jwtTools = new JwtTools(FakeSecurityContext.SECRET);
	private final FakeSecurityContext users = new FakeSecurityContext();

	@POST
	public Response login(LoginRequest loginRequest) {
		String id = loginRequest.getId();
		User user = users.getUserById(id);

		if (user == null || !user.password.equals(loginRequest.getPassword())) {
			throw new NotAuthorizedException("Wrong user id or password!");
		}
		String jwt = jwtTools.create(id, user.name);
		int cookieAgeSeconds = 100000;
		return Response.ok().cookie(new NewCookie("jwt", jwt, "/", "", "jwt token for poker app", cookieAgeSeconds, false)).build();
	}

	@DELETE
	public Response logout() {
		return Response.noContent().cookie(new NewCookie("jwt", "", "/", "", "jwt token for poker app", 0, false)).build();
	}
	
	@GET
	@Path("user")
	public UserResponse getLoggedInUser(@Context SecurityContext securityContext) {
		PokerUserPrincipal principal = (PokerUserPrincipal) securityContext.getUserPrincipal();
		return new UserResponse(principal.getName(), principal.getDisplayName());
	}

	public static class LoginRequest {
		private String id;
		private String password;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
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
