package com.rctereza.robotbx.tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class ValidateDate {

	public static boolean isValidDate(String dateStr) {
		boolean result = true;

		if (dateStr == null || dateStr.isBlank()) {
			result = false; // Reject null or empty strings
		} else {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))
					.withResolverStyle(ResolverStyle.STRICT); // STRICT ensures 2023-02-29 is rejected

			try {
				LocalDate.parse(dateStr, formatter);
			} catch (DateTimeParseException e) {
				result = false; // Invalid date format or value
			}
		}

		return result;
	}

	public static boolean isValidRange(String startDateStr, String endDateStr) {

		boolean result = true;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		try {
			// Parse strings into LocalDate objects
			LocalDate startDate = LocalDate.parse(startDateStr, formatter);
			LocalDate endDate = LocalDate.parse(endDateStr, formatter);

			// Compare dates
			if (!startDate.isBefore(endDate)) {
				result = false;
			}

		} catch (DateTimeParseException e) {
			result = false;
		}
		
		return result;
	}

}
