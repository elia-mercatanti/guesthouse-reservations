package com.eliamercatanti.guesthousebooking.model;

public class Guest {

	private String id;
	private String name;
	private String surname;
	private String email;
	private String telephoneNumber;

	public Guest() {
	}

	public Guest(String id, String name, String surname, String email, String telephoneNumber) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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

}
