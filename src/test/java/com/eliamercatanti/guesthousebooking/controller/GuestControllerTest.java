package com.eliamercatanti.guesthousebooking.controller;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
		@DisplayName("testAllGuests: delegate to repository a guest list request, delegate to the view the results")
		void testAllGuests() {
			List<Guest> guestsList = Arrays.asList(new Guest());
			when(guestRepository.findAll()).thenReturn(guestsList);
			guestController.allGuests();
			verify(guesthouseView).showAllGuests(guestsList);
		}

	}

	@Nested
	@DisplayName("Exceptional Cases")
	class ExceptionalCases {

	}

}
