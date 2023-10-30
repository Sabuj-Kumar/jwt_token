package com.springJWTProject.springSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springJWTProject.springSecurity.dto.JWTAuthResponse;
import com.springJWTProject.springSecurity.dto.RefreshTokenRequest;
import com.springJWTProject.springSecurity.dto.SignInRequest;
import com.springJWTProject.springSecurity.dto.SignUpRequest;
import com.springJWTProject.springSecurity.entities.User;
import com.springJWTProject.springSecurity.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

	@Autowired
	private AuthenticationService authService;
	
	
	@PostMapping("signUp")
	public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest){
		
		return new ResponseEntity<User>(this.authService.signUp(signUpRequest),HttpStatus.OK);
	}
	
	@PostMapping("signIn")
	public ResponseEntity<JWTAuthResponse> signIn(@RequestBody SignInRequest signInRequest){
		
	  return new ResponseEntity<JWTAuthResponse>(this.authService.singIn(signInRequest),HttpStatus.OK);
	}
	
	@PostMapping("refresh")
	public ResponseEntity<JWTAuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken){
		
	  return new ResponseEntity<JWTAuthResponse>(this.authService.refreshToken(refreshToken),HttpStatus.OK);
	}
}
