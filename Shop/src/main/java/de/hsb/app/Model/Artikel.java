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
	private Long id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "BESCHREIBUNG")
	private String beschreibung;
	@Column(name = "PREIS")
	private double preis;
	@Column(name = "TOTAL_ANZAHL")
	private long anzahl;
	
	private long kaufAnzahl=1;
	
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

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAnzahl() {
		return this.anzahl;
	}

	public void setAnzahl(long anzahl) {
		this.anzahl = anzahl;
	}

	public double getPreis() {
		return this.preis;
	}

	public void setPreis(double preis) {
		this.preis = preis;
	}
	
	public int getPreisxnumber() {
		return (int) (this.getPreis() * this.getKaufAnzahl());
	}

	public long getKaufAnzahl() {
		return kaufAnzahl;
	}

	public void setKaufAnzahl(long kaufAnzahl) {
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
    public boolean equals(Object obj) {
//        if (this == o) return true;
//        if (!(o instanceof Artikel )) return false;
//        return id != null && id.equals(((Artikel) o).getId());
		if (this == null || obj == null) {
			return false;
		} else if (this.getClass() == obj.getClass()) {
			if ((this.id == ((Artikel) obj).id)
					&& ((this.name == ((Artikel) obj).name) && (this.name == ((Artikel) obj).name))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
    }
	 
	@Override
    public int hashCode() {
        return 62;
    }
}
