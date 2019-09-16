package com.sap.ase.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class JwtUserHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private String name;
	private String displayName;

	public JwtUserHttpServletRequestWrapper(String id, String name, HttpServletRequest request) {
		super(request);
		this.name = id;
		this.displayName = name;
	}
	
	@Override
	public Principal getUserPrincipal() {
		return new PokerUserPrincipal(name, displayName);
	}
	
	public class PokerUserPrincipal implements Principal {
		
		private final String name;
		private final String displayName;
		
		private PokerUserPrincipal(String name, String displayName) {
			this.name = name;
			this.displayName = displayName;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	}
}
