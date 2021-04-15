package com.eliamercatanti.guesthousebooking.controller;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for Guest Controller")
class GuestControllerTest {

	@Mock
	private GuestRepository guestRepository;

	@Mock
	private GuesthouseView guesthouseView;

	@Mock
	private InputValidation inputValidation;

	@InjectMocks
	private GuestController guestController;

	@Nested
	@DisplayName("Guest Controller Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("All Guests list request - testAllGuests()")
		void testAllGuests() {
			Guest guest1 = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "1111111111");
			Guest guest2 = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "2222222222");
			List<Guest> guestsList = Arrays.asList(guest1, guest2);
			when(guestRepository.findAll()).thenReturn(guestsList);
			guestController.allGuests();
			verify(guesthouseView).showGuests(guestsList);
			verifyNoMoreInteractions(guestRepository, guesthouseView);
		}

		@Test
		@DisplayName("New guest request when infos are valid - testNewGuestWhenGuestInfosAreValid()")
		void testNewGuestWhenGuestInfosAreValid() {
			Guest newGuest = new Guest("testFirstName", "testLastName", "test@email.com", "1234567890");
			when(inputValidation.validateEmail("test@email.com")).thenReturn(true);
			when(inputValidation.validateTelephoneNumber("1234567890")).thenReturn(true);
			guestController.newGuest("testFirstName", "testLastName", "test@email.com", "1234567890");
			InOrder inOrder = inOrder(guestRepository, guesthouseView);
			inOrder.verify(guestRepository).save(newGuest);
			inOrder.verify(guesthouseView).guestAdded(newGuest);
			verifyNoMoreInteractions(guestRepository, guesthouseView);
		}

		@Test
		@DisplayName("Delete guest request when guest exist - testDeleteGuestWhenGuestExist()")
		void testDeleteGuestWhenGuestExist() {
			Guest guestToDelete = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(guestRepository.findById(guestToDelete.getId())).thenReturn(guestToDelete);
			guestController.deleteGuest(guestToDelete);
			InOrder inOrder = inOrder(guestRepository, guesthouseView);
			inOrder.verify(guestRepository).delete(guestToDelete.getId());
			inOrder.verify(guesthouseView).guestRemoved(guestToDelete);
			verifyNoMoreInteractions(guestRepository, guesthouseView);
		}
	}

	@Nested
	@DisplayName("Guest Controller Exceptional Cases")
	class ExceptionalCases {

		@Test
		@DisplayName("New guest request when email is not valid - testNewGuestWhenEmailIsNotValid()")
		void testNewGuestWhenEmailIsNotValid() {
			when(inputValidation.validateEmail("emailNotValid")).thenReturn(false);
			guestController.newGuest("testFirstName", "testLastName", "emailNotValid", "1234567890");
			verify(guesthouseView)
					.showError("Guest Email is not valid: emailNotValid. Format must be like prefix@domain.");
			verifyNoInteractions(guestRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("New guest request when telephone number is not valid - testNewGuestWhenTelephoneNumberIsNotValid()")
		void testNewGuestWhenTelephoneNumberIsNotValid() {
			when(inputValidation.validateEmail("test@email.com")).thenReturn(true);
			when(inputValidation.validateTelephoneNumber("telephoneNumNotValid")).thenReturn(false);
			guestController.newGuest("testFirstName", "testLastName", "test@email.com", "telephoneNumNotValid");
			verify(guesthouseView).showError(
					"Guest Telephone N. is not valid: telephoneNumNotValid. Format must be like +10000000000.");
			verifyNoInteractions(guestRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("Delete guest request when guest not exist - testDeleteGuestWhenGuestNotExist()")
		void testDeleteGuestWhenGuestNotExist() {
			Guest guestNotPresent = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(guestRepository.findById(guestNotPresent.getId())).thenReturn(null);
			guestController.deleteGuest(guestNotPresent);
			verify(guesthouseView).showErrorGuestNotFound("There is no guest with id " + guestNotPresent.getId() + ".",
					guestNotPresent);
			verifyNoMoreInteractions(guestRepository, guesthouseView);
		}

	}

}
