package de.hsb.app.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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

import de.hsb.app.Model.Artikel;
import de.hsb.app.Model.KreditKarte;
import de.hsb.app.Model.User;
import de.hsb.app.util.Anrede;
import de.hsb.app.util.KarteArt;
import de.hsb.app.util.Resources.LoggedIn;
import de.hsb.app.util.Rolle;

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
	private boolean skip;
	private Date maxDate;

	private long totalArtikelInsWarenkorb = 0;
	private double totalPreisInsWarenkorb = 0;

	private String locale = "de";

	public UserHandler() {
	}

	/**
	 * Wird während des initialisation des Beans @this aufgerufen
	 */
	@PostConstruct
	public void init() {
		kundenList = new ListDataModel<User>();
		kundenList.setWrappedData(entityManager.createNamedQuery("SelectUser").getResultList());
		this.maxDate = getMaxDate();
	}

	/**
	 * Check ob die eingetragene Daten richtig. Wenn es der Fall ist, dass wird der
	 * User auf den richtigen Seite seiner Status weitergeleitet. Ansonsten bekommt
	 * er eine Fehlermeldung
	 */
	public void login() {
		updateUserList();
		Query query = entityManager.createQuery(
				"select k from User k " + "where k.benutzername = :benutzername and k.passwort = :passwort ");
		query.setParameter("benutzername", credential.getUsername().trim());
		query.setParameter("passwort", credential.getPassword().trim());

		@SuppressWarnings("unchecked")
		List<User> kunden = query.getResultList();

		if (kunden.size() == 1) {
			user = kunden.get(0);
			this.totalPreisInsWarenkorb = this.user.getGesamtPreis();
			this.totalArtikelInsWarenkorb = this.user.getTotalArtikel();
			if (user.getKreditKarte() == null) {
				kreditKarte = new KreditKarte();
			} else
				kreditKarte = user.getKreditKarte();

			if (user.getRolle() == Rolle.ADMIN) {
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/homePageAdmin.xhtml?faces-redirect=true");
			} else {
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/home.xhtml?faces-redirect=true");
			}

		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falsches Passwort",
					"Ihr Passwort oder Benutzername ist falsch"));
		}
	}

	/** normal Kunde registrieren **/
	public void registrieren() {
		System.out.println("Neue Kunde wird registriert");
		merkeKunde = new User();
		merkeKunde.setGeburtsdatum(getMaxDate());
		merkeKunde.setRolle(Rolle.KUNDE);
		kreditKarte = new KreditKarte();
		context.getApplication().getNavigationHandler().handleNavigation(context, null,
				"/registrieren.xhtml?faces-redirect=true");
	}

	/** Weiterleitung auf die LoginSeite **/
	public void redirectToLoginPage() {
		context.getApplication().getNavigationHandler().handleNavigation(context, null,
				"/loginSeite.xhtml?faces-redirect=true");
	}

	/**
	 * Speicherung des bereit angetragene User oder Admin beim Registrieren
	 * 
	 * @return login Seite zurueck
	 */
	@SuppressWarnings("unchecked")
	public void registrierungSpeichern() {

		Query query = entityManager
				.createQuery("Select k from User k " + "where k.benutzername = :username or k.passwort = :passwort ");
		query.setParameter("username", merkeKunde.getBenutzername());
		query.setParameter("passwort", merkeKunde.getPasswort());

		List<User> tmpKundeList = query.getResultList();
		if (tmpKundeList.isEmpty()) {
			try {
				userTransaction.begin();
				merkeKunde.setKreditKarte(kreditKarte);
				merkeKunde = entityManager.merge(merkeKunde);
				entityManager.persist(merkeKunde);
				kreditKarte.setUser(merkeKunde);
				kreditKarte = entityManager.merge(kreditKarte);
				entityManager.persist(kreditKarte);
				userTransaction.commit();
				updateUserList();
				if (user.getRolle() == Rolle.ADMIN) {
					context.getApplication().getNavigationHandler().handleNavigation(context, null,
							"/homePageAdmin.xhtml?faces-redirect=true");
				} else {
					user = null;
					context.getApplication().getNavigationHandler().handleNavigation(context, null,
							"/loginSeite.xhtml?faces-redirect=true");
				}
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				e.printStackTrace();
			}
		} else
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anmeldedaten",
					"Ihr Passwort und/oder Benutzername sind bereit Vorhanden"));

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
					"/shopView.xhtml?faces-redirect=true");
		}
	}

	/**
	 * mach es sicher dass die jetzige Sitzung während der Abmeldung prozess des
	 * Benutzer beendet wird
	 **/
	public void logout() {
		user = null;
		merkeKunde = null;
		kreditKarte = null;
		context.addMessage(null, new FacesMessage("Sie wurden erfolgreich ausgeloggt"));
		context.getExternalContext().invalidateSession();
		context.getApplication().getNavigationHandler().handleNavigation(context, null,
				"/shopView.xhtml?faces-redirect=true");
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
	public void neu() {
		merkeKunde = new User();
		merkeKunde.setRolle(Rolle.ADMIN);
		merkeKunde.setGeburtsdatum(getMaxDate());
		kreditKarte = new KreditKarte();
		context.getApplication().getNavigationHandler().handleNavigation(context, null,
				"/neueAdminAnlegen?faces-redirect=true");
	}

	/** Speichert die bearbeitete Daten des aktuellen Benutzer **/
	public void bearbeitungSpeichern() {

		Query query = entityManager
				.createQuery("Select k from User k " + "where k.benutzername = :username or k.passwort = :passwort ");
		query.setParameter("username", user.getBenutzername());
		query.setParameter("passwort", user.getPasswort());

		@SuppressWarnings("unchecked")
		List<User> tmpKundeList = query.getResultList();
		if (((tmpKundeList.size() == 1) || (user.getId() == tmpKundeList.get(0).getId())) || tmpKundeList.isEmpty()) {
			try {
				userTransaction.begin();

				user.setKreditKarte(kreditKarte);
				user = entityManager.merge(user);
				entityManager.persist(user);

				kreditKarte = entityManager.merge(kreditKarte);
				entityManager.persist(kreditKarte);

				userTransaction.commit();
				updateUserList();
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				e.printStackTrace();
			}

			if (user.getRolle() == Rolle.ADMIN) {
				System.out.println("WEITERLEITUNG ZU hOME PAGE ADMIN");
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/homePageAdmin.xhtml?faces-redirect=true");
			} else {
				System.out.println("WEITERLEITUNG ZU HOMEALS NORMAL USER");
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/home.xhtml?faces-redirect=true");
			}
		} else
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anmeldedaten",
					"Ihr Passwort und/oder Benutzername sind bereit Vorhanden"));
	}

	/** Speichern des neuen angelegten ADMIN **/
	public void speichern() {
		Query query = entityManager
				.createQuery("Select k from User k " + "where k.benutzername = :username or k.passwort = :passwort ");
		query.setParameter("username", merkeKunde.getBenutzername());
		query.setParameter("passwort", merkeKunde.getPasswort());

		@SuppressWarnings("unchecked")
		List<User> tmpKundeList = query.getResultList();
		if (((tmpKundeList.size() == 1) || (merkeKunde.getId() == tmpKundeList.get(0).getId()))
				|| tmpKundeList.isEmpty()) {
			try {
				System.out.println("Speichern wurde aufgerufen");
				userTransaction.begin();

				merkeKunde.setKreditKarte(kreditKarte);
				merkeKunde = entityManager.merge(merkeKunde);
				entityManager.persist(merkeKunde);

				kreditKarte.setUser(merkeKunde);
				kreditKarte = entityManager.merge(kreditKarte);
				entityManager.persist(kreditKarte);
				userTransaction.commit();
				updateUserList();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "USER",
						"Bearbeitung wurde Erfolgreich gespeichert"));
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/homePageAdmin.xhtml?faces-redirect=true");
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				e.printStackTrace();
			}
		} else
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anmeldedaten",
					"Ihr Passwort und/oder Benutzername sind bereit Vorhanden"));

	}

	/**
	 * Zurueck zum Home Page
	 * 
	 */
	public void abbrechen() {
		if (user != null)
			if (user.getRolle() == Rolle.ADMIN)
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/homePageAdmin.xhtml?faces-redirect=true");
			else
				context.getApplication().getNavigationHandler().handleNavigation(context, null,
						"/home.xhtml?faces-redirect=true");
		else
			context.getApplication().getNavigationHandler().handleNavigation(context, null,
					"/shopView.xhtml?faces-redirect=true");
	}

	public void normaleKundeBearbeiten() {
		merkeKunde = kundenList.getRowData();
		kreditKarte = merkeKunde.getKreditKarte();
		context.getApplication().getNavigationHandler().handleNavigation(context, null,
				"/KundeBearbeiten.xhtml?faces-redirect=true");
	}

	/**
	 * Wenn ein Admin einen Benutzer löscht
	 * 
	 * @return Admin StartSeite
	 */
	public void userLöschen() {
		merkeKunde = kundenList.getRowData();
		try {
			if (!merkeKunde.equals(user)) {
				clearArtikels();
				userTransaction.begin();
				merkeKunde = entityManager.merge(merkeKunde);
				entityManager.remove(merkeKunde);
				userTransaction.commit();
				updateUserList();
				merkeKunde = null;
//				context.getApplication().getNavigationHandler().handleNavigation(context, null,
//						"/homePageAdmin.xhtml?faces-redirect=true");
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "USER", "USER wurde erfolgreich gelöscht"));
			} else
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "USER", "User kann sich selbst nicht löschen!!"));

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zum Aktualisieren des Warenkorbs
	 **/
	public void updateUserList() {
		kundenList = new ListDataModel<User>();
		kundenList.setWrappedData(entityManager.createNamedQuery("SelectUser").getResultList());
	}

	/**
	 * Weiterleiten zum Warenkorb
	 * 
	 */
	public void goToShopCard() {
		context.getApplication().getNavigationHandler().handleNavigation(context, null,
				"/Warenkorb.xhtml?faces-redirect=true");
	}

	public void warenkorbLeeren() {
		try {
			clearArtikels();
			totalArtikelInsWarenkorb = 0;
			totalPreisInsWarenkorb = 0;
			userTransaction.begin();
			user = entityManager.merge(user);
			entityManager.persist(user);
			userTransaction.commit();
//			context.getApplication().getNavigationHandler().handleNavigation(context, null,
//					"/Warenkorb.xhtml?faces-redirect=true");
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Warenkorb", "WarenKorb erfolgreich geleert"));
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Artikel aus dem Warenkorb löschen
	 * 
	 */
	public void deleteFromCardShop() {
		try {
			user.getWarenkorb().remove(merkeArtikel);
			merkeArtikel.setUser(null);
			totalArtikelInsWarenkorb = user.getTotalArtikel();
			totalPreisInsWarenkorb = user.getGesamtPreis();
			userTransaction.begin();
			user = entityManager.merge(user);
			merkeArtikel = entityManager.merge(merkeArtikel);
			entityManager.persist(user);
			entityManager.persist(merkeArtikel);
			userTransaction.commit();

			context.getApplication().getNavigationHandler().handleNavigation(context, null,
					"/Warenkorb.xhtml?faces-redirect=true");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "WARENKORB",
					"Artikel erfolgreich vom Warenkorb gelöscht "));

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Auftrag zum Einkauf abgeben
	 * 
	 */
	public void Kaufbestätigen() {
		if ((!user.getWarenkorb().isEmpty())) {
			if (updateArtikelValue()) {

				try {
					totalArtikelInsWarenkorb = 0;
					totalPreisInsWarenkorb = 0;
					clearArtikels();
					userTransaction.begin();
					user = entityManager.merge(user);
					entityManager.persist(user);
					userTransaction.commit();
					context.getApplication().getNavigationHandler().handleNavigation(context, null,
							"/home.xhtml?faces-redirect=true");
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "WARENKORB", "Ihr Einkauf war Erfolgreich"));
				} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException
						| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
					e.printStackTrace();
				}
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "WARENKORB",
						"Der Anzahl des Artikel ist überschriten. Der aktuelle Bestand dieser Artikel ist:"));
			}
		} else
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "WARENKORB",
					"Der Warenkorb ist leer, somit kann der Kauf nicht bestätigt werden !!"));
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer seiner Warenkorb leer. Also
	 * er setzt alle Artikel, die diesen Benutzer auf @null hatten.
	 */
	public void clearArtikels() {
		try {
			userTransaction.begin();
			for (Artikel artikel : user.getWarenkorb()) {
				artikel.setUser(null);
				artikel = entityManager.merge(artikel);
				entityManager.persist(artikel);
			}
			user.getWarenkorb().clear();
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	/**
	 * gekaufte Artikel vom Bestand abziehen
	 * 
	 */
	public boolean updateArtikelValue() {
		try {
			userTransaction.begin();
			long artikelAnzahl = 0;
			for (Artikel artikel : user.getWarenkorb()) {
				artikelAnzahl = artikel.getAnzahl() - artikel.getKaufAnzahl();
				if (artikelAnzahl < 0) 
					return false;
				artikel.setAnzahl(artikelAnzahl);
				artikel = entityManager.merge(artikel);
				entityManager.persist(artikel);
			}
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Artikel zum Warenkorb hinzufeugen
	 * 
	 */
	public void addToCardShop() {
		try {
			if (merkeArtikel.getKaufAnzahl() < merkeArtikel.getAnzahl()) {
				if (user.getWarenkorb().contains(merkeArtikel)) {
					long tmpIndex = user.getWarenkorb().indexOf(merkeArtikel), tmpKaufAnzahl;

					Artikel tmpArtikel = user.getWarenkorb().get((int) tmpIndex);
					tmpKaufAnzahl = tmpArtikel.getKaufAnzahl();
					merkeArtikel.setKaufAnzahl(merkeArtikel.getKaufAnzahl() + tmpKaufAnzahl);
					user.getWarenkorb().remove(tmpArtikel);
					user.getWarenkorb().add(merkeArtikel);
				} else
					user.getWarenkorb().add(merkeArtikel);
				userTransaction.begin();
				merkeArtikel.setUser(user);
				totalArtikelInsWarenkorb = user.getTotalArtikel();
				totalPreisInsWarenkorb = user.getGesamtPreis();
				user = entityManager.merge(user);
				entityManager.persist(user);
				merkeArtikel = entityManager.merge(merkeArtikel);
				entityManager.persist(merkeArtikel);
				userTransaction.commit();
				context.addMessage(null, new FacesMessage("Artikel erfolgreich in Warenkorb hinzugefügt"));
			} else
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Artikel",
						"Leider ist die gewünschte Anzahl nicht möglich"));
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sprache auf Englisch wechseln
	 * 
	 */
	public void englischLang() {
		context.getViewRoot().setLocale(new Locale("en"));
		locale = "en";
	}

	/**
	 * Sprache auf Deutsch wechseln
	 * 
	 */
	public void deutschLang() {
		context.getViewRoot().setLocale(new Locale("de"));
		locale = "de";
	}

	/**
	 * Sprache auf Französich wechseln
	 * 
	 */
	public void frenchLang() {
		context.getViewRoot().setLocale(new Locale("fr"));
		locale = "fr";
	}

//	private int getInteger(String string) {
//		try {
//			return Integer.valueOf(string);
//		} catch (NumberFormatException ex) {
//			return 0;
//		}
//	}

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

	public long getTotalArtikelInsWarenkorb() {
		return totalArtikelInsWarenkorb;
	}

	public void setTotalArtikelInsWarenkorb(long totalArtikelInsWarenkorb) {
		this.totalArtikelInsWarenkorb = totalArtikelInsWarenkorb;
	}

	public double getTotalPreisInsWarenkorb() {
		return totalPreisInsWarenkorb;
	}

	public void setTotalPreisInsWarenkorb(double totalPreisInsWarenkorb) {
		this.totalPreisInsWarenkorb = totalPreisInsWarenkorb;
	}

	public Date getMaxDate() {
		LocalDate date = LocalDate.now().minusYears(18);
		this.maxDate = new GregorianCalendar(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).getTime();
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
