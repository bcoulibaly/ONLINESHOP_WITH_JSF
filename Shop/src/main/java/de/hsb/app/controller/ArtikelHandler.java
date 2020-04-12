package de.hsb.app.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.event.ComponentSystemEvent;
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

	private Artikel merkeArtikel = new Artikel();

	private UploadedFile uploadedFile;
	private UploadedFile tmpuploadedFile;
	private String fileName="nichts";
	InputStream imageInput;

//	@PostConstruct
//	public void init() {
//		artikelListe = new ListDataModel<Artikel>();
//		artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());
//	}

	public void neuArtikel() {
		merkeArtikel = new Artikel();
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null,
				"/neuerArtikel.xhtml?faces-redirect=true");
	}

	public void artikelBearbeiten() {
		merkeArtikel = artikelListe.getRowData();
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null,
				"/artikelBearbeiten.xhtml?faces-redirect=true");
	}

	public void speichernArtikel() {
		try {
			
			Path folder=Paths.get( FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")+File.separator
					+"resources"+File.separator+"IMAGES"+File.separator+ "ARTIKEL"+File.separator);
			System.out.println(folder.toAbsolutePath().toString());
			
			artikelTransaction.begin();
			merkeArtikel = entityManager.merge(merkeArtikel);
			entityManager.persist(merkeArtikel);
			artikelTransaction.commit();
			updateArtikelList();

		} catch (NotSupportedException | SystemException  | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null,
				"/homePageAdmin.xhtml?faces-redirect=true");
	}

	public void löschen() {
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
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Artikeln",
				"Artikel wurde erfolgreich geloscht"));
		
	}
	
	public void saveFileListener(FileUploadEvent event) {
		uploadedFile= event.getFile();	
		fileName = event.getFile().getFileName();
		saveImage();
		FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void saveImage() {

		String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")+File.separator
				+"resources"+File.separator+"IMAGES"+File.separator+ "ARTIKEL"+File.separator;
		System.out.println(path);

		try {
			OutputStream out = new FileOutputStream(new File(path + uploadedFile.getFileName()));

			int read = 0;
			byte[] bytes = uploadedFile.getContents();

			InputStream in = uploadedFile.getInputstream();
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();
			FacesMessage msg = new FacesMessage("Succesful", uploadedFile.getFileName() + " is uploaded at " +path);
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

	public void updateArtikelnListen(ComponentSystemEvent event) {
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

	public UploadedFile getTmpuploadedFile() {
		return tmpuploadedFile;
	}

	public void setTmpuploadedFile(UploadedFile tmpuploadedFile) {
		this.tmpuploadedFile = tmpuploadedFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public UserTransaction getArtikelTransaction() {
		return artikelTransaction;
	}

	public void setArtikelTransaction(UserTransaction artikelTransaction) {
		this.artikelTransaction = artikelTransaction;
	}

	public InputStream getImageInput() {
		return imageInput;
	}

	public void setImageInput(InputStream imageInput) {
		this.imageInput = imageInput;
	}

}
