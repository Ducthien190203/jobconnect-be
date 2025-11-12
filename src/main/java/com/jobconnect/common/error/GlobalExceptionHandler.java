package com.jobconnect.common.error;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jobconnect.auth.exception.EmailAlreadyUsedException;
import com.jobconnect.auth.token.RefreshTokenException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
		ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		detail.setTitle("Validation failed");
		Map<String, String> errors = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(Collectors.toMap(
				fieldError -> fieldError.getField(),
				fieldError -> fieldError.getDefaultMessage(),
				(first, second) -> first
			));
		detail.setProperty("errors", errors);
		return detail;
	}

	@ExceptionHandler(EmailAlreadyUsedException.class)
	public ProblemDetail handleEmailAlreadyUsed(EmailAlreadyUsedException ex) {
		ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
		detail.setTitle("Email already registered");
		detail.setDetail(ex.getMessage());
		return detail;
	}

	@ExceptionHandler(RefreshTokenException.class)
	public ProblemDetail handleRefreshToken(RefreshTokenException ex) {
		ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
		detail.setTitle("Invalid refresh token");
		detail.setDetail(ex.getMessage());
		return detail;
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
		ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
		detail.setTitle("Authentication failed");
		detail.setDetail("Email or password is incorrect");
		return detail;
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ProblemDetail handleUserNotFound(UsernameNotFoundException ex) {
		ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		detail.setTitle("User not found");
		detail.setDetail(ex.getMessage());
		return detail;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGeneric(Exception ex) {
		ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		detail.setTitle("Internal server error");
		detail.setDetail("An unexpected error occurred");
		return detail;
	}
}
