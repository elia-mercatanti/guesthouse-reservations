package com.eliamercatanti.guesthousebooking.model;

public class Guest {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String telephoneNumber;

	public Guest() {
	}

	public Guest(String id, String firstName, String lastName, String email, String telephoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Override
	public String toString() {
		return id + " - " + firstName + " - " + lastName + " - " + email + " - " + telephoneNumber;
	}

}
