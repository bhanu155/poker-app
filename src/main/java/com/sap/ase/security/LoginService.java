package com.sap.ase.security;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.NotAuthorizedException;

import com.sap.ase.security.FakeSecurityContext.User;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginService {

	private final JwtTools jwtTools = new JwtTools(FakeSecurityContext.SECRET);
	private final FakeSecurityContext users = new FakeSecurityContext();

	@POST
	public Response login(LoginRequest loginRequest) {
		String id = loginRequest.getId();
		User user = users.getUserById(id);
		
		if (user == null || user.password != loginRequest.getPassword()) {
			throw new NotAuthorizedException("Wrong user id or password!");
		}
		String jwt = jwtTools.create(id, user.name);
		return Response.ok().cookie(new NewCookie("jwt", jwt)).build();
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

}
