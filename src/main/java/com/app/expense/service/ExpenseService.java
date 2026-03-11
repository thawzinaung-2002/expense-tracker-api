package com.app.expense.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.expense.api.exceptions.custom.BusinessException;
import com.app.expense.dto.ExpenseDto;
import com.app.expense.dto.ExpenseFilterSearch;
import com.app.expense.dto.PageInfoDto;
import com.app.expense.entity.Expenses;
import com.app.expense.entity.Expenses_;
import com.app.expense.repo.CategoryRepo;
import com.app.expense.repo.ExpenseRepo;
import com.app.expense.utils.DateUtils;
import com.app.expense.utils.SecurityContextHolderUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

	private final ExpenseRepo expenseRepo;

	private final CategoryRepo categoryRepo;

	@Transactional
	public void createExpense(ExpenseDto form) {

		var expense = new Expenses();
		
		updateEntity(expense, form);
		
		expenseRepo.save(expense);
		
	}

	private Expenses updateEntity(Expenses entity, ExpenseDto form) {
		
		entity.setAmount(form.amount());

		var cat = categoryRepo.findById(form.catergoryId())
				.orElseThrow(() -> new BusinessException("Category not found!"));

		entity.setCategory(cat);
		
		return entity;
	}

	public List<ExpenseDto> getAllExpenses() {

		var userId = SecurityContextHolderUtils.getEmail();

		return expenseRepo.findByCreatedBy(userId).stream().map(Expenses::toDto).collect(Collectors.toList());
	}

	@Transactional
	public void updateExpense(Integer id, ExpenseDto form) {

		var entity = expenseRepo.findById(id).orElseThrow(() -> new BusinessException("Id not found!"));

		updateEntity(entity, form);
		
		expenseRepo.save(entity);

	}

	@Transactional
	public void deleteExpense(@NotNull(message = "Id must not be empty") Integer id) {
		
		var entity = expenseRepo.findById(id).orElseThrow(() -> new BusinessException("Id not found!"));
		
		validateAuthority(entity);
		
		expenseRepo.delete(entity);
		
	}

	private void validateAuthority(Expenses entity) {
		
		var email = entity.getCreatedBy();
		
		if(!SecurityContextHolderUtils.getEmail().equalsIgnoreCase(email)) {
			throw new BusinessException("You are not authorized to delete this record!");
		}
		
	}

	public PageInfoDto<ExpenseDto> searchResult(ExpenseFilterSearch filter) {
		
		//create pageable
		Pageable pageable = PageRequest.of(filter.page(), filter.size());
		
		//create predicates
		var spec = createPredicates(filter);
		
		var contents = expenseRepo.findAll(spec, pageable)
		.stream()
		.map(Expenses::toDto)
		.collect(Collectors.toList());
		
		var totalCount = expenseRepo.count(spec);

		return new PageInfoDto<ExpenseDto>(
				filter.page(),
				filter.size(),
				totalCount,
				contents
				);
	}

	private Specification<Expenses> createPredicates(ExpenseFilterSearch filter) {
		
		return (root, _, cb) -> {
			
			if(null != filter.from() || null != filter.to()) {
				return cb.and(
						cb.greaterThanOrEqualTo(root.get(Expenses_.createDateTime), filter.from().atStartOfDay()),
						cb.lessThan(root.get(Expenses_.createDateTime), filter.to().plusDays(1).atStartOfDay())
						);
			}
			else if(filter.flag() != null){
				return switch(filter.flag()) {
				case "1" -> getPastWeek(root, cb);
				case "2" -> getPastMonth(root, cb);
				case "3" -> getPast3Months(root, cb);
				default -> getToday(root, cb);
				};
			}
			else {
				return getToday(root, cb);
			}
			
		};
		
		
	}

	private Predicate getToday(Root<Expenses> root, CriteriaBuilder cb) {
		return cb.and(
				cb.greaterThanOrEqualTo(root.get(Expenses_.createDateTime), LocalDate.now().atStartOfDay()),
				cb.lessThan(root.get(Expenses_.createDateTime), LocalDate.now().plusDays(1).atStartOfDay())
				);
	}

	private Predicate getPast3Months(Root<Expenses> root, CriteriaBuilder cb) {
		
		var startDay = DateUtils.getFirstDayOfLastNMonth(3);
		var endDay = DateUtils.getLastDayOfLastNMonth(3);
		
		return cb.and(
				cb.greaterThanOrEqualTo(root.get(Expenses_.createDateTime), startDay.atStartOfDay()),
				cb.lessThan(root.get(Expenses_.createDateTime), endDay.plusDays(1).atStartOfDay())
				);
	}

	private Predicate getPastMonth(Root<Expenses> root, CriteriaBuilder cb) {
		var startDay = DateUtils.getFirstDayOfLastNMonth(1);
		var endDay = DateUtils.getLastDayOfLastNMonth(1);
		
		return cb.and(
				cb.greaterThanOrEqualTo(root.get(Expenses_.createDateTime), startDay.atStartOfDay()),
				cb.lessThan(root.get(Expenses_.createDateTime), endDay.plusDays(1).atStartOfDay())
				);
	}

	private Predicate getPastWeek(Root<Expenses> root, CriteriaBuilder cb) {
		
		var startDay = DateUtils.getFirstDayOfLastWeek();
		var endDay = DateUtils.getLastDayOfLastWeek();
		
		return cb.and(
				cb.greaterThanOrEqualTo(root.get(Expenses_.createDateTime), startDay.atStartOfDay()),
				cb.lessThan(root.get(Expenses_.createDateTime), endDay.plusDays(1).atStartOfDay())
				);
	}



}
