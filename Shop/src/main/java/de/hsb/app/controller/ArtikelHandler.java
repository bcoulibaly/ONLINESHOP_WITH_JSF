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

			entityManager.persist(new Artikel("artikel1", "Das sollte eine Beschribung sein", 154.00,
					"Microsoft_Surface_Laptop_2.jpg", 12));
			entityManager.persist(new Artikel("Artikel2", "Das sollte eben auch eine Beschreibung sein", 189.00,
					"Handy.png", 19));
			entityManager.persist(new Artikel("Artikel3", "Auch eine Beschreibung des Artikels 3", 499.00,
					"canon_photo.png", 40));
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
		return "neuerArtikel";
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
		return "homePageAdmin";
	}
	
	public String abbrechen(Kunde kunde) {
		merkeArtikel = null;
		if (kunde.getRolle() == Rolle.ADMIN) {
			return "homePageAdmin";
		} else {
			return "home";
		}
	}
	
	public void saveImage() {
		try{
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
		
        FacesMessage message = new FacesMessage("Succesful", uploadedFile.getFileName() + " is uploaded.");
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
