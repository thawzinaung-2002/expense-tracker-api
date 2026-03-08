package com.app.expense.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.expense.entity.Categories;

public interface CategoryRepo extends JpaRepository<Categories, Integer>{

}
