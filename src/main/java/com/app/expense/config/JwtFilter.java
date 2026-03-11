package com.app.expense.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.app.expense.service.security.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String header = request.getHeader("Authorization");
		
		try {
			if(StringUtils.hasLength(header) && header.startsWith("Bearer ")) {
				
				String token = header.substring(7);
				
				if(jwtService.isValidToken(token)) {
					
					String email = jwtService.extractEmail(token);
					
					var role = jwtService.extractRole(token);
					
					var authority = new SimpleGrantedAuthority("ROLE_" + role);
					
					var authToken = new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
					
				}
				
			}
			
			filterChain.doFilter(request, response);
			
		} catch (Exception e) {
			
			handlerExceptionResolver.resolveException(request, response, null, e);
			
		}
		
	}

}
