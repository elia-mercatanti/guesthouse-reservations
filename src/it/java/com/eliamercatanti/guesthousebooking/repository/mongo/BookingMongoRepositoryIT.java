package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

class BookingMongoRepositoryIT {

	private static final String DATABASE_NAME = "guesthouse";
	private static final String COLLECTION_NAME = "booking";
	private MongoCollection<Booking> bookingCollection;
	private BookingMongoRepository bookingMongoRepository;
	private MongoClient mongoClient;

	@BeforeEach
	void setUp() {
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		mongoClient = new MongoClient(new ServerAddress("localhost", mongoPort));
		bookingMongoRepository = new BookingMongoRepository(mongoClient, DATABASE_NAME, COLLECTION_NAME);
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
		bookingCollection = database.getCollection(COLLECTION_NAME, Booking.class);
		database.drop();
	}

	@AfterEach
	void tearDown() {
		mongoClient.close();
	}

	@Test
	@DisplayName("Find all bookings in the collection - testFindAll()")
	void testFindAll() {
		Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10),
				1, Room.SINGLE);
		Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10),
				2, Room.DOUBLE);
		bookingCollection.insertMany(Arrays.asList(booking1, booking2));
		assertThat(bookingMongoRepository.findAll()).containsExactly(
				new Booking(booking1.getId(), booking1.getGuestId(), LocalDate.of(2021, 1, 1),
						LocalDate.of(2021, 1, 10), 1, Room.SINGLE),
				new Booking(booking2.getId(), booking2.getGuestId(), LocalDate.of(2021, 2, 1),
						LocalDate.of(2021, 2, 10), 2, Room.DOUBLE));
	}

	@Test
	@DisplayName("Save a booking in the collection - testSave()")
	void testSave() {
		Booking booking = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
				Room.SINGLE);
		bookingMongoRepository.save(booking);
		assertThat(getBookingsList()).containsExactly(booking);
	}

	@Test
	@DisplayName("Find a booking in the collection with his id - testFindById()")
	void testFindById() {
		Booking bookingToFind = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
				LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
		Booking anotherBooking = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
				LocalDate.of(2021, 2, 10), 2, Room.DOUBLE);
		bookingCollection.insertMany(Arrays.asList(bookingToFind, anotherBooking));
		assertThat(bookingMongoRepository.findById(bookingToFind.getId())).isEqualTo(new Booking(bookingToFind.getId(),
				bookingToFind.getGuestId(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1, Room.SINGLE));
	}

	@Test
	@DisplayName("Delete a booking in the collection with his id - testDelete()")
	void testDelete() {
		Booking bookingToDelete = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
				LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
		Booking anotherBooking = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
				LocalDate.of(2021, 2, 10), 2, Room.DOUBLE);
		bookingCollection.insertMany(Arrays.asList(bookingToDelete, anotherBooking));
		bookingMongoRepository.delete(bookingToDelete.getId());
		assertThat(getBookingsList()).containsExactly(new Booking(anotherBooking.getId(), anotherBooking.getGuestId(),
				LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2, Room.DOUBLE));
	}

	@Test
	@DisplayName("Check Room Availability In Date Range when room is free - testCheckRoomAvailabilityInDateRangeWhenRoomIsFree()")
	void testCheckRoomAvailabilityInDateRangeWhenRoomIsFree() {
		Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10),
				1, Room.SINGLE);
		Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 20), LocalDate.of(2021, 1, 30),
				2, Room.SINGLE);
		bookingCollection.insertMany(Arrays.asList(booking1, booking2));
		boolean roomAvailability = bookingMongoRepository.checkRoomAvailabilityInDateRange(Room.SINGLE,
				LocalDate.of(2021, 1, 10), LocalDate.of(2021, 1, 20));
		assertThat(roomAvailability).isTrue();
	}

	@Test
	@DisplayName("Find all bookings between a date range - testFindByDates()")
	void testFindByDates() {
		Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10),
				1, Room.SINGLE);
		Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 10), LocalDate.of(2021, 1, 20),
				2, Room.DOUBLE);
		Booking booking3 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 20), LocalDate.of(2021, 1, 30),
				1, Room.SINGLE);
		Booking booking4 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10),
				1, Room.SINGLE);
		bookingCollection.insertMany(Arrays.asList(booking1, booking2, booking3, booking4));
		List<Booking> bookingsList = bookingMongoRepository.findByDates(LocalDate.of(2021, 1, 5),
				LocalDate.of(2021, 1, 25));
		assertThat(bookingsList).containsExactly(booking1, booking2, booking3);
	}

	private List<Booking> getBookingsList() {
		return StreamSupport.stream(bookingCollection.find().spliterator(), false).collect(Collectors.toList());
	}

}
