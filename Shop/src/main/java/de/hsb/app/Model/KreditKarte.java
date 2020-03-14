package de.hsb.app.Model;

import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

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
	private int ID;
	
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
		this.endDate = getEndDate();
	}
	
	public String getNummer() {
		return this.nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}   
	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
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
	 * Setzt den min Datum, ab wann ein Kreditkarte gueltig ist.
	 * @return
	 */
	public Date getEndDate() {
		LocalDate date = LocalDate.now().plusYears(10);
		this.endDate = new GregorianCalendar(date.getYear(), date.getMonthValue()+1, date.getDayOfMonth()).getTime();
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
   
}
