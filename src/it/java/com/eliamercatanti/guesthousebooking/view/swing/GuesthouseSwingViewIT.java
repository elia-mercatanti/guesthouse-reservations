package com.eliamercatanti.guesthousebooking.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.eliamercatanti.guesthousebooking.controller.BookingController;
import com.eliamercatanti.guesthousebooking.controller.GuestController;
import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.repository.BookingRepository;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.eliamercatanti.guesthousebooking.repository.mongo.BookingMongoRepository;
import com.eliamercatanti.guesthousebooking.repository.mongo.GuestMongoRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.eliamercatanti.guesthousebooking.validation.controller.ControllerInputValidator;
import com.mongodb.MongoClient;

@DisplayName("Integration Tests for Guesthouse Swing View")
class GuesthouseSwingViewIT {

	private static final String MONGO_CLIENT_HOST = "localhost";
	private static final String DATABASE_NAME = "guesthouse";
	private static final String GUEST_COLLECTION_NAME = "guest";
	private static final String BOOKING_COLLECTION_NAME = "booking";
	private GuestRepository guestRepository;
	private BookingRepository bookingRepository;
	private GuestController guestController;
	private BookingController bookingController;
	private GuesthouseSwingView guesthouseSwingView;
	private InputValidation inputValidation;
	private FrameFixture window;

	@BeforeAll
	static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@BeforeEach
	void onSetUp() {
		// Set repositories and input validation.
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		guestRepository = new GuestMongoRepository(new MongoClient(MONGO_CLIENT_HOST, mongoPort), DATABASE_NAME,
				GUEST_COLLECTION_NAME);
		bookingRepository = new BookingMongoRepository(new MongoClient(MONGO_CLIENT_HOST, mongoPort), DATABASE_NAME,
				BOOKING_COLLECTION_NAME);
		inputValidation = new ControllerInputValidator();

		// Clean the collections.
		for (Guest guest : guestRepository.findAll()) {
			guestRepository.delete(guest.getId());
		}
		for (Booking booking : bookingRepository.findAll()) {
			bookingRepository.delete(booking.getId());
		}

		// Set swing view.
		GuiActionRunner.execute(() -> {
			guesthouseSwingView = new GuesthouseSwingView();
			guestController = new GuestController(guestRepository, guesthouseSwingView, inputValidation);
			bookingController = new BookingController(bookingRepository, guesthouseSwingView, inputValidation);
			guesthouseSwingView.setGuestController(guestController);
			guesthouseSwingView.setBookingController(bookingController);
			return guesthouseSwingView;
		});
		window = new FrameFixture(guesthouseSwingView);
		window.show();
	}

	@AfterEach
	void tearDown() {
		window.cleanUp();
	}

	@Nested
	@DisplayName("Guests Tab Tests")
	class GuestsTabTests {

		@Test
		@DisplayName("All guest list request - testShowGuests()")
		void testShowAllGuests() {
			// Setup.
			Guest guest1 = new Guest("testFirstName1", "testLastName1", "test1@email.com", "1111111111");
			Guest guest2 = new Guest("testFirstName2", "testLastName2", "test2@email.com", "2222222222");
			guestRepository.save(guest1);
			guestRepository.save(guest2);
			String guest1ListString = "id=" + getIdSubstring(guest1.getId())
					+ ", firstName=testFirstName1, lastName=testLastName1, email=test1@email.com, telNum=1111111111";
			String guest2ListString = "id=" + getIdSubstring(guest2.getId())
					+ ", firstName=testFirstName2, lastName=testLastName2, email=test2@email.com, telNum=2222222222";
			window.tabbedPane("tabbedPane").selectTab("Guests");

			// Execute.
			GuiActionRunner.execute(() -> guestController.allGuests());

			// Verify.
			assertThat(window.list().contents()).containsExactly(guest1ListString, guest2ListString);
		}

		@Test
		@DisplayName("Add guest button success - testAddGuestButtonSuccess()")
		void testAddGuestButtonSuccess() {
			window.tabbedPane("tabbedPane").selectTab("Guests");
			window.textBox("firstNameTextBox").enterText("test");
			window.textBox("lastNameTextBox").enterText("test");
			window.textBox("emailTextBox").setText("test@email.com");
			window.textBox("telephoneNumberTextBox").enterText("0000000000");
			window.button("addGuestButton").click();
			Guest newGuest = guestRepository.findAll().get(0);
			assertThat(window.list().contents()).containsExactly("id=" + getIdSubstring(newGuest.getId())
					+ ", firstName=test, lastName=test, email=test@email.com, telNum=0000000000");
		}

		@Test
		@DisplayName("Delete guest button success - testDeleteGuestButtonSuccess()")
		void testDeleteGuestButtonSuccess() {
			window.tabbedPane().selectTab("Guests");
			GuiActionRunner.execute(
					() -> guestController.newGuest("testFirstName", "testLastName", "test@email.com", "1234567890"));
			window.list().selectItem(0);
			window.button("deleteGuestButton").click();
			assertThat(window.list().contents()).isEmpty();
		}

	}

	@Nested
	@DisplayName("Bookings Tab Tests")
	class BookingsTabTests {

		@Test
		@DisplayName("Add booking button success - testAddBookingButtonSuccess()")
		void testAddBookingButtonSuccess() {
			// Setup.
			Guest guest = new Guest(new ObjectId().toString(), "test", "test", "test@email.com", "1111111111");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.guestAdded(guest));

			// Execute.
			window.textBox("checkInDateTextBox").enterText("01-01-2021");
			window.textBox("checkOutDateTextBox").enterText("10-01-2021");
			window.comboBox("numberOfGuestsComBox").selectItem("1");
			window.comboBox("roomComBox").selectItem("SINGLE");
			window.comboBox("guestComBox").selectItem(0);
			window.button("addBookingButton").click();

			// Verify.
			Booking newBooking = bookingRepository.findAll().get(0);
			String bookingListString = "id=" + getIdSubstring(newBooking.getId()) + ", guestId="
					+ getIdSubstring(guest.getId())
					+ ", checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE";
			assertThat(window.list().contents()).containsExactly(bookingListString);
		}

		@Test
		@DisplayName("Search bookings by dates button success - testSearchBookingsByDatesButtonSuccess()")
		void testSearchBookingsByDatesButtonSuccess() {
			// Setup.
			Guest guest = new Guest(new ObjectId().toString(), "test", "test", "test@email.com", "1111111111");
			GuiActionRunner.execute(() -> guesthouseSwingView.guestAdded(guest));
			Booking booking1 = new Booking(guest.getId(), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Booking booking2 = new Booking(guest.getId(), LocalDate.of(2021, 1, 10), LocalDate.of(2021, 1, 20), 2,
					Room.DOUBLE);
			bookingRepository.save(booking1);
			bookingRepository.save(booking2);
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			// Execute.
			window.textBox("checkInDateTextBox").enterText("05-01-2021");
			window.textBox("checkOutDateTextBox").enterText("15-01-2021");
			window.button("searchByDatesButton").click();

			// Verify.
			String booking1ListString = "id=" + getIdSubstring(booking1.getId()) + ", guestId="
					+ getIdSubstring(booking1.getGuestId())
					+ ", checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE";
			String booking2ListString = "id=" + getIdSubstring(booking2.getId()) + ", guestId="
					+ getIdSubstring(booking2.getGuestId())
					+ ", checkIn=10/01/2021, checkOut=20/01/2021, numGuests=2, room=DOUBLE";
			assertThat(window.list().contents()).containsExactly(booking1ListString, booking2ListString);
		}

		@Test
		@DisplayName("Search bookings by room button success - testSearchBookingsByRoomButtonSuccess()")
		void testSearchBookingsByRoomButton() {
			// Setup.
			Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
					LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
					LocalDate.of(2021, 2, 10), 2, Room.DOUBLE);
			Booking booking3 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
					LocalDate.of(2021, 2, 10), 1, Room.SINGLE);
			bookingRepository.save(booking1);
			bookingRepository.save(booking2);
			bookingRepository.save(booking3);
			String booking1ListString = "id=" + getIdSubstring(booking1.getId()) + ", guestId="
					+ getIdSubstring(booking1.getGuestId())
					+ ", checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE";
			String booking3ListString = "id=" + getIdSubstring(booking3.getId()) + ", guestId="
					+ getIdSubstring(booking3.getGuestId())
					+ ", checkIn=01/02/2021, checkOut=10/02/2021, numGuests=1, room=SINGLE";
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			// Execute.
			window.comboBox("roomComBox").selectItem("SINGLE");
			window.button("searchByRoomButton").click();

			// Verify.
			assertThat(window.list().contents()).containsExactly(booking1ListString, booking3ListString);
		}

		@Test
		@DisplayName("Search bookings by room button success - testSearchBookingsByGuestIdButton()")
		void testSearchBookingsByGuestIdButton() {
			// Setup.
			String guestId = new ObjectId().toString();
			Guest guest = new Guest(guestId, "testFirstName", "testLastName", "test@email.com", "0000000000");
			GuiActionRunner.execute(() -> guesthouseSwingView.guestAdded(guest));
			Booking booking1 = new Booking(guestId, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
					LocalDate.of(2021, 2, 10), 1, Room.SINGLE);
			bookingRepository.save(booking1);
			bookingRepository.save(booking2);
			String booking1ListString = "id=" + getIdSubstring(booking1.getId()) + ", guestId="
					+ getIdSubstring(booking1.getGuestId())
					+ ", checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE";
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			// Execute.
			window.comboBox("guestComBox").selectItem(0);
			window.button("searchByGuestButton").click();

			// Verify.
			assertThat(window.list().contents()).containsExactly(booking1ListString);
		}

		@Test
		@DisplayName("All bookings button button - testShowAllBookingsButton()")
		void testShowAllBookingsButton() {
			// Setup.
			Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
					LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
					LocalDate.of(2021, 2, 10), 2, Room.DOUBLE);
			bookingRepository.save(booking1);
			bookingRepository.save(booking2);
			String booking1ListString = "id=" + getIdSubstring(booking1.getId()) + ", guestId="
					+ getIdSubstring(booking1.getGuestId())
					+ ", checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE";
			String booking2ListString = "id=" + getIdSubstring(booking2.getId()) + ", guestId="
					+ getIdSubstring(booking2.getGuestId())
					+ ", checkIn=01/02/2021, checkOut=10/02/2021, numGuests=2, room=DOUBLE";
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			// Execute.
			window.button("allBookingsButton").click();

			// Verify.
			assertThat(window.list().contents()).containsExactly(booking1ListString, booking2ListString);
		}

		@Test
		@DisplayName("All bookings list request - testShowAllBookingRequest()")
		void testShowAllBookingRequest() {
			// Setup.
			Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
					LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 2, 1),
					LocalDate.of(2021, 2, 10), 2, Room.DOUBLE);
			bookingRepository.save(booking1);
			bookingRepository.save(booking2);
			String booking1ListString = "id=" + getIdSubstring(booking1.getId()) + ", guestId="
					+ getIdSubstring(booking1.getGuestId())
					+ ", checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE";
			String booking2ListString = "id=" + getIdSubstring(booking2.getId()) + ", guestId="
					+ getIdSubstring(booking2.getGuestId())
					+ ", checkIn=01/02/2021, checkOut=10/02/2021, numGuests=2, room=DOUBLE";
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			// Execute.
			GuiActionRunner.execute(() -> bookingController.allBookings());

			// Verify.
			assertThat(window.list().contents()).containsExactly(booking1ListString, booking2ListString);
		}

		@Test
		@DisplayName("Delete booking button success - testDeleteBookingButtonSuccess()")
		void testDeleteBookingButtonSuccess() {
			Guest guest = new Guest(new ObjectId().toString(), "testFirstName", "testLastName", "test@email.com",
					"0000000000");
			window.tabbedPane().selectTab("Bookings");
			GuiActionRunner
					.execute(() -> bookingController.newBooking(guest, "01/01/2021", "10/01/2021", 1, Room.SINGLE));
			window.list().selectItem(0);
			window.button("deleteBookingButton").click();
			assertThat(window.list().contents()).isEmpty();
		}

	}

	private String getIdSubstring(String id) {
		return id.substring(id.length() / 2);
	}

}
