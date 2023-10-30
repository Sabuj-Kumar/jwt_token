package com.springJWTProject.springSecurity.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springJWTProject.springSecurity.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService {

	private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
	@Override
	public String generateToken(UserDetails userDetails) {
		return doGenerateToken(userDetails);
	}
	
	@Override
	public String generateRefreshToken(Map<String, Object> claims,UserDetails userDetails) {
		
		return doGenerateRefreshToken(claims,userDetails);
	} 

	public String doGenerateRefreshToken(Map<String, Object> claims,UserDetails userDetails) {
		return Jwts.builder().setClaims(claims) 
				.setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 604800000))
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();
	}
	
	public String doGenerateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();
	}

	public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
	
	private Boolean isTokenExpired(String token) {
	    final Date expiration = getExpirationDateFromToken(token);
	    return expiration.before(new Date());
	} 
	
	@Override
	public String getUserName(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {

		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getKey() {

		byte[] keys = Decoders.BASE64.decode(secret);

		return Keys.hmacShaKeyFor(keys);
	}
	
	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
	     
		final String username = getUserName(token);
	    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
