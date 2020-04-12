package de.hsb.app.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import de.hsb.app.util.KarteArt;

/**
 * Entity implementation class for Entity: KreditKarte
 *
 */
@Entity
@Table(name = "kreditkarte")
public class KreditKarte {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true)
	private Long ID;
	
	@Column(name = "NUMMER")
	@Size(min = 12, max = 16)
	private String nummer;
	
	@Column(name = "ART")
	private KarteArt karteArt;
	
	@Column(name = "CODE",length = 3)
	private String code;
	
	@Future
	@Temporal(TemporalType.DATE)
	@Column(name = "GUELTIG_BIS")
	private Date endDate;
	
	@OneToOne(mappedBy = "kreditKarte")
	User user;

	public KreditKarte() {
	}   
	
	public KreditKarte(String nummer, String code, KarteArt art) {
		this.nummer = nummer;
		this.code = code;
		this.karteArt = art;
	}
	
	public String getNummer() {
		return this.nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}   
	public long getID() {
		return this.ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}   
	public KarteArt getKarteArt() {
		return this.karteArt;
	}

	public void setKarteArt(KarteArt karteArt) {
		this.karteArt = karteArt;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Setzt den max Datum, bis wann ein Kreditkarte gueltig ist.
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object obj) {

		if (this == null || obj == null) {
			return false;
		} else if (this.getClass() == obj.getClass()) {
			if ((this.ID == ((KreditKarte) obj).ID)
					&& ((this.nummer == ((KreditKarte) obj).nummer) && (this.code == ((KreditKarte) obj).code))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
    }
   
}
