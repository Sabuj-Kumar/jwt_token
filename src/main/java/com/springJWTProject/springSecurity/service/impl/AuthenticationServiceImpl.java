package com.springJWTProject.springSecurity.service.impl;

import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springJWTProject.springSecurity.dto.JWTAuthResponse;
import com.springJWTProject.springSecurity.dto.RefreshTokenRequest;
import com.springJWTProject.springSecurity.dto.SignInRequest;
import com.springJWTProject.springSecurity.dto.SignUpRequest;
import com.springJWTProject.springSecurity.entities.User;
import com.springJWTProject.springSecurity.enums.Role;
import com.springJWTProject.springSecurity.repository.UserRepository;
import com.springJWTProject.springSecurity.service.AuthenticationService;
import com.springJWTProject.springSecurity.service.JWTService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncode;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JWTService jwtService;
	
	@Override
	public User signUp(SignUpRequest signUpRequest) {
		User user = this.modelMapper.map(signUpRequest, User.class);
		
		user.setPassword(this.passwordEncode.encode(signUpRequest.getPassword()));
		if(user.getRole() == null) {
			user.setRole(Role.USER);
		}
		
	  return this.userRepo.save(user);
	}

	@Override
	public JWTAuthResponse singIn(SignInRequest signInRequest) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));         
		
		User user = userRepo.findByEmail(signInRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("Invalid user and password"));
		
		String token = this.jwtService.generateToken(user);
		String refreshToken = this.jwtService.generateRefreshToken(new HashMap<>(), user);
		
		JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
		
		jwtAuthResponse.setToken(token);
		jwtAuthResponse.setRefreshToken(refreshToken);
		
		return jwtAuthResponse;
	} 
	
	@Override
	public JWTAuthResponse refreshToken(RefreshTokenRequest refreshToken) {
		
		String userEmail = this.jwtService.getUserName(refreshToken.getToken());
		
		User user = this.userRepo.findByEmail(userEmail).orElseThrow();
		
		if(this.jwtService.validateToken(refreshToken.getToken(), user)) {
			
			String token = this.jwtService.generateToken(user);
			
			JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
			
			jwtAuthResponse.setToken(token);
			jwtAuthResponse.setRefreshToken(refreshToken.getToken());
			
			return jwtAuthResponse;
		}
		return null;
	}
}
