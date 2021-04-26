package com.eliamercatanti.guesthousebooking.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.eliamercatanti.guesthousebooking.repository.mongo.GuestMongoRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.eliamercatanti.guesthousebooking.validation.controller.ControllerInputValidator;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;
import com.mongodb.MongoClient;

@ExtendWith(MockitoExtension.class)
@DisplayName("Integration Tests for Guest Controller")
class GuestControllerIT {

	private static final String DATABASE_NAME = "guesthouse";
	private static final String COLLECTION_NAME = "guest";
	private GuestRepository guestRepository;
	@InjectMocks
	private GuestController guestController;
	@Mock
	private GuesthouseView guesthouseView;
	private InputValidation inputValidator;

	@BeforeEach
	public void setUp() {
		int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
		guestRepository = new GuestMongoRepository(new MongoClient("localhost", mongoPort), DATABASE_NAME,
				COLLECTION_NAME);
		inputValidator = new ControllerInputValidator();
		for (Guest guest : guestRepository.findAll()) {
			guestRepository.delete(guest.getId());
		}
		guestController = new GuestController(guestRepository, guesthouseView, inputValidator);
	}

	@Test
	@DisplayName("All Guests list request - testAllGuests()")
	void testAllGuests() {
		Guest guest1 = new Guest("testFirstName1", "testLastName1", "test1@email.com", "1111111111");
		Guest guest2 = new Guest("testFirstName2", "testLastName2", "test2@email.com", "2222222222");
		guestRepository.save(guest1);
		guestRepository.save(guest2);
		guestController.allGuests();
		verify(guesthouseView).showGuests(Arrays.asList(guest1, guest2));
		verifyNoMoreInteractions(guesthouseView);
	}

	@Test
	@DisplayName("New guest request - testNewGuest()")
	void testNewGuest() {
		guestController.newGuest("testFirstName", "testLastName", "test@email.com", "1234567890");
		Guest newGuest = guestRepository.findAll().get(0);
		verify(guesthouseView).guestAdded(newGuest);
		verifyNoMoreInteractions(guesthouseView);
	}

	@Test
	@DisplayName("Delete guest request - testDeleteGuest()")
	void testDeleteGuest() {
		Guest guestToDelete = new Guest(new ObjectId().toString(), "testFirstName", "testLastName", "test@email.com",
				"0000000000");
		guestRepository.save(guestToDelete);
		guestController.deleteGuest(guestToDelete);
		verify(guesthouseView).guestRemoved(guestToDelete);
		verifyNoMoreInteractions(guesthouseView);
	}

}
