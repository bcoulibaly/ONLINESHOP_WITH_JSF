package de.hsb.app.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity implementation class for Entity: KreditKarte
 *
 */
@Entity
public class KreditKarte {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "KK_ID")
	private int ID;
	@Column(name = "NUMMER", length = 12 )
	private String nummer;
	@Column(name = "ART")
	private KarteArt karteArt;
	@Column(name = "CODE", length = 3)
	private String code;

	public KreditKarte() {
		nummer = "";
		code = "";
		karteArt = KarteArt.MASTERCARD;
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
   
}
