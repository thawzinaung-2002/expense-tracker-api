package com.app.expense.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.expense.api.response.ApiResponse;
import com.app.expense.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/category")
@ResponseStatus(value = HttpStatus.OK)
@RequiredArgsConstructor
public class CategoryApi {

	private final CategoryService categoryService;
	
	@GetMapping("create")
	public ApiResponse<String> createCategory() {
		
		var result = new ApiResponse<String>();
		
		categoryService.createCategory();
		
		return result;
	}
	
}
