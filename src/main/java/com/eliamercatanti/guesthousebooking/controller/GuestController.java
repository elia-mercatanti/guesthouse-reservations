package com.eliamercatanti.guesthousebooking.controller;

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

public class GuestController {

	private GuesthouseView guesthouseView;
	private GuestRepository guestRepository;

	public void allGuests() {
		guesthouseView.showAllGuests(guestRepository.findAll());
	}

	public void newGuest(Guest guest) {
		guestRepository.save(guest);
		guesthouseView.guestAdded(guest);
	}

	public void deleteGuest(Guest guest) {
		if (guestRepository.findById(guest.getId()) == null) {
			guesthouseView.showErrorGuestNotFound("There is no guest with id " + guest.getId() + ".", guest);
		} else {
			guestRepository.delete(guest.getId());
			guesthouseView.guestRemoved(guest);
		}
	}

	public void newGuest(String firstName, String lastName, String email, String telephoneNumber) {
		Guest guest = new Guest(firstName, lastName, email, telephoneNumber);
		guestRepository.save(guest);
		guesthouseView.guestAdded(guest);
	}

}
