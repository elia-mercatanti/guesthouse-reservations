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

		return Pattern.matches("[^@\s]+@[^@\s]+", email);
	}

	@Override
	public boolean validateTelephoneNumber(String telephoneNumber) {
		if (telephoneNumber == null) {
			return false;
		}
		return true;
	}

	@Override
	public LocalDate validateDate(String date) {
		// TODO Auto-generated method stub
		return null;
	}

}
