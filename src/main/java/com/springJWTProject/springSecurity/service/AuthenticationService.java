package com.springJWTProject.springSecurity.service;

import com.springJWTProject.springSecurity.dto.JWTAuthResponse;
import com.springJWTProject.springSecurity.dto.RefreshTokenRequest;
import com.springJWTProject.springSecurity.dto.SignInRequest;
import com.springJWTProject.springSecurity.dto.SignUpRequest;
import com.springJWTProject.springSecurity.entities.User;

public interface AuthenticationService {
	public User signUp(SignUpRequest signUpRequest);
	public JWTAuthResponse singIn(SignInRequest signInRequest);
	public JWTAuthResponse refreshToken(RefreshTokenRequest refreshToken);
}
