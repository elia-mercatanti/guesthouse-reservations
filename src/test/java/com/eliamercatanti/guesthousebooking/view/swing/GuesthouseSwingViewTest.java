package com.eliamercatanti.guesthousebooking.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.awt.Dimension;
import java.time.LocalDate;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.eliamercatanti.guesthousebooking.controller.BookingController;
import com.eliamercatanti.guesthousebooking.controller.GuestController;
import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;

@RunWith(MockitoJUnitRunner.class)
public class GuesthouseSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private GuesthouseSwingView guesthouseSwingView;

	@Mock
	private GuestController guestController;

	@Mock
	private BookingController bookingController;

	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			guesthouseSwingView = new GuesthouseSwingView();
			guesthouseSwingView.setGuestController(guestController);
			guesthouseSwingView.setBookingController(bookingController);
			return guesthouseSwingView;
		});
		window = new FrameFixture(robot(), guesthouseSwingView);
		window.show();
	}

	@Test
	public void testFrameInitialState() {
		window.requireSize(new Dimension(800, 600));
		window.requireTitle("Guesthouse Reservations");
	}

	@Test
	public void testErrorLogInitialState() {
		window.label("errorLogLabel").requireVisible().requireEnabled().requireText("Error Log:");
		window.label("errorLogMessageLabel").requireVisible().requireEnabled().requireText(" ");
	}

	@Test
	public void testControlsInitialStatesOfGuestsTab() {
		window.tabbedPane("tabbedPane").selectTab("Guests").requireVisible().requireEnabled();
		window.label("firstNameLabel").requireVisible().requireEnabled().requireText("First Name");
		window.textBox("firstNameTextBox").requireVisible().requireEnabled().requireEmpty();
		window.label("lastNameLabel").requireVisible().requireEnabled().requireText("Last Name");
		window.textBox("lastNameTextBox").requireVisible().requireEnabled().requireEmpty();
		window.label("emailLabel").requireVisible().requireEnabled().requireText("Email");
		window.textBox("emailTextBox").requireVisible().requireEnabled().requireEmpty();
		window.label("telephoneNumberLabel").requireVisible().requireEnabled().requireText("Telephone N.");
		window.textBox("telephoneNumberTextBox").requireVisible().requireEnabled().requireEmpty();
		window.button("addGuestButton").requireVisible().requireDisabled().requireText("Add Guest");
		window.list("guestsList").requireVisible().requireEnabled();
		window.button("deleteGuestButton").requireVisible().requireDisabled().requireText("Delete Guest");
	}

	@Test
	public void testControlsInitialStatesOfBookingsTab() {
		window.tabbedPane("tabbedPane").selectTab("Bookings").requireVisible().requireEnabled();
		window.label("checkInDateLabel").requireVisible().requireEnabled().requireText("Check In");
		window.textBox("checkInDateTextBox").requireVisible().requireEnabled().requireEmpty();
		window.label("checkOutDateLabel").requireVisible().requireEnabled().requireText("Check Out");
		window.textBox("checkInDateTextBox").requireVisible().requireEnabled().requireEmpty();
		window.label("numberOfGuestsLabel").requireVisible().requireEnabled().requireText("N. of Guests");
		window.comboBox("numberOfGuestsComBox").requireVisible().requireEnabled();
		String[] comboBoxContents = window.comboBox("numberOfGuestsComBox").contents();
		assertThat(comboBoxContents).containsExactly("1", "2", "3", "4");
		window.label("roomLabel").requireVisible().requireEnabled().requireText("Room");
		window.comboBox("roomComBox").requireVisible().requireEnabled();
		comboBoxContents = window.comboBox("roomComBox").contents();
		assertThat(comboBoxContents).containsExactly("SINGLE", "DOUBLE", "TRIPLE", "QUADRUPLE");
		window.label("guestIdLabel").requireVisible().requireEnabled().requireText("Guest Id");
		window.comboBox("guestIdComBox").requireVisible().requireEnabled();
		window.button("addBookingButton").requireVisible().requireDisabled().requireText("Add Booking");
		window.button("searchByDatesButton").requireVisible().requireDisabled().requireText("Search by Dates");
		window.button("searchByRoomButton").requireVisible().requireDisabled().requireText("Search by Room");
		window.button("searchByGuestButton").requireVisible().requireDisabled().requireText("Search by Guest");
		window.list("bookingsList").requireVisible().requireEnabled();
		window.button("deleteBookingButton").requireVisible().requireDisabled().requireText("Delete Booking");
		window.button("allBookingsButton").requireVisible().requireEnabled().requireText("All Bookings");
	}

	@Test
	public void testWhenGuestInfosAreNonEmptyThenAddGuestButtonShouldBeEnabled() {
		window.tabbedPane("tabbedPane").selectTab("Guests");

		window.textBox("firstNameTextBox").enterText("test");
		window.textBox("lastNameTextBox").enterText("test");
		window.textBox("emailTextBox").enterText("test@email.com");
		window.textBox("telephoneNumberTextBox").enterText("0000000000");
		window.button("addGuestButton").requireEnabled();
	}

	@Test
	public void testWhenSomeGuestInfosAreBlankThenAddGuestButtonShouldBeDisabled() {
		window.tabbedPane("tabbedPane").selectTab("Guests");

		JTextComponentFixture firstNameTextBox = window.textBox("firstNameTextBox");
		JTextComponentFixture lastNameTextBox = window.textBox("lastNameTextBox");
		JTextComponentFixture emailTextBox = window.textBox("emailTextBox");
		JTextComponentFixture telephoneNumberTextBox = window.textBox("telephoneNumberTextBox");
		JButtonFixture addGuestButton = window.button("addGuestButton");

		firstNameTextBox.enterText(" ");
		lastNameTextBox.enterText("test");
		emailTextBox.enterText("test@email.com");
		telephoneNumberTextBox.enterText("0000000000");
		addGuestButton.requireDisabled();

		firstNameTextBox.setText("");
		lastNameTextBox.setText("");
		emailTextBox.setText("");
		telephoneNumberTextBox.setText("");

		firstNameTextBox.enterText("test");
		lastNameTextBox.enterText(" ");
		emailTextBox.enterText("test@email.com");
		telephoneNumberTextBox.enterText("0000000000");
		addGuestButton.requireDisabled();

		firstNameTextBox.setText("");
		lastNameTextBox.setText("");
		emailTextBox.setText("");
		telephoneNumberTextBox.setText("");

		firstNameTextBox.enterText("test");
		lastNameTextBox.enterText("test");
		emailTextBox.enterText(" ");
		telephoneNumberTextBox.enterText("0000000000");
		addGuestButton.requireDisabled();

		firstNameTextBox.setText("");
		lastNameTextBox.setText("");
		emailTextBox.setText("");
		telephoneNumberTextBox.setText("");

		firstNameTextBox.enterText("test");
		lastNameTextBox.enterText("test");
		emailTextBox.enterText("test@email.com");
		telephoneNumberTextBox.enterText(" ");
		addGuestButton.requireDisabled();
	}

	@Test
	public void testDeleteGuestButtonShouldBeEnabledOnlyWhenAGuestIsSelected() {
		// Setup.
		Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		window.tabbedPane("tabbedPane").selectTab("Guests");

		// Execute.
		GuiActionRunner.execute(() -> guesthouseSwingView.getListGuestsModel().addElement(guest));

		// Verify.
		JListFixture guestsList = window.list("guestsList");
		JButtonFixture deleteGuestButton = window.button("deleteGuestButton");
		guestsList.selectItem(0);
		deleteGuestButton.requireEnabled();
		guestsList.clearSelection();
		deleteGuestButton.requireDisabled();
	}

	@Test
	public void testShowAllGuestShouldResetAndFillGuestInfosToTheList() {
		// Setup.
		Guest previusGuest1 = new Guest("3", "testFirstName3", "testLastName3", "test3@email.com", "2222222222");
		Guest previusGuest2 = new Guest("4", "testFirstName4", "testLastName4", "test4@email.com", "4444444444");
		Guest guest1 = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000");
		Guest guest2 = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Guest> listGuestsModel = guesthouseSwingView.getListGuestsModel();
			listGuestsModel.addElement(previusGuest1);
			listGuestsModel.addElement(previusGuest2);
		});

		// Execute.
		GuiActionRunner.execute(() -> guesthouseSwingView.showAllGuests(Arrays.asList(guest1, guest2)));

		// Verify.
		String[] guestsListContent = window.list("guestsList").contents();
		assertThat(guestsListContent).containsExactly("1, testFirstName1, testLastName1, test1@email.com, 0000000000",
				"2, testFirstName2, testLastName2, test2@email.com, 1111111111");
	}

	@Test
	public void testGuestAddedShouldBeAddedToTheGuestListAndComboBoxThenClearTheErrorLogLabel() {
		// Setup.
		Guest guestToAdd = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		window.tabbedPane("tabbedPane").selectTab("Guests");

		// Execute.
		GuiActionRunner.execute(() -> guesthouseSwingView.guestAdded(guestToAdd));

		// Verify.
		String[] guestsListContent = window.list("guestsList").contents();
		assertThat(guestsListContent).containsExactly("1, testFirstName, testLastName, test@email.com, 0000000000");
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		String[] guestIdComBoxContent = window.comboBox("guestIdComBox").contents();
		assertThat(guestIdComBoxContent).containsExactly("1, testFirstName, testLastName");
		window.label("errorLogMessageLabel").requireText(" ");
	}

	@Test
	public void testGuestRemovedShouldRemoveTheGuestFromTheListAndComboBoxThenClearTheErrorLogLabel() {
		// Setup.
		Guest guestToRemove = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000");
		Guest anotherGuest = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Guest> listGuestsModel = guesthouseSwingView.getListGuestsModel();
			listGuestsModel.addElement(guestToRemove);
			listGuestsModel.addElement(anotherGuest);
			DefaultComboBoxModel<Guest> comboBoxGuestsModel = guesthouseSwingView.getComboBoxGuestsModel();
			comboBoxGuestsModel.addElement(guestToRemove);
			comboBoxGuestsModel.addElement(anotherGuest);
		});

		// Execute.
		GuiActionRunner.execute(() -> guesthouseSwingView
				.guestRemoved(new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000")));

		// Verify.
		String[] guestsListContent = window.list("guestsList").contents();
		assertThat(guestsListContent).containsExactly("2, testFirstName2, testLastName2, test2@email.com, 1111111111");
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		String[] guestIdComBoxContent = window.comboBox("guestIdComBox").contents();
		assertThat(guestIdComBoxContent).containsExactly("2, testFirstName2, testLastName2");
		window.label("errorLogMessageLabel").requireText(" ");
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLog() {
		GuiActionRunner.execute(() -> guesthouseSwingView.showError("Error message test"));
		window.label("errorLogMessageLabel").requireText("Error message test");
	}

	@Test
	public void testShowErrorGuestNotFound() {
		// Setup
		Guest guestNoLongerPresent = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000");
		Guest anotherGuest = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Guest> listGuestsModel = guesthouseSwingView.getListGuestsModel();
			listGuestsModel.addElement(guestNoLongerPresent);
			listGuestsModel.addElement(anotherGuest);
		});

		// Execute.
		GuiActionRunner
				.execute(() -> guesthouseSwingView.showErrorGuestNotFound("Error message test", guestNoLongerPresent));

		// Verify.
		window.label("errorLogMessageLabel").requireText("Error message test: 1, testFirstName1, testLastName1");
		String[] guestsListContent = window.list("guestsList").contents();
		assertThat(guestsListContent).containsExactly("2, testFirstName2, testLastName2, test2@email.com, 1111111111");
	}

	@Test
	public void testAddGuestButtonShouldDelegateToGuestControllerNewGuest() {
		// Setup.
		window.tabbedPane("tabbedPane").selectTab("Guests");
		window.textBox("firstNameTextBox").enterText("test");
		window.textBox("lastNameTextBox").enterText("test");
		window.textBox("emailTextBox").enterText("test@email.com");
		window.textBox("telephoneNumberTextBox").enterText("0000000000");

		// Execute.
		window.button("addGuestButton").click();

		// Verify.
		verify(guestController).newGuest(new Guest("test", "test", "test@email.com", "0000000000"));
	}

	@Test
	public void testDeleteGuestButtonShouldDelegateToGuestControllerDeleteGuest() {
		// Setup.
		Guest guestToDelete = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000");
		Guest anotherGuest = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Guest> listGuestsModel = guesthouseSwingView.getListGuestsModel();
			listGuestsModel.addElement(guestToDelete);
			listGuestsModel.addElement(anotherGuest);
		});
		window.list("guestsList").selectItem(0);

		// Execute.
		window.button("deleteGuestButton").click();

		// Verify.
		verify(guestController).deleteGuest(guestToDelete);
	}

	@Test
	public void testWhenBookingInfosAreSetThenAddBookingButtonShouldBeEnabled() {
		// Setup.
		Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));

		// Verify.
		window.textBox("checkInDateTextBox").enterText("00-00-0000");
		window.textBox("checkOutDateTextBox").enterText("00-00-0000");
		window.comboBox("numberOfGuestsComBox").selectItem(0);
		window.comboBox("roomComBox").selectItem(0);
		window.comboBox("guestIdComBox").selectItem(0);
		window.button("addBookingButton").requireEnabled();
	}

	@Test
	public void testWhenSomeBookingInfoAreNotSetThenAddBookingButtonShouldBeDisabled() {
		// Setup.
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));
		JTextComponentFixture checkInDateTextBox = window.textBox("checkInDateTextBox");
		JTextComponentFixture checkOutDateTextBox = window.textBox("checkOutDateTextBox");
		JComboBoxFixture numberOfGuestComboBox = window.comboBox("numberOfGuestsComBox");
		JComboBoxFixture roomComBox = window.comboBox("roomComBox");
		JComboBoxFixture guestIdComBox = window.comboBox("guestIdComBox");
		JButtonFixture addBookingButton = window.button("addBookingButton");

		// Verify.
		checkInDateTextBox.enterText(" ");
		checkOutDateTextBox.enterText("00-00-0000");
		numberOfGuestComboBox.selectItem(0);
		roomComBox.selectItem(0);
		guestIdComBox.selectItem(0);
		addBookingButton.requireDisabled();

		checkInDateTextBox.setText("");
		checkOutDateTextBox.setText("");
		numberOfGuestComboBox.clearSelection();
		roomComBox.clearSelection();
		guestIdComBox.clearSelection();

		checkInDateTextBox.enterText("00-00-0000");
		checkOutDateTextBox.enterText(" ");
		numberOfGuestComboBox.selectItem(0);
		roomComBox.selectItem(0);
		guestIdComBox.selectItem(0);
		addBookingButton.requireDisabled();

		checkInDateTextBox.setText("");
		checkOutDateTextBox.setText("");
		numberOfGuestComboBox.clearSelection();
		roomComBox.clearSelection();
		guestIdComBox.clearSelection();

		checkInDateTextBox.enterText("00-00-0000");
		checkOutDateTextBox.enterText("00-00-0000");
		roomComBox.selectItem(0);
		guestIdComBox.selectItem(0);
		addBookingButton.requireDisabled();

		checkInDateTextBox.setText("");
		checkOutDateTextBox.setText("");
		numberOfGuestComboBox.clearSelection();
		roomComBox.clearSelection();
		guestIdComBox.clearSelection();

		checkInDateTextBox.enterText("00-00-0000");
		checkOutDateTextBox.enterText("00-00-0000");
		numberOfGuestComboBox.selectItem(0);
		guestIdComBox.selectItem(0);
		addBookingButton.requireDisabled();

		checkInDateTextBox.setText("");
		checkOutDateTextBox.setText("");
		numberOfGuestComboBox.clearSelection();
		roomComBox.clearSelection();
		guestIdComBox.clearSelection();

		checkInDateTextBox.enterText("00-00-0000");
		checkOutDateTextBox.enterText("00-00-0000");
		numberOfGuestComboBox.selectItem(0);
		roomComBox.selectItem(0);
		addBookingButton.requireDisabled();
	}

	@Test
	public void testDeleteBookingButtonShouldBeEnabledOnlyWhenABookingIsSelected() {
		// Setup.
		Booking booking = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		GuiActionRunner.execute(() -> guesthouseSwingView.getListBookingsModel().addElement(booking));
		JListFixture bookingsList = window.list("bookingsList");
		JButtonFixture deleteBookingButton = window.button("deleteBookingButton");

		// Verify.
		bookingsList.selectItem(0);
		deleteBookingButton.requireEnabled();
		bookingsList.clearSelection();
		deleteBookingButton.requireDisabled();
	}

	@Test
	public void testWhenBookingDatesAreNotBlankThenSearchByDatesButtonShouldBeEnabled() {
		window.tabbedPane("tabbedPane").selectTab("Bookings");

		window.textBox("checkInDateTextBox").enterText("00-00-0000");
		window.textBox("checkOutDateTextBox").enterText("00-00-0000");
		window.button("searchByDatesButton").requireEnabled();
	}

	@Test
	public void testWhenEitherCheckInOrCheckOutAreBlankThenSearchByDatesButtonShouldBeDisabled() {
		window.tabbedPane("tabbedPane").selectTab("Bookings");

		JTextComponentFixture checkInDateTextBox = window.textBox("checkInDateTextBox");
		JTextComponentFixture checkOutDateTextBox = window.textBox("checkOutDateTextBox");
		JButtonFixture searchByDatesButton = window.button("searchByDatesButton");

		checkInDateTextBox.enterText("00-00-0000");
		checkOutDateTextBox.enterText(" ");
		searchByDatesButton.requireDisabled();

		checkInDateTextBox.setText("");
		checkOutDateTextBox.setText("");

		checkInDateTextBox.enterText(" ");
		checkOutDateTextBox.enterText("00-00-0000");
		searchByDatesButton.requireDisabled();
	}

	@Test
	public void testSearchByRoomButtonShouldBeEnabledOnlyWhenARoomIsSelected() {
		window.tabbedPane("tabbedPane").selectTab("Bookings");

		window.comboBox("roomComBox").selectItem(0);
		window.button("searchByRoomButton").requireEnabled();
		window.comboBox("roomComBox").clearSelection();
		window.button("searchByRoomButton").requireDisabled();
	}

	@Test
	public void testSearchByIdGuestButtonShouldBeEnabledOnlyWhenAGuestIdIsSelected() {
		// Setup.
		Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));

		// Verify.
		window.comboBox("guestIdComBox").selectItem(0);
		window.button("searchByGuestButton").requireEnabled();
		window.comboBox("guestIdComBox").clearSelection();
		window.button("searchByGuestButton").requireDisabled();
	}

	@Test
	public void testsShowAllBookingsShouldResetAndFillBookingDescriptionsToTheList() {
		// Setup.
		Booking previusBooking1 = new Booking("3", "1", LocalDate.of(2021, 3, 1), LocalDate.of(2021, 3, 10), 3,
				Room.TRIPLE);
		Booking previusBooking2 = new Booking("4", "2", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 4,
				Room.QUADRUPLE);
		Booking booking1 = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1, Room.SINGLE);
		Booking booking2 = new Booking("2", "1", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2, Room.DOUBLE);
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Booking> listBookingsModel = guesthouseSwingView.getListBookingsModel();
			listBookingsModel.addElement(previusBooking1);
			listBookingsModel.addElement(previusBooking2);
		});

		// Execute.
		GuiActionRunner.execute(() -> guesthouseSwingView.showAllBookings(Arrays.asList(booking1, booking2)));

		// Verify.
		String[] bookingsListContent = window.list("bookingsList").contents();
		assertThat(bookingsListContent).containsExactly(
				"id=1, guestId=1, checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE",
				"id=2, guestId=1, checkIn=01/02/2021, checkOut=10/02/2021, numGuests=2, room=DOUBLE");
	}

	@Test
	public void testShowErrorBookingNotFound() {
		// Setup.
		Booking bookingNoLongerPresent = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
				Room.SINGLE);
		Booking anotherBooking = new Booking("2", "1", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2,
				Room.DOUBLE);
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Booking> listBookingsModel = guesthouseSwingView.getListBookingsModel();
			listBookingsModel.addElement(bookingNoLongerPresent);
			listBookingsModel.addElement(anotherBooking);
		});

		// Execute.
		GuiActionRunner.execute(
				() -> guesthouseSwingView.showErrorBookingNotFound("Error message test", bookingNoLongerPresent));

		// Verify.
		window.label("errorLogMessageLabel")
				.requireText("Error message test: id=1, guestId=1, checkIn=01/01/2021, checkOut=10/01/2021");
		String[] bookingsListContent = window.list("bookingsList").contents();
		assertThat(bookingsListContent)
				.containsExactly("id=2, guestId=1, checkIn=01/02/2021, checkOut=10/02/2021, numGuests=2, room=DOUBLE");
	}

	@Test
	public void testBookingAddedShouldAddTheBookingToTheListAndClearTheErrorLabel() {
		// Setup.
		Booking bookingToAdd = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
				Room.SINGLE);
		window.tabbedPane("tabbedPane").selectTab("Bookings");

		// Execute.
		GuiActionRunner.execute(() -> guesthouseSwingView.bookingAdded(bookingToAdd));

		// Verify.
		String[] bookingsListContent = window.list("bookingsList").contents();
		assertThat(bookingsListContent)
				.containsExactly("id=1, guestId=1, checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE");
		window.label("errorLogMessageLabel").requireText(" ");
	}

	@Test
	public void testBookingRemovedShouldRemoveTheBookingFromTheListAndResetTheErrorLabel() {
		// Setup.
		Booking bookingToRemove = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
				Room.SINGLE);
		Booking anotherBooking = new Booking("2", "1", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2,
				Room.DOUBLE);
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Booking> listBookingsModel = guesthouseSwingView.getListBookingsModel();
			listBookingsModel.addElement(bookingToRemove);
			listBookingsModel.addElement(anotherBooking);
		});

		// Execute.
		GuiActionRunner.execute(() -> guesthouseSwingView.bookingRemoved(
				new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1, Room.SINGLE)));

		// Verify.
		String[] bookingsListContent = window.list("bookingsList").contents();
		assertThat(bookingsListContent)
				.containsExactly("id=2, guestId=1, checkIn=01/02/2021, checkOut=10/02/2021, numGuests=2, room=DOUBLE");
		window.label("errorLogMessageLabel").requireText(" ");
	}

	@Test
	public void testAddBookingButtonShouldDelegateToBookingControllerNewBooking() {
		// Setup.
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<Guest> comboBoxGuestsModel = guesthouseSwingView.getComboBoxGuestsModel();
			comboBoxGuestsModel.addElement(guest);
		});
		window.textBox("checkInDateTextBox").enterText("1-1-2021");
		window.textBox("checkOutDateTextBox").enterText("1-10-2021");
		window.comboBox("numberOfGuestsComBox").selectItem("1");
		window.comboBox("roomComBox").selectItem("SINGLE");
		window.comboBox("guestIdComBox").selectItem(0);

		// Execute.
		window.button("addBookingButton").click();

		// Verify.
		verify(bookingController).newBooking("1-1-2021", "1-10-2021", 1, Room.SINGLE, guest);
	}

	@Test
	public void testSearchByDatesButtonShouldDelegateToBookingControllerSearchBookingsByDates() {
		window.tabbedPane("tabbedPane").selectTab("Bookings");

		window.textBox("checkInDateTextBox").enterText("1-1-2021");
		window.textBox("checkOutDateTextBox").enterText("1-10-2021");
		window.button("searchByDatesButton").click();
		verify(bookingController).searchBookingsByDates("1-1-2021", "1-10-2021");
	}

	@Test
	public void testSearchByRoomButtonShouldDelegateToBookingControllerSearchBookingsByRoom() {
		window.tabbedPane("tabbedPane").selectTab("Bookings");

		window.comboBox("roomComBox").selectItem("SINGLE");
		window.button("searchByRoomButton").click();
		verify(bookingController).searchBookingsByRoom(Room.SINGLE);
	}

	@Test
	public void testSearchByGuestButtonShouldDelegateToBookingControllerSearchBookingsByGuestId() {
		// Setup.
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<Guest> comboBoxGuestsModel = guesthouseSwingView.getComboBoxGuestsModel();
			comboBoxGuestsModel.addElement(guest);
		});
		window.comboBox("guestIdComBox").selectItem(0);

		// Execute.
		window.button("searchByGuestButton").click();

		// Verify.
		verify(bookingController).searchBookingsByGuest(guest);
	}

	@Test
	public void testDeleteBookingButtonShouldDelegateToBookingControllerDeleteBooking() {
		// Setup.
		Booking bookingToDelete = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
				Room.SINGLE);
		Booking anotherBooking = new Booking("2", "1", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2,
				Room.DOUBLE);
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Booking> listBookingsModel = guesthouseSwingView.getListBookingsModel();
			listBookingsModel.addElement(bookingToDelete);
			listBookingsModel.addElement(anotherBooking);
		});
		window.list("bookingsList").selectItem(0);

		// Execute.
		window.button("deleteBookingButton").click();

		// Verify.
		verify(bookingController).deleteBooking(bookingToDelete);
	}

	@Test
	public void testAllBookingsButtonShouldDelegateToBookingControllerAllBookings() {
		window.tabbedPane("tabbedPane").selectTab("Bookings");

		window.button("allBookingsButton").click();
		verify(bookingController).allBookings();
	}

}
