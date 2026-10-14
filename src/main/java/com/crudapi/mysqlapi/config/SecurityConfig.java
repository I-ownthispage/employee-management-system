package com.crudapi.mysqlapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.crudapi.mysqlapi.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider provider = 
							new DaoAuthenticationProvider(userDetailsService);
		
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) 
		throws Exception {
		
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {

	    return (request, response, authException) -> {

	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");

	        response.getWriter().write(
	            "{\"status\":401,\"message\":\"Authentication required\"}"
	        );
	    };
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
		throws Exception{
			
		http.csrf(csrf -> csrf.disable())
			
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		
			.authorizeHttpRequests(auth -> auth
					
				.requestMatchers("/users", "/auth/login", "/error").permitAll()
				.requestMatchers("/actuator/loggers", "/actuator/loggers/**").hasRole("SUPER_ADMIN")
				.requestMatchers("/actuator/metrics", "/actuator/metrics/**").hasRole("SUPER_ADMIN")
				.anyRequest().authenticated()
			)
			
			.exceptionHandling(exception -> exception
					.authenticationEntryPoint(authenticationEntryPoint()))
			
			.authenticationProvider(authenticationProvider())
			
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
				
		
		return http.build();
	}
}
