package com.app.expense.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.expense.entity.Expenses;

public interface ExpenseRepo extends JpaRepository<Expenses, Integer>{

}
