package de.hsb.app.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Named
@RequestScoped
public class Credential {
	@NotNull
	@NotEmpty
	private String benutzername;
	@NotNull
	@NotEmpty
	private String passwort;
		
	
	public String getUsername() {
		return benutzername;
	}

	public void setUsername(String username) {
		this.benutzername = username;
	}

	
	public String getPassword() {
		return passwort;
	}

	public void setPassword(String password) {
		this.passwort = password;
	}
}