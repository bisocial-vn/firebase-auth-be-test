package com.firebase.auth.example.service;

import org.springframework.security.core.AuthenticationException;

import com.firebase.auth.example.dto.response.TokenResponse;

public interface AuthService {

	TokenResponse authenticationUser(String emailOrPhone, String password, boolean isRemember)
			throws AuthenticationException;

}
