package de.hsb.app.controller;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

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
import javax.persistence.Query;
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

@ManagedBean(name = "loginHandler")
@SessionScoped
public class Login implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PersistenceContext
	private EntityManager entityManager;
	@Resource
	private UserTransaction userTransaction;

	private String benutzername;
	private String passwort;
	private Kunde user;
	
	private DataModel<Kunde> kundenList;

//	@ManagedProperty(value = "#{kundenHandler}")
//	private KundenHandler kundenHandler;
	
	public Login() {
	}
	
	@PostConstruct
	public void init() {

		try {
			userTransaction.begin();

			entityManager.persist(new Kunde("Ben", "Coulibaly", new GregorianCalendar(1997, 4, 3).getTime(),
					"bcoulibaly", "beniboy", Rolle.ADMIN, Anrede.HERR));
			entityManager.persist(new Kunde("Lionel", "Ngoubayou", new GregorianCalendar(1990, 9, 15).getTime(),
					"lngoubayou", "lgoubayou", Rolle.KUNDE, Anrede.HERR));
			entityManager.persist(new Kunde("Amadou", "Sow", new GregorianCalendar(1994, 5, 21).getTime(), "asow",
					"asow", Rolle.KUNDE, Anrede.HERR));
			kundenList = new ListDataModel<Kunde>();
			kundenList.setWrappedData(entityManager.createNamedQuery("SelectKunden").getResultList());

			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public String login() {

		Query query = entityManager.createQuery("select k from Kunde k "
				+ "where k.benutzername = :benutzername and k.passwort = :passwort ");
		query.setParameter("benutzername", benutzername);
		query.setParameter("passwort", passwort);
		
		List<Kunde> kunden = query.getResultList();

		if (kunden.size() == 1) {
			user = kunden.get(0);

			if (user.getRolle() == Rolle.ADMIN) {
				return "/homePageAdmin.xhtml?faces-redirect=true";
			} else {
				return "/home.xhtml?faces-redirect=true";
			}

		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Falsches Passwort", "Ihr Passwort oder Benutzername ist falsch"));
			return null;
		}
	}

	public String registrieren() {
		System.out.println("Neue Kunde wird registriert");
		user = new Kunde();
		user.setRolle(Rolle.KUNDE);
		return "/registrieren.xhtml?faces-redirect=true";
	}

	public String log() {
		return "/loginSeite.xhtml?faces-redirect=true";
	}

	@SuppressWarnings("unchecked")
	public String registrierungSpeichern() {
		try {

			userTransaction.begin();

			Query query = entityManager.createQuery(
					"Select k from Kunde k " + "where k.benutzername = :username and k.passwort = :passwort ");
			query.setParameter("username", user.getBenutzername());
			query.setParameter("passwort", user.getPasswort());

			List<Kunde> tmpKundeList = query.getResultList();

			if (tmpKundeList.size() == 0 || tmpKundeList == null) {
				user = entityManager.merge(user);
				entityManager.persist(user);
				kundenList.setWrappedData(entityManager.createNamedQuery("SelectKunden")
						.getResultList());
			} else
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"benutzername", "Ihr Passwort oder Benutzername sind bereit Vorhanden"));

			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		user = null;
		return "/loginSeite.xhtml?faces-redirect=true";
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}

	public String checkLoggedIn() {
		if (user != null) {
			if (user.getRolle() == Rolle.ADMIN)
				return "/homePageAdmin.xhtml?faces-redirect=true";
			else
				return "/home.xhtml?faces-redirect=true";
		} else
			return "/loginSeite.xhtml?faces-redirect=true";
	}

	public String logout() {
		user = null;
		return "/loginSeite.xhtml?faces-redirect=true";
	}
	
	public String bearbeitungSpeichern() {
		try {
			System.out.println("Speichern wurde aufgerufen");
			userTransaction.begin();
			user = entityManager.merge(user);
			entityManager.persist(user);
			kundenList.setWrappedData(entityManager.createNamedQuery("SelectKunden").getResultList());

			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin.xhtml?faces-redirect=true";
	}
	
	public String abbrechen() {
		if (user != null)
			if (user.getRolle() == Rolle.ADMIN)
				return "/homePageAdmin.xhtml?faces-redirect=true";
			else
				return "/home.xhtml?faces-redirect=true";
		else
			return "/shopView,.xhtml?faces-redirect=true";
	}

	public EntityManager getEm() {
		return entityManager;
	}

	public void setEm(EntityManager em) {
		this.entityManager = em;
	}

	public String getBenutzername() {
		return benutzername;
	}

	public void setBenutzername(String username) {
		this.benutzername = username;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public Kunde getUser() {
		return user;
	}

	public void setUser(Kunde user) {
		this.user = user;
	}

	public DataModel<Kunde> getKundenList() {
		return this.kundenList;
	}

	public void setKundenList(DataModel<Kunde> kunden) {
		this.kundenList = kunden;
	}

}
