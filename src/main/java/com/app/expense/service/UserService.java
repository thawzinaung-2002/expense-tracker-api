package com.app.expense.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.expense.api.exceptions.custom.BusinessException;
import com.app.expense.api.response.ApiResponse;
import com.app.expense.dto.UserForm;
import com.app.expense.entity.User;
import com.app.expense.repo.UserRepo;
import com.app.expense.service.security.JwtService;

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

		var token = jwtService.createToken(result);

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

		var entity = (User) auth.getPrincipal();
				
		var token = jwtService.createToken(entity);
		
		var response = new ApiResponse<UserForm>();

		response.setToken(token);
		
		return response;
	}

	public ApiResponse<String> logout() {
		
		var auth = SecurityContextHolder.getContext().getAuthentication();
		
		var user = userRepo.findByEmail((String)auth.getPrincipal()).orElseThrow(() -> new BusinessException("User not found!"));
		
		user.setTokenVersion(user.getTokenVersion()+1);
		
		userRepo.save(user);
		
		var ret = new ApiResponse<String>();
		
		String message = "Logout Success!";
		
		ret.setContents(message);
		
		return ret;
	}

}
