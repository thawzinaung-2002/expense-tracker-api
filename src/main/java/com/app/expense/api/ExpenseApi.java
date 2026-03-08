package com.app.expense.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.expense.api.response.ApiResponse;
import com.app.expense.dto.ExpenseDto;
import com.app.expense.service.ExpenseService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@ResponseStatus(value = HttpStatus.OK)
public class ExpenseApi {
	
	private final ExpenseService expenseService;
	
	@PostMapping
	public ApiResponse<ExpenseDto> createExpense(@Valid @RequestBody ExpenseDto form) {
		
		ApiResponse<ExpenseDto> result = new ApiResponse<>();
		
		expenseService.createExpense(form);
		
		return result;
		
	}

}
