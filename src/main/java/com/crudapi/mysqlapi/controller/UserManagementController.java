package com.crudapi.mysqlapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crudapi.mysqlapi.dto.ChangePasswordRequestDto;
import com.crudapi.mysqlapi.dto.ChangeUsernameRequestDto;
import com.crudapi.mysqlapi.dto.MessageResponseDto;
import com.crudapi.mysqlapi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/users")
public class UserManagementController {

	private final UserService userService;
	
	public UserManagementController(UserService userService) {
		this.userService = userService;
	}
	
	@PutMapping("/{id}/password")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<MessageResponseDto> changePassword(@PathVariable Long id,
											   @Valid @RequestBody ChangePasswordRequestDto request){
		
		userService.changePassword(id, request.getNewPassword());
		
		return ResponseEntity.ok(new MessageResponseDto(id, "Password updated successfully"));
	}
	
	@PutMapping("/{id}/username")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<MessageResponseDto> changeUsername(@PathVariable Long id,
											   @Valid @RequestBody ChangeUsernameRequestDto request){
		
		userService.changeUsername(id, request.getNewUsername());
		
		return ResponseEntity.ok(new MessageResponseDto(id, "Username updated successfully"));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<MessageResponseDto> deleteUser(@PathVariable Long id) {
		
		userService.deleteUser(id);
		
		return ResponseEntity.ok(new MessageResponseDto(id, "User deleted successfully"));
	}
}
