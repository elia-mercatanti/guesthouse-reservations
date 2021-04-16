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
		
		@Test
		@DisplayName("Email validation should return false when string length is less than 3 - testValidateEmailShouldReturnFalseWhenStringLengthIsLessThanThree()")
		void testValidateEmailShouldReturnFalseWhenStringLengthIsLessThanThree() {
			assertThat(controllerInputValidator.validateEmail("a")).isFalse();
			assertThat(controllerInputValidator.validateEmail("aa")).isFalse();
			assertThat(controllerInputValidator.validateEmail("  ")).isFalse();
		}
		
		@Test
		@DisplayName("Email validation should return false when string is blank - testValidateEmailShouldReturnFalseWhenStringIsBlank()")
		void testValidateEmailShouldReturnFalseWhenStringIsBlank() {
			assertThat(controllerInputValidator.validateEmail("    ")).isFalse();
		}
		
		@Test
		@DisplayName("Email validation should return false when string does not contain a single @ in the middle - testValidateEmailShouldReturnFalseWhenStringDoesNotContainASingleAtSignInTheMiddle()")
		void testValidateEmailShouldReturnFalseWhenStringDoesNotContainASingleAtSignInTheMiddle() {
			assertThat(controllerInputValidator.validateEmail("test..test")).isFalse();
			assertThat(controllerInputValidator.validateEmail("testtest")).isFalse();
			assertThat(controllerInputValidator.validateEmail("testTest")).isFalse();
			assertThat(controllerInputValidator.validateEmail("AAAA")).isFalse();
			assertThat(controllerInputValidator.validateEmail("%A!t&g1=?^")).isFalse();
			assertThat(controllerInputValidator.validateEmail("test@@test")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@@@@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("0000")).isFalse();
		}
		
	}

}
