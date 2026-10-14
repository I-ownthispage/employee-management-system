package com.crudapi.mysqlapi.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crudapi.mysqlapi.dto.LoginRequestDto;
import com.crudapi.mysqlapi.dto.LoginResponseDto;
import com.crudapi.mysqlapi.exception.ErrorResponse;
import com.crudapi.mysqlapi.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
		
		try {

			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					request.getUsername(),
					request.getPassword()
				)
			);

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			String token = jwtService.generateToken(userDetails);

			return ResponseEntity.ok(new LoginResponseDto(token));
			
		}
		catch(AuthenticationException exception) {
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
								 .body(new ErrorResponse(
										 401,
										 "Invalid username or password",
										 LocalDateTime.now()
									  )
							     );
		}
	}
}
