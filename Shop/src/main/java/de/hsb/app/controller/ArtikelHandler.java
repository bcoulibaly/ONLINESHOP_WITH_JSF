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
import javax.faces.event.ActionListener;
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

@ManagedBean(name = "artikelHandler")
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
			artikelTransaction.commit();
			updateArtikelList();

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
			artikelTransaction.commit();
			updateArtikelList();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin.xhtml?faces-redirect=true";
	}

	public void saveImage(FileUploadEvent event) {
		
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();		
		String filename = FilenameUtils.getBaseName(event.getFile().getFileName());
		String extension = FilenameUtils.getExtension(event.getFile().getFileName());
		File result = new File(extContext.getApplicationContextPath()+"/resources/IMAGES/ARTIKEL/"+ event.getFile().getFileName());
		merkeArtikel.setImgName(filename+extension);
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
	
	public void updateArtikelnList(ActionListener event) {
		artikelListe = new ListDataModel<Artikel>();
		artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());
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
