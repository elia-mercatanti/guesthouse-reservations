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
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.eliamercatanti.guesthousebooking.repository.mongo.BookingMongoRepository;
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

	@BeforeEach
	public void setUp() {
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		bookingRepository = new BookingMongoRepository(new MongoClient("localhost", mongoPort), DATABASE_NAME,
				COLLECTION_NAME);
		for (Booking booking : bookingRepository.findAll()) {
			bookingRepository.delete(booking.getId());
		}
		bookingController = new BookingController(bookingRepository, guesthouseView);
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

}
