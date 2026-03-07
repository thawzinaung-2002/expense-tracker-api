package com.app.expense.dto;

import com.app.expense.entity.User;
import com.app.expense.entity.User.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserForm(
		Integer id,
		
		@NotBlank(message = "Please enter username")
		String username,
		
		@NotBlank(message = "Please enter email")
		@Email(message = "Invalid email format")
		String email,
		
		@NotBlank(message = "Please enter password")
		String password,
		
		Role role
		) {

	public User toEntity() {
		return new User(id, username, email, password, role == null ? Role.USER : role, 1);
	}

}
