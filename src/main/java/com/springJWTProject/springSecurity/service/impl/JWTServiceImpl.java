package com.springJWTProject.springSecurity.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springJWTProject.springSecurity.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService {

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<String,Object>();
		return doGenerateToken(claims,userDetails);
	}
	
	public String doGenerateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + 1000* 60 *24);
		JwtBuilder builder = Jwts.builder();
		builder.claims(extraClaims);
		builder.subject(userDetails.getUsername());
		builder.issuedAt(now); 
		builder.expiration(expiration);
		builder.signWith(getKey());
		
		return builder.compact();
	}
	
	public String getUserName(String token) { 
		return getClaimFromToken(token,Claims::getSubject);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
	
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
	}
	
	private SecretKey getKey() {
		
		byte [] keys = Decoders.BASE64.decode("12328945fbwjfbwfebwebfdgwfe325t252fg3b356");
		
		return Keys.hmacShaKeyFor(keys);
	}
}
