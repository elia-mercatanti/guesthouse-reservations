package com.eliamercatanti.guesthousebooking.repository;

import java.time.LocalDate;
import java.util.List;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Room;

public interface BookingRepository {

	public List<Booking> findAll();

	public void save(Booking booking);

	public Booking findById(String id);

	public void delete(String id);

	public boolean checkRoomAvailabilityInDateRange(Room room, LocalDate firstDate, LocalDate secondDate);

}
