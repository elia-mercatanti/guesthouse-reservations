package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

class BookingMongoRepositoryTest {

	private static final String DATABASE_NAME = "guesthouse";
	private static final String COLLECTION_NAME = "booking";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	public BookingMongoRepository bookingMongoRepository;
	private MongoClient mongoClient;

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
	}

	@AfterEach
	void tearDown() {
		mongoClient.close();
	}

	@Nested
	@DisplayName("Booking Mongo Repository Happy Cases")
	class HappyCases {

	}

	@Nested
	@DisplayName("Booking Mongo Repository Exceptional Cases")
	class ExceptionalCases {

		@Test
		@DisplayName("Find all should return an empty list when database is empty - testFindAllShouldReturnAnEmptyListWhenDatabaseIsEmpty()")
		void testFindAllShouldReturnAnEmptyListWhenDatabaseIsEmpty() {
			assertThat(bookingMongoRepository.findAll()).isEmpty();
		}

	}

}
