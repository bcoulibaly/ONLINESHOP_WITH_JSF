package de.hsb.app.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import de.hsb.app.Model.Kunde;
import de.hsb.app.Model.Rolle;

@ManagedBean(name = "loginHandler")
@RequestScoped
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
	
	@ManagedProperty(value = "#{kundenHandler.kundenListe}")
	private DataModel<Kunde> kunden;
	
//	@ManagedProperty(value = "#{kundenHandler}")
//	private KundenHandler kundenHandler;
//	
//	
	
	public Login() {
	
	}
	

	@SuppressWarnings("unchecked")
	public String login() {
		
		Query query = entityManager
				.createQuery("Select k from Kunde k " + "where k.benutzername = :username and k.passwort = :passwort ");
		query.setParameter("username", benutzername);
		query.setParameter("passwort", passwort);

		if (kunden.isRowAvailable()) {
			user = kunden.getRowData();
//			merkeKunde.setArtikelDaten(new ArrayList<Artikel>());
		}

		System.out.println(benutzername + " " + passwort);

		List<Kunde> kunden = query.getResultList();
		if (kunden.size() == 1) {
			user = kunden.get(0);
			
			if (user.getRolle() == Rolle.ADMIN) {
				return "homePageAdmin";
			} else {
				return "home";
			}

		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Falsches Passwort",
							"Ihr Passwort oder Benutzername ist falsch"));
			return null;
		}
	}
	
	public String checkLoggedIn() {
		if(user!=null) {
			if(user.getRolle() == Rolle.ADMIN)
				return "homePageAdmin";
			else
				return "shop";
		}
		else
			return "loginSeite";
	}
	
	public String loggout() {
		user = null;
		return "loginSeite";
	}
	
	public String abbrechen() {
		return "";
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

	public DataModel<Kunde> getKunden() {
		return this.kunden;
	}

	public void setKunden(DataModel<Kunde> kunden) {
		this.kunden = kunden;
	}

//	public KundenHandler getKundenHandler() {
//		return kundenHandler;
//	}
//
//	public void setKundenHandler(KundenHandler kundenHandler) {
//		this.kundenHandler = kundenHandler;
//	}

}
