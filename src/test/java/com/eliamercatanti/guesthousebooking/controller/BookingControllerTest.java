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
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for Booking Controller")
class BookingControllerTest {

	@InjectMocks
	private BookingController bookingController;

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private GuesthouseView guesthouseView;

	@Mock
	private InputValidation inputValidation;

	@Nested
	@DisplayName("Booking Controller Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("All Bookings list request - testAllBookings()")
		void testAllBookings() {
			Booking booking1 = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Booking booking2 = new Booking("2", "1", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2,
					Room.DOUBLE);
			List<Booking> bookingsList = Arrays.asList(booking1, booking2);
			when(bookingRepository.findAll()).thenReturn(bookingsList);
			bookingController.allBookings();
			verify(guesthouseView).showBookings(bookingsList);
			verifyNoMoreInteractions(bookingRepository, guesthouseView);
		}

		@Test
		@DisplayName("New booking request when infos are valid - testNewBookingWhenBookingInfosAreValid()")
		void testNewBookingWhenBookingInfosAreValid() {
			LocalDate checkInDate = LocalDate.of(2021, 1, 1);
			LocalDate checkOutDate = LocalDate.of(2021, 1, 10);
			Booking newBooking = new Booking("1", checkInDate, checkOutDate, 1, Room.SINGLE);
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(inputValidation.validateDate("01/01/2021")).thenReturn(checkInDate);
			when(inputValidation.validateDate("10/01/2021")).thenReturn(checkOutDate);
			when(bookingRepository.checkRoomAvailabilityInDateRange(Room.SINGLE, checkInDate, checkOutDate))
					.thenReturn(true);
			bookingController.newBooking(guest, "01/01/2021", "10/01/2021", 1, Room.SINGLE);
			InOrder inOrder = inOrder(bookingRepository, guesthouseView);
			inOrder.verify(bookingRepository).save(newBooking);
			inOrder.verify(guesthouseView).bookingAdded(newBooking);
			verifyNoMoreInteractions(bookingRepository, guesthouseView);
		}

		@Test
		@DisplayName("Delete booking request when booking exist - testDeleteBookingWhenBookingExist()")
		void testDeleteBookingWhenBookingExist() {
			Booking bookingToDelete = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			when(bookingRepository.findById(bookingToDelete.getId())).thenReturn(bookingToDelete);
			bookingController.deleteBooking(bookingToDelete);
			InOrder inOrder = inOrder(bookingRepository, guesthouseView);
			inOrder.verify(bookingRepository).delete(bookingToDelete.getId());
			inOrder.verify(guesthouseView).bookingRemoved(bookingToDelete);
			verifyNoMoreInteractions(bookingRepository, guesthouseView);
		}

		@Test
		@DisplayName("Search bookings by dates request when dates are valid - testSearchBookingsByDatesWhenDatesAreValid()")
		void testSearchBookingsByDatesWhenDatesAreValid() {
			LocalDate firstDate = LocalDate.of(2021, 1, 1);
			LocalDate secondDate = LocalDate.of(2021, 3, 1);
			Booking booking1 = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Booking booking2 = new Booking("2", "2", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2,
					Room.DOUBLE);
			List<Booking> bookingsList = Arrays.asList(booking1, booking2);
			when(inputValidation.validateDate("01/01/2021")).thenReturn(firstDate);
			when(inputValidation.validateDate("01/03/2021")).thenReturn(secondDate);
			when(bookingRepository.findByDates(firstDate, secondDate)).thenReturn(bookingsList);
			bookingController.searchBookingsByDates("01/01/2021", "01/03/2021");
			verify(guesthouseView).showBookings(bookingsList);
			verifyNoMoreInteractions(bookingRepository, guesthouseView);
		}

		@Test
		@DisplayName("Search bookings by room request - testSearchBookingsByRoom()")
		void testSearchBookingsByRoom() {
			Booking booking1 = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Booking booking2 = new Booking("2", "2", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 1,
					Room.SINGLE);
			List<Booking> bookingsList = Arrays.asList(booking1, booking2);
			when(bookingRepository.findByRoom(Room.SINGLE)).thenReturn(bookingsList);
			bookingController.searchBookingsByRoom(Room.SINGLE);
			verify(guesthouseView).showBookings(bookingsList);
			verifyNoMoreInteractions(bookingRepository, guesthouseView);
		}

		@Test
		@DisplayName("Search bookings by guest request - testSearchBookingsByGuest()")
		void testSearchBookingsByGuest() {
			Booking booking1 = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Booking booking2 = new Booking("2", "1", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 1,
					Room.SINGLE);
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			List<Booking> bookingsList = Arrays.asList(booking1, booking2);
			when(bookingRepository.findByGuestId(guest.getId())).thenReturn(bookingsList);
			bookingController.searchBookingsByGuest(guest);
			verify(guesthouseView).showBookings(bookingsList);
			verifyNoMoreInteractions(bookingRepository, guesthouseView);
		}

	}

	@Nested
	@DisplayName("Booking Controller Exceptional Cases")
	class ExceptionalCases {

		@Test
		@DisplayName("New booking request when check in date is not valid - testNewBookingWhenCheckInDateIsNotValid()")
		void testNewBookingWhenCheckInDateIsNotValid() {
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(inputValidation.validateDate("dateNotValid")).thenReturn(null);
			when(inputValidation.validateDate("10/01/2021")).thenReturn(LocalDate.of(2021, 1, 10));
			bookingController.newBooking(guest, "dateNotValid", "10/01/2021", 1, Room.SINGLE);
			verify(guesthouseView).showError(
					"Booking Check In date is not valid: dateNotValid. Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("New booking request when check out date is not valid - testNewBookingWhenCheckOutDateIsNotValid()")
		void testNewBookingWhenCheckOutDateIsNotValid() {
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(inputValidation.validateDate("01/01/2021")).thenReturn(LocalDate.of(2021, 1, 1));
			when(inputValidation.validateDate("dateNotValid")).thenReturn(null);
			bookingController.newBooking(guest, "01/01/2021", "dateNotValid", 1, Room.SINGLE);
			verify(guesthouseView).showError(
					"Booking Check Out date is not valid: dateNotValid. Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("New booking request when check in date is after check out date - testNewBookingWhenCheckInDateIsAfterCheckOutDate()")
		void testNewBookingWhenCheckInDateIsAfterCheckOutDate() {
			LocalDate checkInDate = LocalDate.of(2021, 1, 10);
			LocalDate checkOutDate = LocalDate.of(2021, 1, 1);
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(inputValidation.validateDate("10/01/2021")).thenReturn(checkInDate);
			when(inputValidation.validateDate("01/01/2021")).thenReturn(checkOutDate);
			bookingController.newBooking(guest, "10/01/2021", "01/01/2021", 1, Room.SINGLE);
			verify(guesthouseView).showError("Check Out date must be after check in date.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("New booking request when check in and check out date are the same - testNewBookingWhenCheckInAndCheckOutDateAreTheSame()")
		void testNewBookingWhenCheckInAndCheckOutDateAreTheSame() {
			LocalDate sameDate = LocalDate.of(2021, 1, 1);
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(inputValidation.validateDate("01/01/2021")).thenReturn(sameDate);
			when(inputValidation.validateDate("01/01/2021")).thenReturn(sameDate);
			bookingController.newBooking(guest, "01/01/2021", "01/01/2021", 1, Room.SINGLE);
			verify(guesthouseView).showError("Check Out date must be after check in date.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("New booking request when number of guests is greater than room type  - testNewBookingWhenNumberOfGuestsIsGreaterThanRoomType()")
		void testNewBookingWhenNumberOfGuestsIsGreaterThanRoomType() {
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(inputValidation.validateDate("01/01/2021")).thenReturn(LocalDate.of(2021, 1, 1));
			when(inputValidation.validateDate("10/01/2021")).thenReturn(LocalDate.of(2021, 1, 10));
			bookingController.newBooking(guest, "01/01/2021", "10/01/2021", 2, Room.SINGLE);
			verify(guesthouseView).showError("Number of Guests must be suitable for the type of the room.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("New booking request when room is already booked on the requested dates - testNewBookingWhenRoomIsAlreadyBookedOnTheRequestedDates()")
		void testNewBookingWhenRoomIsAlreadyBookedOnTheRequestedDates() {
			LocalDate checkInDate = LocalDate.of(2021, 1, 1);
			LocalDate checkOutDate = LocalDate.of(2021, 1, 10);
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			when(inputValidation.validateDate("01/01/2021")).thenReturn(checkInDate);
			when(inputValidation.validateDate("10/01/2021")).thenReturn(checkOutDate);
			when(bookingRepository.checkRoomAvailabilityInDateRange(Room.SINGLE, checkInDate, checkOutDate))
					.thenReturn(false);
			bookingController.newBooking(guest, "01/01/2021", "10/01/2021", 1, Room.SINGLE);
			verify(guesthouseView).showError(
					"The selected room is already booked on the requested dates: SINGLE on (01/01/2021 - 10/01/2021).");
			verifyNoMoreInteractions(bookingRepository, guesthouseView);
		}

		@Test
		@DisplayName("Delete booking request when booking not exist  - testDeleteBookingWhenBookingNotExist()")
		void testDeleteBookingWhenBookingNotExist() {
			Booking bookingNotPresent = new Booking("1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			when(bookingRepository.findById(bookingNotPresent.getId())).thenReturn(null);
			bookingController.deleteBooking(bookingNotPresent);
			verify(guesthouseView).showErrorBookingNotFound(
					"There is no booking with id " + bookingNotPresent.getId(), bookingNotPresent);
			verifyNoMoreInteractions(bookingRepository, guesthouseView);
		}

		@Test
		@DisplayName("Search bookings by dates request when first date is not valid - testSearchBookingsByDatesWhenFirstDateIsNotValid()")
		void testSearchBookingsByDatesWhenFirstDateIsNotValid() {
			when(inputValidation.validateDate("dateNotValid")).thenReturn(null);
			when(inputValidation.validateDate("10/01/2021")).thenReturn(LocalDate.of(2021, 1, 10));
			bookingController.searchBookingsByDates("dateNotValid", "10/01/2021");
			verify(guesthouseView).showError(
					"First date is not valid: dateNotValid. Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("Search bookings by dates request when second date is not valid - testSearchBookingsByDatesWhenSecondDateIsNotValid()")
		void testSearchBookingsByDatesWhenSecondDateIsNotValid() {
			when(inputValidation.validateDate("01/01/2021")).thenReturn(LocalDate.of(2021, 1, 1));
			when(inputValidation.validateDate("dateNotValid")).thenReturn(null);
			bookingController.searchBookingsByDates("01/01/2021", "dateNotValid");
			verify(guesthouseView).showError(
					"Second date is not valid: dateNotValid. Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("Search bookings by dates request when first date is after second date - testSearchBookingsByDatesWhenFirstDateIsAfterSecondDate()")
		void testSearchBookingsByDatesWhenFirstDateIsAfterSecondDate() {
			LocalDate firstDate = LocalDate.of(2021, 1, 10);
			LocalDate secondDate = LocalDate.of(2021, 1, 1);
			when(inputValidation.validateDate("10/01/2021")).thenReturn(firstDate);
			when(inputValidation.validateDate("01/01/2021")).thenReturn(secondDate);
			bookingController.searchBookingsByDates("10/01/2021", "01/01/2021");
			verify(guesthouseView).showError("First date must be after second date.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

		@Test
		@DisplayName("Search bookings by dates request when first and second date are the same - testNewBookingWhenFirstAndSecondDateAreTheSame()")
		void testNewBookingWhenFirstAndSecondDateAreTheSame() {
			LocalDate sameDate = LocalDate.of(2021, 1, 1);
			when(inputValidation.validateDate("01/01/2021")).thenReturn(sameDate);
			when(inputValidation.validateDate("01/01/2021")).thenReturn(sameDate);
			bookingController.searchBookingsByDates("01/01/2021", "01/01/2021");
			verify(guesthouseView).showError("First date must be after second date.");
			verifyNoInteractions(bookingRepository);
			verifyNoMoreInteractions(guesthouseView);
		}

	}

}
