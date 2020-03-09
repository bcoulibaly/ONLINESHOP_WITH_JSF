package de.hsb.app.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Named
@RequestScoped
public class Credential {
	private String benutzername;
	private String passwort;
		
	@NotNull
	@Length(min = 3, max = 25)
	public String getUsername() {
		return benutzername;
	}

	public void setUsername(String username) {
		this.benutzername = username;
	}

	@NotNull
	@Length(min = 3, max = 20)
	public String getPassword() {
		return passwort;
	}

	public void setPassword(String password) {
		this.passwort = password;
	}
}