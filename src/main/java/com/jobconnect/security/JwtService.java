package com.jobconnect.security;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.Duration;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final JwtProperties jwtProperties;
	private final Clock clock;

	public JwtService(JwtProperties jwtProperties, Clock clock) {
		this.jwtProperties = jwtProperties;
		this.clock = clock;
	}

	public String generateAccessToken(UserDetails userDetails) {
		return buildToken(userDetails, jwtProperties.getAccessTokenValidity());
	}

	private String buildToken(UserDetails userDetails, Duration validity) {
		Instant now = Instant.now(clock);
		return Jwts.builder()
			.subject(userDetails.getUsername())
			.issuedAt(Date.from(now))
			.expiration(Date.from(now.plus(validity)))
			.signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
			.compact();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(Date.from(Instant.now(clock)));
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
			.verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
