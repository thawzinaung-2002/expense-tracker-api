package com.app.expense.api.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.expense.api.exceptions.custom.BusinessException;
import com.app.expense.api.response.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class RestAuthException {
	
	private ApiResponse<Object> result = new ApiResponse<>();
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiResponse<Object> tokenExpiredException(HttpServletRequest request, BusinessException exec) {
		
		log.error("Exception in Servlet Path => {}, Type => {}, Reason => {} ", request.getServletPath(), exec.getClass().getSimpleName() , exec.getMessage());
		
		result.setStatus("Failed");
		result.setMessage(exec.getMessage());
		
		return result;
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiResponse<Object> tokenExpiredException(HttpServletRequest request, ExpiredJwtException exec) {
		
		log.error("Exception in Servlet Path => {}, Type => {}, Reason => {} ", request.getServletPath(), exec.getClass().getSimpleName() , exec.getMessage());
		
		result.setStatus("Failed");
		result.setMessage("Token Expired!");
		
		return result;
	}
	
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ApiResponse<Object> usernameNotFound(HttpServletRequest request, AuthenticationException exec) {
		
		log.error("Exception in Servlet Path => {}, Reason => {} ", request.getServletPath(), exec.getMessage());
		
		var msg = switch(exec) {
		case UsernameNotFoundException _ -> "Username is invalid!";
		case InsufficientAuthenticationException _ -> "Insufficient Authentication. Authentication is required!";
		case BadCredentialsException _ -> "Password is invalid!";
		default -> "Authentication Exception";
		};
		
		result.setStatus("Failed");
		result.setMessage(msg);
		
		return result;
	}

	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiResponse<Object> sqlIntegrityFail(HttpServletRequest request, SQLIntegrityConstraintViolationException exec) {
		
		log.error("Exception in Servlet Path => {}, Reason => {} ", request.getServletPath(), exec.getMessage());
		
		result.setStatus("Failed");
		result.setMessage("SQL Constraint Failed!");
		
		return result;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiResponse<Object> beanValidationFail(HttpServletRequest request, MethodArgumentNotValidException exec) {
		
		log.error("Exception in Servlet Path => {} ", request.getServletPath());
		
		result.setStatus("Failed");
		var err = exec.getAllErrors()
				.stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining(","));
		result.setMessage(err);
		
		return result;
	}
	
	
	@ExceptionHandler(Exception.class)
	public ApiResponse<Object> globalException(HttpServletRequest request, Exception exec) {
		
		log.error("Exception in Servlet Path => {}, Type => {}, Reason => {} ", request.getServletPath(), exec.getClass().getSimpleName() , exec.getMessage());
		
		result.setStatus("Failed");
		result.setMessage("Operation Failed!");
		
		return result;
	}
	
}
