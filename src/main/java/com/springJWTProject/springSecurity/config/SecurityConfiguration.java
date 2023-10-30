package com.springJWTProject.springSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springJWTProject.springSecurity.enums.Role;
import com.springJWTProject.springSecurity.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private UserService userService;

	@Bean
	AuthenticationProvider authenticationProvider() {
	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	       
	      authProvider.setUserDetailsService(this.userService.userDetailService());
	      authProvider.setPasswordEncoder(passwordEncoder());
	   
	      return authProvider;
	}
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception{
		
		http
		.authorizeHttpRequests(request-> request
				.requestMatchers("/api/v1/auth/**").permitAll()
				.requestMatchers("/api/v1/admin/**").hasAnyAuthority(Role.ADMIN.name())
				.requestMatchers("/api/v1/user/**").hasAnyAuthority(Role.USER.name())
		        .anyRequest().authenticated())
		.sessionManagement(manager-> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider())
		.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		.csrf(AbstractHttpConfigurer::disable); 
		
		return http.build();
	}

    @Bean
    BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
