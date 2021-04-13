package com.eliamercatanti.guesthousebooking.repository;

import java.util.List;

import com.eliamercatanti.guesthousebooking.model.Booking;

public interface BookingRepository {

	List<Booking> findAll();

	void save(Booking newBooking);

}
