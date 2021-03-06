package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.InetSocketAddress;
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

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@DisplayName("Tests for Guest Mongo Repository")
class GuestMongoRepositoryTest {

	private static final String DATABASE_NAME = "guesthouse";
	private static final String COLLECTION_NAME = "guest";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	private GuestMongoRepository guestMongoRepository;
	private MongoClient mongoClient;
	private MongoCollection<Guest> guestCollection;

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

	@Nested
	@DisplayName("Guest Mongo Repository Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("Find all should return a list of all guests when collection is not empty - testFindAllShouldReturnAListOfAllGuestsWhenCollectionIsNotEmpty()")
		void testFindAllShouldReturnAListOfAllGuestsWhenCollectionIsNotEmpty() {
			Guest guest1 = new Guest("testFirstName1", "testLastName1", "test1@email.com", "1111111111");
			Guest guest2 = new Guest("testFirstName2", "testLastName2", "test2@email.com", "2222222222");
			guestCollection.insertMany(Arrays.asList(guest1, guest2));
			assertThat(guestMongoRepository.findAll()).containsExactly(guest1, guest2);
		}

		@Test
		@DisplayName("Save should save a guest in the collection - testSaveShouldSaveAGuestInTheCollection()")
		void testSaveShouldSaveAGuestInTheCollection() {
			Guest guest = new Guest("testFirstName", "testLastName", "test@email.com", "1111111111");
			guestMongoRepository.save(guest);
			assertThat(guestCollection.find().first()).isEqualTo(guest);
		}

		@Test
		@DisplayName("Find by id should return the guest when id is found - testFindByIdShouldReturnTheGuestWhenIdIsFound()")
		void testFindByIdShouldReturnTheGuestWhenIdIsFound() {
			Guest guestToFind = new Guest("testFirstName", "testLastName", "test@email.com", "1111111111");
			Guest anotherGuest = new Guest("testFirstName2", "testLastName2", "test2@email.com", "2222222222");
			guestCollection.insertMany(Arrays.asList(guestToFind, anotherGuest));
			assertThat(guestMongoRepository.findById(guestToFind.getId())).isEqualTo(guestToFind);
		}

		@Test
		@DisplayName("Delete should delete a guest from the collection - testDeleteShouldDeleteAGuestFromTheCollection()")
		void testDeleteShouldDeleteAGuestFromTheCollection() {
			Guest guestToDelete = new Guest("testFirstName", "testLastName", "test@email.com", "1111111111");
			guestCollection.insertOne(guestToDelete);
			guestMongoRepository.delete(guestToDelete.getId());
			assertThat(guestCollection.countDocuments()).isZero();
		}

	}

	@Nested
	@DisplayName("Guest Mongo Repository Exceptional Cases")
	class ExceptionalCases {

		@Test
		@DisplayName("Find all should return an empty list when guest collection is empty - testFindAllShouldReturnAnEmptyListWhenGuestCollectionIsEmpty()")
		void testFindAllShouldReturnAnEmptyListWhenGuestCollectionIsEmpty() {
			assertThat(guestMongoRepository.findAll()).isEmpty();
		}

		@Test
		@DisplayName("Find by id should return null when string id is not parsable into an object id - testFindByIdShouldReturnNullWhenStringIdIsNotParsableIntoAnObjectId()")
		void testFindByIdShouldReturnNullWhenStringIdIsNotParsableIntoAnObjectId() {
			assertThat(guestMongoRepository.findById("1")).isNull();
			assertThat(guestMongoRepository.findById("-")).isNull();
			assertThat(guestMongoRepository.findById("$")).isNull();
			assertThat(guestMongoRepository.findById("aaa")).isNull();
		}

		@Test
		@DisplayName("Find by id should return null when guest id is not found - testFindByIdShouldReturnNullWhenGuestIdIsNotFound()")
		void testFindByIdShouldReturnNullWhenGuestIdIsNotFound() {
			assertThat(guestMongoRepository.findById(new ObjectId().toString())).isNull();
		}

		@Test
		@DisplayName("Delete should do nothing when string id is not parsable into an object id - testDeleteShouldDoNothingWhenStringIdIsNotParsableIntoAnObjectId()")
		void testDeleteShouldDoNothingWhenStringIdIsNotParsableIntoAnObjectId() {
			Guest guest = new Guest("testFirstName1", "testLastName1", "test1@email.com", "1111111111");
			guestCollection.insertOne(guest);
			guestMongoRepository.delete("1");
			guestMongoRepository.delete("-");
			guestMongoRepository.delete("$");
			guestMongoRepository.delete("aaa");
			assertThat(guestCollection.countDocuments()).isEqualTo(1);
		}

	}

}
