package com.app.expense.api.exceptions.custom;

public class BusinessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
		super(message);
	}
	
}
