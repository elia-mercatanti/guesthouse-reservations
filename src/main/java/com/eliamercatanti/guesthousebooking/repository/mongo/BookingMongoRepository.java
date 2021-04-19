package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BookingMongoRepository implements BookingRepository {

	private MongoCollection<Booking> bookingCollection;

	public BookingMongoRepository(MongoClient mongoClient, String databaseName, String collectionName) {
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoDatabase database = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
		bookingCollection = database.getCollection(collectionName, Booking.class);
	}

	@Override
	public List<Booking> findAll() {
		if (bookingCollection.countDocuments() == 0) {
			return Collections.emptyList();
		}
		return new ArrayList<>();
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
