package com.eliamercatanti.guesthousebooking.validation.controller;

import java.time.LocalDate;
import java.util.regex.Pattern;

import com.eliamercatanti.guesthousebooking.validation.InputValidation;

public class ControllerInputValidator implements InputValidation {

	private static final int TELEPHONE_NUM_MAX_LENGTH = 15;

	@Override
	public boolean validateEmail(String email) {
		if (email == null) {
			return false;
		}

		return Pattern.matches("[^@\s]+@[^@\s]+", email);
	}

	@Override
	public boolean validateTelephoneNumber(String telephoneNumber) {
		if (telephoneNumber == null || telephoneNumber.length() > TELEPHONE_NUM_MAX_LENGTH) {
			return false;
		}

		return Pattern.matches("^[+]\\d+$|^\\d+$", telephoneNumber);
	}

	@Override
	public LocalDate validateDate(String date) {
		if (date == null || date.isEmpty()) {
			return null;
		}
		
		return LocalDate.parse(date);
	}

}
