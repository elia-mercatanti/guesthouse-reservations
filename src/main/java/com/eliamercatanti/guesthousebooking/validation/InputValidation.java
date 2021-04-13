package com.eliamercatanti.guesthousebooking.validation;

import java.time.LocalDate;

public interface InputValidation {

	boolean validateEmail(String email);

	boolean validateTelephoneNumber(String telephoneNumber);

	LocalDate validateDate(String date);

}
