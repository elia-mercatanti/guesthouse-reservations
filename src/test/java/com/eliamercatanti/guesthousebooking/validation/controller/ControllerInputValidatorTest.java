package com.eliamercatanti.guesthousebooking.validation.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for Controller Input Validator")
class ControllerInputValidatorTest {

	static ControllerInputValidator controllerInputValidator;
	
	@BeforeAll
	static void setup() {
		controllerInputValidator = new ControllerInputValidator();
	}

	@Nested
	@DisplayName("Controller Input Validator Happy Cases")
	class HappyCases {
		
	}

	@Nested
	@DisplayName("Controller Input Validator Exceptional Cases")
	class ExceptionalCases {
		
		@Test
		@DisplayName("Email validation should return false when string is null - testValidateEmailShouldReturnFalseWhenStringIsNull()")
		void testValidateEmailShouldReturnFalseWhenStringIsNull() {
			assertThat(controllerInputValidator.validateEmail(null)).isFalse();
		}
		
		@Test
		@DisplayName("Email validation should return false when string is empty - testValidateEmailShouldReturnFalseWhenStringIsEmpty()")
		void testValidateEmailShouldReturnFalseWhenStringIsEmpty() {
			assertThat(controllerInputValidator.validateEmail("")).isFalse();
		}

	}

}
