package com.eliamercatanti.guesthousebooking.validation.controller;

import java.time.LocalDate;
import java.util.regex.Pattern;

import com.eliamercatanti.guesthousebooking.validation.InputValidation;

public class ControllerInputValidator implements InputValidation {

	@Override
	public boolean validateEmail(String email) {
		if (email == null) {
			return false;
		}

		return Pattern.matches("[^@]+@[^@]+", email);
	}

	@Override
	public boolean validateTelephoneNumber(String telephoneNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LocalDate validateDate(String date) {
		// TODO Auto-generated method stub
		return null;
	}

}
