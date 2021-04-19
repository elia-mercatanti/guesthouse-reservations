package com.eliamercatanti.guesthousebooking.repository.mongo;

import java.time.LocalDate;
import java.util.List;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.mongodb.MongoClient;

public class BookingMongoRepository implements BookingRepository {

	public BookingMongoRepository(MongoClient mongoClient, String databaseName, String collectionName) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Booking> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Booking booking) {
		// TODO Auto-generated method stub

	}

	@Override
	public Booking findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkRoomAvailabilityInDateRange(Room room, LocalDate firstDate, LocalDate secondDate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Booking> findByDates(LocalDate firstDate, LocalDate secondDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Booking> findByRoom(Room room) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Booking> findByGuestId(String guestId) {
		// TODO Auto-generated method stub
		return null;
	}

}
