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

	public DecodedJWT verifyAndDecode(String jwt) throws JWTVerificationException {
		if (jwt == null) {
			throw new JWTVerificationException("Missing jwt token");
		}
		return verifier.verify(jwt);
	}

	public String create(String id, String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
