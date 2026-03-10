package com.app.expense.dto;

import java.util.List;

public record PageInfoDto<T>(
		int page,
		int size,
		long totalCount,
		List<T> conents
		) {

}
