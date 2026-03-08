package com.app.expense.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.expense.api.exceptions.custom.BusinessException;
import com.app.expense.dto.ExpenseDto;
import com.app.expense.entity.Expenses;
import com.app.expense.repo.CategoryRepo;
import com.app.expense.repo.ExpenseRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

	private final ExpenseRepo expenseRepo;
	
	private final CategoryRepo categoryRepo;
	
	@Transactional
	public void createExpense(ExpenseDto form) {
		
		var entity = new Expenses();
		
		entity.setAmount(form.amount());
		
		var cat = categoryRepo.findById(form.catergoryId()).orElseThrow(() -> new BusinessException("Category not found!"));
		
		entity.setCategory(cat);
		
		expenseRepo.save(entity);
	}

}
