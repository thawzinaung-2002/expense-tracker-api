package com.app.expense.api.response;

import java.util.List;

import lombok.Data;

@Data
public class ApiResponse<T>{

	private String status = "Success";
	private String message;
	private List<T> contents;
	private String token;
	
	@SuppressWarnings("unchecked")
	public void setContents(T... contents) {
		this.contents = List.of(contents);
	}
	
	public void setContents(List<T> contents) {
		this.contents = contents;
	}
}
