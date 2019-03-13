package com.sap.ase.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtTools {

	private JWTVerifier verifier;
	private Algorithm algorithm;

	public JwtTools(String secret) {
		algorithm = Algorithm.HMAC256(secret);
		this.verifier = JWT.require(algorithm).build();
	}

	public DecodedJWT verifyAndDecodeJwtCookie(String cookieValue) throws JWTVerificationException {
		if (cookieValue == null) {
			throw new JWTVerificationException("Missing jwt cookie");
		}
		String token = cookieValue.replaceFirst("[bB]earer ", "");
		DecodedJWT jwt = verifier.verify(token);
		return jwt;
	}

	public String getUserNameFromAuthCookie(String authorizationCookie) throws JWTVerificationException {
		DecodedJWT decodedToken = this.verifyAndDecodeJwtCookie(authorizationCookie);
		return decodedToken.getClaim("user_name").asString();
	}

}