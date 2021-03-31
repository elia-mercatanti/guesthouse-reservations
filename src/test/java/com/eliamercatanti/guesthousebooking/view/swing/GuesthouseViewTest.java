package com.eliamercatanti.guesthousebooking.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import com.eliamercatanti.guesthousebooking.controller.GuestController;

public class GuesthouseViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private GuesthouseSwingView guesthouseSwingView;

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
	public void testControlsInitialStatesOfGuestsTab() {
		window.label("errorLogLabel").requireVisible().requireEnabled().requireText("Error Log:");
		window.label("errorLogMessageLabel").requireEnabled().requireText(" ");
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
		window.table("guestsTable").requireVisible().requireEnabled();
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
		window.table("bookingsTable").requireVisible().requireEnabled();
		window.button("deleteBookingButton").requireVisible().requireDisabled().requireText("Delete Booking");
		window.button("allBookingsButton").requireVisible().requireDisabled().requireText("All Bookings");
	}

	@Test
	public void testWhenGuestInfosAreNonEmptyThenAddGuestButtonShouldBeEnabled() {
		window.textBox("firstNameTextBox").enterText("test");
		window.textBox("lastNameTextBox").enterText("test");
		window.textBox("emailTextBox").setText("test@email.com");
		window.textBox("telephoneNumberTextBox").enterText("0000000000");
		window.button("addGuestButton").requireEnabled();
	}
	
	@Test
	public void testWhenSomeGuestInfosAreBlankThenAddGuestButtonShouldBeDisabled() {
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
		emailTextBox.setText("email.com");
		telephoneNumberTextBox.setText(" ");
		addGuestButton.requireDisabled();
		
		firstNameTextBox.setText(" ");
		lastNameTextBox.setText(" ");
		emailTextBox.setText(" ");
		telephoneNumberTextBox.setText("0000000000");
		addGuestButton.requireDisabled();
	}

}
