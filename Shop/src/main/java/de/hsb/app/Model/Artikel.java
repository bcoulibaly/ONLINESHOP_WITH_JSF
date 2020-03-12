package de.hsb.app.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQuery(name = "SelectArtikel", query = "Select a from Artikel a")
@Entity
@Table(name="artikel")
public class Artikel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Artikel_ID", unique = true)
	private Integer id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "BESCHREIBUNG")
	private String beschreibung;
	@Column(name = "PREIS")
	private double preis;
	@Column(name = "TOTAL_ANZAHL")
	private int anzahl;
	
	private int kaufAnzahl=1;
	
	@ManyToOne(fetch = FetchType.LAZY )
	User user;

	@Column(name = "IMAGE")
	private String imgName;

	public Artikel() {
	}

	public Artikel(String name, String beschreibung, double preis, String image, int anzahl) {
		this.name = name;
		this.beschreibung = beschreibung;
		this.preis = preis;
		this.imgName = image;
		this.anzahl = anzahl;
		this.user = null;
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
		return this.preis * this.getKaufAnzahl();
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

	public int getKaufAnzahl() {
		return kaufAnzahl;
	}

	public void setKaufAnzahl(int kaufAnzahl) {
		this.kaufAnzahl = kaufAnzahl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Artikel )) return false;
	        return id != null && id.equals(((Artikel) o).getId());
	    }
	 
	 @Override
	    public int hashCode() {
	        return 62;
	    }
}
