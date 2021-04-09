package com.eliamercatanti.guesthousebooking.view;

import java.util.List;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;

public interface GuesthouseView {

	public void showAllGuests(List<Guest> guests);

	public void guestAdded(Guest guest);

	public void guestRemoved(Guest guest);

	public void showErrorGuestNotFound(String message, Guest guest);

	void showError(String message);

	public void showAllBookings(List<Booking> bookings);

	void bookingAdded(Booking booking);

	void bookingRemoved(Booking booking);

	void showErrorBookingNotFound(String message, Booking booking);

}
