package com.eliamercatanti.guesthousebooking.controller;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

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
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for Guest Controller")
class GuestControllerTest {

	@Mock
	private GuestRepository guestRepository;

	@Mock
	private GuesthouseView guesthouseView;

	@InjectMocks
	private GuestController guestController;

	@Nested
	@DisplayName("Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("testAllGuests - Guest list request")
		void testAllGuests() {
			List<Guest> guestsList = Arrays.asList(new Guest());
			when(guestRepository.findAll()).thenReturn(guestsList);
			guestController.allGuests();
			verify(guesthouseView).showAllGuests(guestsList);
		}

		@Test
		@DisplayName("testNewGuest - New guest request")
		void testNewGuest() {
			Guest newGuest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			guestController.newGuest(newGuest);
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
		@DisplayName("testDeleteGuestWhenGuestNotExist - Delete guest request when not exist")
		void testDeleteGuestWhenGuestNotExist() {
			Guest guestNotPresent = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(guestRepository.findById(guestNotPresent.getId())).thenReturn(null);
			guestController.deleteGuest(guestNotPresent);
			verify(guesthouseView).errorGuestNotFound("There is no guest with id " + guestNotPresent.getId() + ".", guestNotPresent);
			verifyNoMoreInteractions(ignoreStubs(guestRepository));
		}

	}

}
