package de.hsb.app.controller;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
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

import de.hsb.app.Model.Anrede;
import de.hsb.app.Model.Artikel;
import de.hsb.app.Model.Rolle;
import de.hsb.app.Model.User;

@ApplicationScoped
@ManagedBean
public class Shop {

	@Resource
	private UserTransaction appTransaction;

	@PersistenceContext
	private EntityManager entityManager;

	private DataModel<User> kundenList;

	private DataModel<Artikel> artikelListe;

	String passwortWiederholen;

	public Shop() {
	}

	@PostConstruct
	public void init() {

		try {
			
			appTransaction.begin();
			entityManager.persist(new User("Ben", "Coulibaly", new GregorianCalendar(1997, 4, 3).getTime(), "ben",
					"ben", Rolle.ADMIN, Anrede.HERR));
			entityManager.persist(new User("Lionel", "Ngoubayou", new GregorianCalendar(1990, 9, 15).getTime(),
					"lngoubayou", "lgoubayou", Rolle.KUNDE, Anrede.HERR));
			entityManager.persist(new User("Amadou", "Sow", new GregorianCalendar(1994, 5, 21).getTime(), "asow",
					"asow", Rolle.KUNDE, Anrede.HERR));	
			appTransaction.commit();
			this.kundenList = new ListDataModel<User>();
			this.kundenList.setWrappedData(entityManager.createNamedQuery("SelectUser").getResultList());
			
			
			appTransaction.begin();
			entityManager.persist(new Artikel("Surface Book 2", "Das sollte eine Beschribung sein", 2999.99,
					"Microsoft_Surface_Laptop_2.jpg", 12));
			entityManager.persist(
					new Artikel("IPhone 7", "Tolles Handy mit hohem Display auflösung", 689.99, "Iphone7.jpg", 19));
			entityManager.persist(new Artikel("Canon Photo",
					"Apparat Photo von Canon mit tollen Effekten wie Panorama,Zeitraffler, Scharf, 8GB-Speicherplatz",
					259.99, "canon_photo.png", 40));

			entityManager.persist(new Artikel("Apple Monitor",
					"Letzte Generation Monitor von Apple mit 3258*2500 Auflösung, 4HDMI, VGA, 4USB 3.0, integrierte Front-Kamera",
					1099.99, "apple-Monitor.png", 40));

			entityManager.persist(new Artikel("Arduino Board ",
					"Mini-Computer Arduino neueste Generation mit Kamera-Anschlüss, Microphone", 69.99,
					"ArduinoBoard.jpg", 40));

			entityManager.persist(new Artikel("Gaming Mouse",
					"Sensibele Gaming Mouse von ??, Empfindlichkeit bis 4 Stufe Einstellbar, Extra Knöpfe an den Seiten fuer ein gemuetliches Surfen/ Spiel",
					49.99, "Gaming-mouse.jpg", 40));

			entityManager.persist(new Artikel("Gaming Tastaur",
					"Gaming Tastatur füe ein entspanntes Erlebnis beim Spielen oder Schreiben, Soundlos, mit Kabel",
					79.99, "Gaming Tastatur USB.jpg", 40));

			entityManager.persist(new Artikel("JBL Lautsprecher Xtrem",
					"Lautsprecher von JBL Xtrem, eigetlich kann man es für eine Party benutzen, weil es zu Laut ist",
					259.99, "jbl-sound-xtrem.jpg", 40));

			entityManager.persist(new Artikel("Monitor Benq",
					"Monitor vonb Benq, 3D, 3 USB-Anschluesse, 4KUHD, 3 HDMI-Anschluesse, VGA, 3258*2500 Auflösung",
					799.00, "monitor_Benq.jpg", 40));

			entityManager.persist(new Artikel("Sony Camera S5248",
					"Sony Kamera mit tollen Effekten wie Panorama, Zeitraffler, slow motion, Portrait, 16GB-Speicherplatz",
					499.99, "sony-camera.png", 40));

			entityManager.persist(new Artikel("Surface Arc Mouse",
					"Surface Mouse von Microsoft, angenehme Führung des Mauses und Geschwingkeit bis 5mal einstellen, Das Maus ist von Form her verstellbar",
					99.99, "Surface Arc Mouse.jpg", 40));
			appTransaction.commit();
			this.artikelListe = new ListDataModel<Artikel>();
			this.artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	public void updateArtikelList( ActionListener event) {
		artikelListe = new ListDataModel<Artikel>();
		artikelListe.setWrappedData(entityManager.createNamedQuery("SelectArtikel").getResultList());
	}

	public DataModel<User> getKundenList() {
		return this.kundenList;
	}

	public DataModel<Artikel> getArtikelList() {
		return this.artikelListe;
	}

	public UserTransaction getUserTransaction() {
		return this.appTransaction;
	}

	public void setUserTransaction(UserTransaction userTransaction) {
		this.appTransaction = userTransaction;
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
