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
	@DisplayName("Happy Cases")
	class HappyCases {

		@Test
		@DisplayName("All Guest list request - testShowGuests()")
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
			assertThat(window.list("guestsList").contents()).containsExactly(guest1ListString, guest2ListString);
		}

		@Test
		@DisplayName("Add Guest button success - testAddGuestButtonSuccess()")
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
		@DisplayName("Delete Guest button success - testDeleteGuestButtonSuccess()")
		void testDeleteGuestButtonSuccess() {
			window.tabbedPane().selectTab("Guests");
			GuiActionRunner.execute(
					() -> guestController.newGuest("testFirstName", "testLastName", "test@email.com", "1234567890"));
			window.list().selectItem(0);
			window.button("deleteGuestButton").click();
			assertThat(window.list().contents()).isEmpty();
		}

		@Test
		@DisplayName("Add Booking button success - testAddBookingButtonSuccess()")
		void testAddBookingButtonSuccess() {
			// Setup.
			Guest guest = new Guest(new ObjectId().toString(), "test", "test", "test@email.com", "1111111111");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));
			
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

	}

	@Nested
	@DisplayName("Exceptional Cases")
	class ExceptionalCases {

		@Test
		@DisplayName("Add Guest button error when email is not Valid - testAddGuestButtonErrorWhenEmailIsNotValid()")
		void testAddGuestButtonErrorWhenEmailIsNotValid() {
			window.tabbedPane("tabbedPane").selectTab("Guests");
			window.textBox("firstNameTextBox").enterText("test");
			window.textBox("lastNameTextBox").enterText("test");
			window.textBox("emailTextBox").enterText("email");
			window.textBox("telephoneNumberTextBox").enterText("0000000000");
			window.button("addGuestButton").click();
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLogMessageLabel")
					.requireText("Guest Email is not valid: email. Format must be similar to prefix@domain.");
		}

		@Test
		@DisplayName("Add Guest button error when telephni n. is not Valid - testAddGuestButtonErrorWhenTelNumIsNotValid()")
		void testAddGuestButtonErrorWhenTelNumIsNotValid() {
			window.tabbedPane("tabbedPane").selectTab("Guests");
			window.textBox("firstNameTextBox").enterText("test");
			window.textBox("lastNameTextBox").enterText("test");
			window.textBox("emailTextBox").setText("test@email.com");
			window.textBox("telephoneNumberTextBox").enterText("aaaa");
			window.button("addGuestButton").click();
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLogMessageLabel")
					.requireText("Guest Telephone N. is not valid: aaaa. Format must be similar to +10000000000.");
		}

		@Test
		@DisplayName("Delete Guest button error when guest is not in the database - testDeleteGuestButtonErrorWhenGuestIsNotInTheDB()")
		void testDeleteGuestButtonErrorWhenGuestIsNotInTheDB() {
			// Setup.
			Guest guestNotPresent = new Guest(new ObjectId().toString(), "testFirstName", "testLastName",
					"test@email.com", "1111111111");
			window.tabbedPane().selectTab("Guests");
			GuiActionRunner.execute(() -> guesthouseSwingView.getListGuestsModel().addElement(guestNotPresent));
			
			// Execute.
			window.list().selectItem(0);
			window.button("deleteGuestButton").click();
			
			// Verify.
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLogMessageLabel").requireText("There is no guest with id " + guestNotPresent.getId()
					+ ": " + guestNotPresent.getId() + ", testFirstName, testLastName");
		}

		@Test
		@DisplayName("Add Booking button error when check in date is not valid - testAddBookingButtonErrorWhenCheckInDateIsNotValid()")
		void testAddBookingButtonErrorWhenCheckInDateIsNotValid() {
			// Setup.
			Guest guest = new Guest(new ObjectId().toString(), "test", "test", "test@email.com", "1111111111");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));
			
			// Execute.
			window.textBox("checkInDateTextBox").enterText("01012021");
			window.textBox("checkOutDateTextBox").enterText("10-01-2021");
			window.comboBox("numberOfGuestsComBox").selectItem("1");
			window.comboBox("roomComBox").selectItem("SINGLE");
			window.comboBox("guestComBox").selectItem(0);
			window.button("addBookingButton").click();
			
			// Verify.
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLogMessageLabel").requireText(
					"Booking Check In date is not valid: 01012021. Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.");
		}

		@Test
		@DisplayName("Add Booking button error when check out date is not valid - testAddBookingButtonErrorWhenCheckOutDateIsNotValid()")
		void testAddBookingButtonErrorWhenCheckOutDateIsNotValid() {
			// Setup.
			Guest guest = new Guest(new ObjectId().toString(), "test", "test", "test@email.com", "1111111111");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));
			
			// Execute.
			window.textBox("checkInDateTextBox").enterText("01-01-2021");
			window.textBox("checkOutDateTextBox").enterText("10012021");
			window.comboBox("numberOfGuestsComBox").selectItem("1");
			window.comboBox("roomComBox").selectItem("SINGLE");
			window.comboBox("guestComBox").selectItem(0);
			window.button("addBookingButton").click();
			
			// Verify.
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLogMessageLabel").requireText(
					"Booking Check Out date is not valid: 10012021. Format must be like dd(/.-)mm(/.-)yyyy or yyyy(/.-)mm(/.-)dd.");
		}

		@Test
		@DisplayName("Add Booking button error when check out date is not after check out date - testAddBookingButtonErrorWhenCheckOutDateIsNotAfterCheckInDate()")
		void testAddBookingButtonErrorWhenCheckOutDateIsNotAfterCheckInDate() {
			// Setup.
			Guest guest = new Guest(new ObjectId().toString(), "test", "test", "test@email.com", "1111111111");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));
			
			// Execute.
			window.textBox("checkInDateTextBox").enterText("20-01-2021");
			window.textBox("checkOutDateTextBox").enterText("10-01-2021");
			window.comboBox("numberOfGuestsComBox").selectItem("1");
			window.comboBox("roomComBox").selectItem("SINGLE");
			window.comboBox("guestComBox").selectItem(0);
			window.button("addBookingButton").click();
			
			// Verify.
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLogMessageLabel").requireText("Check Out date must be after check in date.");
		}

		@Test
		@DisplayName("Add Booking button error when number of guests is greater than room type - testAddBookingButtonErrorWhenNumberOfGuestsIsGreaterThanRoomType()")
		void testAddBookingButtonErrorWhenNumberOfGuestsIsGreaterThanRoomType() {
			// Setup.
			Guest guest = new Guest(new ObjectId().toString(), "test", "test", "test@email.com", "1111111111");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));
			
			// Execute.
			window.textBox("checkInDateTextBox").enterText("01-01-2021");
			window.textBox("checkOutDateTextBox").enterText("10-01-2021");
			window.comboBox("numberOfGuestsComBox").selectItem("2");
			window.comboBox("roomComBox").selectItem("SINGLE");
			window.comboBox("guestComBox").selectItem(0);
			window.button("addBookingButton").click();
			
			// Verify.
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLogMessageLabel")
					.requireText("Number of Guests must be suitable for the type of the room.");
		}

		@Test
		@DisplayName("Add Booking button error when room is already booked on the requested dates - testAddBookingButtonErrorWhenRoomIsAlreadyBookedOnTheRequestedDates()")
		void testAddBookingButtonErrorWhenRoomIsAlreadyBookedOnTheRequestedDates() {
			// Setup.
			Booking booking1 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 1),
					LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
			Booking booking2 = new Booking(new ObjectId().toString(), LocalDate.of(2021, 1, 20),
					LocalDate.of(2021, 1, 30), 1, Room.SINGLE);
			Guest guest = new Guest(new ObjectId().toString(), "test", "test", "test@email.com", "1111111111");
			bookingRepository.save(booking1);
			bookingRepository.save(booking2);
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));

			// Execute.
			window.textBox("checkInDateTextBox").enterText("09-01-2021");
			window.textBox("checkOutDateTextBox").enterText("21-01-2021");
			window.comboBox("numberOfGuestsComBox").selectItem("1");
			window.comboBox("roomComBox").selectItem("SINGLE");
			window.comboBox("guestComBox").selectItem(0);
			window.button("addBookingButton").click();
			
			// Verify.
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLogMessageLabel").requireText(
					"The selected room is already booked on the requested dates: SINGLE on (09-01-2021 - 21-01-2021).");
		}

	}

	private String getIdSubstring(String id) {
		return id.substring(id.length() / 2);
	}

}
