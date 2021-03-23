package com.eliamercatanti.guesthousebooking.controller;

import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

public class GuestController {

	private GuesthouseView guesthouseView;
	private GuestRepository guestRepository;

	public void allGuests() {
		guesthouseView.showAllGuests(guestRepository.findAll());
	}

}
