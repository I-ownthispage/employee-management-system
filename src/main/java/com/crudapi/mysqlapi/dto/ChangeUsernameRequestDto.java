package com.crudapi.mysqlapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangeUsernameRequestDto {

	@NotBlank(message = "New username is required")
	@Size(
	    min = 3,
	    max = 50,
	    message = "New username must be between 3 and 50 characters"
	)
    private String newUsername;

    public ChangeUsernameRequestDto() {
    }

    public ChangeUsernameRequestDto(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}