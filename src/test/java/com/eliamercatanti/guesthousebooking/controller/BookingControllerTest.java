package com.eliamercatanti.guesthousebooking.controller;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
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

	@Mock
	public InputValidation inputValidation;

	@Nested
	@DisplayName("Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("Bookings list request - testAllBookings()")
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
		@DisplayName("New booking request when infos are valid - testNewBookingWhenBookingInfosAreValid()")
		void testNewBookingWhenBookingInfosAreValid() {
			Booking newBooking = new Booking("1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			when(inputValidation.validateDate("01/01/2021")).thenReturn(LocalDate.of(2021, 1, 1));
			when(inputValidation.validateDate("10/01/2021")).thenReturn(LocalDate.of(2021, 1, 10));
			bookingController.newBooking("1", "01/01/2021", "10/01/2021", 1, Room.SINGLE);
			InOrder inOrder = inOrder(bookingRepository, guesthouseView);
			inOrder.verify(bookingRepository).save(newBooking);
			inOrder.verify(guesthouseView).bookingAdded(newBooking);
			verifyNoMoreInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("Delete booking request when exist - testdeleteBookingWhenBookingExist()")
		void testdeleteBookingWhenBookingExist() {
			Booking bookingToDelete = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			when(bookingRepository.findById(bookingToDelete.getId())).thenReturn(bookingToDelete);
			bookingController.deleteBooking(bookingToDelete);
			InOrder inOrder = inOrder(bookingRepository, guesthouseView);
			inOrder.verify(bookingRepository).delete(bookingToDelete.getId());
			inOrder.verify(guesthouseView).bookingRemoved(bookingToDelete);
			verifyNoMoreInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

	}

	@Nested
	@DisplayName("Exceptional Cases")
	class ExceptionalCases {

		@Test
		@DisplayName("New booking request when check in date is not valid - testAddBookingWhenCheckInDateIsNotValid()")
		void testAddBookingWhenCheckInDateIsNotValid() {
			when(inputValidation.validateDate("dateNotValid")).thenReturn(null);
			when(inputValidation.validateDate("10/01/2021")).thenReturn(LocalDate.of(2021, 1, 10));
			bookingController.newBooking("1", "dateNotValid", "10/01/2021", 1, Room.SINGLE);
			verify(guesthouseView).showError(
					"Booking Check In Date is not valid: dateNotValid. Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("New booking request when check out date is not valid - testAddBookingWhenCheckOutDateIsNotValid()")
		void testAddBookingWhenCheckOutDateIsNotValid() {
			when(inputValidation.validateDate("01/01/2021")).thenReturn(LocalDate.of(2021, 1, 1));
			when(inputValidation.validateDate("dateNotValid")).thenReturn(null);
			bookingController.newBooking("1", "01/01/2021", "dateNotValid", 1, Room.SINGLE);
			verify(guesthouseView).showError(
					"Booking Check Out Date is not valid: dateNotValid. Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

	}

}
