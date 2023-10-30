package com.springJWTProject.springSecurity.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
	
	String generateToken(UserDetails useDetails);
	Boolean validateToken(String token, UserDetails userDetails);
	String generateRefreshToken(Map<String, Object> claims,UserDetails userDetails);
	String getUserName(String token);
}

