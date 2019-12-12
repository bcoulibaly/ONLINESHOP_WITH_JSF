package de.hsb.app.controller;

public class Credentials {

	protected static String username;
	protected static String password;

	public String getUsername() {
		return Credentials.username;
	}

	public void setUsername(String username) {
		Credentials.username = username;
	}

	public String getPassword() {
		return Credentials.password;
	}

	public void setPassword(String password) {
		Credentials.password = password;
	}

}