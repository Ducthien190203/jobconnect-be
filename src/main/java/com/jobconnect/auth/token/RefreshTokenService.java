package com.jobconnect.auth.token;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.Duration;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobconnect.security.JwtProperties;
import com.jobconnect.user.User;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProperties jwtProperties;
	private final SecureRandom secureRandom = new SecureRandom();
	private final Clock clock;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtProperties jwtProperties, Clock clock) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtProperties = jwtProperties;
		this.clock = clock;
	}

	@Transactional
	public RefreshToken create(User user) {
		refreshTokenRepository.deleteByUserId(user.getId());
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(generateTokenValue());
		refreshToken.setUser(user);
		refreshToken.setExpiresAt(Instant.now(clock).plus(getRefreshValidity()));
		return refreshTokenRepository.save(refreshToken);
	}

	@Transactional(readOnly = true)
	public RefreshToken validate(String tokenValue) {
		return refreshTokenRepository.findByToken(tokenValue)
			.filter(token -> token.getExpiresAt().isAfter(Instant.now(clock)))
			.orElseThrow(() -> new RefreshTokenException("Invalid or expired refresh token"));
	}

	private Duration getRefreshValidity() {
		return jwtProperties.getRefreshTokenValidity();
	}

	private String generateTokenValue() {
		byte[] bytes = new byte[48];
		secureRandom.nextBytes(bytes);
		return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}
