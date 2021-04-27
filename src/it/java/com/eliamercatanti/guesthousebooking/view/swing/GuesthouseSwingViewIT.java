package com.eliamercatanti.guesthousebooking.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eliamercatanti.guesthousebooking.controller.GuestController;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.eliamercatanti.guesthousebooking.repository.mongo.GuestMongoRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.mongodb.MongoClient;

@DisplayName("Integration Tests for Guesthouse Swing View")
class GuesthouseSwingViewIT {

	private static final String DATABASE_NAME = "guesthouse";
	private static final String GUEST_COLLECTION_NAME = "guest";
	private GuestRepository guestRepository;
	private GuestController guestController;
	private GuesthouseSwingView guesthouseSwingView;
	private InputValidation inputValidation;
	private FrameFixture window;

	@BeforeAll
	static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@BeforeEach
	void onSetUp() {
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		guestRepository = new GuestMongoRepository(new MongoClient("localhost", mongoPort), DATABASE_NAME,
				GUEST_COLLECTION_NAME);
		for (Guest guest : guestRepository.findAll()) {
			guestRepository.delete(guest.getId());
		}
		GuiActionRunner.execute(() -> {
			guesthouseSwingView = new GuesthouseSwingView();
			guestController = new GuestController(guestRepository, guesthouseSwingView, inputValidation);
			guesthouseSwingView.setGuestController(guestController);
			return guesthouseSwingView;
		});
		window = new FrameFixture(guesthouseSwingView);
		window.show();
	}

	@AfterEach
	void tearDown() {
		window.cleanUp();
	}

	@Test
	@DisplayName("All Guest list request - testShowGuests()")
	void testShowAllGuests() throws IOException {
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

	private String getIdSubstring(String id) {
		return id.substring(id.length() / 2);
	}

}
