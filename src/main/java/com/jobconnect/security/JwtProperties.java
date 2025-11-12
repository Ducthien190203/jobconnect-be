package com.jobconnect.security;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

	private String secret = "please-change-this-secret-key-which-should-be-long";
	private Duration accessTokenValidity = Duration.ofMinutes(15);
	private Duration refreshTokenValidity = Duration.ofDays(7);

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Duration getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(Duration accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}

	public Duration getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(Duration refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}
}
