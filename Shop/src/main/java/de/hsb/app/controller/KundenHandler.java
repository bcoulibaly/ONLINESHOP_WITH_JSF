package de.hsb.app.controller;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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

import org.primefaces.event.FlowEvent;

import de.hsb.app.Model.Anrede;
import de.hsb.app.Model.Kunde;
import de.hsb.app.Model.Rolle;


@ManagedBean(name = "kundenHandler")
@SessionScoped
public class KundenHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4013914896252407461L;
	
	@PersistenceContext
	private EntityManager entityManager;
	@Resource
	private UserTransaction userTransaction;
	
	@ManagedProperty(value ="#{loginHandler.kundenList}")
	private DataModel<Kunde> kundenListe= new ListDataModel<Kunde>();
	
	private Kunde merkeKunde;
	private Kunde selectedUser;
	private boolean skip;
	
	String passwortWiederholen;
	

	public KundenHandler() {
	}

	public String neu() {
		merkeKunde = new Kunde();
		merkeKunde.setRolle(Rolle.ADMIN);
		return "/neueAdminAnlegen?faces-redirect=true";
	}

	public String loggedKundeBearbeiten() {
		return "/loggedKundeBearbeiten.xhtml?faces-redirect=true";
	}
	
	public String normaleKundeBearbeiten() {
		merkeKunde = kundenListe.getRowData();
		return "/KundeBearbeiten.xhtml?faces-redirect=true";
	}

	public String speichern() {
		try {
			System.out.println("Speichern wurde aufgerufen");
			userTransaction.begin();
			merkeKunde = entityManager.merge(merkeKunde);
			entityManager.persist(merkeKunde);
			kundenListe.setWrappedData(entityManager.createNamedQuery("SelectKunden").getResultList());

			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin.xhtml?faces-redirect=true";
	}

	public String löschen() {
		try {
			merkeKunde = kundenListe.getRowData();
			userTransaction.begin();
			merkeKunde = entityManager.merge(merkeKunde);
			entityManager.remove(merkeKunde);
			kundenListe = new ListDataModel<Kunde>();
			kundenListe.setWrappedData(entityManager.createNamedQuery("SelectKunden").getResultList());
			kundenListe.notifyAll();
			userTransaction.commit();
			userTransaction.notifyAll();
			merkeKunde = null;

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin.xhtml?faces-redirect=true";

	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}

	public Rolle[] getRolleValues() {
		return Rolle.values();
	}

	public Anrede[] getAnredeValues() {
		return Anrede.values();
	}

	public DataModel<Kunde> getKundenListe() {
		return kundenListe;
	}

	public void setKundenListe(DataModel<Kunde> kundenListe) {
		this.kundenListe = kundenListe;
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

	public Kunde getMerkeKunde() {
		return merkeKunde;
	}

	public void setMerkeKunde(Kunde merkeKunde) {
		this.merkeKunde = merkeKunde;
	}

	public Kunde getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Kunde actuelUser) {
		this.selectedUser = actuelUser;
	}
	
	public String getPasswortWiederholen() {
		return passwortWiederholen;
	}

	public void setPasswortWiederholen(String passwortWiederholen) {
		this.passwortWiederholen = passwortWiederholen;
	}

}
