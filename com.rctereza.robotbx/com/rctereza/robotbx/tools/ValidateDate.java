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
			//System.out.println("Reject null or empty strings");
			result = false; // Reject null or empty strings
		} else {

			//"uuuu" = proleptic year (the actual year number, no era needed)
			//"yyyy" = year-of-era (requires era in STRICT mode)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.forLanguageTag("pt-BR"))
					.withResolverStyle(ResolverStyle.STRICT); // STRICT ensures 2023-02-29 is rejected

			try {
				LocalDate.parse(dateStr, formatter);
			} catch (DateTimeParseException e) {
				//System.out.println("Parsing error: " + e.getMessage());
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

	public static boolean isGraterThanToday(String dateStr) {
		boolean result = true;

		LocalDate today = LocalDate.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		try {
			// Parse strings into LocalDate objects
			LocalDate date = LocalDate.parse(dateStr, formatter);

			// Compare dates
			if (date.isBefore(today)) {
				result = false;
			}

		} catch (DateTimeParseException e) {
			result = false;
		}

		return result;
	}

}
