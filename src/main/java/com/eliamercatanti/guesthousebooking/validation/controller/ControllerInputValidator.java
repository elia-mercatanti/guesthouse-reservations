package com.eliamercatanti.guesthousebooking.validation.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import com.eliamercatanti.guesthousebooking.validation.InputValidation;

public class ControllerInputValidator implements InputValidation {

	private static final int TELEPHONE_NUM_MAX_LENGTH = 15;

	private static final String[] DATE_PATTERNS = { "dd/MM/yyyy", "dd-MM-yyyy", "dd.MM.yyyy", "yyyy/MM/dd",
			"yyyy-MM-dd", "yyyy.MM.dd" };

	@Override
	public boolean validateEmail(String email) {
		if (email == null) {
			return false;
		}

		return Pattern.matches("[^@\\s]+@[^@\\s]+", email);
	}

	@Override
	public boolean validateTelephoneNumber(String telephoneNumber) {
		if (telephoneNumber == null || telephoneNumber.length() > TELEPHONE_NUM_MAX_LENGTH) {
			return false;
		}

		return Pattern.matches("^[+]\\d+$|^\\d+$", telephoneNumber);
	}

	@Override
	public LocalDate validateDate(String dateString) {
		if (dateString == null) {
			return null;
		}

		for (String pattern : DATE_PATTERNS) {
			if (isParsable(dateString, pattern)) {
				return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
			}
		}

		return null;
	}

	private boolean isParsable(String dateString, String pattern) {
		try {
			LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
		} catch (DateTimeParseException e) {
			return false;
		}

		return true;
	}

}
