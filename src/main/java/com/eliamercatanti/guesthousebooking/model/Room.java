package com.eliamercatanti.guesthousebooking.model;

public enum Room {

	SINGLE(1), DOUBLE(2), TRIPLE(3), QUADRUPLE(4);

	private int numberOfBeds;

	Room(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public int getNumberOfBeds() {
		return numberOfBeds;
	}

}
