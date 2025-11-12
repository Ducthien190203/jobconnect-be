package com.jobconnect.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobconnect.auth.dto.AuthResponse;
import com.jobconnect.auth.dto.LoginRequest;
import com.jobconnect.auth.dto.RefreshTokenRequest;
import com.jobconnect.auth.dto.RegisterRequest;
import com.jobconnect.auth.exception.EmailAlreadyUsedException;
import com.jobconnect.auth.token.RefreshToken;
import com.jobconnect.auth.token.RefreshTokenService;
import com.jobconnect.security.JwtService;
import com.jobconnect.user.RoleName;
import com.jobconnect.user.User;
import com.jobconnect.user.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmailIgnoreCase(request.email())) {
			throw new EmailAlreadyUsedException("Email already registered");
		}

		User user = new User();
		user.setEmail(request.email().toLowerCase());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setFullName(request.fullName());
		user.setRoles(new HashSet<>(Set.of(RoleName.FREELANCER)));
		user.setEnabled(true);

		User saved = userRepository.save(user);
		String accessToken = jwtService.generateAccessToken(saved);
		RefreshToken refreshToken = refreshTokenService.create(saved);
		return AuthResponse.bearer(accessToken, refreshToken.getToken());
	}

	@Transactional
	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.email(), request.password())
		);

		User principal = (User) authentication.getPrincipal();
		String accessToken = jwtService.generateAccessToken(principal);
		RefreshToken refreshToken = refreshTokenService.create(principal);
		return AuthResponse.bearer(accessToken, refreshToken.getToken());
	}

	@Transactional(readOnly = true)
	public AuthResponse refresh(RefreshTokenRequest request) {
		RefreshToken refreshToken = refreshTokenService.validate(request.refreshToken());
		String accessToken = jwtService.generateAccessToken(refreshToken.getUser());
		return AuthResponse.bearer(accessToken, refreshToken.getToken());
	}
}
