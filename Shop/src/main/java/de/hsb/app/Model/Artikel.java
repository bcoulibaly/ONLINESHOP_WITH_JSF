package de.hsb.app.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@NamedQuery(name = "SelectArtikel", query = "Select a from Artikel a")
@Entity
public class Artikel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Artikel_ID")
	private Integer id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "BESCHREIBUNG")
	private String beschreibung;
	@Column(name = "PREIS")
	private double preis;
	@Column(name = "ANZAHL")
	private int anzahl;

	@Column(name = "IMAGE")
	String imgName;

	public Artikel() {
	}

	public Artikel(String name, String beschreibung, double preis, String image, int anzahl) {
		this.name = name;
		this.beschreibung = beschreibung;
		this.preis = preis;
		this.imgName = image;
		this.anzahl = anzahl;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeschreibung() {
		return this.beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getAnzahl() {
		return this.anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public double getPreis() {
		return this.preis;
	}

	public void setPreis(double preis) {
		this.preis = preis;
	}

	public String getImage() {
		return this.imgName;
	}

	public void setImage(String image) {
		this.imgName = image;
	}

}
