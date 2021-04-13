package com.eliamercatanti.guesthousebooking.controller;

import java.time.LocalDate;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

public class BookingController {

	private GuesthouseView guesthouseView;
	private BookingRepository bookingRepository;
	private InputValidation inputValidation;

	public void allBookings() {
		guesthouseView.showAllBookings(bookingRepository.findAll());
	}

	public void newBooking(String guestId, String checkInDateString, String checkOutDateString, int numberOfGuests, Room room) {
		LocalDate checkInDate = inputValidation.validateDate(checkInDateString);
		LocalDate checkOutDate = inputValidation.validateDate(checkOutDateString);

		if (checkInDate != null && checkOutDate != null) {
			Booking newBooking = new Booking(guestId, checkInDate, checkOutDate, numberOfGuests, room);
			bookingRepository.save(newBooking);
			guesthouseView.bookingAdded(newBooking);
		}
	}

	public void deleteBooking(Booking booking) {
		if(bookingRepository.findById(booking.getId()) != null) {
			bookingRepository.delete(booking.getId());
			guesthouseView.bookingRemoved(booking);
		}
	}

	public void searchBookingsByDates(String checkInDate, String checkOutDate) {
		// TODO Auto-generated method stub

	}

	public void searchBookingsByRoom(Room room) {
		// TODO Auto-generated method stub

	}

	public void searchBookingsByGuest(Guest guest) {
		// TODO Auto-generated method stub

	}

}
