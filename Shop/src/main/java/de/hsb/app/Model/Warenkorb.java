package de.hsb.app.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Warenkorb {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Warenkorb_ID", nullable = false, unique = true)
	private int id;
	
	@OneToMany(orphanRemoval=true, fetch = FetchType.EAGER)
    @JoinColumn(name="User_ID")
	private List<Artikel> artikels;
	
	@Column(name = "Gesamt_Preis")
	private double gesamtPreis;
	
	@Column(name = "TOTAL_ARTIKEL")
	private int totalArtikel;
	
	
	Warenkorb(){
		totalArtikel = 0;
		gesamtPreis = 0.0;
		artikels = new ArrayList<Artikel>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Artikel> getArtikels() {
		return this.artikels;
	}

	public void setArtikels(List<Artikel> warenkorb) {
		this.artikels = warenkorb;
	}

	public double getGesamtPreis() {
		updateValue();
		return this.gesamtPreis;
	}

	public void setGesamtPreis(double gesamtPreis) {
		this.gesamtPreis = gesamtPreis;
	}

	public int getTotalArtikel() {
		this.totalArtikel = this.artikels.size();
		return this.totalArtikel;
	}

	public void setTotalArtikel(int totalArtikel) {
		this.totalArtikel = totalArtikel;
	}
	
	private void updateValue() {
		for (Artikel artikel : artikels) {
			this.gesamtPreis += artikel.getPreis();
		}
	}
	
	
}
