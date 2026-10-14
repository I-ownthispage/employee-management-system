package com.crudapi.mysqlapi.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import com.crudapi.mysqlapi.dto.AdminCreationRequestDto;
import com.crudapi.mysqlapi.dto.UserRequestDto;
import com.crudapi.mysqlapi.dto.UserResponseDto;
import com.crudapi.mysqlapi.entity.User;
import com.crudapi.mysqlapi.exception.UserNotFoundException;
import com.crudapi.mysqlapi.exception.UsernameAlreadyExistsException;
import com.crudapi.mysqlapi.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	public UserResponseDto createUser(UserRequestDto request) {
		
		if(userRepo.findByUsername(request.getUsername()).isPresent()) {
			
			throw new UsernameAlreadyExistsException("Username already exists");
		}
		
		User user = new User();
		
		user.setUsername(request.getUsername());
		
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		user.setRole("USER");
		
		User savedUser = userRepo.save(user);
		
		return new UserResponseDto(
				savedUser.getId(),
				savedUser.getUsername(),
				savedUser.getRole()
		);
	}
	
	public UserResponseDto createAdmin(AdminCreationRequestDto request) {
		
		if(userRepo.findByUsername(request.getUsername()).isPresent()) {
			
			throw new UsernameAlreadyExistsException("Username already exists");
		}
		
		User user = new User();
		
		user.setUsername(request.getUsername());
		
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		user.setRole("ADMIN");
		
		User savedUser = userRepo.save(user);
		
		return new UserResponseDto(
				savedUser.getId(),
				savedUser.getUsername(),
				savedUser.getRole()
		);
	}
	
	public void promoteToSuperAdmin(String username) {
		
		User user = userRepo.findByUsername(username).orElseThrow(() ->
						new UserNotFoundException("User not found: " + username));
		
		if("SUPER_ADMIN".equals(user.getRole())) {
			return;
		}
		
		if(!"ADMIN".equals(user.getRole())) {
			throw new IllegalStateException("Only an existing ADMIN can be promoted to SUPER_ADMIN");
		}
		
		user.setRole("SUPER_ADMIN");
		
		userRepo.save(user);
	}
	
	public void changePassword(Long userId, String newPassword) {
		
		User targetUser = userRepo.findById(userId).orElseThrow(() ->
													new UserNotFoundException(
															"User not found: " + userId
															)
													);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String currentUserRole = null;
		
		for(GrantedAuthority authority : authentication.getAuthorities()) {
			
			currentUserRole = authority.getAuthority();
			break;
		}
		
		if(currentUserRole == null) {
			
			throw new IllegalStateException("User role not found");
		}
		
		if("ROLE_ADMIN".equals(currentUserRole) && !"USER".equals(targetUser.getRole())) {
			
			throw new AccessDeniedException(
					"ADMIN can only change USER passwords");
		}
		
		targetUser.setPassword(passwordEncoder.encode(newPassword));
		
		userRepo.save(targetUser);
	}
	
	public void changeUsername(Long userId, String newUsername) {
		
		User targetUser = userRepo.findById(userId).orElseThrow(() ->
														new UserNotFoundException(
																"User not found: " + userId
																)
														);
		
		if (userRepo.findByUsername(newUsername).isPresent()) {
	        
			throw new UsernameAlreadyExistsException(
	                "Username already exists"
	        );
	    }
		
		Authentication authentication = SecurityContextHolder.getContext()
															 .getAuthentication();
		
		String currentUserRole = null;
		
		for(GrantedAuthority authority : authentication.getAuthorities()) {
			
			currentUserRole = authority.getAuthority();
			break;
		}
		
		if (currentUserRole == null) {
	        throw new IllegalStateException("User role not found");
	    }
		
		String currentUsername = authentication.getName();
		
		 if ("SUPER_ADMIN".equals(targetUser.getRole()) 
				 && !currentUsername.equals(targetUser.getUsername())) {
		       
			 throw new AccessDeniedException(
		                "SUPER_ADMIN username cannot be changed by another SUPER_ADMIN"
		        );
		 }
		 
		 /*
		  * ADMIN can only modify USER accounts.
		  */
		 if ("ROLE_ADMIN".equals(currentUserRole)
		            && !"USER".equals(targetUser.getRole())) {

		     throw new AccessDeniedException(
		                "ADMIN can only change USER usernames"
		     );
		 }
		 
		 targetUser.setUsername(newUsername);
		 
		 userRepo.save(targetUser);
	}
	
	
	public void deleteUser(Long userId) {
		
		User targetUser = userRepo.findById(userId).orElseThrow(() ->
												new UserNotFoundException(
														"User not found: " + userId
														)
												);
		
		Authentication authentication = SecurityContextHolder
													.getContext()
													.getAuthentication();
		
		String currentUserRole = null;
		
		for(GrantedAuthority authority : authentication.getAuthorities()) {
			
			currentUserRole = authority.getAuthority();
			break;
		}
		
		if(currentUserRole == null) {
			throw new IllegalStateException("User role not found");
		}
		
		if("SUPER_ADMIN".equals(targetUser.getRole())) {
			throw new AccessDeniedException("SUPER_ADMIN cannot be deleted");
		}
		
		if("ROLE_ADMIN".equals(currentUserRole) && !"USER".equals(targetUser.getRole())) {
			throw new AccessDeniedException("ADMIN can only delete user accounts");
		}
		
		userRepo.delete(targetUser);
	}
}
