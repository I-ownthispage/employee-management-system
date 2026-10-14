package com.crudapi.mysqlapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crudapi.mysqlapi.dto.UserRequestDto;
import com.crudapi.mysqlapi.dto.UserResponseDto;

import com.crudapi.mysqlapi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request){
		
		UserResponseDto response = userService.createUser(request);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
