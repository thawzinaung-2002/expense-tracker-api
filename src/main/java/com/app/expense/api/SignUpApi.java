package com.app.expense.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.expense.api.response.ApiResponse;
import com.app.expense.dto.UserForm;
import com.app.expense.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SignUpApi {
	
	private final UserService userService;
	

	@PostMapping("signup")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<UserForm> signup(@Valid @RequestBody UserForm req) {
		return userService.createUser(req);	
	}
	
	@PostMapping("login")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<UserForm> login(@Valid @RequestBody UserForm req) {
		return userService.loginUser(req);	
	}
	
	@GetMapping("user/{id}")
	public UserForm findyById(@PathVariable @NotNull String id) {
		return userService.findById(id);
	}
	
}
