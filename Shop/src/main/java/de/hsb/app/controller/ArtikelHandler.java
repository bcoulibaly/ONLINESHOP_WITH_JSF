package de.hsb.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.primefaces.model.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

import de.hsb.app.Model.Artikel;
import de.hsb.app.Model.Kunde;
import de.hsb.app.Model.Rolle;

@ManagedBean
@SessionScoped
public class ArtikelHandler {

	@PersistenceContext
	private EntityManager entityManager;
	@Resource
	private UserTransaction userTransaction;

	private DataModel<Artikel> artikelListe;
	private Artikel merkeArtikel;
	private UploadedFile uploadedFile;

	@PostConstruct
	public void init() {
		try {
			userTransaction.begin();

			entityManager.persist(new Artikel("Surface Book 2", "Das sollte eine Beschribung sein", 2999.99,
					"Microsoft_Surface_Laptop_2.jpg", 12));
			entityManager.persist(
					new Artikel("IPhone Xs", "Tolles Handy mit hohem Display auflösung", 189.00, "Handy.png", 19));
			entityManager.persist(
					new Artikel("Canon Photo", "Apparat Photo von Canon mit tollen Effekten wie Panorama,Zeitraffler, Scharf, 8GB-Speicherplatz", 259.99, "canon_photo.png", 40));
			
			entityManager.persist(
					new Artikel("Apple Monitor", "Letzte Generation Monitor von Apple mit 3258*2500 Auflösung, 4HDMI, VGA, 4USB 3.0, integrierte Front-Kamera", 1099.99, "apple-Monitor.png", 40));
			
			entityManager.persist(
					new Artikel("Arduino Board ", "Mini-Computer Arduino neueste Generation mit Kamera-Anschlüss, Microphone", 69.99, "ArduinoBoard.jpg", 40));
		
			entityManager.persist(
					new Artikel("Gaming Mouse", "Sensibele Gaming Mouse von ??, Empfindlichkeit bis 4 Stufe Einstellbar, Extra Knöpfe an den Seiten fuer ein gemuetliches Surfen/ Spiel", 49.99, "Gaming-mouse.jpg", 40));
	
			entityManager.persist(
					new Artikel("Gaming Tastaur", "Gaming Tastatur füe ein entspanntes Erlebnis beim Spielen oder Schreiben, Soundlos, mit Kabel", 79.99, "Gaming Tastatur USB.jpg", 40));
		
			entityManager.persist(
					new Artikel("JBL Lautsprecher Xtrem", "Lautsprecher von JBL Xtrem, eigetlich kann man es für eine Party benutzen, weil es zu Laut ist", 259.99, "jbl-sound-xtrem.jpg", 40));
		
			entityManager.persist(
					new Artikel("Monitor Benq", "Monitor vonb Benq, 3D, 3 USB-Anschluesse, 4KUHD, 3 HDMI-Anschluesse, VGA, 3258*2500 Auflösung", 799.00, "monitor_Benq.jpg", 40));
			
			entityManager.persist(
					new Artikel("Sony Camera S5248", "Sony Kamera mit tollen Effekten wie Panorama, Zeitraffler, slow motion, Portrait, 16GB-Speicherplatz", 499.99, "sony-camera.png", 40));
			
			entityManager.persist(
					new Artikel("Surface Arc Mouse", "Surface Mouse von Microsoft, angenehme Führung des Mauses und Geschwingkeit bis 5mal einstellen, Das Maus ist von Form her verstellbar", 99.99, "Surface Arc Mouse.jpg", 40));
			
			artikelListe = new ListDataModel<Artikel>();
			artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());

			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	public String neuArtikel() {
		merkeArtikel = new Artikel();
		return "/neuerArtikel?faces-redirect=true";
	}

	public String speichernArtikel() {
		try {
			userTransaction.begin();
			saveImage();
			merkeArtikel = entityManager.merge(merkeArtikel);
			entityManager.persist(merkeArtikel);
			artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());

			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin?faces-redirect=true";
	}

	public String abbrechen(Kunde kunde) {
		merkeArtikel = null;
		if (kunde.getRolle() == Rolle.ADMIN) {
			return "/homePageAdmin?faces-redirect=true";
		} else {
			return "/home?faces-redirect=true";
		}
	}

	public String löschen() {
		try {
			userTransaction.begin();

			entityManager.remove(artikelListe.getRowData());
			artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());

			userTransaction.commit();

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin.xhtml?faces-redirect=true";
	}

	public void saveImage() {
		try {
			Path folder = Paths.get("/path/to/uploads");
			String filename = FilenameUtils.getBaseName(uploadedFile.getFileName());
			String extension = FilenameUtils.getExtension(uploadedFile.getFileName());
			InputStream input = uploadedFile.getInputstream();
			Path file = Files.createTempFile(folder, filename + "-", "." + extension);
			Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
			merkeArtikel.setName(filename);
			System.out.println("Uploaded file successfully saved in " + file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FacesMessage message = new FacesMessage("Succesfull", uploadedFile.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public UserTransaction getUserTransaction() {
		return userTransaction;
	}

	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

	public DataModel<Artikel> getArtikelListe() {
		return artikelListe;
	}

	public void setArtikelListe(DataModel<Artikel> artikelListe) {
		this.artikelListe = artikelListe;
	}

	public Artikel getMerkeArtikel() {
		return merkeArtikel;
	}

	public void setMerkeArtikel(Artikel merkeArtikel) {
		this.merkeArtikel = merkeArtikel;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

}
