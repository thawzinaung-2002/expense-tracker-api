package com.app.expense.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {

	public static LocalDate getFirstDayOfLastWeek() {
		
		return LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		
	}

	public static LocalDate getLastDayOfLastWeek() {
		
		return LocalDate.now().minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		
	}

	public static LocalDate getFirstDayOfLastNMonth(int months) {

		return LocalDate.now().minusMonths(months).with(TemporalAdjusters.firstDayOfMonth());
	}

	public static LocalDate getLastDayOfLastNMonth(int months) {
		
		return LocalDate.now().minusMonths(months).with(TemporalAdjusters.lastDayOfMonth());
	}
	

}
