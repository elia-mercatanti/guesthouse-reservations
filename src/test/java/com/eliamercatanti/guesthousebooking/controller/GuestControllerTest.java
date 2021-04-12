package com.eliamercatanti.guesthousebooking.controller;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
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
	@DisplayName("Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("testAllGuests - Guests list request")
		void testAllGuests() {
			List<Guest> guestsList = Arrays.asList(new Guest());
			when(guestRepository.findAll()).thenReturn(guestsList);
			guestController.allGuests();
			verify(guesthouseView).showAllGuests(guestsList);
		}

		@Test
		@DisplayName("testNewGuestWhenGuestInfosAreValid - New guest request when guest infos are valid")
		void testNewGuestWhenGuestInfosAreValid() {
			Guest newGuest = new Guest("testFirstName", "testLastName", "test@email.com", "1234567890");
			guestController.newGuest("testFirstName", "testLastName", "test@email.com", "1234567890");
			InOrder inOrder = inOrder(guestRepository, guesthouseView);
			inOrder.verify(guestRepository).save(newGuest);
			inOrder.verify(guesthouseView).guestAdded(newGuest);
		}

		@Test
		@DisplayName("testDeleteGuestWhenGuestExist - Delete guest request when exist")
		void testDeleteGuestWhenGuestExist() {
			Guest guestToDelete = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(guestRepository.findById(guestToDelete.getId())).thenReturn(guestToDelete);
			guestController.deleteGuest(guestToDelete);
			InOrder inOrder = inOrder(guestRepository, guesthouseView);
			inOrder.verify(guestRepository).delete(guestToDelete.getId());
			inOrder.verify(guesthouseView).guestRemoved(guestToDelete);
		}
	}

	@Nested
	@DisplayName("Exceptional Cases")
	class ExceptionalCases {

		@Test
		@DisplayName("testNewGuestWhenEmailIsNotValid - New guest request when guest email is not valid")
		void testNewGuestWhenEmailIsNotValid() {
			when(inputValidation.validateEmail("testEmail")).thenReturn(false);
			guestController.newGuest("testFirstName", "testLastName", "testEmail", "1234567890");
			verify(guesthouseView).showError("Guest Email is not valid: testEmail. The email format must be like prefix@domain." );
			verifyNoInteractions(guestRepository);
		}

		@Test
		@DisplayName("testDeleteGuestWhenGuestNotExist - Delete guest request when not exist")
		void testDeleteGuestWhenGuestNotExist() {
			Guest guestNotPresent = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(guestRepository.findById(guestNotPresent.getId())).thenReturn(null);
			guestController.deleteGuest(guestNotPresent);
			verify(guesthouseView).showErrorGuestNotFound("There is no guest with id " + guestNotPresent.getId() + ".",
					guestNotPresent);
			verifyNoMoreInteractions(guestRepository);
		}

	}

}
