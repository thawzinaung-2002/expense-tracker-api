package com.app.expense.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class Auditing {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createDateTime;
	
	@CreatedBy
	@Column(updatable = false)
	private String createdBy;
	
	@LastModifiedDate
	private LocalDateTime lastUpdatedAt;
	
	@LastModifiedBy
	private String lastUpdateBy;
	
}
