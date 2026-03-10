package com.app.expense.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.expense.entity.Categories;
import com.app.expense.repo.CategoryRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepo catRepo;
	
	@Transactional
	public void createCategory() {
		
		var names = List.of("Groceries", "Leisure", "Electronics", "Utilities", "Clothing", "Health" , "Others");
		
		names.forEach(name -> {
			var cat = new Categories();
			cat.setName(name);
			catRepo.save(cat);
		});
		
	}

}
