package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@DisplayName("Integration Tests for Guest Mongo Repository")
class GuestMongoRepositoryIT {

	private static final String DATABASE_NAME = "guesthouse";
	private static final String COLLECTION_NAME = "guest";
	private GuestMongoRepository guestMongoRepository;
	private MongoClient mongoClient;
	private MongoCollection<Guest> guestCollection;

	@BeforeEach
	void setUp() {
		mongoClient = new MongoClient();
		guestMongoRepository = new GuestMongoRepository(mongoClient, DATABASE_NAME, COLLECTION_NAME);

		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
		guestCollection = database.getCollection(COLLECTION_NAME, Guest.class);
		database.drop();
	}

	@AfterEach
	void tearDown() {
		mongoClient.close();
	}

	@Test
	@DisplayName("Find all guest in the collection - testFindAll()")
	void testFindAll() {
		Guest guest1 = new Guest("testFirstName1", "testLastName1", "test1@email.com", "1111111111");
		Guest guest2 = new Guest("testFirstName2", "testLastName2", "test2@email.com", "2222222222");
		guestCollection.insertMany(Arrays.asList(guest1, guest2));
		assertThat(guestMongoRepository.findAll()).containsExactly(
				new Guest(guest1.getId(), "testFirstName1", "testLastName1", "test1@email.com", "1111111111"),
				new Guest(guest2.getId(), "testFirstName2", "testLastName2", "test2@email.com", "2222222222"));
	}

	@Test
	@DisplayName("Save a guest in the collection - testSave()")
	void testSave() {
		Guest guestToSave = new Guest("testFirstName1", "testLastName1", "test1@email.com", "1111111111");
		guestMongoRepository.save(guestToSave);
		List<Guest> guestsList = StreamSupport.stream(guestCollection.find().spliterator(), false)
				.collect(Collectors.toList());
		assertThat(guestsList).containsExactly(
				new Guest(guestToSave.getId(), "testFirstName1", "testLastName1", "test1@email.com", "1111111111"));
	}

	@Test
	@DisplayName("Find a Guest in the colelction by his id - testFindById()")
	void testFindById() {
		Guest guestToFind = new Guest("testFirstName1", "testLastName1", "test1@email.com", "1111111111");
		Guest anotherGuest = new Guest("testFirstName2", "testLastName2", "test2@email.com", "2222222222");
		guestCollection.insertMany(Arrays.asList(guestToFind, anotherGuest));
		assertThat(guestMongoRepository.findById(guestToFind.getId())).isEqualTo(
				new Guest(guestToFind.getId(), "testFirstName1", "testLastName1", "test1@email.com", "1111111111"));

	}

}
