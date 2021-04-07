package com.eliamercatanti.guesthousebooking.model;

import java.time.LocalDate;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(id, guestId, checkInDate, checkOutDate, numberOfGuests, room);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		return Objects.equals(id, other.id) && Objects.equals(guestId, other.guestId)
				&& Objects.equals(checkInDate, other.checkInDate) && Objects.equals(checkOutDate, other.checkOutDate)
				&& Objects.equals(numberOfGuests, other.numberOfGuests) && Objects.equals(room, other.room);
	}

}
