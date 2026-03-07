package com.app.expense.service;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.expense.api.exceptions.custom.BusinessException;
import com.app.expense.entity.User;
import com.app.expense.repo.UserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	@Autowired
	private UserRepo userRepo;

	private static final String KEY_CONSTANT = "vS9N8kR3pX5mQ2wz8vB7yD1G4J6M9QnS2V5X8ZaBcDe=";
	private static final String ISSUER = "expense-tracker-api";

	public String createToken(User user) {

		Date now = new Date();

		Date expiry = new Date(now.getTime() + (1000 * 60 * 3));

		return Jwts.builder().subject(user.getEmail()).issuer(ISSUER)
				.claims(Map.of("role", user.getRole(), "version", user.getTokenVersion()))
				.issuedAt(now).expiration(expiry).signWith(getKey()).compact();
	}

	private SecretKey getKey() {
		byte[] bytes = Decoders.BASE64.decode(KEY_CONSTANT);
		return Keys.hmacShaKeyFor(bytes);
	}

	public Jws<Claims> parseToken(String token) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);

	}

	public boolean isValidToken(String token) {
		
		var jws = parseToken(token);
		
		var tokenVersion = jws.getPayload().get("version");
		
		var email = extractEmail(token);
		
		var user = userRepo.findByEmail(email).orElseThrow(() -> new BusinessException("User not found!"));
		
		if(!tokenVersion.equals(user.getTokenVersion())) {
			
			throw new BusinessException("Token is revoked!");
			
		}
		
		return true;
	}

	public String extractEmail(String token) {
		
		return parseToken(token).getPayload().getSubject();
	}


	public String extractRole(String token) {
		
		return parseToken(token).getPayload().get("role", String.class);
		
	}

}
