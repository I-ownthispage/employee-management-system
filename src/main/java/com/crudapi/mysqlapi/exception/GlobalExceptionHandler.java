package com.crudapi.mysqlapi.exception;

import java.time.LocalDateTime;
//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleEmployeeNotFound(EmployeeNotFoundException exception) {
		
		logger.warn(exception.getMessage());
		
		return new ErrorResponse(
				404,
				exception.getMessage(),
				LocalDateTime.now()
		);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException exception) {
				
		Map<String, String> errors = new LinkedHashMap<>();
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		for(FieldError error : fieldErrors) {
			
			String fieldName = error.getField();
			
			String errorMessage = error.getDefaultMessage();
			
			errors.put(fieldName, errorMessage);
		}
		
		logger.warn("Validation failed for request: {}", errors);
		
		return new ValidationErrorResponse(
				400,
				"Validation Failed",
				errors,
				LocalDateTime.now()
		);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleGeneralException(Exception exception) {
		
		logger.error("Unexpected error occured: {}", 
						exception.getMessage(),
						exception);
		
		return new ErrorResponse(
				500,
				"An unexpected error occurred",
				LocalDateTime.now()
		);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException exception) {
		
		logger.warn("Data integrity violation: {}", exception.getMessage());
		
		return new ErrorResponse(
				409,
				"Email already exists",
				LocalDateTime.now()
		);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleUserNotFound(UserNotFoundException exception) {
		
		logger.warn(exception.getMessage());
		
		return new ErrorResponse(
				404,
				exception.getMessage(),
				LocalDateTime.now()
		);
	}
	
	@ExceptionHandler(UsernameAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception) {
		
		logger.warn(exception.getMessage());
		
		return new ErrorResponse(
				404,
				exception.getMessage(),
				LocalDateTime.now()
		);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse handleAccessDeniedException(AccessDeniedException exception) {
		
		logger.warn("Access denied: {}", exception.getMessage());
		
		return new ErrorResponse(
				403,
				exception.getMessage(),
				LocalDateTime.now()
		);
	}
}
