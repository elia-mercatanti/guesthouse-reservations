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

	private static final String CHECK_IN_FIELD_NAME = "checkInDate";
	private static final String CHECK_OUT_FIELD_NAME = "checkOutDate";
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
		Bson filterQuery = Filters.and(Filters.eq("room", room.name()), Filters.or(
				Filters.and(Filters.gt(CHECK_IN_FIELD_NAME, firstDate), Filters.lt(CHECK_IN_FIELD_NAME, secondDate)),
				Filters.and(Filters.lt(CHECK_OUT_FIELD_NAME, secondDate), Filters.gt(CHECK_OUT_FIELD_NAME, firstDate)),
				Filters.and(Filters.lt(CHECK_IN_FIELD_NAME, firstDate), Filters.gt(CHECK_OUT_FIELD_NAME, secondDate))));

		return bookingCollection.find(filterQuery).first() == null;
	}

	@Override
	public List<Booking> findByDates(LocalDate firstDate, LocalDate secondDate) {
		Bson filterQuery = Filters.or(
				Filters.and(Filters.gt(CHECK_IN_FIELD_NAME, firstDate), Filters.lt(CHECK_IN_FIELD_NAME, secondDate)),
				Filters.and(Filters.gt(CHECK_OUT_FIELD_NAME, firstDate), Filters.lt(CHECK_OUT_FIELD_NAME, secondDate)));

		return StreamSupport.stream(bookingCollection.find(filterQuery).spliterator(), false)
				.collect(Collectors.toList());
	}

	@Override
	public List<Booking> findByRoom(Room room) {
		return StreamSupport.stream(bookingCollection.find(Filters.eq("room", room.name())).spliterator(), false)
				.collect(Collectors.toList());
	}

	@Override
	public List<Booking> findByGuestId(String guestId) {
		return StreamSupport
				.stream(bookingCollection.find(Filters.eq("guestId", stringToObjectId(guestId))).spliterator(), false)
				.collect(Collectors.toList());
	}

	private ObjectId stringToObjectId(String id) {
		try {
			return new ObjectId(id);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
