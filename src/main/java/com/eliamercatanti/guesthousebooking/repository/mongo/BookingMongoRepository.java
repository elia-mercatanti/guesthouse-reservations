package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

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
		return StreamSupport.stream(bookingCollection.find().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public void save(Booking booking) {
		bookingCollection.insertOne(booking);
	}

	@Override
	public Booking findById(String id) {
		return bookingCollection.find(Filters.eq("_id", stringToObjectId(id))).first();
	}

	@Override
	public void delete(String id) {
		bookingCollection.deleteOne(Filters.eq("_id", stringToObjectId(id)));
	}

	@Override
	public boolean checkRoomAvailabilityInDateRange(Room room, LocalDate firstDate, LocalDate secondDate) {
		String checkInFieldName = "checkInDate";
		String checkOutFieldName = "checkOutDate";

		Bson filterQuery = Filters.and(Filters.eq("room", room.name()), Filters.or(
				Filters.and(Filters.gt(checkInFieldName, firstDate), Filters.lt(checkInFieldName, secondDate)),
				Filters.and(Filters.lt(checkOutFieldName, secondDate), Filters.gt(checkOutFieldName, firstDate)),
				Filters.and(Filters.lt(checkInFieldName, firstDate), Filters.gt(checkOutFieldName, secondDate))));

		return bookingCollection.find(filterQuery).first() == null;
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

	private ObjectId stringToObjectId(String id) {
		try {
			return new ObjectId(id);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
