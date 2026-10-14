package com.crudapi.mysqlapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crudapi.mysqlapi.dto.AdminCreationRequestDto;
import com.crudapi.mysqlapi.dto.UserResponseDto;
import com.crudapi.mysqlapi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private final UserService userService;
	
	public AdminController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/admins")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public ResponseEntity<UserResponseDto> createAdmin(
										@Valid @RequestBody AdminCreationRequestDto request){
		
		UserResponseDto response = userService.createAdmin(request);
		
		return new ResponseEntity<>(
				response,
				HttpStatus.CREATED
		);
	}
}
