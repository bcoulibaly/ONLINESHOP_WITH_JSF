package de.hsb.app.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
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
import de.hsb.app.Model.Artikel;
import de.hsb.app.Model.KarteArt;
import de.hsb.app.Model.KreditKarte;
import de.hsb.app.Model.Rolle;
import de.hsb.app.Model.User;
import de.hsb.app.Model.Warenkorb;
import de.hsb.app.util.Resources.LoggedIn;

@ManagedBean(name = "loginHandler")
@SessionScoped
public class UserHandler implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 7014613660083721772L;

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserTransaction userTransaction;

	@Inject
	Credential credential;

	private User user;

	private DataModel<User> kundenList;

	@Inject
	private FacesContext context;
	
	private KreditKarte kreditKarte;
	private Artikel merkeArtikel;
	private User merkeKunde;
	private Warenkorb tmpWarenKorb;
	private boolean skip;

	public UserHandler() {
	}

	@PostConstruct
	public void init() {
		kundenList = new ListDataModel<User>();
		kundenList.setWrappedData(entityManager.createNamedQuery("SelectUser").getResultList());
	}

	public void login() {

		Query query = entityManager.createQuery(
				"select k from User k " + "where k.benutzername = :benutzername and k.passwort = :passwort ");
		query.setParameter("benutzername", credential.getUsername());
		query.setParameter("passwort", credential.getPassword());

		@SuppressWarnings("unchecked")
		List<User> kunden = query.getResultList();

		if (kunden.size() == 1) {
			user = kunden.get(0);
			tmpWarenKorb = user.getWarenkorb();
			if (user.getRolle() == Rolle.ADMIN) {
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/homePageAdmin.xhtml?faces-redirect=true");
			} else {
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/home.xhtml?faces-redirect=true");
//				return "/home.xhtml?faces-redirect=true";
			}

		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falsches Passwort",
					"Ihr Passwort oder Benutzername ist falsch"));
		}
	}

	/** normal Kunde registrieren **/
	public String registrieren() {
		System.out.println("Neue Kunde wird registriert");
		merkeKunde = new User();
		merkeKunde.setRolle(Rolle.KUNDE);
		kreditKarte = new KreditKarte();
		return "/registrieren.xhtml?faces-redirect=true";
	}

	/** Weiterleitung auf die LoginSeite **/
	public String redirectToLoginPage() {
		return "/loginSeite.xhtml?faces-redirect=true";
	}

	@SuppressWarnings("unchecked")
	public String registrierungSpeichern() {
		try {

			userTransaction.begin();

			Query query = entityManager.createQuery(
					"Select k from User k " + "where k.benutzername = :username and k.passwort = :passwort ");
			query.setParameter("username", user.getBenutzername());
			query.setParameter("passwort", user.getPasswort());

			List<User> tmpKundeList = query.getResultList();
			if (tmpKundeList.size() == 0 || tmpKundeList == null) {
				merkeKunde.setKreditKarte(kreditKarte);
				merkeKunde = entityManager.merge(merkeKunde);
				entityManager.persist(merkeKunde);
				entityManager.flush();
				updateUserList();
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

	/**
	 * Das schaltet den Tabs bei der Registrierung oder bei den Daten-Bearbeitung
	 * weiter
	 **/
	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}

	/**
	 * checkt ob den aktuellen Nutzer der Applikation berechtigt ist, die
	 * aufgerufene Seite zu visualiesiren
	 **/
	public void checkLoggedIn(ComponentSystemEvent cse) {
		if (user == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,
					"/loginSeite.xhtml?faces-redirect=true");
		}
	}

	/**
	 * mach es sicher dass die jetzige Sitzung während der Abmeldung prozess des
	 * Benutzer beendet wird
	 **/
	public String logout() {
		user = null;
		merkeKunde = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/shopView.xhtml?faces-redirect=true";
	}

	/**
	 * Weiterleitung zur Seite des bearbeiten der Daten des aktuellen eingelogte
	 * Benutzer
	 **/
	public void loggedKundeBearbeiten() {
		kreditKarte = user.getKreditKarte();
		context.getApplication().getNavigationHandler().handleNavigation(context, null,
				"/loggedKundeBearbeiten.xhtml?faces-redirect=true");
	}

	/** Erstellen eines neuen Benutzer mit dem Status ADMIN **/
	public String neu() {
		merkeKunde = new User();
		merkeKunde.setRolle(Rolle.ADMIN);
		kreditKarte = new KreditKarte();
		return "/neueAdminAnlegen?faces-redirect=true";
	}

	/** Speichert die bearbeitete Daten des aktuellen Benutzer **/
	public String bearbeitungSpeichern() {
		try {
			System.out.println("Speichern wurde aufgerufen");
			userTransaction.begin();
			user.setKreditKarte(kreditKarte);
			user = entityManager.merge(user);
			entityManager.persist(user);
			updateUserList();
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin.xhtml?faces-redirect=true";
	}

	/** Speichern des neuen angelegten ADMIN-Benutzer **/
	public String speichern() {
		try {
			System.out.println("Speichern wurde aufgerufen");
			userTransaction.begin();
			merkeKunde.setKreditKarte(kreditKarte);
			merkeKunde = entityManager.merge(merkeKunde);
			entityManager.persist(merkeKunde);
			userTransaction.commit();
			updateUserList();
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

	public String normaleKundeBearbeiten() {
		merkeKunde = kundenList.getRowData();
		if (merkeKunde.getKreditKarte()== null || (merkeKunde.getKreditKarte().getNummer()=="" && merkeKunde.getKreditKarte().getCode()==""))
			kreditKarte = new KreditKarte();
		else
			kreditKarte = merkeKunde.getKreditKarte();
		return "/KundeBearbeiten.xhtml?faces-redirect=true";
	}

	public String löschen() {
		try {
			userTransaction.begin();
			merkeKunde = kundenList.getRowData();
			merkeKunde = entityManager.merge(merkeKunde);
			entityManager.remove(merkeKunde.getKreditKarte());
			entityManager.remove(merkeKunde.getWarenkorb());
			entityManager.remove(merkeKunde);
			userTransaction.commit();
			updateUserList();
			merkeKunde = null;

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return "/homePageAdmin.xhtml?faces-redirect=true";

	}

	public void updateUserList() {
		kundenList = new ListDataModel<User>();
		kundenList.setWrappedData(entityManager.createNamedQuery("SelectUser").getResultList());
	}

	public void artikelGespeichert(ActionEvent actionEvent) {
		context.addMessage(null, new FacesMessage("Daten gespeichert", "Artikel erfolgreich Gespeichert"));
	}

	public void addToCardShop() {
		try {
			userTransaction.begin();
			tmpWarenKorb.getArtikels().add(merkeArtikel);
			tmpWarenKorb.getGesamtPreis();
			tmpWarenKorb.getTotalArtikel();
			user.setWarenkorb(tmpWarenKorb);
			entityManager.merge(tmpWarenKorb);
			entityManager.persist(tmpWarenKorb);
			entityManager.merge(user);
			entityManager.persist(user);
			userTransaction.commit();
			context.addMessage(null, new FacesMessage(null, "Artikel erfolgreich in Warenkorb hinzugefügt"));
			
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}
		int filterInt = getInteger(filterText);

		User car = (User) value;
		return car.getId() == (filterInt) || car.getName().toLowerCase().contains(filterText)
				|| car.getBenutzername().toLowerCase().contains(filterText) || car.getPlz().contains(filterText);
	}

	private int getInteger(String string) {
		try {
			return Integer.valueOf(string);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	public EntityManager getEm() {
		return entityManager;
	}

	public void setEm(EntityManager em) {
		this.entityManager = em;
	}

	@LoggedIn
	@Produces
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DataModel<User> getKundenList() {
		return this.kundenList;
	}

	public void setKundenList(DataModel<User> kunden) {
		this.kundenList = kunden;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public Rolle[] getRolleValues() {
		return Rolle.values();
	}

	public Anrede[] getAnredeValues() {
		return Anrede.values();
	}

	public KarteArt[] getKarteArtValues() {
		return KarteArt.values();
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

	public User getMerkeKunde() {
		return merkeKunde;
	}

	public void setMerkeKunde(User merkeKunde) {
		this.merkeKunde = merkeKunde;
	}

	public Artikel getMerkeArtikel() {
		return merkeArtikel;
	}

	public void setMerkeArtikel(Artikel merkeArtikel) {
		this.merkeArtikel = merkeArtikel;
	}

	public KreditKarte getKreditKarte() {
		return kreditKarte;
	}

	public void setKreditKarte(KreditKarte kreditKarte) {
		this.kreditKarte = kreditKarte;
	}

	public Warenkorb getTmpWarenKorb() {
		return tmpWarenKorb;
	}

	public void setTmpWarenKorb(Warenkorb tmpWarenKorb) {
		this.tmpWarenKorb = tmpWarenKorb;
	}

}
