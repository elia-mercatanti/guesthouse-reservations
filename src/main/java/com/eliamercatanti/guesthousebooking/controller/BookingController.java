package com.eliamercatanti.guesthousebooking.controller;

import java.time.LocalDate;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

public class BookingController {

	private GuesthouseView guesthouseView;
	private BookingRepository bookingRepository;

	public void allBookings() {
		guesthouseView.showAllBookings(bookingRepository.findAll());
	}

	public void newBooking(String guestId, String checkInDateString, String checkOutDateString, int i, Room room) {
		LocalDate checkInDate = LocalDate.parse(checkInDateString);
		LocalDate checkOutDate = LocalDate.parse(checkOutDateString);
		Booking newBooking = new Booking(guestId, checkInDate, checkOutDate, i, room);
		bookingRepository.save(newBooking);
		guesthouseView.bookingAdded(newBooking);
	}

	public void deleteBooking(Booking booking) {
		// TODO Auto-generated method stub

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
