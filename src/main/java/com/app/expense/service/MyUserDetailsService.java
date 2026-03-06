package com.app.expense.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.expense.api.exceptions.custom.BusinessException;
import com.app.expense.entity.User;
import com.app.expense.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService{

	private final UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User - %s not found!".formatted(username)));
		
		return org.springframework.security.core.userdetails.User
				.withUsername(username)
				.password(user.getPassword())
				.build();
	}

}
