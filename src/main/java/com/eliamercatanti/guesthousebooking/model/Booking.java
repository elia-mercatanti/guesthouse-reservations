package com.eliamercatanti.guesthousebooking.model;

import java.time.LocalDate;

public class Booking {

	private String id;
	private String guestId;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private int numberOfGuests;
	private Room room;

	public Booking(String id, String guestId, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests,
			Room room) {
		this.id = id;
		this.guestId = guestId;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.numberOfGuests = numberOfGuests;
		this.room = room;
	}

	public String getId() {
		return id;
	}

	public String getGuestId() {
		return guestId;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public Room getRoom() {
		return room;
	}

	@Override
	public String toString() {
		return "Booking [id=" + id + ", guestId=" + guestId + ", checkInDate=" + checkInDate + ", checkOutDate="
				+ checkOutDate + ", numberOfGuests=" + numberOfGuests + ", room=" + room + "]";
	}

}
