package com.eliamercatanti.guesthousebooking.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.eliamercatanti.guesthousebooking.controller.GuestController;
import com.eliamercatanti.guesthousebooking.model.Guest;

@RunWith(MockitoJUnitRunner.class)
public class GuesthouseSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private GuesthouseSwingView guesthouseSwingView;

	@Mock
	private GuestController guestController;

	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			guesthouseSwingView = new GuesthouseSwingView();
			guesthouseSwingView.setGuestController(guestController);
			return guesthouseSwingView;
		});
		window = new FrameFixture(robot(), guesthouseSwingView);
		window.show(); // shows the frame to test
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
		window.label("guestIdLabel").requireVisible().requireEnabled().requireText("Guest ID");
		window.comboBox("guestIdComBox").requireVisible().requireEnabled();
		window.button("addBookingButton").requireVisible().requireDisabled().requireText("Add Booking");
		window.button("searchBookingsButton").requireVisible().requireDisabled().requireText("Search Bookings");
		window.list("bookingsList").requireVisible().requireEnabled();
		window.button("deleteBookingButton").requireVisible().requireDisabled().requireText("Delete Booking");
		window.button("allBookingsButton").requireVisible().requireDisabled().requireText("All Bookings");
	}

	@Test
	public void testWhenGuestInfosAreNonEmptyThenAddGuestButtonShouldBeEnabled() {
		window.tabbedPane("tabbedPane").selectTab("Guests");
		window.textBox("firstNameTextBox").enterText("test");
		window.textBox("lastNameTextBox").enterText("test");
		window.textBox("emailTextBox").setText("test@email.com");
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

		firstNameTextBox.setText(" ");
		lastNameTextBox.setText(" ");
		emailTextBox.setText(" ");
		telephoneNumberTextBox.setText(" ");
		addGuestButton.requireDisabled();

		firstNameTextBox.setText("test");
		lastNameTextBox.setText(" ");
		emailTextBox.setText(" ");
		telephoneNumberTextBox.setText(" ");
		addGuestButton.requireDisabled();

		firstNameTextBox.setText(" ");
		lastNameTextBox.setText("test");
		emailTextBox.setText(" ");
		telephoneNumberTextBox.setText(" ");
		addGuestButton.requireDisabled();

		firstNameTextBox.setText(" ");
		lastNameTextBox.setText(" ");
		emailTextBox.setText("test@email.com");
		telephoneNumberTextBox.setText(" ");
		addGuestButton.requireDisabled();

		firstNameTextBox.setText(" ");
		lastNameTextBox.setText(" ");
		emailTextBox.setText(" ");
		telephoneNumberTextBox.setText("0000000000");
		addGuestButton.requireDisabled();
	}

	@Test
	public void testDeleteGuestButtonShouldBeEnabledOnlyWhenAGuestIsSelected() {
		Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> guesthouseSwingView.getListGuestsModel().addElement(guest));
		window.list("guestsList").selectItem(0);
		window.button("deleteGuestButton").requireEnabled();
		window.list("guestsList").clearSelection();
		window.button("deleteGuestButton").requireDisabled();
	}

	@Test
	public void testsShowAllGuestShouldAddGuestInfosToTheList() {
		Guest guest1 = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000");
		Guest guest2 = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> guesthouseSwingView.showAllGuests(Arrays.asList(guest1, guest2)));
		String[] guestsListContent = window.list("guestsList").contents();
		assertThat(guestsListContent).containsExactly(
				"1 - testFirstName1 - testLastName1 - test1@email.com - 0000000000",
				"2 - testFirstName2 - testLastName2 - test2@email.com - 1111111111");
	}

	@Test
	public void testGuestAddedShouldBeAddedToTheGuestListAndClearTheErrorLogLabel() {
		Guest guestToAdd = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> guesthouseSwingView.guestAdded(guestToAdd));
		String[] guestsListContent = window.list("guestsList").contents();
		assertThat(guestsListContent).containsExactly("1 - testFirstName - testLastName - test@email.com - 0000000000");
		window.label("errorLogMessageLabel").requireText(" ");
	}

	@Test
	public void testGuestRemovedShouldRemoveTheGuestFromTheListAndClearTheErrorLogLabel() {
		Guest guestToRemove = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000");
		Guest anotherGuest = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Guest> listGuestsModel = guesthouseSwingView.getListGuestsModel();
			listGuestsModel.addElement(guestToRemove);
			listGuestsModel.addElement(anotherGuest);
		});
		GuiActionRunner.execute(() -> guesthouseSwingView
				.guestRemoved(new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000")));
		String[] guestsListContent = window.list("guestsList").contents();
		assertThat(guestsListContent)
				.containsExactly("2 - testFirstName2 - testLastName2 - test2@email.com - 1111111111");
		window.label("errorLogMessageLabel").requireText(" ");
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLog() {
		GuiActionRunner.execute(() -> guesthouseSwingView.showError("Error message test"));
		window.label("errorLogMessageLabel").requireText("Error message test");
	}

	@Test
	public void testShowErrorGuestNotFound() {
		Guest guestNoLongerPresent = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000");
		Guest anotherGuest = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Guest> listGuestsModel = guesthouseSwingView.getListGuestsModel();
			listGuestsModel.addElement(guestNoLongerPresent);
			listGuestsModel.addElement(anotherGuest);
		});
		GuiActionRunner
				.execute(() -> guesthouseSwingView.showErrorGuestNotFound("Error message test", guestNoLongerPresent));
		window.label("errorLogMessageLabel").requireText("Error message test: 1 - testFirstName1 - testLastName1");
		String[] guestsListContent = window.list("guestsList").contents();
		assertThat(guestsListContent)
				.containsExactly("2 - testFirstName2 - testLastName2 - test2@email.com - 1111111111");
	}

	@Test
	public void testAddGuestShouldDelegateToGuestControllerNewGuest() {
		window.tabbedPane("tabbedPane").selectTab("Guests");
		window.textBox("firstNameTextBox").enterText("testFirstName");
		window.textBox("lastNameTextBox").enterText("testLastName");
		window.textBox("emailTextBox").setText("test@email.com");
		window.textBox("telephoneNumberTextBox").enterText("0000000000");
		window.button("addGuestButton").click();
		verify(guestController).newGuest(new Guest("testFirstName", "testLastName", "test@email.com", "0000000000"));
	}

	@Test
	public void testDeleteGuestShouldDelegateToGuestControllerDeleteGuest() {
		Guest guestToDelete = new Guest("1", "testFirstName1", "testLastName1", "test1@email.com", "0000000000");
		Guest anotherGuest = new Guest("2", "testFirstName2", "testLastName2", "test2@email.com", "1111111111");
		window.tabbedPane("tabbedPane").selectTab("Guests");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Guest> listGuestsModel = guesthouseSwingView.getListGuestsModel();
			listGuestsModel.addElement(guestToDelete);
			listGuestsModel.addElement(anotherGuest);
		});
		window.list("guestsList").selectItem(0);
		window.button("deleteGuestButton").click();
		verify(guestController).deleteGuest(guestToDelete);
	}

	@Test
	public void testWhenBookingInfosAreSetThenAddBookingButtonShouldBeEnabled() {
		Guest guest = new Guest("1", "testFirstName", "testLastName", "test@email.com", "0000000000");
		GuiActionRunner.execute(() -> guesthouseSwingView.getComboBoxGuestsModel().addElement(guest));
		window.tabbedPane("tabbedPane").selectTab("Bookings");
		window.textBox("checkInDateTextBox").enterText("04-04-2021");
		window.textBox("checkOutDateTextBox").enterText("07-04-2021");
		window.comboBox("numberOfGuestsComBox").selectItem("3");
		window.comboBox("roomComBox").selectItem("TRIPLE");
		window.comboBox("guestIdComBox").selectItem(0);
		window.button("addBookingButton").requireEnabled();
	}

}
