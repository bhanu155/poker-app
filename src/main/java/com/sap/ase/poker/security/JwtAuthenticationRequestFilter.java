package com.sap.ase.poker.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

public class JwtAuthenticationRequestFilter implements Filter {

	private JwtTools jwtTools;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		jwtTools = new JwtTools(JwtTools.SECRET);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().toLowerCase().equals("jwt")) {
					try {
						String cookieValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
						DecodedJWT decodedJwt = jwtTools.verifyAndDecode(cookieValue);
						String userId = decodedJwt.getClaim("user_id").asString();
						String userName = decodedJwt.getClaim("user_name").asString();
						// REVISE just checking, is it really meant to be like this - we're re-setting
						// the input parameter??
						request = new JwtUserHttpServletRequestWrapper(userId, userName, httpRequest);
						chain.doFilter(request, response);
						return;
					} catch (JWTVerificationException e) {
						response.reset();
						httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT Token");
						return;
					}
				}
			}
		}

		response.reset();
		httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"Missing authentication. Request must provide JWT Token in authorization header (type Bearer)");
		return;
	}

	@Override
	public void destroy() {
	}
}