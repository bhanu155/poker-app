package com.sap.ase.security;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtAuthenticationRequestFilter implements Filter {

	private JwtTools jwtTools;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		jwtTools = new JwtTools(filterConfig.getInitParameter("sharedSecret"));
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
						request = new JwtUserHttpServletRequestWrapper(userId, httpRequest);
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