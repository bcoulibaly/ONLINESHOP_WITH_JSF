package de.hsb.app.Model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@NamedQuery(name="SelectKunden", query="Select k from Kunde k") 
@Entity
public class Kunde implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull
	@Column(name = "Kunde_ID")
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "VORNAME")
	private String vorname;
	
	@Column(name = "PASSWORD")
	private String passwort;
	
	@Size(min = 5, max = 100)
	@Column(name = "STRASSE")
	private String strasse;
	
	@Column(name = "EMAIL")
	@Email
	private String email;

	@Size(min = 3, max = 10)
	@Column(name = "PLZ")
	private String plz;
	
	@Size(min = 3, max = 100)
	@Column(name = "Ort")
	private String ort;
	
	@Size(min = 3, max = 100)
	@Column(name = "BENUTZERNAME")
	private String benutzername;
	
	@Column(name = "ANREDE")
	private Anrede anrede;
	
	
	@Column(name = "ROLLE")
	private Rolle rolle;
	
	@Past
	@Temporal(TemporalType.DATE)
	@Column(name = "GEBURTSDATUM")
	private Date geburtsdatum;
	
	//beim Registrieren oder Daten Änderungen wird das überprüft
	String passwortWiederholen;
	
	public Kunde() {
	
	}
	
	public Kunde(String vorname, String name, Date geburtsdatum, String benutzername, String password, Rolle rolle, Anrede anrede) {
		this.name = name;
		this.vorname = vorname;
		this.geburtsdatum = geburtsdatum;
		this.benutzername = benutzername;
		this.passwort = password;
		this.rolle = rolle;
		this.anrede= anrede;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public Date getGeburtsdatum() {
		return this.geburtsdatum;
	}

	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setPasswort(String password) {
		this.passwort = password;
	}

	public String getBenutzername() {
		return this.benutzername;
	}

	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}

	public Rolle getRolle() {
		return rolle;
	}

	public void setRolle(Rolle rolle) {
		this.rolle = rolle;
	}

	public Anrede getAnrede() {
		return this.anrede;
	}

	public void setAnrede(Anrede anrede) {
		this.anrede = anrede;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}
	
	public String getPasswortWiederholen() {
		return passwortWiederholen;
	}

	public void setPasswortWiederholen(String passwortWiederholen) {
		this.passwortWiederholen = passwortWiederholen;
	}
}
