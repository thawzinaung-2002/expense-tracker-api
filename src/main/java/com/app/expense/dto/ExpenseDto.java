package com.app.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record ExpenseDto(
		
		Integer id,
		
		@NotNull(message = "Amount must not be empty!")
		BigDecimal amount,
		
		@NotNull(message="Category must not be empty!")
		Integer catergoryId,
		
		String categoryName,
		
		String createdBy,
		
		LocalDateTime createdDateTime,
		
		String updatedBy,
		
		LocalDateTime updateDateTime
		) {

}
