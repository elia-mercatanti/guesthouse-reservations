package com.eliamercatanti.guesthousebooking.controller;

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.eliamercatanti.guesthousebooking.validation.InputValidation;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

public class GuestController {

	private GuesthouseView guesthouseView;
	private GuestRepository guestRepository;
	private InputValidation inputValidation;

	public void allGuests() {
		guesthouseView.showGuests(guestRepository.findAll());
	}

	public void newGuest(String firstName, String lastName, String email, String telephoneNumber) {
		if ((!inputValidation.validateEmail(email))) {
			guesthouseView.showError("Guest Email is not valid: " + email + ". Format must be like prefix@domain.");
		}
		else if (!inputValidation.validateTelephoneNumber(telephoneNumber)) {
			guesthouseView.showError(
					"Guest Telephone N. is not valid: " + telephoneNumber + ". Format must be like +10000000000.");
		} else {
			Guest guest = new Guest(firstName, lastName, email, telephoneNumber);
			guestRepository.save(guest);
			guesthouseView.guestAdded(guest);
		}

	}

	public void deleteGuest(Guest guest) {
		if (guestRepository.findById(guest.getId()) == null) {
			guesthouseView.showErrorGuestNotFound("There is no guest with id " + guest.getId() + ".", guest);
		} else {
			guestRepository.delete(guest.getId());
			guesthouseView.guestRemoved(guest);
		}
	}

}
