package com.eliamercatanti.guesthousebooking.view;

import java.util.List;

import com.eliamercatanti.guesthousebooking.controller.GuestController;
import com.eliamercatanti.guesthousebooking.model.Guest;

public interface GuesthouseView {

	public void showAllGuests(List<Guest> guests);

	public void guestAdded(Guest guest);

	public void guestRemoved(Guest guest);

	public void errorGuestNotFound(String message, Guest guest);

	public void setGuestController(GuestController guestController);

}
