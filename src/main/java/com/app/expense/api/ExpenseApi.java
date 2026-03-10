package com.app.expense.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.expense.api.response.ApiResponse;
import com.app.expense.dto.ExpenseDto;
import com.app.expense.dto.ExpenseFilterSearch;
import com.app.expense.dto.PageInfoDto;
import com.app.expense.service.ExpenseService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@ResponseStatus(value = HttpStatus.OK)
public class ExpenseApi {

	private final ExpenseService expenseService;

	@GetMapping
	public ApiResponse<ExpenseDto> getAllExpenses() {

		var result = new ApiResponse<ExpenseDto>();

		result.setContents(expenseService.getAllExpenses());

		return result;
	}

	@PostMapping("create")
	public ApiResponse<ExpenseDto> createExpense(@Valid @RequestBody ExpenseDto form) {

		ApiResponse<ExpenseDto> result = new ApiResponse<>();

		expenseService.createExpense(form);

		return result;

	}

	@PostMapping("update/{id}")
	public ApiResponse<Object> createExpense(@PathVariable @NotNull(message = "Id must not be empty") Integer id,
			@RequestBody ExpenseDto form) {

		var result = new ApiResponse<Object>();

		expenseService.updateExpense(id, form);

		return result;

	}

	@DeleteMapping("delete/{id}")
	public ApiResponse<Object> deleteExpense(@PathVariable @NotNull(message = "Id must not be empty") Integer id) {

		var result = new ApiResponse<Object>();

		expenseService.deleteExpense(id);

		return result;
	}

	@PostMapping("filter/search")
	public ApiResponse<PageInfoDto<ExpenseDto>> filterSearch(@RequestBody ExpenseFilterSearch filter) {

		var result = new ApiResponse<PageInfoDto<ExpenseDto>>();

		result.setContents(expenseService.searchResult(filter));

		return result;
	}

}
