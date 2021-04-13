package com.eliamercatanti.guesthousebooking.model;

import java.util.Objects;

public class Guest {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String telephoneNumber;

	public Guest(String id, String firstName, String lastName, String email, String telephoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
	}

	public Guest(String firstName, String lastName, String email, String telephoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	@Override
	public String toString() {
		return "Guest [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", telephoneNumber=" + telephoneNumber + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, telephoneNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Guest other = (Guest) obj;
		return Objects.equals(id, other.id) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(telephoneNumber, other.telephoneNumber);
	}

}
