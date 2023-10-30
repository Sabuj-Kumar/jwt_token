package com.springJWTProject.springSecurity.config;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springJWTProject.springSecurity.service.JWTService;
import com.springJWTProject.springSecurity.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		String username = null;
        String token = null;
        
        if(authHeader != null && authHeader.startsWith("Bearer")) {
        	
        	token = authHeader.substring(7);
        	
        	try {
        		username = this.jwtService.getUserName(token);
        	}
        	 catch (IllegalArgumentException e) {
                 
                 e.printStackTrace();
             } catch (ExpiredJwtException e) {
                
                 e.printStackTrace();
             } catch (MalformedJwtException e) {
                
                 e.printStackTrace();
             } catch (Exception e) {
                 e.printStackTrace();
             }
        }
        
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	
        	UserDetails userDetails = this.userService.userDetailService().loadUserByUsername(username);
        	
        	
        	if(this.jwtService.validateToken(token, userDetails)) {
        		
        		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        		
        		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.setContext(securityContext);
                
        	}
        }
        
        filterChain.doFilter(request, response);
	}
}
