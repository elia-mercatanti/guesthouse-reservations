package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.Arrays;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

class BookingMongoRepositoryTest {

	private static final String DATABASE_NAME = "guesthouse";
	private static final String COLLECTION_NAME = "booking";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	public BookingMongoRepository bookingMongoRepository;
	private MongoClient mongoClient;
	private MongoCollection<Booking> bookingCollection;

	@BeforeAll
	static void setupServer() {
		mongoServer = new MongoServer(new MemoryBackend());
		serverAddress = mongoServer.bind();
	}

	@AfterAll
	static void shutdownServer() {
		mongoServer.shutdown();
	}

	@BeforeEach
	void setup() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
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

	@Nested
	@DisplayName("Booking Mongo Repository Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("Find all should return a list of all bookings when collection is not empty - testFindAllShouldReturnAListOfAllBookingsWhenCollectionIsNotEmpty()")
		void testFindAllShouldReturnAListOfAllBookingsWhenCollectionIsNotEmpty() {
			Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
					LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
					LocalDate.of(2021, 2, 10), 2, Room.DOUBLE);
			bookingCollection.insertMany(Arrays.asList(booking1, booking2));
			assertThat(bookingMongoRepository.findAll()).containsExactly(booking1, booking2);
		}

		@Test
		@DisplayName("Save should save a booking in the collection - testSaveShouldSaveABookingInTheCollection()")
		void testSaveShouldSaveABookingInTheCollection() {
			Booking booking = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
					LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			bookingMongoRepository.save(booking);
			assertThat(bookingCollection.find().first()).isEqualTo(booking);
		}

		@Test
		@DisplayName("Find by id should return null when booking id is not found - testFindByIdShouldReturnTheBookingWhenIdIsFound()")
		void testFindByIdShouldReturnTheBookingWhenIdIsFound() {
			Booking bookingToFind = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
					LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			Booking anotherBooking = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
					LocalDate.of(2021, 2, 10), 2, Room.DOUBLE);
			bookingCollection.insertMany(Arrays.asList(bookingToFind, anotherBooking));
			assertThat(bookingMongoRepository.findById(bookingToFind.getId())).isEqualTo(bookingToFind);
		}

		@Test
		@DisplayName("Delete should delete a booking from the collection - testDeleteShouldDeleteABookingFromTheCollection()")
		void testDeleteShouldDeleteABookingFromTheCollection() {
			Booking bookingToDelete = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
					LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			bookingCollection.insertOne(bookingToDelete);
			bookingMongoRepository.delete(bookingToDelete.getId());
			assertThat(bookingCollection.countDocuments()).isZero();
		}

	}

	@Nested
	@DisplayName("Booking Mongo Repository Exceptional Cases")
	class ExceptionalCases {

		@Test
		@DisplayName("Find all should return an empty list when booking collection is empty - testFindAllShouldReturnAnEmptyListWhenBookingCollectioneIsEmpty()")
		void testFindAllShouldReturnAnEmptyListWhenBookingCollectioneIsEmpty() {
			assertThat(bookingMongoRepository.findAll()).isEmpty();
		}

		@Test
		@DisplayName("Find by id should return null when string id is not parsable into an object id - testFindByIdShouldReturnNullWhenStringIdIsNotParsableIntoAnObjectId()")
		void testFindByIdShouldReturnNullWhenStringIdIsNotParsableIntoAnObjectId() {
			assertThat(bookingMongoRepository.findById("1")).isNull();
			assertThat(bookingMongoRepository.findById("-")).isNull();
			assertThat(bookingMongoRepository.findById("$")).isNull();
			assertThat(bookingMongoRepository.findById("aaa")).isNull();
		}

		@Test
		@DisplayName("Find by id should return null when booking id is not found - testFindByIdShouldReturnNullWhenBookingIdIsNotFound()")
		void testFindByIdShouldReturnNullWhenBookingIdIsNotFound() {
			assertThat(bookingMongoRepository.findById(new ObjectId().toString())).isNull();
		}

	}

}
