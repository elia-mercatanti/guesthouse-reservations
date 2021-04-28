package com.eliamercatanti.guesthousebooking.app.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDate;
import java.util.regex.Pattern;

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
import com.mongodb.client.model.Filters;

@RunWith(GUITestRunner.class)
public class GuesthouseSwingAppE2E extends AssertJSwingJUnitTestCase {

	private static final String MONGO_CLIENT_HOST = "localhost";
	private static final String DATABASE_NAME = "guesthouse";
	private static final String GUEST_COLLECTION_NAME = "guest";
	private static final String BOOKING_COLLECTION_NAME = "booking";
	private FrameFixture window;
	private MongoClient mongoClient;
	private CodecRegistry pojoCodecRegistry;
	private String guest1Id;
	private String guest2Id;

	@Override
	protected void onSetUp() {
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		mongoClient = new MongoClient(MONGO_CLIENT_HOST, mongoPort);
		pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		// Reset database, add guests and bookings to the database.
		mongoClient.getDatabase(DATABASE_NAME).drop();
		guest1Id = new ObjectId().toString();
		guest2Id = new ObjectId().toString();
		addTestGuestToDatabase(new Guest(guest1Id, "testFirstName1", "testLastName1", "test1@email.com", "1111111111"));
		addTestGuestToDatabase(new Guest(guest2Id, "testFirstName2", "testLastName2", "test2@email.com", "2222222222"));
		addTestBookingToDatabase(
				new Booking(guest1Id, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1, Room.SINGLE));
		addTestBookingToDatabase(
				new Booking(guest2Id, LocalDate.of(2021, 1, 20), LocalDate.of(2021, 1, 30), 2, Room.DOUBLE));

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

	private void removeTestGuestFromDatabase(String id) {
		mongoClient.getDatabase(DATABASE_NAME).getCollection(GUEST_COLLECTION_NAME).withCodecRegistry(pojoCodecRegistry)
				.deleteOne(Filters.eq("_id", new ObjectId(id)));
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
				.anySatisfy(e -> assertThat(e).contains("20/01/2021", "30/01/2021", "2", "DOUBLE"));
	}

	@Test
	@GUITest
	public void testAddGuestButtonSuccess() {
		window.tabbedPane().selectTab("Guests");
		window.textBox("firstNameTextBox").enterText("guest");
		window.textBox("lastNameTextBox").enterText("guest");
		window.textBox("emailTextBox").setText("guest@email.com");
		window.textBox("telephoneNumberTextBox").enterText("0000000000");
		window.button("addGuestButton").click();
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains("guest", "guest", "guest@email.com", "0000000000"));
	}

	@Test
	@GUITest
	public void testDeleteGuestButtonSuccess() {
		window.tabbedPane().selectTab("Guests");
		window.list().selectItem(Pattern.compile(".*testFirstName1.*testLastName1.*"));
		window.button("deleteGuestButton").click();
		assertThat(window.list().contents()).noneMatch(e -> e.contains("testFirstName1"));
	}

	@Test
	@GUITest
	public void testAddBookingButtonSuccess() {
		window.tabbedPane().selectTab("Bookings");
		window.textBox("checkInDateTextBox").enterText("10-01-2021");
		window.textBox("checkOutDateTextBox").enterText("20-01-2021");
		window.comboBox("numberOfGuestsComBox").selectItem("1");
		window.comboBox("roomComBox").selectItem("SINGLE");
		window.comboBox("guestComBox").selectItem(Pattern.compile(".*testFirstName1.*"));
		window.button("addBookingButton").click();
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains("10/01/2021", "20/01/2021", "1", "SINGLE"));
	}

	@Test
	@GUITest
	public void testSearchBookingsByDatesButtonSuccess() {
		window.tabbedPane().selectTab("Bookings");
		window.textBox("checkInDateTextBox").enterText("01-01-2021");
		window.textBox("checkOutDateTextBox").enterText("20-01-2021");
		window.button("searchByDatesButton").click();
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains("01/01/2021", "10/01/2021", "1", "SINGLE"));
	}

	@Test
	@GUITest
	public void testSearchBookingsByRoomButton() {
		window.tabbedPane().selectTab("Bookings");
		window.comboBox("roomComBox").selectItem("SINGLE");
		window.button("searchByRoomButton").click();
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains("01/01/2021", "10/01/2021", "1", "SINGLE"));
	}

	@Test
	@GUITest
	public void testSearchBookingsByGuestButton() {
		window.tabbedPane().selectTab("Bookings");
		window.comboBox("guestComBox").selectItem(0);
		window.button("searchByGuestButton").click();
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains("01/01/2021", "10/01/2021", "1", "SINGLE"));
	}

	@Test
	@GUITest
	public void testShowAllBookingsButton() {
		window.tabbedPane().selectTab("Bookings");
		window.button("allBookingsButton").click();
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains("01/01/2021", "10/01/2021", "1", "SINGLE"))
				.anySatisfy(e -> assertThat(e).contains("20/01/2021", "30/01/2021", "2", "DOUBLE"));
	}

	@Test
	@GUITest
	public void testDeleteBookingButtonSuccess() {
		window.tabbedPane().selectTab("Bookings");
		window.list().selectItem(Pattern.compile(".*01/01/2021.*10/01/2021.*"));
		window.button("deleteBookingButton").click();
		assertThat(window.list().contents()).noneMatch(e -> e.contains("01/01/2021"));
	}

	@Test
	@GUITest
	public void testAddGuestButtonErrorWhenEmailIsNotValid() {
		window.tabbedPane("tabbedPane").selectTab("Guests");
		window.textBox("firstNameTextBox").enterText("test");
		window.textBox("lastNameTextBox").enterText("test");
		window.textBox("emailTextBox").enterText("email");
		window.textBox("telephoneNumberTextBox").enterText("0000000000");
		window.button("addGuestButton").click();
		assertThat(window.list().contents()).hasSize(2);
		window.label("errorLogMessageLabel")
				.requireText("Guest Email is not valid: email. Format must be similar to prefix@domain.");
	}

	@Test
	@GUITest
	public void testDeleteGuestButtonErrorWhenGuestIsNotInTheDB() {
		window.tabbedPane().selectTab("Guests");
		window.list().selectItem(Pattern.compile(".*testFirstName1.*testLastName1.*"));
		removeTestGuestFromDatabase(guest1Id);
		window.button("deleteGuestButton").click();
		assertThat(window.list().contents()).noneMatch(e -> e.contains("testFirstName1"));
		assertThat(window.label("errorLogMessageLabel").text()).contains("testFirstName1", "testLastName1");
	}

}
