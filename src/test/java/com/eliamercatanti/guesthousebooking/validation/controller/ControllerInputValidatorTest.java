package com.eliamercatanti.guesthousebooking.validation.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

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
	@DisplayName("Email Validation")
	class EmailValidation {

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
			assertThat(controllerInputValidator.validateEmail("test$%@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("test123@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("000@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("test123!$@")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@test")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@test$%")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@test123")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@000")).isFalse();
			assertThat(controllerInputValidator.validateEmail("@test123!$")).isFalse();
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
		@DisplayName("Email validation should return true when string format is valid - testValidateEmailShouldReturnTrueWhenStringFormatIsValid()")
		void testValidateEmailShouldReturnTrueWhenStringFormatIsValid() {
			assertThat(controllerInputValidator.validateEmail("test@test.com")).isTrue();
			assertThat(controllerInputValidator.validateEmail("test12@test12.com")).isTrue();
			assertThat(controllerInputValidator.validateEmail("test12$@test12$.com")).isTrue();
			assertThat(controllerInputValidator.validateEmail("1234@1234.com")).isTrue();
			assertThat(controllerInputValidator.validateEmail("test@test")).isTrue();
			assertThat(controllerInputValidator.validateEmail("1234@1234")).isTrue();
		}

	}

	@Nested
	@DisplayName("Telephone N. Validation")
	class TelephoneNumberValidation {

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

		@Test
		@DisplayName("Telephone N. validation should return false when string length is more than 15 - testValidateTelephoneNumberShouldReturnFalseWhenStringLengthIsMoreThanFifteen()")
		void testValidateTelephoneNumberShouldReturnFalseWhenStringLengthIsMoreThanFifteen() {
			assertThat(controllerInputValidator.validateTelephoneNumber("0000000000000000")).isFalse();
		}

		@Test
		@DisplayName("Telephone N. validation should return false when string does not contain only numbers or with a plus at the beginning - testValidateTelephoneNumberShouldReturnFalseWhenStringDoesNotContainOnlyNumbersOrWithAPlusAtTheBeginning()")
		void testValidateTelephoneNumberShouldReturnFalseWhenStringDoesNotContainOnlyNumbersOrWithAPlusAtTheBeginning() {
			assertThat(controllerInputValidator.validateTelephoneNumber("aaaaaaaaaa!$%")).isFalse();
			assertThat(controllerInputValidator.validateTelephoneNumber("00000a$%A0")).isFalse();
			assertThat(controllerInputValidator.validateTelephoneNumber("+aaaaaaaaaa")).isFalse();
			assertThat(controllerInputValidator.validateTelephoneNumber("+00000a$%A0")).isFalse();
			assertThat(controllerInputValidator.validateTelephoneNumber("+")).isFalse();
			assertThat(controllerInputValidator.validateTelephoneNumber("++++")).isFalse();
			assertThat(controllerInputValidator.validateTelephoneNumber("aaa000aaa")).isFalse();
		}

		@Test
		@DisplayName("Telephone N. validation should return true when string format is valid - testValidateTelephoneNumberShouldReturnTrueWhenStringFormatIsValid()")
		void testValidateTelephoneNumberShouldReturnTrueWhenStringFormatIsValid() {
			assertThat(controllerInputValidator.validateTelephoneNumber("1234567890")).isTrue();
			assertThat(controllerInputValidator.validateTelephoneNumber("+1234567890")).isTrue();
			assertThat(controllerInputValidator.validateTelephoneNumber("+00000000000000")).isTrue();
		}

	}

	@Nested
	@DisplayName("Date Validation")
	class DateValidation {

		@Test
		@DisplayName("Date validation should return null when string is null - testValidateDateShouldReturnNullWhenStringIsNull()")
		void testValidateDateShouldReturnNullWhenStringIsNull() {
			assertThat(controllerInputValidator.validateDate(null)).isNull();
		}

		@Test
		@DisplayName("Date validation should return null when string is empty - testValidateDateShouldReturnNullWhenStringIsEmpty()")
		void testValidateDateShouldReturnNullWhenStringIsEmpty() {
			assertThat(controllerInputValidator.validateDate("")).isNull();
		}

		@Test
		@DisplayName("Date validation should return null when string is blank - testValidateDateShouldReturnNullWhenStringIsBlank()")
		void testValidateDateShouldReturnNullWhenStringIsBlank() {
			assertThat(controllerInputValidator.validateDate("    ")).isNull();
		}

		@Test
		@DisplayName("Date validation should return null when string length is not 10 - testValidateDateShouldReturnNullWhenStringLengthIsNotTen()")
		void testValidateDateShouldReturnNullWhenStringLengthIsNotTen() {
			assertThat(controllerInputValidator.validateDate("00-00-00000")).isNull();
			assertThat(controllerInputValidator.validateDate("000-00-00")).isNull();
		}

		@Test
		@DisplayName("Date validation should return null when string date format is not valid =! dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd - testValidateDateShouldReturnNullWhenStringLengthIsNotValid()")
		void testValidateDateShouldReturnNullWhenStringFormatIsNotValid() {
			assertThat(controllerInputValidator.validateDate("0123456789")).isNull();
			assertThat(controllerInputValidator.validateDate("aaaaaaaaaa")).isNull();
			assertThat(controllerInputValidator.validateDate("aaaaa12345")).isNull();
			assertThat(controllerInputValidator.validateDate("00-00-0000")).isNull();
			assertThat(controllerInputValidator.validateDate("0000/00/00")).isNull();
			assertThat(controllerInputValidator.validateDate("aaaa/aa/aa")).isNull();
			assertThat(controllerInputValidator.validateDate("00/aa/0000")).isNull();
			assertThat(controllerInputValidator.validateDate("01$01%2021")).isNull();
		}

		@Test
		@DisplayName("Date validation should return correct local date when string date format is valid == dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd - testValidateDateShouldReturnCorrectLocalDateWhenStringLengthIsValid()")
		void testValidateDateShouldReturnCorrectLocalDateWhenStringFormatIsValid() {
			LocalDate dateToValidate = LocalDate.of(2021, 1, 1);
			assertThat(controllerInputValidator.validateDate("01/01/2021")).isEqualTo(dateToValidate);
			assertThat(controllerInputValidator.validateDate("01-01-2021")).isEqualTo(dateToValidate);
			assertThat(controllerInputValidator.validateDate("01.01.2021")).isEqualTo(dateToValidate);
			assertThat(controllerInputValidator.validateDate("2021/01/01")).isEqualTo(dateToValidate);
			assertThat(controllerInputValidator.validateDate("2021-01-01")).isEqualTo(dateToValidate);
			assertThat(controllerInputValidator.validateDate("2021.01.01")).isEqualTo(dateToValidate);
		}

	}

}
