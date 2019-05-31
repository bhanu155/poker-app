package com.sap.ase.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class SecretTest {

	@Test
	public void encodeSign_decodeVerify() {
		JwtTools jwtTools = new JwtTools("test-secret");
		String signedAndEncoded = jwtTools.create("john-doe", "John Doe");
		
		DecodedJWT decoded = jwtTools.verifyAndDecode(signedAndEncoded);
		String decodedId = decoded.getClaim("user_id").asString();
		String decodedName = decoded.getClaim("user_name").asString();
		
		assertEquals("john-doe", decodedId);
		assertEquals("John Doe", decodedName);
	}
	
	@Test(expected = SignatureVerificationException.class)
	public void encodeSign_decodeVerify_differentSecret() {
		JwtTools jwtTools1 = new JwtTools("test-secret1");
		JwtTools jwtTools2 = new JwtTools("test-secret2");
		String signedAndEncoded = jwtTools1.create("john-doe", "John Doe");
		
		DecodedJWT decoded = jwtTools2.verifyAndDecode(signedAndEncoded);
		String decodedId = decoded.getClaim("user_id").asString();
		String decodedName = decoded.getClaim("user_name").asString();
		
		assertEquals("john-doe", decodedId);
		assertEquals("John Doe", decodedName);
	}
}
