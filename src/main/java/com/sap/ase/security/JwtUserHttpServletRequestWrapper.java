package com.sap.ase.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class JwtUserHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private String userPrincipal;

	public JwtUserHttpServletRequestWrapper(String userPrincipal, HttpServletRequest request) {
		super(request);
		this.userPrincipal = userPrincipal;
	}

	@Override
	public Principal getUserPrincipal() {
		return new Principal() {
			@Override
			public String getName() {
				return userPrincipal;
			}
		};
	}
}
