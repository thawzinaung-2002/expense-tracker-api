package com.app.expense.utils;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderUtils {

	public static String getEmail() {
		
		var auth = SecurityContextHolder.getContext().getAuthentication();
		
		return Optional.ofNullable(auth.getName())
				.orElse("---");
	}
	
}
