package com.app.expense.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record ExpenseDto(
		@NotNull(message = "Amount must not be empty!")
		BigDecimal amount,
		
		@NotNull(message="Category must not be empty!")
		Integer catergoryId
		) {

}
