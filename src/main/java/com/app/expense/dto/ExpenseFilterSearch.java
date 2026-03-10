package com.app.expense.dto;

import java.time.LocalDate;

public record ExpenseFilterSearch(
		String flag,
		LocalDate from,
		LocalDate to,
		int page,
		int size
		) {

}
