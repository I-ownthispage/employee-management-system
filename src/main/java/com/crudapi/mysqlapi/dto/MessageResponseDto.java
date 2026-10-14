package com.crudapi.mysqlapi.dto;

public class MessageResponseDto {

	private Long userId;
	private String message;
	
	public MessageResponseDto() {
		
	}
	
	public MessageResponseDto(Long userId, String message) {
		this.userId = userId;
		this.message = message;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
