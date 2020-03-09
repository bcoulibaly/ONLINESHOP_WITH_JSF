package de.hsb.app.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

import de.hsb.app.Model.Artikel;

@ManagedBean(name = "artikelHandler", eager = true)
@SessionScoped
public class ArtikelHandler {

	@PersistenceContext
	private EntityManager entityManager;
	@Resource
	private UserTransaction artikelTransaction;
	
	@ManagedProperty(value = "#{shop.artikelList}")
	private DataModel<Artikel> artikelListe;

	private Artikel merkeArtikel;

	private UploadedFile uploadedFile;

//	@PostConstruct
//	public void init() {
//		artikelListe = new ListDataModel<Artikel>();
//		artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());
//	}

//	@PostConstruct
//	public void init() {
//		try {
//			userTransaction.begin();
//
//			entityManager.persist(new Artikel("Surface Book 2", "Das sollte eine Beschribung sein", 2999.99,
//					"Microsoft_Surface_Laptop_2.jpg", 12));
//			entityManager.persist(
//					new Artikel("IPhone 7", "Tolles Handy mit hohem Display auflösung", 689.99, "Iphone7.jpg", 19));
//			entityManager.persist(
//					new Artikel("Canon Photo", "Apparat Photo von Canon mit tollen Effekten wie Panorama,Zeitraffler, Scharf, 8GB-Speicherplatz", 259.99, "canon_photo.png", 40));
//			
//			entityManager.persist(
//					new Artikel("Apple Monitor", "Letzte Generation Monitor von Apple mit 3258*2500 Auflösung, 4HDMI, VGA, 4USB 3.0, integrierte Front-Kamera", 1099.99, "apple-Monitor.png", 40));
//			
//			entityManager.persist(
//					new Artikel("Arduino Board ", "Mini-Computer Arduino neueste Generation mit Kamera-Anschlüss, Microphone", 69.99, "ArduinoBoard.jpg", 40));
//		
//			entityManager.persist(
//					new Artikel("Gaming Mouse", "Sensibele Gaming Mouse von ??, Empfindlichkeit bis 4 Stufe Einstellbar, Extra Knöpfe an den Seiten fuer ein gemuetliches Surfen/ Spiel", 49.99, "Gaming-mouse.jpg", 40));
//	
//			entityManager.persist(
//					new Artikel("Gaming Tastaur", "Gaming Tastatur füe ein entspanntes Erlebnis beim Spielen oder Schreiben, Soundlos, mit Kabel", 79.99, "Gaming Tastatur USB.jpg", 40));
//		
//			entityManager.persist(
//					new Artikel("JBL Lautsprecher Xtrem", "Lautsprecher von JBL Xtrem, eigetlich kann man es für eine Party benutzen, weil es zu Laut ist", 259.99, "jbl-sound-xtrem.jpg", 40));
//		
//			entityManager.persist(
//					new Artikel("Monitor Benq", "Monitor vonb Benq, 3D, 3 USB-Anschluesse, 4KUHD, 3 HDMI-Anschluesse, VGA, 3258*2500 Auflösung", 799.00, "monitor_Benq.jpg", 40));
//			
//			entityManager.persist(
//					new Artikel("Sony Camera S5248", "Sony Kamera mit tollen Effekten wie Panorama, Zeitraffler, slow motion, Portrait, 16GB-Speicherplatz", 499.99, "sony-camera.png", 40));
//			
//			entityManager.persist(
//					new Artikel("Surface Arc Mouse", "Surface Mouse von Microsoft, angenehme Führung des Mauses und Geschwingkeit bis 5mal einstellen, Das Maus ist von Form her verstellbar", 99.99, "Surface Arc Mouse.jpg", 40));
//			
//			artikelListe = new ListDataModel<Artikel>();
//			artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());
//
//			userTransaction.commit();
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
//				| HeuristicMixedException | HeuristicRollbackException e) {
//			e.printStackTrace();
//		}
//	}

	public String neuArtikel() {
		merkeArtikel = new Artikel();
		return "/neuerArtikel?faces-redirect=true";
	}

	public String artikelBearbeiten() {
		merkeArtikel = artikelListe.getRowData();
		return "/artikelBearbeiten?faces-redirect=true";
	}

	public String speichernArtikel() {
		try {
			artikelTransaction.begin();
			merkeArtikel = entityManager.merge(merkeArtikel);
			entityManager.persist(merkeArtikel);
			entityManager.flush();
			updateArtikelList();
			artikelTransaction.commit();

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin?faces-redirect=true";
	}

	public String löschen() {
		try {
			merkeArtikel = artikelListe.getRowData();
			artikelTransaction.begin();
			merkeArtikel = entityManager.merge(merkeArtikel);
			entityManager.remove(merkeArtikel);
			entityManager.flush();
			updateArtikelList();
			artikelTransaction.commit();

			merkeArtikel = null;
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin.xhtml?faces-redirect=true";
	}

	public void saveImage(FileUploadEvent event) {
//		try {
////			System.out.println(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath());
////			String path =FacesContextWrapper.getCurrentInstance().getExternalContext().getApplicationContextPath();
//			Path folder = Paths.get("/resources/IMAGES/ARTIKEL/");
//			uploadedFile = event.getFile();
//			String filename = FilenameUtils.getBaseName(uploadedFile.getFileName());
//			String extension = FilenameUtils.getExtension(uploadedFile.getFileName());
//			InputStream input = uploadedFile.getInputstream();
//	
//			Path file = Files.createTempFile(folder, filename + "-", "." + extension);
//			Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
//			merkeArtikel.setImage(filename+extension);
//			System.out.println("Uploaded file successfully saved in " + file);
//			FacesMessage message = new FacesMessage("Succesfull", uploadedFile.getFileName() + " is uploaded.");
//			FacesContext.getCurrentInstance().addMessage(null, message);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		
		String filename = FilenameUtils.getBaseName(event.getFile().getFileName());
		String extension = FilenameUtils.getExtension(event.getFile().getFileName());
		File result = new File(extContext.getApplicationContextPath()+"/resources/IMAGES/ARTIKEL/"+ event.getFile().getFileName());
		merkeArtikel.setImage(filename+extension);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(result);

			byte[] buffer = new byte[1024];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}

			fileOutputStream.close();
			inputStream.close();

			FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage error = new FacesMessage("The files were  not uploaded!");
			FacesContext.getCurrentInstance().addMessage(null, error);
		}
	}
	
	public void updateArtikelList() {
		artikelListe = new ListDataModel<Artikel>();
		artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public UserTransaction getUserTransaction() {
		return artikelTransaction;
	}

	public void setUserTransaction(UserTransaction userTransaction) {
		this.artikelTransaction = userTransaction;
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
