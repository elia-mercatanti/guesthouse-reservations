package com.eliamercatanti.guesthousebooking.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDate;
import java.util.Arrays;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.eliamercatanti.guesthousebooking.repository.mongo.BookingMongoRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.eliamercatanti.guesthousebooking.validation.controller.ControllerInputValidator;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;
import com.mongodb.MongoClient;

@ExtendWith(MockitoExtension.class)
@DisplayName("Integration Tests for Booking Controller")
class BookingControllerIT {

	private static final String DATABASE_NAME = "guesthouse";
	private static final String COLLECTION_NAME = "booking";
	private BookingRepository bookingRepository;
	private BookingController bookingController;
	@Mock
	private GuesthouseView guesthouseView;
	private InputValidation inputValidation;

	@BeforeEach
	public void setUp() {
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		bookingRepository = new BookingMongoRepository(new MongoClient("localhost", mongoPort), DATABASE_NAME,
				COLLECTION_NAME);
		inputValidation = new ControllerInputValidator();
		for (Booking booking : bookingRepository.findAll()) {
			bookingRepository.delete(booking.getId());
		}
		bookingController = new BookingController(bookingRepository, guesthouseView, inputValidation);
	}

	@Test
	@DisplayName("All Bookings list request - testAllBookings()")
	void testAllBookings() {
		Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10),
				1, Room.SINGLE);
		Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10),
				2, Room.DOUBLE);
		bookingRepository.save(booking1);
		bookingRepository.save(booking2);
		bookingController.allBookings();
		verify(guesthouseView).showBookings(Arrays.asList(booking1, booking2));
		verifyNoMoreInteractions(guesthouseView);
	}

	@Test
	@DisplayName("New booking request - testNewBooking()")
	void testNewBooking() {
		Guest guest = new Guest(new ObjectId().toString(), "testFirstName", "testLastName", "test@email.com",
				"0000000000");
		bookingController.newBooking(guest, "01/01/2021", "10/01/2021", 1, Room.SINGLE);
		Booking newBooking = bookingRepository.findAll().get(0);
		verify(guesthouseView).bookingAdded(newBooking);
		verifyNoMoreInteractions(guesthouseView);
	}

	@Test
	@DisplayName("Delete booking request - testDeleteBooking()")
	void testDeleteBooking() {
		Guest guest = new Guest(new ObjectId().toString(), "testFirstName", "testLastName", "test@email.com",
				"0000000000");
		Booking bookingToDelete = new Booking(guest.getId(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
				Room.SINGLE);
		bookingRepository.save(bookingToDelete);
		bookingController.deleteBooking(bookingToDelete);
		verify(guesthouseView).bookingRemoved(bookingToDelete);
		verifyNoMoreInteractions(guesthouseView);
	}

	@Test
	@DisplayName("Search bookings by dates request - testSearchBookingsByDates()")
	void testSearchBookingsByDates() {
		Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10),
				1, Room.SINGLE);
		Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 10), LocalDate.of(2021, 1, 20),
				2, Room.DOUBLE);
		Booking booking3 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 20), LocalDate.of(2021, 1, 30),
				1, Room.SINGLE);
		bookingRepository.save(booking1);
		bookingRepository.save(booking2);
		bookingRepository.save(booking3);
		bookingController.searchBookingsByDates("05/01/2021", "25/01/2021");
		verify(guesthouseView).showBookings(Arrays.asList(booking1, booking2, booking3));
		verifyNoMoreInteractions(guesthouseView);
	}

	@Test
	@DisplayName("Search bookings by room request - testSearchBookingsByRoom()")
	void testSearchBookingsByRoom() {
		Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10),
				1, Room.SINGLE);
		Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10),
				2, Room.DOUBLE);
		Booking booking3 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10),
				1, Room.SINGLE);
		bookingRepository.save(booking1);
		bookingRepository.save(booking2);
		bookingRepository.save(booking3);
		bookingController.searchBookingsByRoom(Room.SINGLE);
		verify(guesthouseView).showBookings(Arrays.asList(booking1, booking3));
		verifyNoMoreInteractions(guesthouseView);
	}

	@Test
	@DisplayName("Search bookings by guest request - testSearchBookingsByGuest()")
	void testSearchBookingsByGuest() {
		String guestId = new ObjectId().toString();
		Guest guest = new Guest(guestId, "testFirstName", "testLastName", "test@email.com", "0000000000");
		Booking booking1 = new Booking(guestId, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
		Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10),
				1, Room.SINGLE);
		bookingRepository.save(booking1);
		bookingRepository.save(booking2);
		bookingController.searchBookingsByGuest(guest);
		verify(guesthouseView).showBookings(Arrays.asList(booking1));
		verifyNoMoreInteractions(guesthouseView);
	}

}
