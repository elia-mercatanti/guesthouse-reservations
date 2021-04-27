package com.eliamercatanti.guesthousebooking.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eliamercatanti.guesthousebooking.controller.BookingController;
import com.eliamercatanti.guesthousebooking.controller.GuestController;
import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for Guesthouse Swing View")
class GuesthouseSwingViewTest {

	private FrameFixture window;

	private GuesthouseSwingView guesthouseSwingView;

	@Mock
	private GuestController guestController;

	@Mock
	private BookingController bookingController;

	@BeforeAll
	static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@BeforeEach
	void setUp() {
		GuiActionRunner.execute(() -> {
			guesthouseSwingView = new GuesthouseSwingView();
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
	@DisplayName("Inital State Tests")
	class InitalStateTests {

		@Test
		@DisplayName("Frame initial state - testFrameInitialState()")
		void testFrameInitialState() {
			window.requireTitle("Guesthouse Reservations");
		}

		@Test
		@DisplayName("Error Log initial state - testErrorLogInitialState()")
		void testErrorLogInitialState() {
			window.label("errorLogLabel").requireVisible().requireEnabled().requireText("Error Log:");
			window.label("errorLogMessageLabel").requireVisible().requireEnabled().requireText(" ");
		}

		@Test
		@DisplayName("Error Log initial state - testErrorLogInitialState()")
		void testInitialStateOfGuestsTab() {
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
			window.list("guestsList").requireVisible().requireEnabled().requireNoSelection();
			window.button("deleteGuestButton").requireVisible().requireDisabled().requireText("Delete Guest");
		}

		@Test
		@DisplayName("Bookings Tab initial state - testInitialStatesOfBookingsTab()")
		void testInitialStatesOfBookingsTab() {
			window.tabbedPane("tabbedPane").selectTab("Bookings").requireVisible().requireEnabled();
			window.label("checkInDateLabel").requireVisible().requireEnabled().requireText("Check In");
			window.textBox("checkInDateTextBox").requireVisible().requireEnabled().requireEmpty();
			window.label("checkOutDateLabel").requireVisible().requireEnabled().requireText("Check Out");
			window.textBox("checkInDateTextBox").requireVisible().requireEnabled().requireEmpty();
			window.label("numberOfGuestsLabel").requireVisible().requireEnabled().requireText("N. of Guests");
			window.comboBox("numberOfGuestsComBox").requireVisible().requireEnabled().requireNoSelection();
			String[] comboBoxContents = window.comboBox("numberOfGuestsComBox").contents();
			assertThat(comboBoxContents).containsExactly("1", "2", "3", "4");
			window.label("roomLabel").requireVisible().requireEnabled().requireText("Room");
			window.comboBox("roomComBox").requireVisible().requireEnabled().requireNoSelection();
			comboBoxContents = window.comboBox("roomComBox").contents();
			assertThat(comboBoxContents).containsExactly("SINGLE", "DOUBLE", "TRIPLE", "QUADRUPLE");
			window.label("guestLabel").requireVisible().requireEnabled().requireText("Guest");
			window.comboBox("guestComBox").requireVisible().requireEnabled().requireNoSelection();
			window.button("addBookingButton").requireVisible().requireDisabled().requireText("Add Booking");
			window.button("searchByDatesButton").requireVisible().requireDisabled().requireText("Search by Dates");
			window.button("searchByRoomButton").requireVisible().requireDisabled().requireText("Search by Room");
			window.button("searchByGuestButton").requireVisible().requireDisabled().requireText("Search by Guest");
			window.list("bookingsList").requireVisible().requireEnabled().requireNoSelection();
			window.button("deleteBookingButton").requireVisible().requireDisabled().requireText("Delete Booking");
			window.button("allBookingsButton").requireVisible().requireEnabled().requireText("All Bookings");
		}

	}

	@Nested
	@DisplayName("Content Pane Tests")
	class ContentPaneTests {

		@Test
		@DisplayName("Show Error should show the message in the error log  - testShowErrorShouldShowTheMessageInTheErrorLog()")
		void testShowErrorShouldShowTheMessageInTheErrorLog() {
			GuiActionRunner.execute(() -> guesthouseSwingView.showError("Error message test"));
			window.label("errorLogMessageLabel").requireText("Error message test");
		}

	}

	@Nested
	@DisplayName("Guests Tab Enabling Buttons Tests")
	class GuestTabEnablingButtonsTests {

		@Test
		@DisplayName("Add Guest Button should be enabled when guest infos are not empty - testWhenGuestInfosAreNotEmptyThenAddGuestButtonShouldBeEnabled()")
		void testWhenGuestInfosAreNotEmptyThenAddGuestButtonShouldBeEnabled() {
			window.tabbedPane("tabbedPane").selectTab("Guests");

			window.textBox("firstNameTextBox").enterText("test");
			window.textBox("lastNameTextBox").enterText("test");
			window.textBox("emailTextBox").enterText("test");
			window.textBox("telephoneNumberTextBox").enterText("test");
			window.button("addGuestButton").requireEnabled();
		}

		@Test
		@DisplayName("Add Guest Button should be disabled when guest infos are blank - testWhenSomeGuestInfosAreBlankThenAddGuestButtonShouldBeDisabled()")
		void testWhenSomeGuestInfosAreBlankThenAddGuestButtonShouldBeDisabled() {
			window.tabbedPane("tabbedPane").selectTab("Guests");

			JTextComponentFixture firstNameTextBox = window.textBox("firstNameTextBox");
			JTextComponentFixture lastNameTextBox = window.textBox("lastNameTextBox");
			JTextComponentFixture emailTextBox = window.textBox("emailTextBox");
			JTextComponentFixture telephoneNumberTextBox = window.textBox("telephoneNumberTextBox");
			JButtonFixture addGuestButton = window.button("addGuestButton");

			firstNameTextBox.enterText(" ");
			lastNameTextBox.enterText("test");
			emailTextBox.enterText("test");
			telephoneNumberTextBox.enterText("test");
			addGuestButton.requireDisabled();

			firstNameTextBox.setText("");
			lastNameTextBox.setText("");
			emailTextBox.setText("");
			telephoneNumberTextBox.setText("");

			firstNameTextBox.enterText("test");
			lastNameTextBox.enterText(" ");
			emailTextBox.enterText("test");
			telephoneNumberTextBox.enterText("test");
			addGuestButton.requireDisabled();

			firstNameTextBox.setText("");
			lastNameTextBox.setText("");
			emailTextBox.setText("");
			telephoneNumberTextBox.setText("");

			firstNameTextBox.enterText("test");
			lastNameTextBox.enterText("test");
			emailTextBox.enterText(" ");
			telephoneNumberTextBox.enterText("test");
			addGuestButton.requireDisabled();

			firstNameTextBox.setText("");
			lastNameTextBox.setText("");
			emailTextBox.setText("");
			telephoneNumberTextBox.setText("");

			firstNameTextBox.enterText("test");
			lastNameTextBox.enterText("test");
			emailTextBox.enterText("test");
			telephoneNumberTextBox.enterText(" ");
			addGuestButton.requireDisabled();
		}

		@Test
		@DisplayName("Delete Guest Button should be enabled only when a guest is selected - testDeleteGuestButtonShouldBeEnabledOnlyWhenAGuestIsSelected()")
		void testDeleteGuestButtonShouldBeEnabledOnlyWhenAGuestIsSelected() {
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

	}

	@Nested
	@DisplayName("Guests Tab Methods tests")
	class GuestsTabInterfaceMethodsTests {

		@Test
		@DisplayName("Show Guests should reset the list and fill guest infos to it - testShowGuestShouldResetTheListAndFillGuestInfosToIt()")
		void testShowGuestShouldResetTheListAndFillGuestInfosToIt() {
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
			GuiActionRunner.execute(() -> guesthouseSwingView.showGuests(Arrays.asList(guest1, guest2)));

			// Verify.
			String[] guestsListContent = window.list("guestsList").contents();
			assertThat(guestsListContent).containsExactly(
					"id=1, firstName=testFirstName1, lastName=testLastName1, email=test1@email.com, telNum=0000000000",
					"id=2, firstName=testFirstName2, lastName=testLastName2, email=test2@email.com, telNum=1111111111");
		}

		@Test
		@DisplayName("Guest Added should added guest to the list and combo box then clear the error log and guest form - testGuestAddedShouldAddedGuestToTheListAndComboBoxThenClearErrorLogAndGuestForm()")
		void testGuestAddedShouldAddedGuestToTheListAndComboBoxThenClearErrorLogAndGuestForm() {
			// Setup.
			Guest guestToAdd = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			window.tabbedPane("tabbedPane").selectTab("Guests");
			window.textBox("firstNameTextBox").setText("testFirstName");
			window.textBox("lastNameTextBox").setText("testLastName");
			window.textBox("emailTextBox").setText("test@email.com");
			window.textBox("telephoneNumberTextBox").setText("0000000000");

			// Execute.
			GuiActionRunner.execute(() -> guesthouseSwingView.guestAdded(guestToAdd));

			// Verify.
			String[] guestsListContent = window.list("guestsList").contents();
			assertThat(guestsListContent).containsExactly(
					"id=1, firstName=testFirstName, lastName=testLastName, email=test@email.com, telNum=0000000000");
			window.label("errorLogMessageLabel").requireText(" ");
			window.textBox("firstNameTextBox").requireEmpty();
			window.textBox("lastNameTextBox").requireEmpty();
			window.textBox("emailTextBox").requireEmpty();
			window.textBox("telephoneNumberTextBox").requireEmpty();
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			String[] guestComBoxContent = window.comboBox("guestComBox").contents();
			assertThat(guestComBoxContent).containsExactly("1, testFirstName, testLastName");
		}

		@Test
		@DisplayName("Guest Removed should remove the guest from the list and combo box then clear the error log label - testGuestRemovedShouldRemoveTheGuestFromTheListAndComboBoxThenClearTheErrorLogLabel()")
		void testGuestRemovedShouldRemoveTheGuestFromTheListAndComboBoxThenClearTheErrorLogLabel() {
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
			assertThat(guestsListContent).containsExactly(
					"id=2, firstName=testFirstName2, lastName=testLastName2, email=test2@email.com, telNum=1111111111");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			String[] guestComBoxContent = window.comboBox("guestComBox").contents();
			assertThat(guestComBoxContent).containsExactly("2, testFirstName2, testLastName2");
			window.label("errorLogMessageLabel").requireText(" ");
		}

		@Test
		@DisplayName("Show Error Guest Not Found should show an error message - testShowErrorGuestNotFoundShouldShowAnErrorMessage()")
		void testShowErrorGuestNotFoundShouldShowAnErrorMessage() {
			// Setup
			Guest guestNoLongerPresent = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com",
					"0000000000");
			Guest anotherGuest = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
			window.tabbedPane("tabbedPane").selectTab("Guests");
			GuiActionRunner.execute(() -> {
				DefaultListModel<Guest> listGuestsModel = guesthouseSwingView.getListGuestsModel();
				listGuestsModel.addElement(guestNoLongerPresent);
				listGuestsModel.addElement(anotherGuest);
			});

			// Execute.
			GuiActionRunner.execute(
					() -> guesthouseSwingView.showErrorGuestNotFound("Error message test", guestNoLongerPresent));

			// Verify.
			window.label("errorLogMessageLabel").requireText("Error message test: 1, testFirstName1, testLastName1");
			String[] guestsListContent = window.list("guestsList").contents();
			assertThat(guestsListContent).containsExactly(
					"id=2, firstName=testFirstName2, lastName=testLastName2, email=test2@email.com, telNum=1111111111");
		}

	}

	@Nested
	@DisplayName("Guests Tab Delegations Tests")
	class GuestsTabDelegationsTests {

		@Test
		@DisplayName("Add Guest Button should delegate to guest controller newGuest() - testAddGuestButtonShouldDelegateToGuestControllerNewGuest()")
		void testAddGuestButtonShouldDelegateToGuestControllerNewGuest() {
			// Setup.
			window.tabbedPane("tabbedPane").selectTab("Guests");
			window.textBox("firstNameTextBox").enterText("test");
			window.textBox("lastNameTextBox").enterText("test");
			window.textBox("emailTextBox").setText("test@email.com");
			window.textBox("telephoneNumberTextBox").enterText("0000000000");

			// Execute.
			window.button("addGuestButton").click();

			// Verify.
			verify(guestController).newGuest("test", "test", "test@email.com", "0000000000");
		}

		@Test
		@DisplayName("Delete Guest Button should delegate to guest controller deleteGuest() - testDeleteGuestButtonShouldDelegateToGuestControllerDeleteGuest()")
		void testDeleteGuestButtonShouldDelegateToGuestControllerDeleteGuest() {
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

	}

	@Nested
	@DisplayName("Bookings Tab Enabling Buttons Tests")
	class BookingsTabEnablingButtonsTests {

		@Test
		@DisplayName("Add Booking Button should be enabled when booking infos are set - testWhenBookingInfosAreSetThenAddBookingButtonShouldBeEnabled()")
		void testWhenBookingInfosAreSetThenAddBookingButtonShouldBeEnabled() {
			// Setup.
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));

			// Verify.
			window.textBox("checkInDateTextBox").enterText("test");
			window.textBox("checkOutDateTextBox").enterText("test");
			window.comboBox("numberOfGuestsComBox").selectItem(0);
			window.comboBox("roomComBox").selectItem(0);
			window.comboBox("guestComBox").selectItem(0);
			window.button("addBookingButton").requireEnabled();
		}

		@Test
		@DisplayName("Add Booking Button should be disabled when some booking infos are not set - testWhenBookingInfosAreSetThenAddBookingButtonShouldBeEnabled()")
		void testWhenSomeBookingInfosAreNotSetThenAddBookingButtonShouldBeDisabled() {
			// Setup.
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));
			JTextComponentFixture checkInDateTextBox = window.textBox("checkInDateTextBox");
			JTextComponentFixture checkOutDateTextBox = window.textBox("checkOutDateTextBox");
			JComboBoxFixture numberOfGuestComboBox = window.comboBox("numberOfGuestsComBox");
			JComboBoxFixture roomComBox = window.comboBox("roomComBox");
			JComboBoxFixture guestComBox = window.comboBox("guestComBox");
			JButtonFixture addBookingButton = window.button("addBookingButton");

			// Verify.
			checkInDateTextBox.enterText(" ");
			checkOutDateTextBox.enterText("test");
			numberOfGuestComboBox.selectItem(0);
			roomComBox.selectItem(0);
			guestComBox.selectItem(0);
			addBookingButton.requireDisabled();

			checkInDateTextBox.setText("");
			checkOutDateTextBox.setText("");
			numberOfGuestComboBox.clearSelection();
			roomComBox.clearSelection();
			guestComBox.clearSelection();

			checkInDateTextBox.enterText("test");
			checkOutDateTextBox.enterText(" ");
			numberOfGuestComboBox.selectItem(0);
			roomComBox.selectItem(0);
			guestComBox.selectItem(0);
			addBookingButton.requireDisabled();

			checkInDateTextBox.setText("");
			checkOutDateTextBox.setText("");
			numberOfGuestComboBox.clearSelection();
			roomComBox.clearSelection();
			guestComBox.clearSelection();

			checkInDateTextBox.enterText("test");
			checkOutDateTextBox.enterText("test");
			roomComBox.selectItem(0);
			guestComBox.selectItem(0);
			addBookingButton.requireDisabled();

			checkInDateTextBox.setText("");
			checkOutDateTextBox.setText("");
			numberOfGuestComboBox.clearSelection();
			roomComBox.clearSelection();
			guestComBox.clearSelection();

			checkInDateTextBox.enterText("test");
			checkOutDateTextBox.enterText("test");
			numberOfGuestComboBox.selectItem(0);
			guestComBox.selectItem(0);
			addBookingButton.requireDisabled();

			checkInDateTextBox.setText("");
			checkOutDateTextBox.setText("");
			numberOfGuestComboBox.clearSelection();
			roomComBox.clearSelection();
			guestComBox.clearSelection();

			checkInDateTextBox.enterText("test");
			checkOutDateTextBox.enterText("test");
			numberOfGuestComboBox.selectItem(0);
			roomComBox.selectItem(0);
			addBookingButton.requireDisabled();
		}

		@Test
		@DisplayName("Delete Booking Button should be enabled only when a booking is selected - testDeleteBookingButtonShouldBeEnabledOnlyWhenABookingIsSelected()")
		void testDeleteBookingButtonShouldBeEnabledOnlyWhenABookingIsSelected() {
			// Setup.
			Booking booking = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
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
		@DisplayName("Search By Dates Button should be enabled when booking dates are not blank - testWhenBookingDatesAreNotBlankThenSearchByDatesButtonShouldBeEnabled()")
		void testWhenBookingDatesAreNotBlankThenSearchByDatesButtonShouldBeEnabled() {
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			window.textBox("checkInDateTextBox").enterText("test");
			window.textBox("checkOutDateTextBox").enterText("test");
			window.button("searchByDatesButton").requireEnabled();
		}

		@Test
		@DisplayName("Search By Dates Button should be disabled when either check in or check out are blank - testWhenEitherCheckInOrCheckOutAreBlankThenSearchByDatesButtonShouldBeDisabled()")
		void testWhenEitherCheckInOrCheckOutAreBlankThenSearchByDatesButtonShouldBeDisabled() {
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			JTextComponentFixture checkInDateTextBox = window.textBox("checkInDateTextBox");
			JTextComponentFixture checkOutDateTextBox = window.textBox("checkOutDateTextBox");
			JButtonFixture searchByDatesButton = window.button("searchByDatesButton");

			checkInDateTextBox.enterText("test");
			checkOutDateTextBox.enterText(" ");
			searchByDatesButton.requireDisabled();

			checkInDateTextBox.setText("");
			checkOutDateTextBox.setText("");

			checkInDateTextBox.enterText(" ");
			checkOutDateTextBox.enterText("test");
			searchByDatesButton.requireDisabled();
		}

		@Test
		@DisplayName("Search By Room Button should be enabled only when a room is selected - testSearchByRoomButtonShouldBeEnabledOnlyWhenARoomIsSelected()")
		void testSearchByRoomButtonShouldBeEnabledOnlyWhenARoomIsSelected() {
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			window.comboBox("roomComBox").selectItem(0);
			window.button("searchByRoomButton").requireEnabled();
			window.comboBox("roomComBox").clearSelection();
			window.button("searchByRoomButton").requireDisabled();
		}

		@Test
		@DisplayName("Search By Guest Button should be enabled only when a guest is selected - testSearchByGuestButtonShouldBeEnabledOnlyWhenAGuestIsSelected()")
		void testSearchByGuestButtonShouldBeEnabledOnlyWhenAGuestIsSelected() {
			// Setup.
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));

			// Verify.
			window.comboBox("guestComBox").selectItem(0);
			window.button("searchByGuestButton").requireEnabled();
			window.comboBox("guestComBox").clearSelection();
			window.button("searchByGuestButton").requireDisabled();
		}

	}

	@Nested
	@DisplayName("Bookings Tab Methods tests")
	class BookingsTabInterfaceMethodsTests {

		@Test
		@DisplayName("Show Bookings should clear booking form and list then should fill new bookings to it - testShowBookingsShouldclearBookingFormAndListThenShouldFillNewBookingsToIt()")
		void testShowBookingsShouldClearBookingFormAndListThenShouldFillNewBookingsToIt() {
			// Setup.
			Booking previusBooking1 = new Booking("3", "1", LocalDate.of(2021, 3, 1), LocalDate.of(2021, 3, 10), 3,
					Room.TRIPLE);
			Booking previusBooking2 = new Booking("4", "2", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 4,
					Room.QUADRUPLE);
			Booking booking1 = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Booking booking2 = new Booking("2", "1", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 10), 2,
					Room.DOUBLE);
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			GuiActionRunner.execute(() -> {
				DefaultListModel<Booking> listBookingsModel = guesthouseSwingView.getListBookingsModel();
				listBookingsModel.addElement(previusBooking1);
				listBookingsModel.addElement(previusBooking2);
				DefaultComboBoxModel<Guest> comboBoxGuestsModel = guesthouseSwingView.getComboBoxGuestsModel();
				comboBoxGuestsModel.addElement(guest);
			});
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			window.textBox("checkInDateTextBox").setText("01-01-2021");
			window.textBox("checkOutDateTextBox").setText("10-01-2021");
			window.comboBox("numberOfGuestsComBox").selectItem(0);
			window.comboBox("roomComBox").selectItem(0);
			window.comboBox("guestComBox").selectItem(0);

			// Execute.
			GuiActionRunner.execute(() -> guesthouseSwingView.showBookings(Arrays.asList(booking1, booking2)));

			// Verify.
			window.textBox("checkInDateTextBox").requireEmpty();
			window.textBox("checkOutDateTextBox").requireEmpty();
			window.comboBox("numberOfGuestsComBox").requireNoSelection();
			window.comboBox("roomComBox").requireNoSelection();
			window.comboBox("guestComBox").requireNoSelection();
			String[] bookingsListContent = window.list("bookingsList").contents();
			assertThat(bookingsListContent).containsExactly(
					"id=1, guestId=1, checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE",
					"id=2, guestId=1, checkIn=01/02/2021, checkOut=10/02/2021, numGuests=2, room=DOUBLE");
		}

		@Test
		@DisplayName("Show Error Booking Not Found should show an error message - testShowErrorBookingNotFoundShouldShowAnErrorMessage()")
		void testShowErrorBookingNotFoundShouldShowAnErrorMessage() {
			// Setup.
			Booking bookingNoLongerPresent = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10),
					1, Room.SINGLE);
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
			assertThat(bookingsListContent).containsExactly(
					"id=2, guestId=1, checkIn=01/02/2021, checkOut=10/02/2021, numGuests=2, room=DOUBLE");
		}

		@Test
		@DisplayName("Booking Added should add the booking to the list then clear the error label and booking form - testBookingAddedShouldAddTheBookingToTheListThenClearTheErrorLabelAndBookingForm()")
		void testBookingAddedShouldAddTheBookingToTheListThenClearTheErrorLabelAndBookingForm() {
			// Setup.
			Booking bookingToAdd = new Booking("1", "1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 10), 1,
					Room.SINGLE);
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			GuiActionRunner.execute(() -> {
				DefaultComboBoxModel<Guest> comboBoxGuestsModel = guesthouseSwingView.getComboBoxGuestsModel();
				comboBoxGuestsModel.addElement(guest);
			});
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			window.textBox("checkInDateTextBox").setText("01-01-2021");
			window.textBox("checkOutDateTextBox").setText("10-01-2021");
			window.comboBox("numberOfGuestsComBox").selectItem(0);
			window.comboBox("roomComBox").selectItem(0);
			window.comboBox("guestComBox").selectItem(0);

			// Execute.
			GuiActionRunner.execute(() -> guesthouseSwingView.bookingAdded(bookingToAdd));

			// Verify.
			String[] bookingsListContent = window.list("bookingsList").contents();
			assertThat(bookingsListContent).containsExactly(
					"id=1, guestId=1, checkIn=01/01/2021, checkOut=10/01/2021, numGuests=1, room=SINGLE");
			window.textBox("checkInDateTextBox").requireEmpty();
			window.textBox("checkOutDateTextBox").requireEmpty();
			window.comboBox("numberOfGuestsComBox").requireNoSelection();
			window.comboBox("roomComBox").requireNoSelection();
			window.comboBox("guestComBox").requireNoSelection();
			window.label("errorLogMessageLabel").requireText(" ");
		}

		@Test
		@DisplayName("Booking Removed should remove the booking from the list and reset the error label - testBookingRemovedShouldRemoveTheBookingFromTheListAndResetTheErrorLabel()")
		void testBookingRemovedShouldRemoveTheBookingFromTheListAndResetTheErrorLabel() {
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
			assertThat(bookingsListContent).containsExactly(
					"id=2, guestId=1, checkIn=01/02/2021, checkOut=10/02/2021, numGuests=2, room=DOUBLE");
			window.label("errorLogMessageLabel").requireText(" ");
		}

	}

	@Nested
	@DisplayName("Bookings Tab Delegations Tests")
	class BookingsTabDelegationsTests {

		@Test
		@DisplayName("Add Booking Button should delegate to booking controller newBooking() - testAddBookingButtonShouldDelegateToBookingControllerNewBooking()")
		void testAddBookingButtonShouldDelegateToBookingControllerNewBooking() {
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
			window.comboBox("guestComBox").selectItem(0);

			// Execute.
			window.button("addBookingButton").click();

			// Verify.
			verify(bookingController).newBooking(guest, "1-1-2021", "1-10-2021", 1, Room.SINGLE);
		}

		@Test
		@DisplayName("Search By Dats Button should delegate to booking controller searchBookingsByDates() - testSearchByDatesButtonShouldDelegateToBookingControllerSearchBookingsByDates()")
		void testSearchByDatesButtonShouldDelegateToBookingControllerSearchBookingsByDates() {
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			window.textBox("checkInDateTextBox").enterText("1-1-2021");
			window.textBox("checkOutDateTextBox").enterText("1-10-2021");
			window.button("searchByDatesButton").click();
			verify(bookingController).searchBookingsByDates("1-1-2021", "1-10-2021");
		}

		@Test
		@DisplayName("Search By Room Button should delegate to booking controller searchBookingsByRoom() - testSearchByRoomButtonShouldDelegateToBookingControllerSearchBookingsByRoom()")
		void testSearchByRoomButtonShouldDelegateToBookingControllerSearchBookingsByRoom() {
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			window.comboBox("roomComBox").selectItem("SINGLE");
			window.button("searchByRoomButton").click();
			verify(bookingController).searchBookingsByRoom(Room.SINGLE);
		}

		@Test
		@DisplayName("Search By Guest Button should delegate to booking controller searchBookingsByGuest() - testSearchByGuestButtonShouldDelegateToBookingControllerSearchBookingsByGuest()")
		void testSearchByGuestButtonShouldDelegateToBookingControllerSearchBookingsByGuest() {
			// Setup.
			window.tabbedPane("tabbedPane").selectTab("Bookings");
			Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
			GuiActionRunner.execute(() -> {
				DefaultComboBoxModel<Guest> comboBoxGuestsModel = guesthouseSwingView.getComboBoxGuestsModel();
				comboBoxGuestsModel.addElement(guest);
			});
			window.comboBox("guestComBox").selectItem(0);

			// Execute.
			window.button("searchByGuestButton").click();

			// Verify.
			verify(bookingController).searchBookingsByGuest(guest);
		}

		@Test
		@DisplayName("Delete Booking Button should delegate to booking controller deleteBooking() - testDeleteBookingButtonShouldDelegateToBookingControllerDeleteBooking()")
		void testDeleteBookingButtonShouldDelegateToBookingControllerDeleteBooking() {
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
		@DisplayName("All Bookings Button should delegate to booking controller allBookings() - testAllBookingsButtonShouldDelegateToBookingControllerAllBookings()")
		void testAllBookingsButtonShouldDelegateToBookingControllerAllBookings() {
			window.tabbedPane("tabbedPane").selectTab("Bookings");

			window.button("allBookingsButton").click();
			verify(bookingController).allBookings();
		}

	}

}
