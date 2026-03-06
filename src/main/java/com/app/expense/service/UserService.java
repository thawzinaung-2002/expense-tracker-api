package com.app.expense.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.expense.api.exceptions.custom.BusinessException;
import com.app.expense.api.response.ApiResponse;
import com.app.expense.dto.UserForm;
import com.app.expense.entity.User;
import com.app.expense.repo.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authManager;

	@Transactional
	public ApiResponse<UserForm> createUser(UserForm req) {

		var entity = req.toEntity();

		entity.setPassword(passwordEncoder.encode(entity.getPassword()));

		User result = userRepo.save(entity);

		var token = jwtService.createToken(req.email());

		UserForm dto = result.toDto();

		var response = new ApiResponse<UserForm>();

		response.setContents(dto);

		response.setToken(token);

		return response;
	}

	public UserForm findById(String id) {
		return userRepo.findById(Integer.parseInt(id)).map(User::toDto)
				.orElseThrow(() -> new BusinessException("User - %s not found!".formatted(id)));
	}

	public ApiResponse<UserForm> loginUser(UserForm req) {
		
		var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());

		var auth = authManager.authenticate(authToken);

		var token = jwtService.createToken(req.email());
		
		var response = new ApiResponse<UserForm>();

		response.setContents(req);

		response.setToken(token);
		
		return response;
	}

}
