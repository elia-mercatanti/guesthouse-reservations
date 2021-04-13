package com.eliamercatanti.guesthousebooking.controller;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for Booking Controller")
class BookingControllerTest {

	@InjectMocks
	public BookingController bookingController;

	@Mock
	public BookingRepository bookingRepository;

	@Mock
	public GuesthouseView guesthouseView;

	@Nested
	@DisplayName("Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("testAllBookings - Bookings list request")
		void testAllBookings() {
			Booking booking1 = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Booking booking2 = new Booking("2", "1", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2,
					Room.DOUBLE);
			List<Booking> bookingsList = Arrays.asList(booking1, booking2);
			when(bookingRepository.findAll()).thenReturn(bookingsList);
			bookingController.allBookings();
			verify(guesthouseView).showAllBookings(bookingsList);
			verifyNoMoreInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("testNewBookingWhenBookingInfosAreValid - New booking request when infos are valid")
		void testNewBookingWhenBookingInfosAreValid() {
			Booking newBooking = new Booking("1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			bookingController.newBooking("1", "2021-01-01", "2021-01-10", 1, Room.SINGLE);
			InOrder inOrder = inOrder(bookingRepository, guesthouseView);
			inOrder.verify(bookingRepository).save(newBooking);
			inOrder.verify(guesthouseView).bookingAdded(newBooking);
			verifyNoMoreInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

	}

	@Nested
	@DisplayName("Exceptional Cases")
	class ExceptionalCases {

	}

}
