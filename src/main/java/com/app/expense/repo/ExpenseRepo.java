package com.app.expense.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.app.expense.entity.Expenses;


public interface ExpenseRepo extends JpaRepository<Expenses, Integer>, JpaSpecificationExecutor<Expenses>{

	List<Expenses> findByCreatedBy(String createdBy);

}
