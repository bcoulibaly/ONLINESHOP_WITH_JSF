package de.hsb.app.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Named
@RequestScoped
public class Credential {
	private String benutzername;
	private String passwort;
		
	@NotNull
	public String getUsername() {
		return benutzername;
	}

	public void setUsername(String username) {
		this.benutzername = username;
	}

	@NotNull
	public String getPassword() {
		return passwort;
	}

	public void setPassword(String password) {
		this.passwort = password;
	}
}