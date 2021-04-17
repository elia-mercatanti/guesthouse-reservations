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
		
		@Test
		@DisplayName("Email validation should return true when string format is valid - testValidateEmailShouldReturnFalseWhenStringFormatIsValid()")
		void testValidateEmailShouldReturnFalseWhenStringFormatIsValid() {
			assertThat(controllerInputValidator.validateEmail("test@test.com")).isTrue();
			assertThat(controllerInputValidator.validateEmail("test12@test12.com")).isTrue();
			assertThat(controllerInputValidator.validateEmail("test12$@test12$.com")).isTrue();
			assertThat(controllerInputValidator.validateEmail("1234@1234.com")).isTrue();
			assertThat(controllerInputValidator.validateEmail("test@test")).isTrue();
			assertThat(controllerInputValidator.validateEmail("1234@1234")).isTrue();
		}
		
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
		
		@Test
		@DisplayName("Email validation should return false when string does not contain prefix and suffix around @ - testValidateEmailShouldReturnFalseWhenStringDoesNotContainPrefixAndSuffixAroundAtSign()")
		void testValidateEmailShouldReturnFalseWhenStringDoesNotContainPrefixAndSuffixAroundAtSign() {
			assertThat(controllerInputValidator.validateEmail("@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("test@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("test£$%@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("test123@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("000@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("test123!£$@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@test")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@test£$%")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@test123")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@000")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@test123!£$")).isFalse();
		}

		@Test
		@DisplayName("Email validation should return false when string contains spaces - testValidateEmailShouldReturnFalseWhenStringContainsSpaces()")
		void testValidateEmailShouldReturnFalseWhenStringContainsSpaces() {
			assertThat(controllerInputValidator.validateEmail(" @ ")).isFalse();
			assertThat(controllerInputValidator.validateEmail("a1$ @ a1%")).isFalse();
			assertThat(controllerInputValidator.validateEmail("a1$  @  a1%")).isFalse();
			assertThat(controllerInputValidator.validateEmail("  a1$  @  a1%  ")).isFalse();
		}
		
		@Test
		@DisplayName("Telephone N. validation should return false when string is null - testValidateTelephoneNumberShouldReturnFalseWhenStringIsNull()")
		void testValidateTelephoneNumberShouldReturnFalseWhenStringIsNull() {
			assertThat(controllerInputValidator.validateTelephoneNumber(null)).isFalse();
		}
		
		@Test
		@DisplayName("Telephone N. validation should return false when string is empty - testValidateTelephoneNumberShouldReturnFalseWhenStringIsEmpty()")
		void testValidateTelephoneNumberShouldReturnFalseWhenStringIsEmpty() {
			assertThat(controllerInputValidator.validateTelephoneNumber("")).isFalse();
		}
		
		@Test
		@DisplayName("Telephone N. validation should return false when string is blank - testValidateTelephoneNumberShouldReturnFalseWhenStringIsBlank()")
		void testValidateTelephoneNumberShouldReturnFalseWhenStringIsBlank() {
			assertThat(controllerInputValidator.validateTelephoneNumber("    ")).isFalse();
		}
		
	}

}
