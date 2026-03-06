package com.app.expense.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityConfig {
	
	@Autowired
	private AuthEntryPointExceptionHandling exceptionHandling;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		
		http.csrf(csrf -> csrf.disable());
		
		http.formLogin(form -> form.disable());
		
		http.sessionManagement(sess -> {
			sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		});
		
		http.authorizeHttpRequests(req -> {
			req.requestMatchers("/signup/**", "/login/**", "/docs/**", "/api-spec/**").permitAll();
			req.anyRequest().authenticated();
		});
		
		http.exceptionHandling(exec -> {
			exec.authenticationEntryPoint(exceptionHandling);
		});
		
				
		return http.build();
	}
	
	@Bean
	AuthenticationProvider authProvider(UserDetailsService userDetailsService) {
		var provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		provider.setHideUserNotFoundExceptions(false);
		return provider;
	}
	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
