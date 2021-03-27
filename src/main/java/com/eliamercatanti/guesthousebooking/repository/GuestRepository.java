package com.eliamercatanti.guesthousebooking.repository;

import java.util.List;

import com.eliamercatanti.guesthousebooking.model.Guest;

public interface GuestRepository {

	public List<Guest> findAll();

	public void save(Guest guest);

	public Guest findById(String id);

	public void delete(String id);

}
