package com.eliamercatanti.guesthousebooking.controller;

import java.time.LocalDate;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

public class BookingController {

	private static final String DATE_FORMAT_ERROR_MESSAGE = "Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.";
	private GuesthouseView guesthouseView;
	private BookingRepository bookingRepository;
	private InputValidation inputValidation;

	public BookingController(BookingRepository bookingRepository, GuesthouseView guesthouseView,
			InputValidation inputValidation) {
		this.bookingRepository = bookingRepository;
		this.guesthouseView = guesthouseView;
		this.inputValidation = inputValidation;
	}

	public void allBookings() {
		guesthouseView.showBookings(bookingRepository.findAll());
	}

	public void newBooking(Guest guest, String checkInDateString, String checkOutDateString, int numberOfGuests,
			Room room) {
		LocalDate checkInDate = inputValidation.validateDate(checkInDateString);
		LocalDate checkOutDate = inputValidation.validateDate(checkOutDateString);

		if (checkInDate == null) {
			guesthouseView.showError(
					"Booking Check In date is not valid: " + checkInDateString + ". " + DATE_FORMAT_ERROR_MESSAGE);
		} else if (checkOutDate == null) {
			guesthouseView.showError(
					"Booking Check Out date is not valid: " + checkOutDateString + ". " + DATE_FORMAT_ERROR_MESSAGE);
		} else if (checkInDate.compareTo(checkOutDate) >= 0) {
			guesthouseView.showError("Check out date must be after check in date.");
		} else if (numberOfGuests > room.getNumberOfBeds()) {
			guesthouseView.showError("Number of Guests must be suitable for the type of the room.");
		} else if (!bookingRepository.checkRoomAvailabilityInDateRange(room, checkInDate, checkOutDate)) {
			guesthouseView.showError("The selected room is already booked on the requested dates: " + room + " on ("
					+ checkInDateString + " - " + checkOutDateString + ").");
		} else {
			Booking newBooking = new Booking(guest.getId(), checkInDate, checkOutDate, numberOfGuests, room);
			bookingRepository.save(newBooking);
			guesthouseView.bookingAdded(newBooking);
		}
	}

	public void deleteBooking(Booking booking) {
		if (bookingRepository.findById(booking.getId()) == null) {
			guesthouseView.showErrorBookingNotFound("There is no booking with id " + booking.getId() + ".", booking);
		} else {
			bookingRepository.delete(booking.getId());
			guesthouseView.bookingRemoved(booking);
		}
	}

	public void searchBookingsByDates(String firstDateString, String secondDateString) {
		LocalDate firstDate = inputValidation.validateDate(firstDateString);
		LocalDate secondDate = inputValidation.validateDate(secondDateString);

		if (firstDate == null) {
			guesthouseView.showError("First date is not valid: " + firstDateString + ". " + DATE_FORMAT_ERROR_MESSAGE);
		} else if (secondDate == null) {
			guesthouseView
					.showError("Second date is not valid: " + secondDateString + ". " + DATE_FORMAT_ERROR_MESSAGE);
		} else if (firstDate.compareTo(secondDate) >= 0) {
			guesthouseView.showError("First date must be after second date.");
		} else {
			guesthouseView.showBookings(bookingRepository.findByDates(firstDate, secondDate));
		}
	}

	public void searchBookingsByRoom(Room room) {
		guesthouseView.showBookings(bookingRepository.findByRoom(room));
	}

	public void searchBookingsByGuest(Guest guest) {
		guesthouseView.showBookings(bookingRepository.findByGuestId(guest.getId()));
	}

}
