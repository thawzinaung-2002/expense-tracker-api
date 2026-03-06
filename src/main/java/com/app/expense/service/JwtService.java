package com.app.expense.service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	private static final String KEY_CONSTANT = "vS9N8kR3pX5mQ2wz8vB7yD1G4J6M9QnS2V5X8ZaBcDe=";

	public String createToken(String email) {
		
		Date now = new Date();
		
		Date expiry = new Date(now.getTime() + (1000*60*3));

		return Jwts.builder()
			.subject(email)
			.issuedAt(now)
			.expiration(expiry)
			.signWith(getKey())
			.compact();
	}

	private Key getKey() {
		byte[] bytes = Decoders.BASE64.decode(KEY_CONSTANT);
		return Keys.hmacShaKeyFor(bytes);
	}

}
