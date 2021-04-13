package com.eliamercatanti.guesthousebooking.repository;

import java.util.List;

import com.eliamercatanti.guesthousebooking.model.Booking;

public interface BookingRepository {

	public List<Booking> findAll();

	public void save(Booking booking);

	public Booking findById(String id);

	public void delete(String id);

}
