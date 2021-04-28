package com.eliamercatanti.guesthousebooking.app.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;

@RunWith(GUITestRunner.class)
public class GuesthouseSwingAppE2E extends AssertJSwingJUnitTestCase {

	private static final String MONGO_CLIENT_HOST = "localhost";
	private static final String DATABASE_NAME = "guesthouse";
	private static final String GUEST_COLLECTION_NAME = "guest";
	private static final String BOOKING_COLLECTION_NAME = "booking";
	private FrameFixture window;
	private MongoClient mongoClient;
	private CodecRegistry pojoCodecRegistry;

	@Override
	protected void onSetUp() {
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		mongoClient = new MongoClient(MONGO_CLIENT_HOST, mongoPort);
		pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		// Reset database, add guests and bookings to the database.
		mongoClient.getDatabase(DATABASE_NAME).drop();

		addTestGuestToDatabase(new Guest(new ObjectId().toString(), "testFirstName1", "testLastName1",
				"test1@email.com", "1111111111"));
		addTestGuestToDatabase(new Guest(new ObjectId().toString(), "testFirstName2", "testLastName2",
				"test2@email.com", "2222222222"));

		addTestBookingToDatabase(new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
				LocalDate.of(2021, 1, 10), 1, Room.SINGLE));
		addTestBookingToDatabase(new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
				LocalDate.of(2021, 2, 10), 2, Room.DOUBLE));

		// Start the Swing application.
		application("com.eliamercatanti.guesthousebooking.app.swing.GuesthouseSwingApp")
				.withArgs("--mongo-host=" + MONGO_CLIENT_HOST, "--mongo-port=" + mongoPort,
						"--db-name=" + DATABASE_NAME, "--db-guest-collection=" + GUEST_COLLECTION_NAME,
						"--db-booking-collection=" + BOOKING_COLLECTION_NAME)
				.start();

		// Get a reference of its JFrame.
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Guesthouse Reservations".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}

	private void addTestBookingToDatabase(Booking booking) {
		mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry)
				.getCollection(BOOKING_COLLECTION_NAME, Booking.class).insertOne(booking);
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	private void addTestGuestToDatabase(Guest guest) {
		mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry)
				.getCollection(GUEST_COLLECTION_NAME, Guest.class).insertOne(guest);
	}

	@Test
	@GUITest
	public void testOnStartAllGuestsAreShown() {
		window.tabbedPane().selectTab("Guests");
		assertThat(window.list().contents())
				.anySatisfy(
						e -> assertThat(e).contains("testFirstName1", "testLastName1", "test1@email.com", "1111111111"))
				.anySatisfy(e -> assertThat(e).contains("testFirstName2", "testLastName2", "test2@email.com",
						"2222222222"));
	}

	@Test
	@GUITest
	public void testOnStartAllBookingsAreShown() {
		window.tabbedPane().selectTab("Bookings");
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains("01/01/2021", "10/01/2021", "1", "SINGLE"))
				.anySatisfy(e -> assertThat(e).contains("01/02/2021", "10/02/2021", "2", "DOUBLE"));
	}

}
